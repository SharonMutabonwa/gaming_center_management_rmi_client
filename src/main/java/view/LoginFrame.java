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
import model.Customer;
import model.User;
import rmi.RemoteService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame - Login GUI with OTP Verification
 */
public class LoginFrame extends JFrame {
    
    // UI Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtOTP;
    private JButton btnRequestOTP;
    private JButton btnLogin;
    private JButton btnCancel;
    private JLabel lblStatus;
    private JPanel otpPanel;
    private Timer countdownTimer;
    private int countdown = 300; // 5 minutes in seconds

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null);
        
        // Test server connection on startup
        if (!RMIClient.isConnected()) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "Cannot connect to server. Do you want to retry?",
                "Connection Error",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                RMIClient.reconnect();
            }
        }
    }

    private void initComponents() {
        setTitle("Gaming Center - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(102, 126, 234);
                Color color2 = new Color(118, 75, 162);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);

        // Logo/Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBounds(50, 30, 400, 100);
        titlePanel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("🎮 Gaming Center", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Management System", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        titlePanel.add(lblSubtitle, BorderLayout.SOUTH);

        // Login Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBounds(50, 150, 400, 380);
        cardPanel.setLayout(null);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Welcome Label
        JLabel lblWelcome = new JLabel("Welcome Back!", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(new Color(102, 126, 234));
        lblWelcome.setBounds(0, 10, 340, 30);
        cardPanel.add(lblWelcome);

        // Username Field
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUsername.setBounds(20, 60, 100, 25);
        cardPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(20, 85, 300, 35);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cardPanel.add(txtUsername);

        // Password Field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setBounds(20, 130, 100, 25);
        cardPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(20, 155, 300, 35);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cardPanel.add(txtPassword);

        // Request OTP Button
        btnRequestOTP = new JButton("Request OTP");
        btnRequestOTP.setBounds(20, 205, 300, 40);
        btnRequestOTP.setFont(new Font("Arial", Font.BOLD, 14));
        btnRequestOTP.setBackground(new Color(102, 126, 234));
        btnRequestOTP.setForeground(Color.WHITE);
        btnRequestOTP.setFocusPainted(false);
        btnRequestOTP.setBorderPainted(false);
        btnRequestOTP.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRequestOTP.addActionListener(e -> requestOTP());
        cardPanel.add(btnRequestOTP);

        // OTP Panel (initially hidden)
        otpPanel = new JPanel();
        otpPanel.setLayout(null);
        otpPanel.setBackground(Color.WHITE);
        otpPanel.setBounds(0, 255, 340, 100);
        otpPanel.setVisible(false);
        cardPanel.add(otpPanel);

        JLabel lblOTP = new JLabel("Enter OTP (sent to your email):");
        lblOTP.setFont(new Font("Arial", Font.PLAIN, 12));
        lblOTP.setForeground(new Color(100, 100, 100));
        lblOTP.setBounds(20, 0, 300, 25);
        otpPanel.add(lblOTP);

        txtOTP = new JTextField();
        txtOTP.setBounds(20, 25, 300, 35);
        txtOTP.setFont(new Font("Arial", Font.PLAIN, 16));
        txtOTP.setHorizontalAlignment(JTextField.CENTER);
        txtOTP.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(102, 126, 234), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        otpPanel.add(txtOTP);

        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(102, 126, 234));
        lblStatus.setBounds(20, 65, 300, 20);
        otpPanel.add(lblStatus);

        // Login Button (initially hidden)
        btnLogin = new JButton("Login");
        btnLogin.setBounds(20, 305, 145, 40);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setVisible(false);
        btnLogin.addActionListener(e -> login());
        cardPanel.add(btnLogin);

        // Cancel Button
        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(175, 305, 145, 40);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setVisible(false);
        btnCancel.addActionListener(e -> cancelOTP());
        cardPanel.add(btnCancel);

        // Add panels to main panel
        mainPanel.add(titlePanel);
        mainPanel.add(cardPanel);

        add(mainPanel);

        // Add key listeners for Enter key
        txtUsername.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtPassword.requestFocus();
                }
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    requestOTP();
                }
            }
        });

        txtOTP.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
    }

    /**
     * Request OTP from server
     */
    private void requestOTP() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Validation
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter username", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter password", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        // Show loading
        btnRequestOTP.setEnabled(false);
        btnRequestOTP.setText("Sending OTP...");

        // Use SwingWorker for background operation
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                RemoteService service = RMIClient.getService();
                return service.generateAndSendOTP(username);
            }

            @Override
            protected void done() {
                try {
                    boolean sent = get();
                    if (sent) {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                            "OTP sent to your email!\nPlease check your inbox.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Show OTP panel
                        otpPanel.setVisible(true);
                        btnLogin.setVisible(true);
                        btnCancel.setVisible(true);
                        btnRequestOTP.setVisible(false);
                        txtOTP.requestFocus();
                        
                        // Start countdown
                        startCountdown();
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                            "Failed to send OTP. Please check username and try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        btnRequestOTP.setEnabled(true);
                        btnRequestOTP.setText("Request OTP");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    btnRequestOTP.setEnabled(true);
                    btnRequestOTP.setText("Request OTP");
                }
            }
        };
        worker.execute();
    }

    /**
     * Login with OTP
     */
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String otp = txtOTP.getText().trim();

        // Validation
        if (otp.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter OTP",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            txtOTP.requestFocus();
            return;
        }

        // Show loading
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() throws Exception {
                RemoteService service = RMIClient.getService();
                return service.login(username, password, otp);
            }

            @Override
            protected void done() {
                try {
                    User user = get();
                    if (user != null) {
                        // Stop countdown
                        if (countdownTimer != null) {
                            countdownTimer.stop();
                        }

                        // Set session
                        SessionManager.setCurrentUser(user);

                        // If customer, get customer details
                        if (user.getRole().toString().equals("CUSTOMER")) {
                            RemoteService service = RMIClient.getService();
                            Customer customer = service.getCustomerByUserId(user.getUserId());
                            SessionManager.setCurrentCustomer(customer);
                        }

                        JOptionPane.showMessageDialog(LoginFrame.this,
                            "Welcome, " + user.getUsername() + "!",
                            "Login Successful",
                            JOptionPane.INFORMATION_MESSAGE);

                        // Open main dashboard
                        dispose();
                        new MainDashboardFrame().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                            "Login failed. Please check your credentials and OTP.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Login");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                        "Error: " + ex.getMessage(),
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login");
                }
            }
        };
        worker.execute();
    }

    /**
     * Cancel OTP and reset form
     */
    private void cancelOTP() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        
        otpPanel.setVisible(false);
        btnLogin.setVisible(false);
        btnCancel.setVisible(false);
        btnRequestOTP.setVisible(true);
        btnRequestOTP.setEnabled(true);
        btnRequestOTP.setText("Request OTP");
        txtOTP.setText("");
        countdown = 300;
    }

    /**
     * Start OTP countdown timer (5 minutes)
     */
    private void startCountdown() {
        countdown = 300; // 5 minutes
        
        countdownTimer = new Timer(1000, e -> {
            countdown--;
            int minutes = countdown / 60;
            int seconds = countdown % 60;
            lblStatus.setText(String.format("OTP expires in %d:%02d", minutes, seconds));
            
            if (countdown <= 0) {
                countdownTimer.stop();
                lblStatus.setText("OTP expired. Please request a new one.");
                lblStatus.setForeground(new Color(231, 76, 60));
                btnLogin.setEnabled(false);
            }
        });
        countdownTimer.start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
