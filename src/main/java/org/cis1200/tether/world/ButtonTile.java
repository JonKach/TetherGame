package org.cis1200.tether.world;

import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.Sprites;

import java.awt.*;
import java.util.ArrayList;

public class ButtonTile extends Tile {

    ArrayList<Door> linkedDoors;

    public ButtonTile(int x, int y, ArrayList<Door> linkedDoors) {
        super(x, y + World.TILE_SIZE - 40, 50, 40, false,
                new Color(255, 71, 76), Sprites.buttonSprite, true);
        this.linkedDoors = linkedDoors;
    }

    @Override
    public Collision collidesWith(double playerX, double playerY, int playerWidth, int playerHeight, double vx, double vy) {
        int boxTop = getY() + 20;
        int boxBottom = getY() + getHeight();
        int boxLeft = getX() + 7;
        int boxRight = getX() + getWidth() - 7;

        boolean xCollided =  playerX + playerWidth >= boxLeft && playerX <= boxRight;
        boolean yCollided = playerY + playerHeight >= boxTop && playerY <= boxBottom;

        boolean smallerYCollided = playerY + playerHeight - 4 >= boxTop && playerY <= boxBottom;
        boolean biggerXCollided = playerX + playerWidth + 4 >= boxLeft && playerX - 4 <= boxRight;
        boolean sideCollided = biggerXCollided && smallerYCollided;

        //if side collided calc the distance from player left edge to block right edge and play right edge to block left
        //whichever is less its that kind of collsion
        //do a simialr thing for y collision, now we will know what type of collision, use that to solve the other issue
        // which is that tether nudges to the right while still be in contact with left wall, so we think we can move left.
        boolean topCollided = Math.abs(playerY - boxBottom) <= Math.abs(playerY + playerHeight - boxTop);

        return new Collision(xCollided && yCollided, false, false,
                topCollided, false);
    }

    @Override
    public void onCollide() {
        for (Door door : linkedDoors) {
            door.unlockDoor();
        }
    }
}
