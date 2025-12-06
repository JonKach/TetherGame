package org.cis1200.tether;

import org.cis1200.tether.UI.BackgroundPanel;
import org.cis1200.tether.UI.StatusPanel;
import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.screens.LevelsScreen;
import org.cis1200.tether.screens.TitleScreen;
import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class ScreenManager extends JPanel {

    JPanel currScreen;
    Screen currentScreenEnum;

    UIView currentUIView;
    World currentWorld;

    public ScreenManager() {
        setOpaque(false);
        setFocusable(true);
        setLayout(new BorderLayout());
        setScreen(Screen.TITLE_SCREEN);
        currentUIView = null;
        currentWorld = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void setScreen(Screen screen) {
        this.removeAll();
        switch (screen) {
            case TITLE_SCREEN:
                currScreen = new TitleScreen(this);
                currentScreenEnum = Screen.TITLE_SCREEN;
                StatusPanel.setStatusLabel("Running...");
                SwingUtilities.invokeLater(currScreen::requestFocusInWindow);
                break;
            case LEVEL_1:
                newLevel("files/level_1.txt", 50, 350, 60, 350, -1900, false);
                currentScreenEnum = Screen.LEVEL_1;
                break;
            case LEVEL_2:
                newLevel("files/level_2.txt", 50, 350, 60, 350, -1900, false);
                currentScreenEnum = Screen.LEVEL_2;
                break;
            case LEVEL_3:
                newLevel("files/level_3.txt", 50, 350, 60, 350, -1900, false);
                currentScreenEnum = Screen.LEVEL_3;
                break;
            case LEVELS_SCREEN:
                StatusPanel.setStatusLabel("Running...");
                currScreen = new LevelsScreen(this);
                currentScreenEnum = Screen.LEVELS_SCREEN;
                SwingUtilities.invokeLater(currScreen::requestFocusInWindow);
                break;
            default:
                currentScreenEnum = Screen.DEFAULT;
                currScreen = new JPanel();
                currScreen.setBackground(Color.white);
                currScreen.add(new JLabel("Default Screen"));
        }
        this.add(currScreen, BorderLayout.CENTER);
        this.revalidate();
        repaint();
    }

    private void newLevel(
            String filename, double p1x, double p1y, double p2x, double p2y,
            double lavaX, boolean unlockedDoors
    ) {
        currScreen = new BackgroundPanel(BackgroundPanel.Background.BROWN);
        currScreen.setLayout(new OverlayLayout(currScreen));
        currentScreenEnum = Screen.LEVEL_1;

        final UIView ui = new UIView(this);
        ui.setPreferredSize(new Dimension(1000, 500));
        ui.setAlignmentX(0.5f);
        ui.setAlignmentY(0.5f);

        final World world = new World(filename, p1x, p1y, p2x, p2y, lavaX, unlockedDoors, ui);
        world.setPreferredSize(new Dimension(1000, 500));
        world.setAlignmentX(0.5f);
        world.setAlignmentY(0.5f);

        currentUIView = ui;
        currentWorld = world;

        currScreen.add(ui);
        currScreen.add(world);
        SwingUtilities.invokeLater(world::requestFocusInWindow);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }

    public Screen getScreen() {
        return currentScreenEnum;
    }

    public enum Screen {
        DEFAULT,
        TITLE_SCREEN,
        LEVELS_SCREEN,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
    }

    public Screen getNextLevel() {
        switch (getScreen()) {
            case LEVEL_1:
                return Screen.LEVEL_2;
            case LEVEL_2:
                return Screen.LEVEL_3;
            case LEVEL_3:
                return Screen.TITLE_SCREEN;
            default:
                return Screen.DEFAULT;
        }
    }

    public void saveGameState() {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("files/savedGameState.txt")
        )) {
            writer.write("");
            writer.close();
            System.out.println("Content successfully cleared of " + "savedGameState.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("files/savedGameState.txt", true)
        )) {
            writer.write("---SCREEN---" + "\n");
            writer.write("CurrentScreen: " + currentScreenEnum + "\n");
            writer.close();
            System.out.println("Content successfully written to " + "savedGameState.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        currentUIView.saveState();
        currentWorld.saveState();
    }

    public void stopGame() {
        StatusPanel.setStatusLabel("Paused");
        currentWorld.stopLevelNoDisplay();
    }

    public void loadSave() {
        HashMap<String, String> gameData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("files/savedGameState.txt")
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                if (data.length > 1) {
                    gameData.put(data[0], data[1]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "You have no saved game data!");
            return;
        }
        System.out.println("Saved game data: " + gameData);
        this.removeAll();
        double p1x = Double.parseDouble(gameData.get("P1X:"));
        double p1y = Double.parseDouble(gameData.get("P1Y:"));
        double p2x = Double.parseDouble(gameData.get("P2X:"));
        double p2y = Double.parseDouble(gameData.get("P2Y:"));
        double initLavaX = Double.parseDouble(gameData.get("LavaX:"));
        boolean unlockedDoors = Boolean.parseBoolean(gameData.get("unlockedDoors:"));
        switch (gameData.get("CurrentScreen:")) {
            case "LEVEL_1":
                newLevel("files/level_1.txt", p1x, p1y, p2x, p2y, initLavaX, unlockedDoors);
                currentScreenEnum = Screen.LEVEL_1;
                break;
            case "LEVEL_2":
                newLevel("files/level_2.txt", p1x, p1y, p2x, p2y, initLavaX, unlockedDoors);
                currentScreenEnum = Screen.LEVEL_2;
                break;
            case "LEVEL_3":
                newLevel("files/level_3.txt", p1x, p1y, p2x, p2y, initLavaX, unlockedDoors);
                currentScreenEnum = Screen.LEVEL_3;
                break;
            default:
                throw new IllegalStateException(
                        "Unexpected value: " + gameData.get("Current Screen:")
                );
        }

        currentUIView.setExistingDuration(gameData.get("Duration:"));
        World.setTetherColor(gameData.get("tetherColor:"));
        HashSet<PowerUp> p1PowerUps = new HashSet<>();
        HashSet<PowerUp> p2PowerUps = new HashSet<>();
        if (gameData.containsKey("P1PowerUps:")) {
            for (String powerUp : gameData.get("P1PowerUps:").split("/")) {
                fillPowerUpsSet(p1PowerUps, powerUp);
            }
        }
        if (gameData.containsKey("P2PowerUps:")) {
            for (String powerUp : gameData.get("P2PowerUps:").split("/")) {
                fillPowerUpsSet(p2PowerUps, powerUp);
            }
        }
        currentWorld.loadP1Info(
                Boolean.parseBoolean(gameData.get("P1isGrounded:")),
                Boolean.parseBoolean(gameData.get("P1isOnDoubleJump:")),
                Boolean.parseBoolean(gameData.get("P1dashAvailable:")),
                Boolean.parseBoolean(gameData.get("upReleased:")),
                Boolean.parseBoolean(gameData.get("isUntethered")),
                p1PowerUps
        );
        currentWorld.loadP2Info(
                Boolean.parseBoolean(gameData.get("P1isGrounded:")),
                Boolean.parseBoolean(gameData.get("P1isOnDoubleJump:")),
                Boolean.parseBoolean(gameData.get("P1dashAvailable:")),
                Boolean.parseBoolean(gameData.get("upReleased:")),
                Boolean.parseBoolean(gameData.get("isUntethered")),
                p2PowerUps
        );
        this.add(currScreen, BorderLayout.CENTER);
        this.revalidate();
        repaint();
    }

    private void fillPowerUpsSet(HashSet<PowerUp> playerPowerUps, String powerUp) {
        switch (powerUp) {
            case "DOUBLE_JUMP":
                playerPowerUps.add(new PowerUp(5, PowerUp.PowerUpType.DOUBLE_JUMP));
                break;
            case "DASH":
                playerPowerUps.add(new PowerUp(10, PowerUp.PowerUpType.DASH));
                break;
            case "UNTETHER":
                playerPowerUps.add(new PowerUp(6, PowerUp.PowerUpType.UNTETHER));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + powerUp);
        }
    }
}
