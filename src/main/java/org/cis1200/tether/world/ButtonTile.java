package org.cis1200.tether.world;

import java.awt.*;
import java.util.ArrayList;

public class ButtonTile extends Tile {

    ArrayList<Door> linkedDoors;

    public ButtonTile(int x, int y, ArrayList<Door> linkedDoors) {
        super(x, y + World.TILE_SIZE - 10, 50, 10, false,
                new Color(255, 71, 76), true);
        this.linkedDoors = linkedDoors;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
    @Override
    public void onCollide() {
        for (Door door : linkedDoors) {
            door.unlockDoor();
        }
    }
}
