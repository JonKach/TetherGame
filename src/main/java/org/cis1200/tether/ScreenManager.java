package org.cis1200.tether;

import org.cis1200.tether.UI.BackgroundPanel;
import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.screens.TitleScreen;
import org.cis1200.tether.world.Tile;
import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;

public class ScreenManager extends JPanel {

    JPanel currScreen;

    public ScreenManager() {
        setOpaque(false);
        setFocusable(true);
        setLayout(new BorderLayout());
        setScreen(Screen.TITLE_SCREEN);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("yo");
    }

    public void setScreen(Screen screen) {
        this.removeAll();
        switch (screen) {
            case TITLE_SCREEN:
                currScreen = new TitleScreen(this);
                SwingUtilities.invokeLater(currScreen::requestFocusInWindow);
                break;
            case LEVEL_1:
                currScreen = new BackgroundPanel(BackgroundPanel.Background.BROWN);
                currScreen.setLayout(new OverlayLayout(currScreen));

                final UIView ui = new UIView();
                ui.setPreferredSize(new Dimension(1000, 500));
                ui.setAlignmentX(0.5f);
                ui.setAlignmentY(0.5f);

                final World world = new World("files/level_1.txt", 50, 350, 60, 350);
                world.setPreferredSize(new Dimension(1000, 500));
                world.setAlignmentX(0.5f);
                world.setAlignmentY(0.5f);

                currScreen.add(ui);
                currScreen.add(world);
                SwingUtilities.invokeLater(world::requestFocusInWindow);
                break;
            default:
                currScreen = new JPanel();
                currScreen.setBackground(Color.white);
                currScreen.add(new JLabel("Default Screen"));
        }
        this.add(currScreen, BorderLayout.CENTER);
        this.revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 500);
    }

    public enum Screen {
        TITLE_SCREEN,
        INSTRUCTIONS_SCREEN,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
    }
}
