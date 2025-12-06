package org.cis1200.tether;

import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.utility.Collision;
import org.cis1200.tether.utility.PhysicsObject;
import org.cis1200.tether.world.Tile;
import org.cis1200.tether.world.World;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class TetherTest {

    @Test
    public void testPhysicsImpulse() {
        PhysicsObject object = new PhysicsObject(0, 0, 100, 100, 10) {
            @Override
            public void draw(Graphics g) {

            }
        };
        object.setApplyGravity(false);
        double originalY = object.getPy();
        object.impulse(0, 100);
        object.update(false);
        assertTrue(object.getPy() > originalY);
        assertTrue(object.getVy() > 0);
    }

    @Test
    public void testPhysicsGravity() {
        PhysicsObject object = new PhysicsObject(0, 0, 100, 100, 10) {
            @Override
            public void draw(Graphics g) {

            }
        };
        object.setApplyGravity(true);
        double originalY = object.getPy();
        object.update(false);
        assertTrue(object.getPy() > originalY);
        assertTrue(object.getVy() > 0);
    }

    @Test
    public void testTileCollision() {
        Tile tile = new Tile(
                0, 0, 100, 100,
                false, Color.black, null, true, null
        );
        Collision collision1 = tile.collidesWith(0, 0, 10, 10, 1, 1);
        assertTrue(collision1.isCollided());
        Collision collision2 = tile.collidesWith(500, 100, 10, 10, 1, 1);
        assertFalse(collision2.isCollided());
    }

    @Test
    public void testPowerUp() {
        Player player = new Player(
                0, 0, 10, 10, 10, Color.blue, new Tile[10][20],
                null, null, "P1", null
        );
        Player other = new Player(
                0, 0, 10, 10, 10, Color.blue, new Tile[10][20],
                null, null, "P2", null
        );
        player.setPair(other);
        assertFalse(player.getDashAvailable());
        player.addPowerUp(new PowerUp(5, PowerUp.PowerUpType.DASH));
        player.tick();
        assertTrue(player.getDashAvailable());
    }
}
