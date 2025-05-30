package com.forbiddenisland.ui.util;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private static final String IMAGE_PATH = "/images/";
    private final Map<String, Image> imageCache;

    public AssetLoader() {
        this.imageCache = new HashMap<>();
    }

    public Image getImage(String name) {
        if (imageCache.containsKey(name)) {
            return imageCache.get(name);
        }

        try {
            Image image = new Image(getClass().getResourceAsStream(IMAGE_PATH + name));
            imageCache.put(name, image);
            return image;
        } catch (Exception e) {
            // Return default image when loading fails
            return getDefaultImage();
        }
    }

    private Image getDefaultImage() {
        try {
            return new Image(getClass().getResourceAsStream(IMAGE_PATH + "default.png"));
        } catch (Exception e) {
            // Create a blank image if the default image is also missing
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=");
        }
    }
}