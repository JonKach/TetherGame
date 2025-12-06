package org.cis1200.tether.utility;

public class Collision {

    boolean collided;
    // boolean sideCollided;
    boolean leftCollided;
    boolean rightCollided;
    boolean topCollided;
    boolean powerUpCollided;
    boolean debug;

    public Collision(
            boolean collided,
            boolean leftCollided, boolean rightCollided, boolean topCollided,
            boolean powerUpCollided,
            boolean debug
    ) {
        this.collided = collided;
        // this.sideCollided = sideCollided;
        this.leftCollided = leftCollided;
        this.rightCollided = rightCollided;
        this.topCollided = topCollided;
        this.powerUpCollided = powerUpCollided;
        this.debug = debug;
    }

    public boolean isCollided() {
        return collided;
    }

    public boolean isLeftCollided() {
        return leftCollided;
    }

    public boolean isRightCollided() {
        return rightCollided;
    }

    public boolean isTopCollided() {
        return topCollided;
    }

    public boolean isPowerUpCollided() {
        return powerUpCollided;
    }

    public boolean getDebug() {
        return debug;
    }
}
