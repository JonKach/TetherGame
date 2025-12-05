package org.cis1200.tether;

import org.cis1200.tether.utility.PhysicsObject;
import org.cis1200.tether.utility.SpriteSheetLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lava extends PhysicsObject {

    TexturePaint lavaTexture;

    public Lava(double px, double py, int width, int height, int mass) {
        super(px, py, width, height, mass);
        BufferedImage lavaSprite = SpriteSheetLoader.loadImage("files/lava.png");
        lavaTexture = new TexturePaint(lavaSprite, new Rectangle(0, 0,
                128, 128));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(lavaTexture); // Set the paint to our TexturePaint
        g.fillRect((int) getPx(), (int) getPy(), getWidth(), getHeight());
    }
}
