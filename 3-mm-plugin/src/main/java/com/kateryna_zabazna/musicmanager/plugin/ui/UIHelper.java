package com.kateryna_zabazna.musicmanager.plugin.ui;

import javax.swing.*;
import java.awt.*;

public class UIHelper {

    // Private constructor to hide the public one (Good according to SonarCloud)
    private UIHelper() {}

    public static void placeUIComp(JPanel rootPanel, JComponent component, int gridX, int gridY,
                                   int gridWidth, int gridHeight, double weightX, double weightY, int fill) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = gridX;
        constraints.gridy = gridY;
        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;
        constraints.weightx = weightX;
        constraints.weighty = weightY;
        constraints.fill = fill;
        constraints.insets = new Insets(2, 2, 2, 2);
        rootPanel.add(component, constraints);
    }
}
