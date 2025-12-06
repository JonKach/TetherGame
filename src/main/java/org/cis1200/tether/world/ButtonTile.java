package org.cis1200.tether.world;

import org.cis1200.tether.Direction;
import org.cis1200.tether.Player;
import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.Sprites;

import java.awt.*;
import java.util.ArrayList;

public class ButtonTile extends Tile {

    ArrayList<Door> linkedDoors;

    public ButtonTile(int x, int y, ArrayList<Door> linkedDoors, World world) {
        super(
                x, y + World.TILE_SIZE - 40, 50, 40, false,
                new Color(255, 71, 76), Sprites.getButtonSprite(), true, world
        );
        this.linkedDoors = linkedDoors;
    }

    @Override
    public Collision collidesWith(
            double playerX, double playerY, int playerWidth, int playerHeight, double vx,
            double vy
    ) {
        int boxTop = getY() + 25;
        int boxBottom = getY() + getHeight();
        int boxLeft = getX() + 7;
        int boxRight = getX() + getWidth() - 7;

        if ((playerY + playerHeight - boxTop > 10)) {
            return new Collision(
                    false, false, false, false,
                    false, false
            );
        }

        boolean xCollided = playerX + playerWidth >= boxLeft && playerX <= boxRight;
        boolean yCollided = playerY + playerHeight >= boxTop && playerY <= boxBottom;

        boolean smallerYCollided = playerY + playerHeight - 4 >= boxTop && playerY <= boxBottom;
        boolean biggerXCollided = playerX + playerWidth + 4 >= boxLeft && playerX - 4 <= boxRight;
        boolean sideCollided = biggerXCollided && smallerYCollided;

        boolean topCollided = Math.abs(playerY - boxBottom) <= Math
                .abs(playerY + playerHeight - boxTop);
        if (sideCollided) {
            if (Math.abs(playerX - boxRight) <= Math.abs(playerX + playerWidth - boxLeft)) {
                return new Collision(
                        xCollided && yCollided, true, false,
                        topCollided, false, false
                );
            } else {
                return new Collision(
                        xCollided && yCollided, false, true,
                        topCollided, false, false
                );
            }
        }
        return new Collision(
                xCollided && yCollided, false, false,
                topCollided, false, false
        );
    }

    @Override
    public void onCollide(Direction[] colDirection, Player player) {
        if (colDirection[1] == Direction.DOWN) {
            for (Door door : linkedDoors) {
                door.unlockDoor();
                World.setUnlockedDoors(true);
            }
        }
    }
}
