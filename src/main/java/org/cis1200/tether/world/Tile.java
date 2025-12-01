package org.cis1200.tether.world;

import org.cis1200.tether.Direction;
import org.cis1200.tether.utility.Collision;

import java.awt.*;

public class Tile {


    private int x;
    private int y;
    private final int width;
    private final int height;
    private final Color color;

    private final boolean passableFromBelow;
    private boolean visible;

    public Tile(int x, int y,  int width, int height,  boolean passableFromBelow, Color color, boolean visible) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.passableFromBelow = passableFromBelow;
        this.color = color;
        this.visible = visible;
    }

    public void draw(Graphics g) {
        if(visible) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }
    }

    public Collision collidesWith(double playerX, double playerY,
                                  int playerWidth, int playerHeight) {

        int boxTop = this.y;
        int boxBottom = this.y + height;
        int boxLeft = this.x;
        int boxRight = this.x + width;

        if((playerY + playerHeight - boxTop > 10 && passableFromBelow) || !visible) {
            return new Collision(false, false, false, false, false);
        }

//        System.out.println(playerX + playerWidth + "left " + boxLeft + "x" + playerX + "right" + boxRight);
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
        if (sideCollided) {
            if (Math.abs(playerX - boxRight) <= Math.abs(playerX + playerWidth -  boxLeft)) {
                return new Collision(xCollided && yCollided, true, false,
                         topCollided, false);
            } else {
                return new Collision(xCollided && yCollided, false, true,
                         topCollided, false);
            }
        }
        return new Collision(xCollided && yCollided, false, false,
                 topCollided, false);

//        return new Collision(xCollided && yCollided, sideCollided, isGround ||
//                collDirection[1] == Direction.DOWN, biggerXCollided && getColor().equals(new Color(210, 180, 140)));
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void onCollide() {
        //does nothing by default, for dynamic effects (button pushing, doors, etc.) and powerup collection
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }
}
