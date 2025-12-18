/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Odeth
 */


import rmi.RMIClient;
import view.LoginFrame;

import javax.swing.*;

/**
 * Main - Client Application Entry Point
 * Gaming Center Management System Client
 */
public class Main {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show splash screen
        showSplashScreen();

        // Connect to server
        SwingUtilities.invokeLater(() -> {
            boolean connected = RMIClient.connect();
            
            if (connected) {
                // Open login frame
                new LoginFrame().setVisible(true);
            } else {
                // Show connection error
                int result = JOptionPane.showConfirmDialog(
                    null,
                    "Cannot connect to server at " + RMIClient.getServerHost() + ":" + RMIClient.getServerPort() + 
                    "\n\nPlease ensure:\n" +
                    "1. Server is running\n" +
                    "2. Port " + RMIClient.getServerPort() + " is accessible\n" +
                    "3. No firewall blocking the connection\n\n" +
                    "Do you want to retry?",
                    "Connection Error",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    main(args); // Retry
                } else {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Show splash screen while connecting
     */
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(500, 300);
        splash.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, 
                                    java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                java.awt.Color color1 = new java.awt.Color(102, 126, 234);
                java.awt.Color color2 = new java.awt.Color(118, 75, 162);
                java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panel.setLayout(new java.awt.BorderLayout());

        JLabel lblTitle = new JLabel("🎮 Gaming Center", SwingConstants.CENTER);
        lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 36));
        lblTitle.setForeground(java.awt.Color.WHITE);

        JLabel lblSubtitle = new JLabel("Management System", SwingConstants.CENTER);
        lblSubtitle.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
        lblSubtitle.setForeground(new java.awt.Color(255, 255, 255, 200));

        JLabel lblLoading = new JLabel("Connecting to server...", SwingConstants.CENTER);
        lblLoading.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        lblLoading.setForeground(java.awt.Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblTitle);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lblSubtitle);
        centerPanel.add(Box.createVerticalStrut(30));
        centerPanel.add(lblLoading);
        centerPanel.add(Box.createVerticalGlue());

        panel.add(centerPanel, java.awt.BorderLayout.CENTER);

        splash.setContentPane(panel);
        splash.setVisible(true);

        // Close splash after 2 seconds
        Timer timer = new Timer(2000, e -> splash.dispose());
        timer.setRepeats(false);
        timer.start();
    }
}
