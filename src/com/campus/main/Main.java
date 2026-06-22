package com.campus.main;

import com.campus.gui.MainFrame;
import com.campus.repository.DataManager;
import com.campus.service.AuthenticationService;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize DataManager and load data
        DataManager dataManager = new DataManager();
        dataManager.loadAllData();
        dataManager.seedDefaultUsers();

        // Initialize Authentication Service
        AuthenticationService authService = new AuthenticationService(dataManager.getUserRepo());

        // Launch the GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(dataManager, authService);
            mainFrame.setVisible(true);
        });
        
        // Add a shutdown hook to save data before exiting just in case
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving all data before application exit...");
            dataManager.saveAllData();
        }));
    }
}
