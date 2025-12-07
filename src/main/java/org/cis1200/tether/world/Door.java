package org.cis1200.tether.world;

import org.cis1200.tether.Direction;
import org.cis1200.tether.Player;
import org.cis1200.tether.utility.Sprites;

import java.awt.*;

public class Door extends Tile {

    public Door(int x, int y, World world) {
        super(
                x, y, World.TILE_SIZE, World.TILE_SIZE, false,
                new Color(78, 80, 85), Sprites.getDoorSprite(), true, world
        );
    }

    @Override
    public void onCollide(Direction[] colDirection, Player player) {
        // does nothing on collision
    }

    public void unlockDoor() {
        setVisible(false);
    }
}
