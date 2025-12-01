package org.cis1200.tether.world;

import org.cis1200.tether.Player;
import org.cis1200.tether.UI.UIView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class World extends JPanel {


    public static final int WORLD_WIDTH = 1000;
    public static final int WORLD_HEIGHT = 500;
    public static final int TILE_SIZE = 50;
    public static final int INTERVAL = 20;
    public static final int TETHER_DIST = 200;
    private Tile[][] tiles = new Tile[10][20]; //benefit of 2d is that I can check collisions nearby not all
    private HashSet<PowerUp> powerUps = new HashSet<>();

    Player p1;
    private boolean p1Left = false;
    private boolean p1Right = false;
    private boolean p1Up = false;

    Player p2;
    private boolean p2Left = false;
    private boolean p2Right = false;
    private boolean p2Up = false;

    static Timer timer;

    public World(String filename) {
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        timer = new Timer(INTERVAL, e -> tick());
        timer.start();
        setFocusable(true);
        createLevel(filename);

        p1 = new Player(250, 50, 25, 50, 10, Color.RED, tiles);
        p2 = new Player(250, 50, 25, 50, 10, Color.BLUE, tiles);
        p1.setPair(p2);
        p2.setPair(p1);

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
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    p2Left = true;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    p2Right = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    p2Up = true;
                }
                if(e.getKeyCode() == KeyEvent.VK_G){
                    timer.stop();
                    UIView.displayLost();
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
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    p2Left = false;
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    p2Right = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    p2Up = false;
                }
            }

        });

    }

    public void tick() {
        if(p1Left) {
            p1.impulse(-10, 0);
        }
        if(p1Right) {
            p1.impulse(10, 0);
        }
        if(p1Up) {
            p1.jump();
        }
        if(p2Left) {
            p2.impulse(-10, 0);
        }
        if(p2Right) {
            p2.impulse(10, 0);
        }
        if(p2Up) {
            p2.jump();
        }
        p1.tick();
        p2.tick();
//        System.out.println(p2.debug);
        repaint();
    }

    private void createTile(int row, int col, boolean passableFromBelow, Color color, boolean slab) {
        if(slab) {
            tiles[row][col] = new Tile(col * TILE_SIZE,
                    row * TILE_SIZE + TILE_SIZE - 10, TILE_SIZE, 10,
                    passableFromBelow, color, true);
        } else {
            tiles[row][col] = new Tile(col * TILE_SIZE,
                    row * TILE_SIZE, TILE_SIZE, TILE_SIZE,
                    passableFromBelow, color, true);
        }

    }

    private void createLevel(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int row = 0;
            boolean readingAdditionalData = false;
            ArrayList<Door> doors = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if(line.contains("DATA")) {
                    readingAdditionalData = true;
                    continue;
                }
                if(!readingAdditionalData) {
                    for (int col = 0; col < WORLD_WIDTH / World.TILE_SIZE; col++) {
                        String[] rowData = line.split(" ");
                        Color color;
                        switch (rowData[col]) {
                            case "G":
                                color = new Color(126, 200, 80);
                                createTile(row, col, false, color, false);
                                break;
                            case "D":
                                color = new Color(131, 101, 57);
                                createTile(row, col, false, color, false);
                                break;
                            case "W":
                                color = new Color(210, 180, 140);
                                createTile(row, col, false, color, false);
                                break;
                            case "P":
                                color = new Color(148, 115, 82);
                                createTile(row, col, true, color, true);
                                break;
                            case "S":
                                System.out.println("spike");
                                Spike spike = new Spike(col * TILE_SIZE, row * TILE_SIZE);
                                tiles[row][col] = spike;
                                break;
                            default:
                                continue;
                        }
                    }
                    row++;
                } else {
                    String[] rowData = line.split(" ");
                    int y =  Integer.parseInt(rowData[1])-1;
                    int x =  Integer.parseInt(rowData[2])-1;
                    switch (rowData[0]) {
                        case "O":
                            Door door = new Door(x * TILE_SIZE, y * TILE_SIZE);
                            doors.add(door);
                            tiles[y][x] = door;
                            break;
                        case "B":
                            ButtonTile button = new ButtonTile(x * TILE_SIZE, y * TILE_SIZE,
                                    new ArrayList<>(doors));
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

    public static void stopLevel() {
        timer.stop();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Tile[] tileRow : tiles) {
            for(Tile tile : tileRow) {
                if(tile != null) {
                    tile.draw(g);
                }
            }
        }
        p1.draw(g);
        p2.draw(g);
        if(p1.getDist() >= World.TETHER_DIST - 20) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5.0f));
        g2d.drawLine((int) p1.getPx() + p1.getWidth() / 2, (int) p1.getPy() + p1.getHeight() / 2,
                (int) p2.getPx() + p2.getWidth() / 2, (int) p2.getPy() + p2.getHeight() / 2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }
}
