/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Odeth
 */

import rmi.RMIClient;
import utils.SessionManager;
//import rw.ac.rca.rmi.DashboardStats;
import model.DashboardStats;
import rmi.RemoteService;

import javax.swing.*;
import java.awt.*;

/**
 * MainDashboardFrame - Main application dashboard
 */
public class MainDashboardFrame extends JFrame {
    
    private JLabel lblTotalCustomers;
    private JLabel lblTotalBookings;
    private JLabel lblActiveStations;
    private JLabel lblUpcomingTournaments;
    private JLabel lblTodayBookings;
    private JLabel lblTotalRevenue;
    private JLabel lblWelcome;

    public MainDashboardFrame() {
        initComponents();
        loadDashboardStats();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Gaming Center - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setResizable(true);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 242, 245));

        // Top bar
        JPanel topBar = createTopBar();
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Center content
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(240, 242, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Statistics cards
        JPanel statsPanel = createStatsPanel();
        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Menu buttons
        JPanel menuPanel = createMenuPanel();
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Create top navigation bar
     */
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(102, 126, 234));
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left side - Welcome message
        lblWelcome = new JLabel("Welcome, " + SessionManager.getFullName() + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);

        // Right side - Logout button
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setPreferredSize(new Dimension(100, 40));
        btnLogout.addActionListener(e -> logout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(new JLabel("Role: " + SessionManager.getCurrentUserRole() + "  ") {{
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 14));
        }});
        rightPanel.add(btnLogout);

        topBar.add(lblWelcome, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        return topBar;
    }

    /**
     * Create statistics panel
     */
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBackground(new Color(240, 242, 245));
        statsPanel.setPreferredSize(new Dimension(0, 250));

        // Create stat cards
        JPanel card1 = createStatCard("Total Customers", "0", new Color(52, 152, 219), "👥");
        JPanel card2 = createStatCard("Total Bookings", "0", new Color(46, 204, 113), "📅");
        JPanel card3 = createStatCard("Active Stations", "0", new Color(155, 89, 182), "🎮");
        JPanel card4 = createStatCard("Upcoming Tournaments", "0", new Color(241, 196, 15), "🏆");
        JPanel card5 = createStatCard("Today's Bookings", "0", new Color(230, 126, 34), "📊");
        JPanel card6 = createStatCard("Total Revenue", "0 RWF", new Color(231, 76, 60), "💰");

        // Store labels for updating
        lblTotalCustomers = (JLabel) ((JPanel) card1.getComponent(1)).getComponent(0);
        lblTotalBookings = (JLabel) ((JPanel) card2.getComponent(1)).getComponent(0);
        lblActiveStations = (JLabel) ((JPanel) card3.getComponent(1)).getComponent(0);
        lblUpcomingTournaments = (JLabel) ((JPanel) card4.getComponent(1)).getComponent(0);
        lblTodayBookings = (JLabel) ((JPanel) card5.getComponent(1)).getComponent(0);
        lblTotalRevenue = (JLabel) ((JPanel) card6.getComponent(1)).getComponent(0);

        statsPanel.add(card1);
        statsPanel.add(card2);
        statsPanel.add(card3);
        statsPanel.add(card4);
        statsPanel.add(card5);
        statsPanel.add(card6);

        return statsPanel;
    }

    /**
     * Create a statistics card
     */
    private JPanel createStatCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Icon
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Arial", Font.PLAIN, 40));
        lblIcon.setPreferredSize(new Dimension(80, 80));
        lblIcon.setOpaque(true);
        lblIcon.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        lblIcon.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 28));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(100, 100, 100));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblValue);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblTitle);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Create menu panel with navigation buttons
     */
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        menuPanel.setBackground(new Color(240, 242, 245));

        // Menu items
        menuPanel.add(createMenuButton("👥 Customers", "Manage customers", 
            new Color(52, 152, 219), () -> openCustomerManagement()));
        menuPanel.add(createMenuButton("📅 Bookings", "Manage bookings", 
            new Color(46, 204, 113), () -> openBookingManagement()));
        menuPanel.add(createMenuButton("🎮 Gaming Stations", "Manage stations", 
            new Color(155, 89, 182), () -> openStationManagement()));
        menuPanel.add(createMenuButton("🎯 Games", "Manage games", 
            new Color(241, 196, 15), () -> openGameManagement()));
        menuPanel.add(createMenuButton("🏆 Tournaments", "Manage tournaments", 
            new Color(230, 126, 34), () -> openTournamentManagement()));
        menuPanel.add(createMenuButton("💳 Membership Cards", "Manage memberships", 
            new Color(231, 76, 60), () -> openMembershipManagement()));
        menuPanel.add(createMenuButton("💰 Transactions", "View transactions", 
            new Color(26, 188, 156), () -> openTransactionHistory()));
        menuPanel.add(createMenuButton("📊 Reports", "Generate reports", 
            new Color(52, 73, 94), () -> openReports()));
        menuPanel.add(createMenuButton("⚙️ Settings", "Application settings", 
            new Color(127, 140, 141), () -> openSettings()));

        return menuPanel;
    }

    /**
     * Create a menu button
     */
    private JPanel createMenuButton(String title, String description, Color color, Runnable action) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(color);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(100, 100, 100));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(lblDesc);

        panel.add(contentPanel, BorderLayout.CENTER);

        // Add hover effect
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(245, 245, 245));
                contentPanel.setBackground(new Color(245, 245, 245));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(Color.WHITE);
                contentPanel.setBackground(Color.WHITE);
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.run();
            }
        });

        return panel;
    }

    /**
     * Load dashboard statistics
     */
    private void loadDashboardStats() {
        SwingWorker<DashboardStats, Void> worker = new SwingWorker<>() {
            @Override
            protected DashboardStats doInBackground() throws Exception {
                RemoteService service = RMIClient.getService();
                return service.getDashboardStats();
            }

            @Override
            protected void done() {
                try {
                    DashboardStats stats = get();
                    lblTotalCustomers.setText(String.valueOf(stats.getTotalCustomers()));
                    lblTotalBookings.setText(String.valueOf(stats.getTotalBookings()));
                    lblActiveStations.setText(String.valueOf(stats.getActiveStations()));
                    lblUpcomingTournaments.setText(String.valueOf(stats.getUpcomingTournaments()));
                    lblTodayBookings.setText(String.valueOf(stats.getTodayBookings()));
                    lblTotalRevenue.setText(String.format("%.0f RWF", stats.getTotalRevenue()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainDashboardFrame.this,
                        "Error loading dashboard stats: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    /**
     * Navigation methods
     */
    private void openCustomerManagement() {
        new CustomerManagementFrame().setVisible(true);
    }

    private void openBookingManagement() {
        new BookingManagementFrame().setVisible(true);
    }

    private void openStationManagement() {
        JOptionPane.showMessageDialog(this, "Gaming Station Management - Coming Soon!");
    }

    private void openGameManagement() {
        JOptionPane.showMessageDialog(this, "Game Management - Coming Soon!");
    }

    private void openTournamentManagement() {
        JOptionPane.showMessageDialog(this, "Tournament Management - Coming Soon!");
    }

    private void openMembershipManagement() {
        JOptionPane.showMessageDialog(this, "Membership Management - Coming Soon!");
    }

    private void openTransactionHistory() {
        JOptionPane.showMessageDialog(this, "Transaction History - Coming Soon!");
    }

    private void openReports() {
        new ReportsFrame().setVisible(true);
    }

    private void openSettings() {
        JOptionPane.showMessageDialog(this, "Settings - Coming Soon!");
    }

    /**
     * Logout
     */
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                RemoteService service = RMIClient.getService();
                service.logout(SessionManager.getSessionToken());
            } catch (Exception ex) {
                // Ignore logout errors
            }

            SessionManager.clearSession();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainDashboardFrame().setVisible(true);
        });
    }
}
