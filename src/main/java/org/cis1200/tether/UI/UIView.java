package org.cis1200.tether.UI;

import org.cis1200.tether.PowerUp;
import org.cis1200.tether.ScreenManager;
import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;

public class UIView extends JPanel {

    ScreenManager screenManager;

    static boolean cardEnabled = false;
    static UICard loseCard;
    static UICard winCard;

    private Instant startTime;

    Timer timer;

    static UICard currentCard;

    private HashSet<PowerUp> p1PowerUps;
    private HashSet<PowerUp> p2PowerUps;

    public UIView(ScreenManager screenManager) {
        setOpaque(false);
        setLayout(null);
        setBounds(0, 0, 1000, 500);
        timer = new Timer(World.INTERVAL, e -> tick());
        timer.start();
        this.screenManager = screenManager;
        cardEnabled = false;
        p1PowerUps = new HashSet<>();
        p2PowerUps = new HashSet<>();

        startTime = Instant.now();
    }

    private void tick() {
//        if (cardEnabled && currentCard.getY() > (double) World.WORLD_HEIGHT / 2 - (double) currentCard.getHeight() / 2) {
//            currentCard.moveCard(0, -20);
//            System.out.println("yo2");
//        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.black);
        Duration duration = Duration.between(startTime, Instant.now());
        g.drawString(duration.getSeconds() + "." + (duration.getNano() / 1000000) % 1000, 10, 30);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.green);
        int lastX = 100;
        for (PowerUp powerUp : p1PowerUps) {
            switch (powerUp.getType()) {
                case DOUBLE_JUMP -> g.drawString("P1 Double Jump " + powerUp.secondsLeft() + "s", lastX, 20);
                case DASH -> g.drawString("P1 Dash " + powerUp.secondsLeft() + "s", lastX, 20);
                case UNTETHER -> g.drawString("P1 Untether " + powerUp.secondsLeft() + "s", lastX, 20);
            }
            lastX += 120;
        }
        g.setColor(Color.blue);
        for (PowerUp powerUp : p2PowerUps) {
            switch (powerUp.getType()) {
                case DOUBLE_JUMP -> g.drawString("P2 Double Jump " + powerUp.secondsLeft() + "s", lastX, 20);
                case DASH -> g.drawString("P2 Dash " + powerUp.secondsLeft() + "s", lastX, 20);
                case UNTETHER -> g.drawString("P2 Untether " + powerUp.secondsLeft() + "s", lastX, 20);
            }
            lastX += 120;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(World.WORLD_WIDTH, World.WORLD_HEIGHT);
    }

    public void displayLost() {
        this.removeAll();
        currentCard = new UICard(275, 125, 400, 200, "You Lose!", true, screenManager);
        cardEnabled = true;
        this.add(currentCard);
        this.revalidate();
        this.repaint();
    }
    public void displayWon() {
        currentCard = new UICard(275, 125, 400, 200, "You Win!", false, screenManager);
        cardEnabled = true;
        this.add(currentCard);
        this.revalidate();
        this.repaint();
    }

    public void displayPowerUps(HashSet<PowerUp> powerUps, String playerName) {
        if (playerName.equals("P1")) {
            p1PowerUps = powerUps;
        } else {
            p2PowerUps = powerUps;
        }
    }

    public void stopUIView() {
        timer.stop();
    }


}
