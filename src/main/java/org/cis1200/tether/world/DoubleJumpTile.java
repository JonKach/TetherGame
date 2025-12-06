package org.cis1200.tether.world;

import org.cis1200.tether.Direction;
import org.cis1200.tether.Player;
import org.cis1200.tether.PowerUp;
import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.Sprites;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class DoubleJumpTile extends Tile {

    public DoubleJumpTile(int x, int y, World world) {
        super(
                x, y + World.TILE_SIZE - 40, 50, 40, false,
                new Color(255, 71, 76), Sprites.doubleJumpSprite, true, world
        );
    }

    @Override
    public Collision collidesWith(
            double playerX, double playerY, int playerWidth, int playerHeight, double vx,
            double vy
    ) {
        int boxTop = getY();
        int boxBottom = getY() + getHeight();
        int boxLeft = getX();
        int boxRight = getX() + getWidth();

        boolean xCollided = playerX + playerWidth >= boxLeft && playerX <= boxRight;
        boolean yCollided = playerY + playerHeight >= boxTop && playerY <= boxBottom;

        if (xCollided && yCollided) {
            return new Collision(
                    false, false, false, false,
                    true, false
            );
        }
        return new Collision(
                false, false, false, false,
                false, false
        );
    }

    @Override
    public void onCollide(Direction[] colDirection, Player player) {
        player.addPowerUp(new PowerUp(5, PowerUp.PowerUpType.DOUBLE_JUMP));
    }
}
