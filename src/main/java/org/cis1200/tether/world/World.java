package org.cis1200.tether.world;

import org.cis1200.tether.Lava;
import org.cis1200.tether.Player;
import org.cis1200.tether.PowerUp;
import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.utility.SpriteSheetLoader;
import org.cis1200.tether.utility.Sprites;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class World extends JPanel {

    public static final int WORLD_WIDTH = 1000;
    public static final int WORLD_HEIGHT = 500;
    public static final int TILE_SIZE = 50;
    public static final int INTERVAL = 20;
    public static final int TETHER_DIST = 200;
    private final Tile[][] tiles = new Tile[10][20]; // benefit of 2d is that I can check collisions
    // nearby not all
    private static String tetherColor = "basic";

    Player p1;
    private boolean p1Left = false;
    private boolean p1Right = false;
    private boolean p1Up = false;
    private boolean p1UpReleased = false;

    Player p2;
    private boolean p2Left = false;
    private boolean p2Right = false;
    private boolean p2Up = false;
    private boolean p2UpReleased = false;

    Lava lava;

    static Timer timer;

    private BufferedImage terrainSpritesheet;

    private UIView ui;

    private static boolean unlockedDoors = false;

    public World(
            String filename, double p1x, double p1y, double p2x, double p2y,
            double lavaX, boolean unlockedDoors, UIView uiView
    ) {
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        timer = new Timer(INTERVAL, e -> tick());
        timer.start();
        setFocusable(true);
        setEnabled(true);

        this.ui = uiView;
        World.tetherColor = "basic";

        World.unlockedDoors = unlockedDoors;

        try {
            terrainSpritesheet = SpriteSheetLoader.loadImage("files/terrain.png");
        } catch (Exception e) {
            throw new RuntimeException("could not load terrain spritesheet");
        }
        Sprites.createAllSprites(terrainSpritesheet);

        createLevel(filename);

        p1 = new Player(
                p1x, p1y, 30, 32, 10, Color.RED, shallowCopy2DArray(tiles),
                Sprites.getP1RightSprite(), Sprites.getP1LeftSprite(), "P1", ui
        );
        p2 = new Player(
                p2x, p2y, 30, 32, 10, Color.BLUE, shallowCopy2DArray(tiles),
                Sprites.getP2RightSprite(), Sprites.getP2LeftSprite(), "P2", ui
        );
        p1.setPair(p2);
        p2.setPair(p1);

        lava = new Lava(lavaX, 0, 1100, 500, 10);
        lava.setApplyGravity(false);
        lava.setApplyFriction(false);
        lava.setVx(1);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    p1Left = true;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    p1Right = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    p1Up = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_M) {
                    p1.dash();
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    p2.dash();
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    p2Left = true;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    p2Right = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    p2Up = true;
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    p1Left = false;
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    p1Right = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    p1Up = false;
                    p1UpReleased = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    p2Left = false;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    p2Right = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    p2Up = false;
                    p2UpReleased = true;
                }
            }

        });

    }

    public void tick() {
        if (p1Left) {
            p1.impulse(-10, 0);
        }
        if (p1Right) {
            p1.impulse(10, 0);
        }
        if (p1Up) {
            p1.setUpReleased(p1UpReleased);
            p1.jump();
            p1UpReleased = false;
        }
        if (p2Left) {
            p2.impulse(-10, 0);
        }
        if (p2Right) {
            p2.impulse(10, 0);
        }
        if (p2Up) {
            p2.setUpReleased(p2UpReleased);
            p2.jump();
            p2UpReleased = false;
        }
        lava.update(false);
        p1.tick();
        p2.tick();
        repaint();
        if (lava.getPx() + lava.getWidth() > p1.getPx() + 20
                || lava.getPx() + lava.getWidth() > p2.getPx() + 20) {
            stopLevel(false);
        }
    }

    private void createTile(
            int row, int col, boolean passableFromBelow, Color color, BufferedImage img,
            boolean slab
    ) {
        if (slab) {
            tiles[row][col] = new Tile(
                    col * TILE_SIZE,
                    row * TILE_SIZE + TILE_SIZE - 20, TILE_SIZE, 20,
                    passableFromBelow, color, img, true, this
            );
        } else {
            tiles[row][col] = new Tile(
                    col * TILE_SIZE,
                    row * TILE_SIZE, TILE_SIZE, TILE_SIZE,
                    passableFromBelow, color, img, true, this
            );
        }

    }

    private void createLevel(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            boolean readingAdditionalData = false;
            ArrayList<Door> doors = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.contains("DATA")) {
                    readingAdditionalData = true;
                    continue;
                }
                if (!readingAdditionalData) {
                    for (int col = 0; col < WORLD_WIDTH / World.TILE_SIZE; col++) {
                        String[] rowData = line.split(" ");
                        Color color;
                        switch (rowData[col]) {
                            case "G":
                                color = new Color(126, 200, 80);
                                createTile(row, col, false, color, Sprites.getGrassSprite(),
                                        false);
                                break;
                            case "W":
                                color = new Color(210, 180, 140);
                                createTile(row, col, false, color, Sprites.getWallSprite(),
                                        false);
                                break;
                            case "P":
                                color = new Color(148, 115, 82);
                                createTile(row, col, true, color, Sprites.getPlatformSprite(),
                                        true);
                                break;
                            case "S":
                                Spike spike = new Spike(col * TILE_SIZE, row * TILE_SIZE, this);
                                tiles[row][col] = spike;
                                break;
                            case "F":
                                Flag flag = new Flag(col * TILE_SIZE, row * TILE_SIZE, this);
                                tiles[row][col] = flag;
                                break;
                            case "D":
                                DoubleJumpTile dj = new DoubleJumpTile(
                                        col * TILE_SIZE,
                                        row * TILE_SIZE, this
                                );
                                tiles[row][col] = dj;
                                break;
                            case "A":
                                DashTile dash = new DashTile(
                                        col * TILE_SIZE, row * TILE_SIZE, this
                                );
                                tiles[row][col] = dash;
                                break;
                            case "U":
                                UntetherTile unt = new UntetherTile(
                                        col * TILE_SIZE, row * TILE_SIZE, this
                                );
                                tiles[row][col] = unt;
                                break;
                            default:
                                continue;
                        }
                    }
                    row++;
                } else {
                    String[] rowData = line.split(" ");
                    int y = Integer.parseInt(rowData[1]) - 1;
                    int x = Integer.parseInt(rowData[2]) - 1;
                    switch (rowData[0]) {
                        case "O":
                            Door door = new Door(x * TILE_SIZE, y * TILE_SIZE, this);
                            if (unlockedDoors) {
                                door.unlockDoor();
                            }
                            doors.add(door);
                            tiles[y][x] = door;
                            break;
                        case "B":
                            ButtonTile button = new ButtonTile(
                                    x * TILE_SIZE, y * TILE_SIZE,
                                    new ArrayList<>(doors), this
                            );
                            tiles[y][x] = button;
                            doors.clear();
                            break;
                        default:
                            continue;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopLevel(boolean won) {
        timer.stop();
        if (won) {
            this.ui.displayWon();
        } else {
            this.ui.displayLost();
        }
        ui.stopUIView();
    }

    public void stopLevelNoDisplay() {
        timer.stop();
        ui.stopUIView();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        lava.draw(g);
        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (tile != null) {
                    tile.draw(g);
                }
            }
        }
        p1.draw(g);
        p2.draw(g);
        switch (tetherColor) {
            case "basic":
                if (p1.getDist() >= World.TETHER_DIST - 20) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLACK);
                }
                break;
            case "P1":
                g.setColor(Color.green);
                break;
            case "P2":
                g.setColor(Color.blue);
                break;
            case "both":
                g.setColor(Color.cyan);
                break;
            default:
                tetherColor = "basic";
                break;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawLine(
                (int) p1.getPx() + p1.getWidth() / 2, (int) p1.getPy() + p1.getHeight() / 2 + 10,
                (int) p2.getPx() + p2.getWidth() / 2, (int) p2.getPy() + p2.getHeight() / 2
        );
    }

    public static void setTetherColor(String playerName) {
        if (tetherColor.equals("both") && playerName.equals("P1basic")) {
            tetherColor = "P2";
        } else if (tetherColor.equals("both") && playerName.equals("P2basic")) {
            tetherColor = "P1";
        } else if (tetherColor.equals("both")) {
            tetherColor = "both";
        } else if (tetherColor.equals("P1") && playerName.equals("P2")
                || tetherColor.equals("P2") && playerName.equals("P1")) {
            tetherColor = "both";
        } else if (playerName.equals("P1") || playerName.equals("P2")) {
            tetherColor = playerName;
        } else if (tetherColor.equals("P1") && playerName.equals("P1basic") ||
                tetherColor.equals("P2") && playerName.equals("P2basic")) {
            tetherColor = "basic";
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }

    private Tile[][] shallowCopy2DArray(Tile[][] tiles) {
        Tile[][] copy = new Tile[tiles.length][tiles[0].length];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[i].length; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    public void saveState() {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("files/savedGameState.txt", true)
        )) {
            writer.write("---WORLD---" + "\n");
            writer.write("tetherColor: " + tetherColor + "\n");
            writer.write("LavaX: " + lava.getPx() + "\n");
            writer.write("P1X: " + p1.getPx() + "\n");
            writer.write("P1Y: " + p1.getPy() + "\n");
            writer.write("P2X: " + p2.getPx() + "\n");
            writer.write("P2Y: " + p2.getPy() + "\n");
            writer.write("unlockedDoors: " + unlockedDoors + "\n");
            writer.close();
            System.out.println("Content successfully written to " + "savedGameState.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        p1.saveState();
        p2.saveState();
    }

    public void loadP1Info(
            boolean isGrounded, boolean doubleJump, boolean dashAvailable, boolean upReleased,
            boolean untethered, HashSet<PowerUp> powerUps
    ) {
        p1.loadInfo(isGrounded, doubleJump, dashAvailable, upReleased, untethered, powerUps);
    }

    public void loadP2Info(
            boolean isGrounded, boolean doubleJump, boolean dashAvailable, boolean upReleased,
            boolean untethered, HashSet<PowerUp> powerUps
    ) {
        p2.loadInfo(isGrounded, doubleJump, dashAvailable, upReleased, untethered, powerUps);
    }

    public static void setUnlockedDoors(boolean unlockedDoors) {
        World.unlockedDoors = unlockedDoors;
    }
}
