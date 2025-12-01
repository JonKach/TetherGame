package org.cis1200.tether.world;

import org.cis1200.tether.UI.UIView;

import java.awt.*;
import java.util.ArrayList;

public class Spike extends Tile {

    public Spike(int x, int y) {
        super(x + 10, y + World.TILE_SIZE - 10, 30, 30, false,
                new Color(255, 0, 0), true);
    }

    @Override
    public void draw(Graphics g) {
        System.out.println("drew spike");
        g.setColor(this.getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void onCollide() {
        World.stopLevel();
        UIView.displayLost();
    }
}
