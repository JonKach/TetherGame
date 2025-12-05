package org.cis1200.tether.UI;

import javax.swing.*;

public class StatusPanel extends JPanel {

    static JLabel currLabel;

    public StatusPanel() {
        currLabel = new JLabel("Running...");
        this.add(currLabel);
    }

    public static void setStatusLabel(String status) {
        currLabel.setText(status);
    }
}
