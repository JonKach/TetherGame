package org.cis1200.tether;

import org.cis1200.tether.world.World;

import javax.swing.*;
import java.awt.*;

public class RunTether implements Runnable {
    @Override
    public void run() {
        // Top-level frame in which game components live.
        final JFrame frame = new JFrame("Tether");
        frame.setLocation(1000, 500);
        frame.getContentPane().setBackground(new Color(135, 206, 235));

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final World world = new World("files/level_1.txt");
        frame.add(world, BorderLayout.CENTER);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
