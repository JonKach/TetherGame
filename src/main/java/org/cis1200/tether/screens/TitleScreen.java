package org.cis1200.tether.screens;

import org.cis1200.tether.ScreenManager;
import org.cis1200.tether.UI.UIButton;
import org.cis1200.tether.utility.SpriteSheetLoader;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class TitleScreen extends JPanel {

    ScreenManager screenManager;

    BufferedImage bgImage;
    TexturePaint texture;
    UIButton play_button;
    UIButton instructions_button;

    public TitleScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        setLayout(null);
        setBounds(0, 0, 1000, 500);
        bgImage = SpriteSheetLoader.loadImage("files/Blue.png");
        texture = new TexturePaint(
                bgImage, new Rectangle(0, 0, bgImage.getWidth(), bgImage.getHeight())
        );
        play_button = new UIButton(
                400, 200, 200, 200, 10,
                SpriteSheetLoader.loadImage("files/Play.png"), null
        );
        play_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                screenManager.setScreen(ScreenManager.Screen.LEVELS_SCREEN);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                play_button.bounce(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                play_button.bounce(false);
            }
        });
        this.add(play_button);
        instructions_button = new UIButton(300, 400, 400, 50, 5, null, "Instructions");
        instructions_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // screenManager.setScreen(ScreenManager.Screen.INSTRUCTIONS_SCREEN);
                System.out.println("instructions");
                JOptionPane.showMessageDialog(
                        TitleScreen.this,
                        "Welcome to Tethered!\n" +
                                "\n" +
                                "This is a 2-player hotseat platformer game, where you and a friend will race \n"
                                +
                                "through a series of difficult levels, trying to avoid the ever encroaching wall \n"
                                +
                                "of lava behind you and the countless spikes in your path. The twist is that both \n"
                                +
                                "players are tethered together, so don’t get too far apart! One player falling may \n"
                                +
                                "bring the other player down as well. \n" +
                                "\n" +
                                "Player 1 will use WASD to move\n" +
                                "Player 2 will use the Arrow Keys to move\n" +
                                "\n" +
                                "Additional Tips:\n" +
                                "- Press red buttons to open stone doors! \n" +
                                "- Collect power ups!\n" +
                                "   - Jump once while in the air using Double Jump Power Up\n" +
                                "   - Player 1 can press E and Player 2 can press M to dash using "
                                +
                                "the Dash Power Up\n" +
                                "   - Players can move without being pulled back by the tether using the Untether "
                                +
                                "Power Up"

                );
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                instructions_button.bounce(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                instructions_button.bounce(false);
            }
        });
        this.add(instructions_button);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(texture);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        char[] title = "TETHERED".toCharArray();
        g.setFont(new Font("Arial", Font.ITALIC, 96));
        g.setColor(Color.black);
        g.drawChars(title, 0, title.length, 250, 150);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }
}
