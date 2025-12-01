package org.cis1200.tether;

import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.PhysicsObject;
import org.cis1200.tether.world.Tile;
import org.cis1200.tether.world.World;

import java.awt.*;

public class Player extends PhysicsObject {

    private boolean isGrounded = false;
    private Tile[][] tiles;
    private Color color;
    private Player other;

    private Direction oldXDirection = Direction.STANDSTILL;
    public String debug;

    public Player(int px, int py, int width, int height, int mass, Color color, Tile[][] tiles) {
        super(px, py, width, height, mass);
        this.tiles = tiles;
        this.color = color;
    }

    public void setPair(Player other) {
        this.other = other;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect((int) getPx(), (int) getPy(), getWidth(), getHeight());
    }

    public void tick() {
        tether();
        Direction[] direction = getDirection();
        double futureX = getPx() + getVx();
        Collision xCollision = collisionCheck(futureX, getPy(), getWidth(), getHeight());
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
        Collision yCollision = collisionCheck(futureX, futureY, getWidth(), getHeight());
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
//        if (!sideCollided && collided) { //good to move x on its own but y causes problems
//            setVy(0);
//        }
        if (direction[0] == Direction.LEFT || direction[0] == Direction.RIGHT) {
            oldXDirection = direction[0];
        }
        update();
    }

    public Collision collisionCheck(double x, double y, int width, int height) {
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
                            width, height);
                    collided = collision.isCollided() || collided;
//                    sideCollided = collision.getSideCollided() || sideCollided;
                    leftCollided = collision.isLeftCollided() || leftCollided;
                    rightCollided = collision.isRightCollided() || rightCollided;
                    debug = collision.getDebug() || debug;
                    if (collision.isCollided()) {
                        tiles[i][j].onCollide();
                        topCollided = collision.isTopCollided() || topCollided;
                    }
                }
            }
        }
        return new Collision(collided, leftCollided, rightCollided, topCollided, debug);
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
        }
    }

    private double dist(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    public double getDist() {
        return dist(getPx(), getPy(), other.getPx(), other.getPy());
    }
}
