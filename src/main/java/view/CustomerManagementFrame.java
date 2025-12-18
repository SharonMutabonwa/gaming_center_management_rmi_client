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
import model.Customer;
import model.User;
import rmi.RemoteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import model.Gender;
import model.UserRole;

/**
 * CustomerManagementFrame - Customer CRUD Operations with JTable
 */
public class CustomerManagementFrame extends JFrame {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    public CustomerManagementFrame() {
        initComponents();
        loadCustomers();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Customer Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel with search
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBackground(new Color(240, 242, 245));

        JLabel lblTitle = new JLabel("👥 Customer Management");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 152, 219));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(240, 242, 245));

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Arial", Font.PLAIN, 14));

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchCustomers();
            }
        });

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Table model
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Phone", "Gender", "DOB", "Balance (RWF)", "Hours Played"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(52, 152, 219, 50));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(null);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 242, 245));

        btnAdd = createButton("Add Customer", new Color(46, 204, 113));
        btnEdit = createButton("Edit Customer", new Color(241, 196, 15));
        btnDelete = createButton("Delete Customer", new Color(231, 76, 60));
        btnRefresh = createButton("Refresh", new Color(52, 152, 219));

        btnAdd.addActionListener(e -> addCustomer());
        btnEdit.addActionListener(e -> editCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnRefresh.addActionListener(e -> loadCustomers());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        return buttonPanel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    /**
     * Load all customers into table
     */
    private void loadCustomers() {
        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                RemoteService service = RMIClient.getService();
                return service.getAllCustomers();
            }

            @Override
            protected void done() {
                try {
                    List<Customer> customers = get();
                    updateTable(customers);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CustomerManagementFrame.this,
                        "Error loading customers: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    /**
     * Search customers
     */
    private void searchCustomers() {
        String searchTerm = txtSearch.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadCustomers();
            return;
        }

        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                RemoteService service = RMIClient.getService();
                return service.searchCustomersByName(searchTerm);
            }

            @Override
            protected void done() {
                try {
                    List<Customer> customers = get();
                    updateTable(customers);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CustomerManagementFrame.this,
                        "Error searching customers: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    /**
     * Update table with customer data
     */
    private void updateTable(List<Customer> customers) {
        tableModel.setRowCount(0);
        
        for (Customer customer : customers) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getUser().getEmail(),
                customer.getUser().getPhoneNumber() != null ? customer.getUser().getPhoneNumber() : "N/A",
                customer.getGender().toString(),
                customer.getDateOfBirth().toString(),
                customer.getAccountBalance().toString(),
                customer.getTotalHoursPlayed()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Add new customer
     */
    private void addCustomer() {
    JDialog dialog = new JDialog(this, "Add Customer", true);
    dialog.setSize(500, 600);
    dialog.setLocationRelativeTo(this);

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    // Form fields
    JTextField txtUsername = new JTextField(20);
    JPasswordField txtPassword = new JPasswordField(20);
    JTextField txtEmail = new JTextField(20);
    JTextField txtPhone = new JTextField(20);
    JTextField txtFirstName = new JTextField(20);
    JTextField txtLastName = new JTextField(20);
    JComboBox<String> cmbGender = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"});
    JTextField txtDOB = new JTextField(20);
    txtDOB.setText("2000-01-01");
    JTextField txtAddress = new JTextField(20);
    JTextField txtAccountBalance = new JTextField(20);  // New field for account balance

    // Add components
    int row = 0;
    addFormField(panel, gbc, "Username:", txtUsername, row++);
    addFormField(panel, gbc, "Password:", txtPassword, row++);
    addFormField(panel, gbc, "Email:", txtEmail, row++);
    addFormField(panel, gbc, "Phone:", txtPhone, row++);
    addFormField(panel, gbc, "First Name:", txtFirstName, row++);
    addFormField(panel, gbc, "Last Name:", txtLastName, row++);
    addFormField(panel, gbc, "Gender:", cmbGender, row++);
    addFormField(panel, gbc, "Date of Birth (YYYY-MM-DD):", txtDOB, row++);
    addFormField(panel, gbc, "Address:", txtAddress, row++);
    addFormField(panel, gbc, "Initial Balance (RWF):", txtAccountBalance, row++);  // Added balance field

    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    btnSave.addActionListener(e -> {
        // Validation and save
        try {
            RemoteService service = RMIClient.getService();

            // Validate account balance input
            BigDecimal initialBalance = BigDecimal.ZERO;
            try {
                initialBalance = new BigDecimal(txtAccountBalance.getText().trim());
                if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new NumberFormatException("Balance cannot be negative.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid balance.", "Invalid Balance", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create user
            User user = new User();
            user.setUsername(txtUsername.getText().trim());
            user.setPassword(new String(txtPassword.getPassword()).trim());
            user.setEmail(txtEmail.getText().trim());
            user.setPhoneNumber(txtPhone.getText().trim());
            user.setRole(UserRole.CUSTOMER);
            
            user = service.createUser(user);

            // Create customer
            Customer customer = new Customer();
            customer.setUser(user);
            customer.setFirstName(txtFirstName.getText().trim());
            customer.setLastName(txtLastName.getText().trim());
            customer.setGender(Gender.valueOf(cmbGender.getSelectedItem().toString()));
            customer.setDateOfBirth(LocalDate.parse(txtDOB.getText().trim()));
            customer.setAddress(txtAddress.getText().trim());
            customer.setAccountBalance(initialBalance);  // Set initial balance

            service.createCustomer(customer);

            JOptionPane.showMessageDialog(dialog, "Customer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadCustomers();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnCancel.addActionListener(e -> dialog.dispose());

    buttonPanel.add(btnSave);
    buttonPanel.add(btnCancel);

    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    panel.add(buttonPanel, gbc);

    dialog.add(new JScrollPane(panel));
    dialog.setVisible(true);
}


    /**
     * Edit selected customer
     */
    private void editCustomer() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Please select a customer to edit",
            "No Selection",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Get customerId from the selected row
    Long customerId = (Long) tableModel.getValueAt(selectedRow, 0);

    // Fetch the selected customer data
    SwingWorker<Customer, Void> worker = new SwingWorker<>() {
        @Override
        protected Customer doInBackground() throws Exception {
            RemoteService service = RMIClient.getService();
            return service.getCustomerById(customerId); // Assume a method to fetch customer by ID
        }

        @Override
        protected void done() {
            try {
                Customer customer = get();
                if (customer != null) {
                    showEditDialog(customer); // Show the dialog to edit the customer details
                } else {
                    JOptionPane.showMessageDialog(CustomerManagementFrame.this,
                        "Customer not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CustomerManagementFrame.this,
                    "Error fetching customer details: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    worker.execute();
}

private void showEditDialog(Customer customer) {
    JDialog dialog = new JDialog(this, "Edit Customer", true);
    dialog.setSize(500, 600);
    dialog.setLocationRelativeTo(this);

    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(5, 5, 5, 5);

    // Form fields (pre-filled with the customer's current data)
    JTextField txtUsername = new JTextField(customer.getUser().getUsername(), 20);
    JPasswordField txtPassword = new JPasswordField(customer.getUser().getPassword(), 20);
    JTextField txtEmail = new JTextField(customer.getUser().getEmail(), 20);
    JTextField txtPhone = new JTextField(customer.getUser().getPhoneNumber(), 20);
    JTextField txtFirstName = new JTextField(customer.getFirstName(), 20);
    JTextField txtLastName = new JTextField(customer.getLastName(), 20);
    JComboBox<String> cmbGender = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"});
    cmbGender.setSelectedItem(customer.getGender().toString());
    JTextField txtDOB = new JTextField(customer.getDateOfBirth().toString(), 20);
    JTextField txtAddress = new JTextField(customer.getAddress(), 20);
    JTextField txtAccountBalance = new JTextField(customer.getAccountBalance().toString(), 20);  // Pre-fill balance

    // Add components
    int row = 0;
    addFormField(panel, gbc, "Username:", txtUsername, row++);
    addFormField(panel, gbc, "Password:", txtPassword, row++);
    addFormField(panel, gbc, "Email:", txtEmail, row++);
    addFormField(panel, gbc, "Phone:", txtPhone, row++);
    addFormField(panel, gbc, "First Name:", txtFirstName, row++);
    addFormField(panel, gbc, "Last Name:", txtLastName, row++);
    addFormField(panel, gbc, "Gender:", cmbGender, row++);
    addFormField(panel, gbc, "Date of Birth (YYYY-MM-DD):", txtDOB, row++);
    addFormField(panel, gbc, "Address:", txtAddress, row++);
    addFormField(panel, gbc, "Account Balance (RWF):", txtAccountBalance, row++);  // Pre-fill balance

    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnSave = new JButton("Save");
    JButton btnCancel = new JButton("Cancel");

    btnSave.addActionListener(e -> {
        // Validation and save
        try {
            RemoteService service = RMIClient.getService();

            // Validate account balance input
            BigDecimal initialBalance = BigDecimal.ZERO;
            try {
                initialBalance = new BigDecimal(txtAccountBalance.getText().trim());
                if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new NumberFormatException("Balance cannot be negative.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid balance.", "Invalid Balance", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update user data
            customer.getUser().setUsername(txtUsername.getText().trim());
            customer.getUser().setPassword(new String(txtPassword.getPassword()).trim());
            customer.getUser().setEmail(txtEmail.getText().trim());
            customer.getUser().setPhoneNumber(txtPhone.getText().trim());

            // Update customer data
            customer.setFirstName(txtFirstName.getText().trim());
            customer.setLastName(txtLastName.getText().trim());
            customer.setGender(Gender.valueOf(cmbGender.getSelectedItem().toString()));
            customer.setDateOfBirth(LocalDate.parse(txtDOB.getText().trim()));
            customer.setAddress(txtAddress.getText().trim());
            customer.setAccountBalance(initialBalance);  // Update balance

            // Update the customer via service
            service.updateCustomer(customer);

            JOptionPane.showMessageDialog(dialog, "Customer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadCustomers();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    btnCancel.addActionListener(e -> dialog.dispose());

    buttonPanel.add(btnSave);
    buttonPanel.add(btnCancel);

    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 2;
    panel.add(buttonPanel, gbc);

    dialog.add(new JScrollPane(panel));
    dialog.setVisible(true);
}


    /**
     * Delete selected customer
     */
    private void deleteCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a customer to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this customer?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Long customerId = (Long) tableModel.getValueAt(selectedRow, 0);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    RemoteService service = RMIClient.getService();
                    service.deleteCustomer(customerId);
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        JOptionPane.showMessageDialog(CustomerManagementFrame.this,
                            "Customer deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        loadCustomers();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(CustomerManagementFrame.this,
                            "Error deleting customer: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };
            worker.execute();
        }
    }

    /**
     * Helper method to add form fields
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent component, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        component.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(component, gbc);
    }
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerManagementFrame().setVisible(true);
        });
    }
}
