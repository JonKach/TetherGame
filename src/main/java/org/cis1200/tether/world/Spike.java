package org.cis1200.tether.world;

import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.Sprites;

import java.awt.*;
import java.util.ArrayList;

public class Spike extends Tile {

    public Spike(int x, int y) {
        super(x + 15, y + World.TILE_SIZE - 40, 20, 40, false,
                new Color(255, 0, 0), Sprites.spikeSprite, true);
    }

    @Override
    public Collision collidesWith(double playerX, double playerY, int playerWidth, int playerHeight) {
        int boxTop = getY();
        int boxBottom = getY() + getHeight();
        int boxLeft = getX();
        int boxRight = getX() + getWidth();

        boolean xCollided =  playerX + playerWidth >= boxLeft && playerX <= boxRight;
        boolean yCollided = playerY + playerHeight >= boxTop && playerY <= boxBottom;

        return new Collision(xCollided && yCollided, false, false,
                false, false);
    }

    @Override
    public void onCollide() {
        World.stopLevel();
        UIView.displayLost();
    }
}
