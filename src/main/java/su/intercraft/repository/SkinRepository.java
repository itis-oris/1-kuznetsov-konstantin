package su.intercraft.repository;

import su.intercraft.model.Skin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkinRepository {
    private final DataSource dataSource = DataSourceConfig.getDataSource();

    public void addSkin(Skin skin, Connection connection) throws SQLException {
        String query = "INSERT INTO skins (file_path) VALUES (?) RETURNING skin_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, skin.getFilePath());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int playerId = resultSet.getInt(1);
                skin.setSkinId(playerId);
            }
        }
    }

    public Skin getSkinById(int skinId) throws SQLException {
        String query = "SELECT * FROM skins WHERE skin_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, skinId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Skin skin = new Skin();
                skin.setSkinId(resultSet.getInt("skin_id"));
                skin.setFilePath(resultSet.getString("file_path"));
                return skin;
            }
        }
        return null;
    }

    public List<Skin> getAllSkins() throws SQLException {
        List<Skin> skinList = new ArrayList<>();
        String query = "SELECT * FROM skins";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Skin skin = new Skin();
                skin.setSkinId(resultSet.getInt("skin_id"));
                skin.setFilePath(resultSet.getString("file_path"));
                skinList.add(skin);
            }
        }
        return skinList;
    }

    public void deleteSkin(Skin skin) throws SQLException {
        deleteSkin(skin.getSkinId());
    }

    public void deleteSkin(int skinId) throws SQLException {
        String query = "DELETE FROM skins WHERE skin_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, skinId);
            preparedStatement.executeUpdate();
        }
    }

    public List<Skin> getAllSkinsByPlayerId(int playerId) throws SQLException {
        List<Skin> skinList = new ArrayList<>();
        String query = "SELECT * FROM skins JOIN player_skins USING (skin_id) WHERE player_id = ? ORDER BY active_timestamp DESC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Skin skin = new Skin();
                skin.setSkinId(resultSet.getInt("skin_id"));
                skin.setFilePath(resultSet.getString("file_path"));
                skinList.add(skin);
            }
        }
        return skinList;
    }
}