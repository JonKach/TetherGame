package org.cis1200.tether.utility;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Sprites {

    private static BufferedImage grassSprite;
    private static BufferedImage wallSprite;
    private static BufferedImage platformSprite;
    private static BufferedImage spikeSprite;
    private static BufferedImage doorSprite;
    private static BufferedImage buttonSprite;
    private static BufferedImage p2RightSprite;
    private static BufferedImage p2LeftSprite;
    private static BufferedImage p1RightSprite;
    private static BufferedImage p1LeftSprite;
    private static Image flagSprite;
    private static BufferedImage doubleJumpSprite;
    private static BufferedImage dashSprite;
    private static BufferedImage untetherSprite;

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

    public static BufferedImage getGrassSprite() {
        return grassSprite;
    }

    public static BufferedImage getWallSprite() {
        return wallSprite;
    }

    public static BufferedImage getPlatformSprite() {
        return platformSprite;
    }

    public static BufferedImage getSpikeSprite() {
        return spikeSprite;
    }

    public static BufferedImage getDoorSprite() {
        return doorSprite;
    }

    public static BufferedImage getButtonSprite() {
        return buttonSprite;
    }

    public static BufferedImage getP2RightSprite() {
        return p2RightSprite;
    }

    public static BufferedImage getP2LeftSprite() {
        return p2LeftSprite;
    }

    public static BufferedImage getP1RightSprite() {
        return p1RightSprite;
    }

    public static BufferedImage getP1LeftSprite() {
        return p1LeftSprite;
    }

    public static Image getFlagSprite() {
        return flagSprite;
    }

    public static BufferedImage getDoubleJumpSprite() {
        return doubleJumpSprite;
    }

    public static BufferedImage getDashSprite() {
        return dashSprite;
    }

    public static BufferedImage getUntetherSprite() {
        return untetherSprite;
    }
}
