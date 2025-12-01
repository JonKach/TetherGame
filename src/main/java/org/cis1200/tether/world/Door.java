package org.cis1200.tether.world;

import java.awt.*;

public class Door extends Tile {

    public Door(int x, int y) {
        super(x, y, World.TILE_SIZE, World.TILE_SIZE, false,
                new Color(78, 80, 85), true);
    }

    @Override
    public void onCollide() {
        //does nothing on collision, maybe add a message later
    }

    public void unlockDoor() {
        setVisible(false);
    }
}
