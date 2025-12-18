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
import model.*;
import rmi.RemoteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * BookingManagementFrame - Booking CRUD with validation
 */
public class BookingManagementFrame extends JFrame {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilterStatus;

    public BookingManagementFrame() {
        initComponents();
        loadBookings();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Booking Management");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 242, 245));

        JLabel lblTitle = new JLabel("📅 Booking Management");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(46, 204, 113));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(new Color(240, 242, 245));
        filterPanel.add(new JLabel("Filter by Status:"));
        cmbFilterStatus = new JComboBox<>(new String[]{"ALL", "PENDING", "CONFIRMED", "ONGOING", "COMPLETED", "CANCELLED"});
        cmbFilterStatus.addActionListener(e -> filterBookings());
        filterPanel.add(cmbFilterStatus);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Customer", "Station", "Date", "Start Time", "End Time", "Duration (hrs)", "Amount (RWF)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(46, 204, 113));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));

        JButton btnNew = new JButton("New Booking");
        btnNew.setBackground(new Color(46, 204, 113));
        btnNew.setForeground(Color.WHITE);
        btnNew.setFont(new Font("Arial", Font.BOLD, 14));
        btnNew.setFocusPainted(false);
        btnNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNew.addActionListener(e -> createNewBooking());

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(52, 152, 219));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.setFocusPainted(false);
        btnRefresh.addActionListener(e -> loadBookings());

        buttonPanel.add(btnNew);
        buttonPanel.add(btnRefresh);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBookings() {
        SwingWorker<List<Booking>, Void> worker = new SwingWorker<>() {
            protected List<Booking> doInBackground() throws Exception {
                return RMIClient.getService().getAllBookings();
            }
            protected void done() {
                try {
                    updateTable(get());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(BookingManagementFrame.this,
                        "Error loading bookings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void filterBookings() {
        // Implementation for filtering
        loadBookings();
    }

    private void updateTable(List<Booking> bookings) {
        tableModel.setRowCount(0);
        for (Booking booking : bookings) {
            Object[] row = {
                booking.getBookingId(),
                booking.getCustomer().getFullName(),
                booking.getGamingStation().getStationName(),
                booking.getBookingDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getDurationHours(),
                booking.getTotalAmount(),
                booking.getStatus()
            };
            tableModel.addRow(row);
        }
    }

    private void createNewBooking() {
        JDialog dialog = new JDialog(this, "Create New Booking", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Load customers and stations
        try {
            RemoteService service = RMIClient.getService();
            List<Customer> customers = service.getAllCustomers();
            List<GamingStation> stations = service.getAvailableStations();

            JComboBox<String> cmbCustomer = new JComboBox<>();
            for (Customer c : customers) {
                cmbCustomer.addItem(c.getCustomerId() + " - " + c.getFullName());
            }

            JComboBox<String> cmbStation = new JComboBox<>();
            for (GamingStation s : stations) {
                cmbStation.addItem(s.getStationId() + " - " + s.getStationName());
            }

            JTextField txtDate = new JTextField(LocalDate.now().toString());
            JTextField txtStartTime = new JTextField("09:00");
            JTextField txtEndTime = new JTextField("11:00");

            int row = 0;
            addField(panel, gbc, "Customer:", cmbCustomer, row++);
            addField(panel, gbc, "Gaming Station:", cmbStation, row++);
            addField(panel, gbc, "Date (YYYY-MM-DD):", txtDate, row++);
            addField(panel, gbc, "Start Time (HH:MM):", txtStartTime, row++);
            addField(panel, gbc, "End Time (HH:MM):", txtEndTime, row++);

            JButton btnCreate = new JButton("Create Booking");
            btnCreate.setBackground(new Color(46, 204, 113));
            btnCreate.setForeground(Color.WHITE);
            btnCreate.setFont(new Font("Arial", Font.BOLD, 14));

            btnCreate.addActionListener(e -> {
                try {
                    Long customerId = Long.parseLong(cmbCustomer.getSelectedItem().toString().split(" - ")[0]);
                    Long stationId = Long.parseLong(cmbStation.getSelectedItem().toString().split(" - ")[0]);
                    LocalDate date = LocalDate.parse(txtDate.getText().trim());
                    LocalTime startTime = LocalTime.parse(txtStartTime.getText().trim());
                    LocalTime endTime = LocalTime.parse(txtEndTime.getText().trim());

                    Booking booking = service.createBooking(customerId, stationId, date, startTime, endTime);
                    
                    JOptionPane.showMessageDialog(dialog,
                        "Booking created successfully!\nAmount: " + booking.getTotalAmount() + " RWF",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    dialog.dispose();
                    loadBookings();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Error creating booking: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            panel.add(btnCreate, gbc);

            dialog.add(new JScrollPane(panel));
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent component, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }
}
