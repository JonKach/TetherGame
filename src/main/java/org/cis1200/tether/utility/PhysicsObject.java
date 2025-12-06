package org.cis1200.tether.utility;

import org.cis1200.tether.Direction;
import org.cis1200.tether.world.World;

import java.awt.*;

public abstract class PhysicsObject {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds:
     * 0 <= px <= maxX 0 <= py <= maxY
     */
    private double px;
    private double py;

    /* Size of object, in pixels. */
    private final int width;
    private final int height;

    /* Velocity: number of pixels to move every time move() is called. */
    private double vx;
    private double vy;

    /* Net Force */
    private double fx;
    private double fy;

    /* Mass */
    private final double mass;

    /*
     * Upper bounds of the area in which the object can be positioned. Maximum
     * permissible x, y positions for the upper-left hand corner of the object.
     */
    private final int maxX;
    private final int maxY;

    private boolean applyGravity = true;
    private boolean applyFriction = true;
    private boolean leftRestrict = false, rightRestrict = false;

    /**
     * Constructor
     */
    public PhysicsObject(
            double px, double py, int width, int height, int mass
    ) {
        this.mass = mass;
        this.vx = 0;
        this.vy = 0;
        this.fx = 0;
        this.fy = 0;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;

        // take the width and height into account when setting the bounds for
        // the upper left corner of the object.
        this.maxX = World.WORLD_WIDTH - width;
        this.maxY = World.WORLD_HEIGHT - height;
    }

    // **********************************************************************************
    // * GETTERS
    // **********************************************************************************
    public double getPx() {
        return this.px;
    }

    public double getPy() {
        return this.py;
    }

    public double getVx() {
        return this.vx;
    }

    public double getVy() {
        return this.vy;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Direction[] getDirection() {
        Direction[] direction = new Direction[2];
        if (this.vx > 0) {
            direction[0] = Direction.RIGHT;
        } else if (this.vx < 0) {
            direction[0] = Direction.LEFT;
        } else {
            direction[0] = Direction.STANDSTILL;
        }
        if (this.vy > 0) {
            direction[1] = Direction.DOWN;
        } else if (this.vy < 0) {
            direction[1] = Direction.UP;
        } else {
            direction[1] = Direction.STANDSTILL;
        }
        return direction;
    }

    // **************************************************************************
    // * SETTERS
    // **************************************************************************
    public void setPx(double px) {
        this.px = px;
        clip();
    }

    public void setPy(double py) {
        this.py = py;
        clip();
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    // **************************************************************************
    // * UPDATES AND OTHER METHODS
    // **************************************************************************

    /**
     * Prevents the object from going outside the bounds of the area
     * designated for the object (i.e. Object cannot go outside the active
     * area the user defines for it).
     */
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    /**
     * Moves the object by its velocity. Ensures that the object does not go
     * outside its bounds by clipping.
     */
    public void update(boolean clip) {
        this.vx += fx / mass;
        this.vy += fy / mass;
        this.fx = 0;
        this.fy = 0;
        if (applyGravity) {
            this.vy += 1; // GRAVITY
        }
        if (applyFriction) {
            this.vx *= 0.9;
        }
        if (Math.abs(vx) < 0.1) {
            this.vx = 0;
        }
        this.px += this.vx;
        this.py += this.vy;
        if (clip) {
            clip();
        }
    }

    public void impulse(int fx, int fy) {
        this.fx += fx;
        this.fy += fy;
        if ((leftRestrict && this.fx < 0) || (rightRestrict && this.fx > 0)) {
            this.fx = 0;
        }
    }

    public void impulseAndUpdateVelocity(int fx, int fy) {
        this.fx += fx;
        this.fy += fy;
        if ((leftRestrict && this.fx < 0) || (rightRestrict && this.fx > 0)) {
            this.fx = 0;
        }
        this.vx += fx / mass;
        this.vy += fy / mass;
        this.fx = 0;
        this.fy = 0;
        if (applyGravity) {
            this.vy += 1; // GRAVITY
        }
        this.vx *= 0.9;
        if (Math.abs(vx) < 0.1) {
            this.vx = 0;
        }
    }

    public void setApplyGravity(boolean applyGravity) {
        this.applyGravity = applyGravity;
    }

    public void setMotionRestrictions(boolean left, boolean right) {
        leftRestrict = left;
        rightRestrict = right;
    }

    public void setApplyFriction(boolean applyFriction) {
        this.applyFriction = applyFriction;
    }

    public abstract void draw(Graphics g);

}
