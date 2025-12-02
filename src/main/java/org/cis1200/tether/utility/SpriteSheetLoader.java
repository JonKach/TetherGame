package org.cis1200.tether.utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SpriteSheetLoader {
    public static BufferedImage loadImage(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
            return image;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image: " + path, e);
        }
    }
}
