package org.cis1200.tether.utility;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprites {

    public static BufferedImage grassSprite;
    public static BufferedImage wallSprite;
    public static BufferedImage platformSprite;
    public static BufferedImage spikeSprite;
    public static BufferedImage doorSprite;
    public static BufferedImage buttonSprite;
    public static BufferedImage p2RightSprite;
    public static BufferedImage p2LeftSprite;
    public static BufferedImage p1RightSprite;
    public static BufferedImage p1LeftSprite;
    public static Image flagSprite;
    public static BufferedImage doubleJumpSprite;
    public static BufferedImage dashSprite;
    public static BufferedImage untetherSprite;

    public static void createAllSprites(BufferedImage spritesheet) {
        grassSprite = spritesheet.getSubimage(96, 0, 48, 48);
        wallSprite = spritesheet.getSubimage(272, 64, 48, 48);
        platformSprite = spritesheet.getSubimage(272, 16, 48, 16);
        doorSprite = spritesheet.getSubimage(208, 80, 32, 32);
        try {
            spikeSprite = SpriteSheetLoader.loadImage("files/spike.png");
            buttonSprite = SpriteSheetLoader.loadImage("files/button.png");
            BufferedImage p2 = SpriteSheetLoader.loadImage("files/p2.png")
                    .getSubimage(0, 0, 32, 32);
            p2RightSprite = p2;
            p2LeftSprite = flipHorizontally(p2);
            BufferedImage p1 = SpriteSheetLoader.loadImage("files/p1.png")
                    .getSubimage(0, 0, 32, 32);
            p1RightSprite = p1;
            p1LeftSprite = flipHorizontally(p1);
            flagSprite = SpriteSheetLoader.loadImage("files/flag.png");
            doubleJumpSprite = SpriteSheetLoader.loadImage("files/DoubleJump.png");
            dashSprite = SpriteSheetLoader.loadImage("files/dash.png");
            untetherSprite = SpriteSheetLoader.loadImage("files/untether.png");
        } catch (Exception e) {
            throw new RuntimeException("could not load terrain spritesheet");
        }
    }

    private static BufferedImage flipHorizontally(BufferedImage originalImage) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); // Scale X by -1
        tx.translate(-originalImage.getWidth(), 0); // Translate back to original position
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(originalImage, null);
    }
}
