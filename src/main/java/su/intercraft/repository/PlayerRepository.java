package su.intercraft.repository;

import su.intercraft.model.Player;
import su.intercraft.model.PlayerSkin;
import su.intercraft.model.Skin;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private final DataSource dataSource = DataSourceConfig.getDataSource();

    public Player findByNickname(String nickname) throws SQLException {
        String query = "SELECT * FROM players WHERE nickname = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nickname);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPlayer(resultSet);
            }
        }
        return null;
    }

    public Player findById(int playerId) {
        String query = "SELECT * FROM players WHERE player_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapRowToPlayer(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT * FROM players";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                players.add(mapRowToPlayer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    public List<Player> findNotExpired() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT * FROM players WHERE expiration_date > ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                players.add(mapRowToPlayer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    public List<Player> findExpired() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT * FROM players WHERE expiration_date < ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                players.add(mapRowToPlayer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    public boolean addFullPlayer(Player player) {
        String query = "INSERT INTO players (nickname, password, registration_date, expiration_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getNickname());
            statement.setString(2, player.getPassword());
            statement.setTimestamp(3, player.getRegistrationDate());
            statement.setTimestamp(4, player.getExpirationDate());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addMinimalPlayer(Player player, Connection connection) throws SQLException {
        String queryInsert = "INSERT INTO players (nickname, password) VALUES (?, ?) RETURNING player_id;";
        try (PreparedStatement statementInsert = connection.prepareStatement(queryInsert)) {

            statementInsert.setString(1, player.getNickname());
            statementInsert.setString(2, player.getPassword());

            ResultSet resultSet = statementInsert.executeQuery();
            if (resultSet.next()) {
                int playerId = resultSet.getInt(1);
                player.setPlayerId(playerId);
            }
        }
    }

    public boolean addPlayerList(List<Player> playerList) {
        String query = "INSERT INTO players (nickname, password, registration_date, expiration_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(query)){
                for (Player player : playerList) {
                    statement.setString(1, player.getNickname());
                    statement.setString(2, player.getPassword());
                    statement.setTimestamp(3, player.getRegistrationDate());
                    statement.setTimestamp(4, player.getExpirationDate());

                    statement.executeUpdate();
                }
                connection.commit();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updatePlayer(Player player) throws SQLException {
        String query = "UPDATE players SET nickname = ?, password = ?, registration_date = ?, expiration_date = ? WHERE player_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, player.getNickname());
            preparedStatement.setString(2, player.getPassword());
            preparedStatement.setTimestamp(3, player.getRegistrationDate());

            if (player.getExpirationDate() != null) {
                preparedStatement.setTimestamp(4, player.getExpirationDate());
            } else {
                preparedStatement.setNull(4, Types.TIMESTAMP);
            }

            preparedStatement.setInt(5, player.getPlayerId());
            preparedStatement.executeUpdate();
        }
    }


    private Player mapRowToPlayer(ResultSet resultSet) throws SQLException {
        int playerId = resultSet.getInt("player_id");
        String nickname = resultSet.getString("nickname");
        String password = resultSet.getString("password");
        Timestamp registrationDate = resultSet.getTimestamp("registration_date");
        Timestamp expirationDate = null;
        if (resultSet.getTimestamp("expiration_date") != null) {
            expirationDate = resultSet.getTimestamp("expiration_date");
        }
        Player player = new Player();
        player.setPlayerId(playerId);
        player.setNickname(nickname);
        player.setPassword(password);
        player.setRegistrationDate(registrationDate);
        player.setExpirationDate(expirationDate);
        return player;
    }
}