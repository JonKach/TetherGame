package org.cis1200.tether.world;

import org.cis1200.tether.Direction;
import org.cis1200.tether.PowerUp;
import org.cis1200.tether.utility.Collision;

import java.awt.*;
import java.util.HashSet;

public class Tile {

    private int x;
    private int y;
    private final int width;
    private final int height;
    private final Color color;
    private final Image sprite;
    private World world;

    private final boolean passableFromBelow;
    private boolean visible;

    public Tile(
            int x, int y, int width, int height, boolean passableFromBelow, Color color,
            Image sprite,
            boolean visible, World world
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.passableFromBelow = passableFromBelow;
        this.color = color;
        this.sprite = sprite;
        this.visible = visible;
        this.world = world;
    }

    public void draw(Graphics g) {
        if (visible) {
            g.setColor(color);
            if (sprite != null) {
                g.drawImage(sprite, x, y, width, height, null);
            } else {
                g.fillRect(x, y, width, height);
            }
        }
    }

    public Collision collidesWith(
            double playerX, double playerY,
            int playerWidth, int playerHeight, double vx, double vy
    ) {

        int boxTop = this.y;
        int boxBottom = this.y + height;
        int boxLeft = this.x;
        int boxRight = this.x + width;

        // if((playerY + playerHeight - boxTop > 10 && passableFromBelow) || !visible) {
        // return new Collision(false, false, false, false,
        // false, false);
        // }
        if ((playerY - vy + playerHeight - boxTop > 5 &&
                passableFromBelow) || !visible) {
            // System.out.println(vy);
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

        // if side collided calc the distance from player left edge to block right edge
        // and play right edge to block left
        // whichever is less its that kind of collsion
        // do a simialr thing for y collision, now we will know what type of collision,
        // use that to solve the other issue
        // which is that tether nudges to the right while still be in contact with left
        // wall, so we think we can move left.
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

        // return new Collision(xCollided && yCollided, sideCollided, isGround ||
        // collDirection[1] == Direction.DOWN, biggerXCollided && getColor().equals(new
        // Color(210, 180, 140)));
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void onCollide(Direction[] colDirection, HashSet<PowerUp> playerPowerUps) {
        // does nothing by default, for dynamic effects (button pushing, doors, etc.)
        // and powerup collection
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

    public World getWorld() {
        return world;
    }
}
