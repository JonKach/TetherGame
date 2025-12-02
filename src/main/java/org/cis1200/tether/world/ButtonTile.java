package org.cis1200.tether.world;

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
    public void onCollide() {
        for (Door door : linkedDoors) {
            door.unlockDoor();
        }
    }
}
