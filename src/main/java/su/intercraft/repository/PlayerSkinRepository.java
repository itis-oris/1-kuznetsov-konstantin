package su.intercraft.repository;

import su.intercraft.model.PlayerSkin;
import su.intercraft.model.Skin;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerSkinRepository {
    private final DataSource dataSource = DataSourceConfig.getDataSource();

    public void addPlayerSkin(PlayerSkin playerSkin, Connection connection) {
        String query = "INSERT INTO player_skins (player_id, skin_id, active_timestamp) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerSkin.getPlayerId());
            preparedStatement.setInt(2, playerSkin.getSkinId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerSkin getPlayerSkinById(int playerSkinId) throws SQLException {
        String query = "SELECT * FROM player_skins WHERE player_skin_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerSkinId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                PlayerSkin playerSkin = new PlayerSkin();
                playerSkin.setPlayerSkinId(resultSet.getInt("player_skin_id"));
                playerSkin.setPlayerId(resultSet.getInt("player_id"));
                playerSkin.setSkinId(resultSet.getInt("skin_id"));
                playerSkin.setActiveTimestamp(resultSet.getTimestamp("active_timestamp"));
                return playerSkin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public List<PlayerSkin> getAllPlayerSkins() {
        List<PlayerSkin> playerSkinList = new ArrayList<>();
        String query = "SELECT * FROM player_skins";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PlayerSkin playerSkin = new PlayerSkin();
                playerSkin.setPlayerSkinId(resultSet.getInt("player_skin_id"));
                playerSkin.setPlayerId(resultSet.getInt("player_id"));
                playerSkin.setSkinId(resultSet.getInt("skin_id"));
                playerSkin.setActiveTimestamp(resultSet.getTimestamp("active_timestamp"));
                playerSkinList.add(playerSkin);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return playerSkinList;
    }

    public void updatePlayerSkin(PlayerSkin playerSkin) {
        String query = "UPDATE player_skins SET player_id = ?, skin_id = ?, active_timestamp = ? WHERE player_skin_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerSkin.getPlayerId());
            preparedStatement.setInt(2, playerSkin.getSkinId());
            preparedStatement.setTimestamp(3, playerSkin.getActiveTimestamp());
            preparedStatement.setInt(4, playerSkin.getPlayerSkinId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reactivateSkin(int skinId, int playerId) throws SQLException {
        String query = "UPDATE player_skins SET active_timestamp = CURRENT_TIMESTAMP WHERE skin_id = ? AND player_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, skinId);
            preparedStatement.setInt(2, playerId);
            preparedStatement.executeUpdate();
        }
    }

    public void deletePlayerSkinById(int playerSkinId) {
        String query = "DELETE FROM player_skins WHERE player_skin_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerSkinId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlayerSkin(int playerId, int skinId) {
        String query = "DELETE FROM player_skins WHERE player_id = ? AND skin_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, skinId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PlayerSkin> getAllPlayerSkinsByPlayerId(int playerId) {
        List<PlayerSkin> playerSkinList = new ArrayList<>();
        String query = "SELECT * FROM player_skins WHERE player_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PlayerSkin playerSkin = new PlayerSkin();
                playerSkin.setPlayerSkinId(resultSet.getInt("player_skin_id"));
                playerSkin.setPlayerId(resultSet.getInt("player_id"));
                playerSkin.setSkinId(resultSet.getInt("skin_id"));
                playerSkin.setActiveTimestamp(resultSet.getTimestamp("active_timestamp"));
                playerSkinList.add(playerSkin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerSkinList;
    }

    public Skin getActiveSkin(int playerId) {
        String query = "SELECT * FROM player_skins JOIN skins USING(skin_id) WHERE player_id = ? ORDER BY active_timestamp DESC LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Skin skin = new Skin();
                skin.setSkinId(resultSet.getInt("skin_id"));
                skin.setFilePath(resultSet.getString("file_path"));
                return skin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}