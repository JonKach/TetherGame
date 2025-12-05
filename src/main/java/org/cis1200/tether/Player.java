package org.cis1200.tether;

import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.PhysicsObject;
import org.cis1200.tether.world.Tile;
import org.cis1200.tether.world.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;

public class Player extends PhysicsObject {

    private boolean isGrounded = false;
    private Tile[][] tiles;
    private Color color;
    private Player other;

    public String debug;

    private BufferedImage playerRightSprite;
    private BufferedImage playerLeftSprite;

    private HashSet<PowerUp> powerUps;
    private boolean isOnDoubleJump;
    private boolean upReleased = false;

    private String playerName;
    private UIView ui;

    public Player(int px, int py, int width, int height, int mass, Color color, Tile[][] tiles,
                  BufferedImage playerRightSprite, BufferedImage playerLeftSprite, String playerName, UIView ui) {
        super(px, py, width, height, mass);
        this.tiles = tiles;
        this.color = color;
        this.playerRightSprite = playerRightSprite;
        this.playerLeftSprite = playerLeftSprite;
        this.isOnDoubleJump = false;
        this.playerName = playerName;
        this.ui = ui;
        powerUps = new HashSet<>();
    }

    public void setPair(Player other) {
        this.other = other;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        if (playerLeftSprite != null && playerRightSprite != null) {
            if (getVx() >= 0) {
                g.drawImage(playerRightSprite, (int) getPx(), (int) getPy(), null);
            } else {
                g.drawImage(playerLeftSprite, (int) getPx(), (int) getPy(), null);
            }
        } else {
            g.fillRect((int) getPx(), (int) getPy(), getWidth(), getHeight());
        }
    }

    public void tick() {
        tether();
        Direction[] direction = getDirection();
        double futureX = getPx() + getVx();
        Collision xCollision = collisionCheck(futureX, getPy(), getWidth(), getHeight(), getVx(), 0);
//        boolean sideCollided = xCollision.getSideCollided();
        boolean leftCollided = xCollision.isLeftCollided();
        boolean rightCollided = xCollision.isRightCollided();
//        debug = oldXDirection.toString();
//        (direction[0] == Direction.LEFT || oldXDirection == Direction.LEFT) && sideCollided
        if(leftCollided) {
            setVx(0);
            setMotionRestrictions(true, false);
        } else if(rightCollided) {
            setVx(0);
            setMotionRestrictions(false, true);
        } else  {
            setMotionRestrictions(false, false);
        }
        double futureY = getPy() + getVy();
        futureX = getPx() + getVx(); //should have changed if it was set to 0 above
        Collision yCollision = collisionCheck(futureX, futureY, getWidth(), getHeight(), getVx(), getVy());
        boolean collided = yCollision.isCollided();
        boolean topCollided = yCollision.isTopCollided();

        if(collided) {
            debug = Boolean.toString(topCollided);
//            direction[1] == Direction.DOWN
            if (direction[1] == Direction.DOWN || direction[1] == Direction.STANDSTILL) {
                setVy(0);
                this.isGrounded = true;
                setApplyGravity(false);
            } else if (direction[1] == Direction.UP && topCollided) {
                setVy(0);
                System.out.println("run");
            } else {
                this.isGrounded = false;
            }
        } else {
            this.isGrounded = false;
            setApplyGravity(true);
        }

        for (PowerUp powerUp : powerUps) {
            if (powerUp.secondsLeft() > 0) {
                switch(powerUp.getType()) {
                    case DOUBLE_JUMP:
                        isOnDoubleJump = true;
                        break;
                }
            } else {
                powerUps.remove(powerUp);
                isOnDoubleJump = false;
            }
        }
        ui.displayPowerUps(powerUps, playerName);
        update(true);
    }

    public Collision collisionCheck(double x, double y, int width, int height, double vx, double vy) {
        int gridColOfPlayer = (int) getPx() / World.TILE_SIZE;
        int gridRowOfPlayer = (int) getPy() / World.TILE_SIZE;
        int gridWidth = (getWidth() / World.TILE_SIZE) + 1;
        int gridHeight = (getHeight() / World.TILE_SIZE) + 1;
        boolean collided = false;
//        boolean sideCollided = false;
        boolean leftCollided = false;
        boolean rightCollided = false;
        boolean topCollided = false;
        boolean debug = false;

        for (int i = gridRowOfPlayer - 1; i <= gridRowOfPlayer + gridHeight + 1; i++) {
            for (int j = gridColOfPlayer - 1; j <= gridColOfPlayer + gridWidth + 1; j++) {
                if (i < 0 || i > tiles.length - 1 || j < 0 || j > tiles[0].length - 1 || tiles[i][j] == null) {
                    continue;
                } else {
                    Collision collision = tiles[i][j].collidesWith(x, y,
                            width, height, vx, vy);
                    collided = collision.isCollided() || collided;
//                    sideCollided = collision.getSideCollided() || sideCollided;
                    leftCollided = collision.isLeftCollided() || leftCollided;
                    rightCollided = collision.isRightCollided() || rightCollided;
                    debug = collision.getDebug() || debug;
                    if (collision.isCollided() || collision.isPowerUpCollided()) {
                        tiles[i][j].onCollide(getDirection(), powerUps);
                        topCollided = collision.isTopCollided() || topCollided;
                    }
                }
            }
        }
        return new Collision(collided, leftCollided, rightCollided, topCollided, false, debug);
    }

    private void tether() {
        double tetherDist = dist(getPx(), getPy(), other.getPx(), other.getPy());
        double xDiff = other.getPx() - getPx();
        double yDiff = other.getPy() - getPy();
        if (tetherDist > World.TETHER_DIST) {
            impulseAndUpdateVelocity((int) xDiff / 5, (int) yDiff / 5);
        }
    }

    public void jump() {
        if(isGrounded) {
            isGrounded = false;
            setApplyGravity(true);
            impulse(0, -80);
        } else if(isOnDoubleJump && upReleased) {
            for(PowerUp powerUp : powerUps) {
                if (powerUp.getType() == PowerUp.PowerUpType.DOUBLE_JUMP) {
                    powerUp.consume();
                }
            }
            isOnDoubleJump = false;
            setApplyGravity(true);
            setVy(0);
            impulse(0, -180);
        }
    }

    public void setUpReleased(boolean upReleased) {
        this.upReleased = upReleased;
    }

    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    public double getDist() {
        return dist(getPx(), getPy(), other.getPx(), other.getPy());
    }
}
