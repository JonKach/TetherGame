package org.cis1200.tether.UI;

import org.cis1200.tether.PowerUp;
import org.cis1200.tether.ScreenManager;
import org.cis1200.tether.utility.SpriteSheetLoader;
import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    private Image saveIcon;
    private UIButton save;

    private Duration existingDuration;

    public UIView(ScreenManager screenManager) {
        setOpaque(false);
        setLayout(null);
        setBounds(0, 0, 1000, 500);
        StatusPanel.setStatusLabel("Playing...");
        existingDuration = Duration.ZERO;
        saveIcon = SpriteSheetLoader.loadImage("files/saveIcon.png");
        save = new UIButton(950, 10, 50, 40, 5, saveIcon, null);
        save.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                screenManager.saveGameState();
                screenManager.stopGame();
                screenManager.setScreen(ScreenManager.Screen.TITLE_SCREEN);
                JOptionPane.showMessageDialog(UIView.this, "Game State Saved!");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                save.bounce(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                save.bounce(false);
            }
        }
        );
        this.add(save);
        timer = new Timer(World.INTERVAL, e -> tick());
        timer.start();
        this.screenManager = screenManager;
        cardEnabled = false;
        p1PowerUps = new HashSet<>();
        p2PowerUps = new HashSet<>();

        startTime = Instant.now();
    }

    private void tick() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(Color.black);
        Duration duration = Duration.between(startTime, Instant.now()).plus(existingDuration);
        g.drawString(duration.getSeconds() + "." + (duration.getNano() / 1000000) % 1000, 10, 30);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(Color.green);
        int lastX = 100;
        for (PowerUp powerUp : p1PowerUps) {
            switch (powerUp.getType()) {
                case DOUBLE_JUMP -> g
                        .drawString("P1 Double Jump " + powerUp.secondsLeft() + "s", lastX, 20);
                case DASH -> g.drawString("P1 Dash " + powerUp.secondsLeft() + "s", lastX, 20);
                case UNTETHER -> g
                        .drawString("P1 Untether " + powerUp.secondsLeft() + "s", lastX, 20);
            }
            lastX += 120;
        }
        g.setColor(Color.blue);
        for (PowerUp powerUp : p2PowerUps) {
            switch (powerUp.getType()) {
                case DOUBLE_JUMP -> g
                        .drawString("P2 Double Jump " + powerUp.secondsLeft() + "s", lastX, 20);
                case DASH -> g.drawString("P2 Dash " + powerUp.secondsLeft() + "s", lastX, 20);
                case UNTETHER -> g
                        .drawString("P2 Untether " + powerUp.secondsLeft() + "s", lastX, 20);
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
        StatusPanel.setStatusLabel("Paused");
        this.revalidate();
        this.repaint();
    }

    public void displayWon() {
        this.removeAll();
        currentCard = new UICard(275, 125, 400, 200, "You Win!", false, screenManager);
        cardEnabled = true;
        this.add(currentCard);
        StatusPanel.setStatusLabel("Paused");
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

    public void saveState() {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("files/savedGameState.txt", true)
        )) {
            writer.write("---UIVIEW---" + "\n");
            writer.write(
                    "Duration: " + Duration.between(startTime, Instant.now()).plus(existingDuration)
                            + "\n"
            );
            writer.close();
            System.out.println("Content successfully written to " + "savedGameState.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void setExistingDuration(String duration) {
        existingDuration = Duration.parse(duration);
    }

}
