/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import rmi.RMIClient;
import rmi.RemoteService;

/**
 *
 * @author Odeth
 */
 /**
 * ReportsFrame - Generate and export reports
 */
public class ReportsFrame extends JFrame {
    
    public ReportsFrame() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Reports");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitle = new JLabel("📊 Reports Generator", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(52, 73, 94));

        // Report types
        JPanel reportsPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        reportsPanel.setBackground(new Color(240, 242, 245));

        reportsPanel.add(createReportCard("Customers Report", "Generate customer list with details"));
        reportsPanel.add(createReportCard("Bookings Report", "Generate booking history report"));
        reportsPanel.add(createReportCard("Revenue Report", "Generate financial transaction report"));
        reportsPanel.add(createReportCard("Stations Report", "Generate gaming station utilization report"));

        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(reportsPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createReportCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setBackground(Color.WHITE);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(100, 100, 100));

        leftPanel.add(lblTitle);
        leftPanel.add(lblDesc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnPDF = createExportButton("PDF", new Color(231, 76, 60));
        JButton btnExcel = createExportButton("Excel", new Color(46, 204, 113));
        JButton btnCSV = createExportButton("CSV", new Color(52, 152, 219));

        String reportType = title.split(" ")[0].toUpperCase();
        btnPDF.addActionListener(e -> generateReport(reportType, "PDF"));
        btnExcel.addActionListener(e -> generateReport(reportType, "EXCEL"));
        btnCSV.addActionListener(e -> generateReport(reportType, "CSV"));

        buttonPanel.add(btnPDF);
        buttonPanel.add(btnExcel);
        buttonPanel.add(btnCSV);

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    private JButton createExportButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(80, 35));
        return btn;
    }

    private void generateReport(String reportType, String format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        
        String extension = format.equals("PDF") ? ".pdf" : (format.equals("EXCEL") ? ".xlsx" : ".csv");
        fileChooser.setSelectedFile(new java.io.File(reportType + "_Report" + extension));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                protected Void doInBackground() throws Exception {
                    RemoteService service = RMIClient.getService();
                    
                    if (format.equals("PDF")) {
                        byte[] data = service.generatePDFReport(reportType);
                        try (FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile())) {
                            fos.write(data);
                        }
                    } else if (format.equals("EXCEL")) {
                        byte[] data = service.generateExcelReport(reportType);
                        try (FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile())) {
                            fos.write(data);
                        }
                    } else {
                        String data = service.generateCSVReport(reportType);
                        try (FileOutputStream fos = new FileOutputStream(fileChooser.getSelectedFile())) {
                            fos.write(data.getBytes());
                        }
                    }
                    return null;
                }

                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(ReportsFrame.this,
                            "Report generated successfully!\nSaved to: " + fileChooser.getSelectedFile().getAbsolutePath(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ReportsFrame.this,
                            "Error generating report: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }
}
