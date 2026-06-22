package com.campus.gui;

import javax.swing.*;
import java.awt.*;

public class FormUtility {
    public static void buildForm(JPanel formPanel, String[] labels, JComponent[] components, JComponent submitButton) {
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < labels.length; i++) {
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = 1;
            formPanel.add(components[i], gbc);
        }

        if (submitButton != null) {
            gbc.gridx = 0;
            gbc.gridy = labels.length;
            gbc.gridwidth = 2;
            formPanel.add(submitButton, gbc);
        }
    }
}
