package org.cis1200.tether.UI;

import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;

public class UIView extends JPanel {

    static boolean hasWon = false;
    static boolean hasLost = false;
    static UICard loseCard;
    static UICard winCard;

    public UIView() {
        setOpaque(false);
        Timer timer = new Timer(World.INTERVAL, e -> tick());
        timer.start();

        reset();
    }

    private void tick() {
        if (hasLost && loseCard.getPy() > (double) World.WORLD_HEIGHT / 2 - (double) loseCard.getHeight() / 2) {
            loseCard.impulse(0, -20);
            loseCard.update(false);
        }
        if (hasWon && winCard.getPy() < (double) World.WORLD_HEIGHT / 2 - (double) loseCard.getHeight() / 2) {
            winCard.impulse(0, -20);
            winCard.update(false);
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (hasLost) {
            System.out.println(loseCard.getPy());
            loseCard.draw(g);
        }
        if (hasWon) {
            winCard.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(World.WORLD_WIDTH, World.WORLD_HEIGHT);
    }

    public static void displayLost() {
        hasLost = true;
    }
    public static void displayWon() {
        hasWon = true;
    }

    public static void reset() {
        hasLost = false;
        hasWon = false;
        loseCard = new UICard(World.WORLD_WIDTH / 2 - World.WORLD_WIDTH / 4,
                World.WORLD_HEIGHT + 10, World.WORLD_WIDTH / 2,
                World.WORLD_HEIGHT / 2, "You Lose!");
        loseCard.setApplyGravity(false);

        winCard = new UICard(World.WORLD_WIDTH / 2 - World.WORLD_WIDTH / 4,
                World.WORLD_HEIGHT + 10, World.WORLD_WIDTH / 2,
                World.WORLD_HEIGHT / 2, "You Win!");
        winCard.setApplyGravity(false);
    }


}
