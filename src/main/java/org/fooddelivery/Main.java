package org.fooddelivery;

import org.fooddelivery.api.ApiServer;
import org.fooddelivery.ui.LoginScreen;
import org.fooddelivery.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // SOAP API server start
        try {
            ApiServer.startAll();
        } catch (Exception e) {
            System.out.println("API Server error: " + e.getMessage());
        }

        // Swing UI start
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = MainFrame.getInstance();
            frame.showPanel(new LoginScreen());
            frame.setVisible(true);
        });
    }
}