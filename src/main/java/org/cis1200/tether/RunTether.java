package org.cis1200.tether;

import org.cis1200.tether.UI.BackgroundPanel;
import org.cis1200.tether.UI.UIView;
import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;

public class RunTether implements Runnable {

    JPanel currPanel;
    final JFrame frame = new JFrame("Tether");

    @Override
    public void run() {
        // Top-level frame in which game components live.
        frame.setLocation(1000, 500);
        frame.getContentPane().setBackground(new Color(135, 206, 235));

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        JPanel containerPanel = new BackgroundPanel(BackgroundPanel.Background.BROWN);
//        containerPanel.setBackground(new Color(135, 206, 235));
        containerPanel.setLayout(new OverlayLayout(containerPanel));

        final UIView ui = new UIView();
        ui.setPreferredSize(new Dimension(1000, 500));
        ui.setAlignmentX(0.5f);
        ui.setAlignmentY(0.5f);

        final World world = new World("files/level_1.txt", 50, 350, 60, 350);
        world.setPreferredSize(new Dimension(1000, 500));
        world.setAlignmentX(0.5f);
        world.setAlignmentY(0.5f);

        containerPanel.add(ui);
        containerPanel.add(world);

//        // Main playing area
//        final World world = new World("files/level_1.txt");
//        frame.add(world, BorderLayout.CENTER);


        frame.add(containerPanel, BorderLayout.CENTER);
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void changePanel() {

    }
}
