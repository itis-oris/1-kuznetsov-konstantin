package su.intercraft.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import su.intercraft.model.Player;
import su.intercraft.model.PlayerSkin;
import su.intercraft.repository.DataSourceConfig;
import su.intercraft.repository.PlayerRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerService {
    private final PlayerRepository playerRepository;
    final static Logger logger = LogManager.getLogger(PlayerService.class);

    public PlayerService() {
        this.playerRepository = new PlayerRepository();
    }

    public Player getPlayerByNickname(String nickname) throws SQLException {
        return playerRepository.findByNickname(nickname);
    }

    public Player getPlayerById(int playerId) {
        return playerRepository.findById(playerId);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> getPlayersNotExpired() {
        return playerRepository.findNotExpired();
    }

    public List<Player> getPlayersExpired() {
        return playerRepository.findExpired();
    }

    public boolean addFullPlayer(Player player) {
        return playerRepository.addFullPlayer(player);
    }

    public void addMinimalPlayer(Player player, Connection connection) throws SQLException {
        playerRepository.addMinimalPlayer(player, connection);
    }

    public boolean addPlayerList(List<Player> playerList) {
        return playerRepository.addPlayerList(playerList);
    }

    public void updatePlayer(Player player) throws SQLException {
        playerRepository.updatePlayer(player);
    }

    public Map<String, String> registerPlayer(String nickname, String password, String password2) {
        Map<String, String> errors = new HashMap<>();

        try (Connection connection = DataSourceConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(false);

            if (nickname.isEmpty()) {
                errors.put("nicknameError", "Никнейм пуст!");
            } else if (nickname.length() < 3) {
                errors.put("nicknameError", "Никнейм должен быть не меньше 3 символов!");
            } else if (nickname.length() > 50) {
                errors.put("nicknameError", "Никнейм должен быть меньше 50 символов!");
            } else if (!nickname.matches("\\w+")) {
                errors.put("nicknameError", "Никнейм не разрешен!");
            } else if (getPlayerByNickname(nickname) != null) {
                errors.put("nicknameError", "Никнейм занят!");
            }

            if (password.isEmpty()){
                errors.put("passwordError", "Пароль пуст!");
            } else if (password.length() < 6){
                errors.put("passwordError", "Пароль слишком короткий!");
            } else if (!password.equals(password2)){
                errors.put("password2Error", "Пароли не совпадают!");
            }

            if (!errors.isEmpty()) {
                return errors;
            }

            try {
                Player newPlayer = new Player();
                newPlayer.setNickname(nickname);
                newPlayer.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                addMinimalPlayer(newPlayer, connection);
                PlayerSkinService playerSkinService = new PlayerSkinService();
                PlayerSkin skin = playerSkinService.createDefault(newPlayer.getPlayerId());
                playerSkinService.addPlayerSkin(skin, connection);

                connection.commit();
                logger.info("Player {} has been registered", nickname);
            } catch (SQLException e) {
                connection.rollback();
                errors.put("error", e.getMessage());
            }

        } catch (SQLException e) {
            errors.put("error", e.getMessage());
            logger.warn("ERROR", e);
        }
        return errors;
    }

    public Player authPlayer(String nickname, String password) throws SQLException {
        Player player = getPlayerByNickname(nickname);
        if (player == null || !player.checkPassword(password)) {
            throw new IllegalArgumentException("Неправильный никнейм или пароль");
        }
        return player;
    }

    public Map<String, String> changePassword(Player player, String oldPassword, String password, String password2) {
        boolean error = false;
        Map<String, String> errors = new HashMap<>();

        if (!player.checkPassword(oldPassword)) {
            errors.put("oldPasswordError", "Неправильный пароль");
        }

        if (password.isEmpty()){
            errors.put("passwordError", "Пароль пуст!");
        } else if (password.length() < 6){
            errors.put("passwordError", "Пароль слишком короткий!");
        } else if (!password.equals(password2)){
            errors.put("password2Error", "Пароли не совпадают!");
        }

        if (!errors.isEmpty()) {
            return errors;
        }

        try{
            player.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            updatePlayer(player);
        } catch (SQLException e) {
            errors.put("error", e.getMessage());
            logger.warn("ERROR", e);
        }
        return errors;
    }

    public void extendPlayerExpirationDate(Player player, int daysToAdd) throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = player.getExpirationDate() != null ? player.getExpirationDate().toLocalDateTime() : now;

        if (expirationDate.isBefore(now)) {
            expirationDate = now;
        }
        expirationDate = expirationDate.plusDays(daysToAdd);
        player.setExpirationDate(Timestamp.valueOf(expirationDate));
        updatePlayer(player);
    }

    public String calculateExpirationDifference(Timestamp expirationDate) {
        LocalDateTime now = LocalDateTime.now();
        if (expirationDate == null || expirationDate.toLocalDateTime().isBefore(now)) {
            return "0d 0h";
        }
        Duration duration = Duration.between(now, expirationDate.toLocalDateTime());
        long days = duration.toDays() + (duration.toHours() + 1) / 24;
        long hours = (duration.toHours() + 1) % 24;
        return String.format("%dd %dh", days, hours);
    }
}