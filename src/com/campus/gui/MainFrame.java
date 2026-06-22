package com.campus.gui;

import com.campus.repository.DataManager;
import com.campus.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private DataManager dataManager;
    private AuthenticationService authService;
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;

    public MainFrame(DataManager dataManager, AuthenticationService authService) {
        this.dataManager = dataManager;
        this.authService = authService;
        
        setupWindow();
        initializeNavigation();
        startBackgroundTasks();
    }

    private void setupWindow() {
        this.setTitle("University Management System");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1100, 800);
        this.setLocationRelativeTo(null);
    }

    private void initializeNavigation() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        loginPanel = new LoginPanel(this, authService);
        mainPanel.add(loginPanel, "LOGIN");
        
        this.add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
    }

    private void startBackgroundTasks() {
        // Setup Auto-Save Timer (every 5 minutes)
        Timer autoSaveTimer = new Timer(300000, e -> dataManager.saveAllData());
        autoSaveTimer.start();
    }
    
    public void showDashboard() {
        if (dashboardPanel != null) {
            mainPanel.remove(dashboardPanel);
        }
        dashboardPanel = new DashboardPanel(this, dataManager, authService);
        mainPanel.add(dashboardPanel, "DASHBOARD");
        cardLayout.show(mainPanel, "DASHBOARD");
    }
    
    public void logout() {
        authService.logout();
        dataManager.saveAllData();
        cardLayout.show(mainPanel, "LOGIN");
    }
}
