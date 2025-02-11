package su.intercraft.service;

import jakarta.servlet.http.Part;
import su.intercraft.exception.SkinUploadException;
import su.intercraft.model.Skin;
import su.intercraft.repository.SkinRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class SkinService {

    private final SkinRepository skinRepository;

    public SkinService() {
        this.skinRepository = new SkinRepository();
    }

    public void addSkin(Skin skin, Connection connection) throws SQLException, IOException {
        if (skin == null || skin.getFilePath() == null || skin.getFilePath().isEmpty()) {
            throw new IllegalArgumentException("Skin or file path cannot be null or empty");
        }
        skinRepository.addSkin(skin, connection);
        generatePreview(skin);
    }

    public Skin getSkinById(int skinId) throws SQLException {
        if (skinId <= 0) {
            throw new IllegalArgumentException("Skin ID must be greater than zero");
        }
        return skinRepository.getSkinById(skinId);
    }

    public List<Skin> getAllSkins() throws SQLException {
        return skinRepository.getAllSkins();
    }

    public void deleteSkin(Skin skin) throws SQLException {
        if (skin == null) {
            throw new IllegalArgumentException("Skin cannot be null");
        }
        skinRepository.deleteSkin(skin);
    }

    public void deleteSkin(int skinId) throws SQLException {
        if (skinId <= 0) {
            throw new IllegalArgumentException("Skin ID must be greater than zero");
        }
        skinRepository.deleteSkin(skinId);
    }

    public List<Skin> getAllSkinsByPlayerId(int playerId) throws SQLException {
        if (playerId <= 0) {
            throw new IllegalArgumentException("Player ID must be greater than zero");
        }
        return skinRepository.getAllSkinsByPlayerId(playerId);
    }

    public String saveSkin(Part filePart) throws IOException, SkinUploadException {
        BufferedImage image = ImageIO.read(filePart.getInputStream());
        if (image == null || !isPng(filePart.getInputStream())) {
            throw new SkinUploadException("Ошибка: файл не является изображением PNG.");
        }

        if (image.getWidth() != 64 || image.getHeight() != 64) {
            throw new SkinUploadException("Ошибка: изображение должно быть 64x64 пикселя");
        }

        String randomFileName = generateRandomFileName() + ".png";

        boolean isSaved = saveFile(filePart, randomFileName);
        if (isSaved) {
            return randomFileName;
        } else {
            throw new SkinUploadException("Ошибка при сохранении файла.");
        }
    }

    private boolean isPng(InputStream inputStream) throws IOException {
        byte[] header = new byte[8];
        int bytesRead = inputStream.read(header);

        return bytesRead == 8 && header[0] == (byte) 0x89 && header[1] == (byte) 0x50 &&
                header[2] == (byte) 0x4E && header[3] == (byte) 0x47 &&
                header[4] == (byte) 0x0D && header[5] == (byte) 0x0A &&
                header[6] == (byte) 0x1A && header[7] == (byte) 0x0A;
    }

    private String generateRandomFileName() {
        int nameLength = 16;
        StringBuilder sb = new StringBuilder(nameLength);
        Random random = new Random();
        for (int i = 0; i < nameLength; i++) {
            int randomChar = random.nextInt(36); // 26 букв + 10 цифр
            if (randomChar < 26) {
                sb.append((char) ('a' + randomChar)); // Буквы a-z
            } else {
                sb.append(randomChar - 26); // Цифры 0-9
            }
        }
        return sb.toString();
    }

    private boolean saveFile(Part filePart, String fileName) {
        String UPLOAD_DIR = "D:/intercraft/skin/";

        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, filePath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void generatePreview(Skin skin) throws IOException {
        String minecraftDirectory = "D:/intercraft/";
        String targetFile = minecraftDirectory + "skin/" + skin.getFilePath();

        // Загрузка оригинального изображения
        BufferedImage originalImage = ImageIO.read(new File(targetFile));
        BufferedImage headImage = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        BufferedImage previewImage = new BufferedImage(58, 32, BufferedImage.TYPE_INT_ARGB);

        // Установка прозрачности
        Graphics2D g2dPreview = previewImage.createGraphics();
        g2dPreview.setComposite(AlphaComposite.Clear);
        g2dPreview.fillRect(0, 0, previewImage.getWidth(), previewImage.getHeight());
        g2dPreview.setComposite(AlphaComposite.SrcOver);

        // Башка
        g2dPreview.drawImage(originalImage, 6, 0, 14, 8, 8, 8, 16, 16, null); // перед
        g2dPreview.drawImage(originalImage, 20, 0, 28, 8, 16, 8, 24, 16, null); // лево
        g2dPreview.drawImage(originalImage, 34, 0, 42, 8, 24, 8, 32, 16, null); // зад
        g2dPreview.drawImage(originalImage, 48, 0, 56, 8, 0, 8, 8, 16, null); // право

        // Превью головы
        Graphics2D g2dHead = headImage.createGraphics();
        g2dHead.drawImage(originalImage, 0, 0, 48, 48, 8, 8, 16, 16, null);

        // Тельце
        g2dPreview.drawImage(originalImage, 6, 8, 14, 20, 20, 20, 28, 32, null); // перед
        g2dPreview.drawImage(originalImage, 34, 8, 42, 20, 32, 20, 40, 32, null); // зад

        // Левая рука
        g2dPreview.drawImage(originalImage, 14, 8, 18, 20, 36, 52, 40, 64, null); // перед
        g2dPreview.drawImage(originalImage, 22, 8, 26, 20, 40, 52, 44, 64, null); // лево
        g2dPreview.drawImage(originalImage, 30, 8, 34, 20, 44, 52, 48, 64, null); // зад

        // Правая рука
        g2dPreview.drawImage(originalImage, 2, 8, 6, 20, 44, 20, 48, 32, null); // перед
        g2dPreview.drawImage(originalImage, 42, 8, 46, 20, 52, 20, 56, 32, null); // зад
        g2dPreview.drawImage(originalImage, 50, 8, 54, 20, 40, 20, 44, 32, null); // право

        // Левая нога
        g2dPreview.drawImage(originalImage, 10, 20, 14, 32, 20, 52, 24, 64, null); // перед
        g2dPreview.drawImage(originalImage, 22, 20, 26, 32, 24, 52, 28, 64, null); // лево
        g2dPreview.drawImage(originalImage, 34, 20, 38, 32, 28, 52, 32, 64, null); // зад

        // Правая нога
        g2dPreview.drawImage(originalImage, 6, 20, 10, 32, 4, 20, 8, 32, null); // перед
        g2dPreview.drawImage(originalImage, 38, 20, 42, 32, 12, 20, 16, 32, null); //зад
        g2dPreview.drawImage(originalImage, 50, 20, 54, 32, 0, 20, 4, 32, null); // право
        /*
        // Башка (верхний слой)
        g2dPreview.drawImage(originalImage, 6, 0, 14, 8, 8, 24, 16, 32, null); // перед
        g2dPreview.drawImage(originalImage, 20, 0, 28, 8, 16, 24, 24, 32, null); // лево
        g2dPreview.drawImage(originalImage, 34, 0, 42, 8, 24, 24, 32, 32, null); // зад
        g2dPreview.drawImage(originalImage, 48, 0, 56, 8, 0, 24, 8, 32, null); // право

        // Тельце (верхний слой)
        g2dPreview.drawImage(originalImage, 6, 8, 14, 20, 20, 36, 28, 48, null); // перед
        g2dPreview.drawImage(originalImage, 34, 8, 42, 20, 32, 36, 40, 48, null); // зад

        // Левая рука (верхний слой)
        g2dPreview.drawImage(originalImage, 14, 8, 18, 20, 36, 20, 40, 32, null); // перед
        g2dPreview.drawImage(originalImage, 22, 8, 26, 20, 40, 20, 44, 32, null); // лево
        g2dPreview.drawImage(originalImage, 30, 8, 34, 20, 44, 20, 48, 32, null); // зад

        // Правая рука (верхний слой)
        g2dPreview.drawImage(originalImage, 2, 8, 6, 20, 44, 20, 48, 32, null); // перед
        g2dPreview.drawImage(originalImage, 42, 8, 46, 20, 52, 20, 56, 32, null); // зад
        g2dPreview.drawImage(originalImage, 50, 8, 54, 20, 40, 20, 44, 32, null); // право

        // Левая нога (верхний слой)
        g2dPreview.drawImage(originalImage, 10, 20, 14, 32, 20, 20, 24, 32, null); // перед
        g2dPreview.drawImage(originalImage, 22, 20, 26, 32, 24, 20, 28, 32, null); // лево
        g2dPreview.drawImage(originalImage, 34, 20, 38, 32, 28, 20, 32, 32, null); // зад

        // Правая нога (верхний слой)
        g2dPreview.drawImage(originalImage, 6, 20, 10, 32, 4, 20, 8, 32, null); // перед
        g2dPreview.drawImage(originalImage, 38, 20, 42, 32, 12, 20, 16, 32, null); // зад
        g2dPreview.drawImage(originalImage, 50, 20, 54, 32, 0, 20, 4, 32, null); // право
        */
        // Масштабирование превью изображения
        Image scaledPreviewImage = previewImage.getScaledInstance(464, 256, Image.SCALE_REPLICATE);
        BufferedImage finalPreviewImage = new BufferedImage(464, 256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dFinalPreview = finalPreviewImage.createGraphics();
        g2dFinalPreview.drawImage(scaledPreviewImage, 0, 0, null);
        g2dFinalPreview.dispose();

        // Сохранение изображений
        ImageIO.write(finalPreviewImage, "png", new File(minecraftDirectory + "preview/" + skin.getFilePath()));
        ImageIO.write(headImage, "png", new File(minecraftDirectory + "head/" + skin.getFilePath()));

        // Освобождение ресурсов
        g2dPreview.dispose();
        g2dHead.dispose();
    }
}
