package org.cis1200.tether;

import org.cis1200.tether.UI.StatusPanel;

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
        frame.setResizable(false);

        // Status panel
        final JPanel status_panel = new StatusPanel();
        frame.add(status_panel, BorderLayout.SOUTH);

        // Game display panel
        currPanel = new ScreenManager();

        frame.add(currPanel, BorderLayout.CENTER);
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
