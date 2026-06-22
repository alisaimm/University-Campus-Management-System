package com.campus.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for our custom UI theme.
 * Keeps colors and styles in one place so everything looks consistent.
 */
public class UITheme {
    // Color palette
    public static final Color SIDEBAR_BG = new Color(44, 62, 80);
    public static final Color SIDEBAR_HOVER = new Color(52, 73, 94);
    public static final Color SIDEBAR_SELECTED = new Color(41, 128, 185);
    public static final Color HEADER_BG = new Color(236, 240, 241);
    public static final Color CONTENT_BG = new Color(248, 249, 250);
    public static final Color ACCENT = new Color(41, 128, 185);
    public static final Color TEXT_LIGHT = new Color(236, 240, 241);
    public static final Color TEXT_DARK = new Color(44, 62, 80);
    public static final Color DANGER = new Color(192, 57, 43);

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font SIDEBAR_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SIDEBAR_FONT_SELECTED = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Creates a styled sidebar button that looks flat (not like a regular JButton).
     */
    public static JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(SIDEBAR_FONT);
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    /**
     * Creates a small styled action button for bottom panels.
     */
    public static JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BODY_FONT);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Creates a danger-colored button.
     */
    public static JButton createDangerButton(String text) {
        JButton btn = createActionButton(text);
        btn.setBackground(DANGER);
        return btn;
    }

    /**
     * Styles a JTable with our theme fonts and row height.
     */
    public static void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(HEADER_BG);
        table.getTableHeader().setForeground(TEXT_DARK);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setGridColor(new Color(220, 220, 220));
    }
}
