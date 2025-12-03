package org.cis1200.tether.world;

import org.cis1200.tether.Direction;
import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.Sprites;

import java.awt.*;

public class Flag extends Tile {

    public Flag(int x, int y) {
        super(x + 15, y + World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE, false,
                new Color(0, 255, 0), Sprites.flagSprite, true);
    }

    @Override
    public Collision collidesWith(double playerX, double playerY, int playerWidth, int playerHeight,
                                  double vx, double vy) {
        playerX -= vx;
        playerY -= vy;

        int boxTop = getY() + 10;
        int boxBottom = getY() + getHeight();
        int boxLeft = getX() + 5;
        int boxRight = getX() + getWidth() - 5;

        boolean xCollided =  playerX + playerWidth >= boxLeft && playerX <= boxRight;
        boolean yCollided = playerY + playerHeight >= boxTop && playerY <= boxBottom;

        return new Collision(xCollided && yCollided, false, false,
                false, false);
    }

    @Override
    public void onCollide(Direction[] colDirection) {
        World.stopLevel(true);
    }
}
