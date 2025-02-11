package su.intercraft;

import su.intercraft.model.Skin;
import su.intercraft.repository.SkinRepository;
import su.intercraft.service.SkinService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SkinService service = new SkinService();
        Skin skin = new Skin();
        skin.setFilePath("steve.png");
        service.generatePreview(skin);
    }
}
