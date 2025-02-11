package su.intercraft.service;

import jakarta.servlet.http.Part;
import su.intercraft.exception.SkinUploadException;
import su.intercraft.model.PlayerSkin;
import su.intercraft.model.Skin;
import su.intercraft.repository.DataSourceConfig;
import su.intercraft.repository.PlayerSkinRepository;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PlayerSkinService {
    private final PlayerSkinRepository playerSkinRepository;

    public PlayerSkinService() {
        this.playerSkinRepository = new PlayerSkinRepository();
    }

    public void addPlayerSkin(PlayerSkin playerSkin, Connection connection) {
        playerSkinRepository.addPlayerSkin(playerSkin, connection);
    }

    public PlayerSkin getPlayerSkinById(int playerSkinId) throws SQLException {
        return playerSkinRepository.getPlayerSkinById(playerSkinId);
    }

    public List<PlayerSkin> getAllPlayerSkins() {
        return playerSkinRepository.getAllPlayerSkins();
    }

    public void updatePlayerSkin(PlayerSkin playerSkin) {
        playerSkinRepository.updatePlayerSkin(playerSkin);
    }

    public void reactivateSkin(int skinId, int playerId) throws SQLException {
        playerSkinRepository.reactivateSkin(skinId, playerId);
    }

    public void deletePlayerSkin(int playerId, int skinId) {
        playerSkinRepository.deletePlayerSkin(playerId, skinId);
    }

    public void deletePlayerSkinById(int playerSkinId) {
        playerSkinRepository.deletePlayerSkinById(playerSkinId);
    }

    public List<PlayerSkin> getAllPlayerSkinsByPlayerId(int playerId) {
        return playerSkinRepository.getAllPlayerSkinsByPlayerId(playerId);
    }

    public Skin getActiveSkin(int playerId) {
        return playerSkinRepository.getActiveSkin(playerId);
    }

    public void createSkinToPlayer(Part skin_part, int playerId) throws SQLException, IOException, SkinUploadException {
        PlayerSkinService playerSkinService = new PlayerSkinService();
        SkinService skinService = new SkinService();
        try (Connection connection = DataSourceConfig.getDataSource().getConnection()) {

            connection.setAutoCommit(false);
            try {
                String skin_path = skinService.saveSkin(skin_part);
                Skin skin = new Skin();
                skin.setFilePath(skin_path);
                skinService.addSkin(skin, connection);

                PlayerSkin playerSkin = new PlayerSkin();
                playerSkin.setPlayerId(playerId);
                playerSkin.setSkinId(skin.getSkinId());
                playerSkinService.addPlayerSkin(playerSkin, connection);

                connection.commit();
            } catch (SkinUploadException | SQLException | IOException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public PlayerSkin createDefault(int playerId) {
        PlayerSkin playerSkin = new PlayerSkin();
        playerSkin.setPlayerId(playerId);
        playerSkin.setSkinId(1); // ID дефолтного скина
        return playerSkin;
    }
}
