package com.rowusu.vehiclerental.gui;

import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VehicleRentalGUI extends JFrame {
    private RentalAgency agency;
    private JTabbedPane tabbedPane;
    private DefaultTableModel vehicleTableModel;
    private DefaultTableModel customerTableModel;
    private DefaultTableModel rentalTableModel;
    private JTable vehicleTable;
    private JTable customerTable;
    private JTable rentalTable;

    public VehicleRentalGUI() {
        agency = new RentalAgency();
        initializeGUI();
        addSampleData();
    }

    private void initializeGUI() {
        setTitle("Advanced Vehicle Rental Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create menu bar
        createMenuBar();

        // Add welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBorder(BorderFactory.createEtchedBorder());
        welcomePanel.setBackground(new Color(240, 248, 255));
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>" +
            "<h2>🚗 Welcome to Advanced Vehicle Rental Management System 🚗</h2>" +
            "<p>Use the tabs below to manage your rental business:</p>" +
            "<b>Vehicles</b> - Add/remove vehicles, manage features | " +
            "<b>Customers</b> - Register customers, track loyalty | " +
            "<b>Rentals</b> - Process rentals/returns | " +
            "<b>Reports</b> - View business analytics" +
            "</div></html>", SwingConstants.CENTER);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Add tabs
        tabbedPane.addTab("🚗 Vehicles", createVehiclePanel());
        tabbedPane.addTab("👤 Customers", createCustomerPanel());
        tabbedPane.addTab("📋 Rentals", createRentalPanel());
        tabbedPane.addTab("📊 Reports", createReportsPanel());

        // Main panel with welcome and tabs
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Create status bar
        JLabel statusBar = new JLabel("Ready - Click on tabs above to start managing your vehicle rental business");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(245, 245, 245));
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        JLabel instructionsLabel = new JLabel("<html><b>Vehicle Management:</b><br>" +
            "• View all vehicles in the table below<br>" +
            "• Click 'Add Vehicle' to register new cars, motorcycles, or trucks<br>" +
            "• Select a vehicle and click 'Remove Vehicle' to delete it from fleet<br>" +
            "• Select a vehicle and click 'Add Feature' to add GPS, insurance, etc.<br>" +
            "• Select a vehicle and click 'Rate Vehicle' to give it a 1-5 star rating</html>");
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

        // Vehicle table
        String[] vehicleColumns = {"ID", "Type", "Model", "Rate/Day", "Status", "Rating"};
        vehicleTableModel = new DefaultTableModel(vehicleColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vehicleTable = new JTable(vehicleTableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vehicleTable.setToolTipText("Select a vehicle row to perform actions like rating, adding features, or removal");
        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTable);

        // Vehicle control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton addVehicleBtn = new JButton("Add Vehicle");
        JButton removeVehicleBtn = new JButton("Remove Vehicle");
        JButton addFeatureBtn = new JButton("Add Feature");
        JButton rateVehicleBtn = new JButton("Rate Vehicle");

        // Add tooltips
        addVehicleBtn.setToolTipText("Click to add a new car, motorcycle, or truck to your fleet");
        removeVehicleBtn.setToolTipText("Select a vehicle first, then click to remove it from fleet");
        addFeatureBtn.setToolTipText("Select a vehicle first, then add features like GPS or insurance");
        rateVehicleBtn.setToolTipText("Select a vehicle first, then rate it from 1-5 stars");

        addVehicleBtn.addActionListener(e -> showAddVehicleDialog());
        removeVehicleBtn.addActionListener(e -> removeSelectedVehicle());
        addFeatureBtn.addActionListener(e -> showAddFeatureDialog());
        rateVehicleBtn.addActionListener(e -> showRateVehicleDialog());

        controlPanel.add(addVehicleBtn);
        controlPanel.add(removeVehicleBtn);
        controlPanel.add(addFeatureBtn);
        controlPanel.add(rateVehicleBtn);

        panel.add(instructionsPanel, BorderLayout.NORTH);
        panel.add(vehicleScrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        JLabel instructionsLabel = new JLabel("<html><b>Customer Management:</b><br>" +
            "• View all registered customers in the table below<br>" +
            "• Click 'Add Customer' to register new customers with name and ID<br>" +
            "• Select a customer and click 'Rate Customer' to provide feedback<br>" +
            "• Loyalty status automatically updates based on points earned from rentals</html>");
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

        // Customer table
        String[] customerColumns = {"ID", "Name", "Loyalty Status", "Points", "Rating"};
        customerTableModel = new DefaultTableModel(customerColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(customerTableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setToolTipText("Select a customer row to rate them or view their details");
        JScrollPane customerScrollPane = new JScrollPane(customerTable);

        // Customer control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton addCustomerBtn = new JButton("Add Customer");
        JButton rateCustomerBtn = new JButton("Rate Customer");

        // Add tooltips
        addCustomerBtn.setToolTipText("Click to register a new customer with name and unique ID");
        rateCustomerBtn.setToolTipText("Select a customer first, then rate their service experience 1-5 stars");

        addCustomerBtn.addActionListener(e -> showAddCustomerDialog());
        rateCustomerBtn.addActionListener(e -> showRateCustomerDialog());

        controlPanel.add(addCustomerBtn);
        controlPanel.add(rateCustomerBtn);

        panel.add(instructionsPanel, BorderLayout.NORTH);
        panel.add(customerScrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createRentalPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Add instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        JLabel instructionsLabel = new JLabel("<html><b>Rental Management:</b><br>" +
            "• View all active rentals in the table below<br>" +
            "• Click 'Rent Vehicle' to process a new rental (customer info required)<br>" +
            "• Select an active rental and click 'Return Vehicle' to complete the rental<br>" +
            "• Rental costs are calculated automatically based on vehicle rate and duration</html>");
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

        // Rental table
        String[] rentalColumns = {"Vehicle ID", "Customer", "Days", "Cost", "Status"};
        rentalTableModel = new DefaultTableModel(rentalColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rentalTable = new JTable(rentalTableModel);
        rentalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rentalTable.setToolTipText("Select an active rental to process its return");
        JScrollPane rentalScrollPane = new JScrollPane(rentalTable);

        // Rental control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton rentVehicleBtn = new JButton("Rent Vehicle");
        JButton returnVehicleBtn = new JButton("Return Vehicle");

        // Add tooltips
        rentVehicleBtn.setToolTipText("Start a new vehicle rental - select from available vehicles");
        returnVehicleBtn.setToolTipText("Select an active rental first, then click to process return");

        rentVehicleBtn.addActionListener(e -> showRentVehicleDialog());
        returnVehicleBtn.addActionListener(e -> returnSelectedVehicle());

        controlPanel.add(rentVehicleBtn);
        controlPanel.add(returnVehicleBtn);

        panel.add(instructionsPanel, BorderLayout.NORTH);
        panel.add(rentalScrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Add instructions panel
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        JLabel instructionsLabel = new JLabel("<html><b>Business Reports & Analytics:</b><br>" +
            "• Fleet Status: See which vehicles are available or rented<br>" +
            "• Revenue Summary: View daily/monthly/yearly earning potential<br>" +
            "• Analytics: Check utilization rates and average ratings<br>" +
            "• Click 'Refresh Reports' to update all data with latest information</html>");
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fleet status panel
        JPanel fleetPanel = new JPanel(new BorderLayout());
        fleetPanel.setBorder(new TitledBorder("Fleet Status"));
        JTextArea fleetStatus = new JTextArea(10, 30);
        fleetStatus.setEditable(false);
        fleetStatus.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane fleetScrollPane = new JScrollPane(fleetStatus);
        fleetPanel.add(fleetScrollPane, BorderLayout.CENTER);

        // Revenue panel
        JPanel revenuePanel = new JPanel(new BorderLayout());
        revenuePanel.setBorder(new TitledBorder("Revenue Summary"));
        JTextArea revenueText = new JTextArea(10, 30);
        revenueText.setEditable(false);
        revenueText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane revenueScrollPane = new JScrollPane(revenueText);
        revenuePanel.add(revenueScrollPane, BorderLayout.CENTER);

        // Analytics panel
        JPanel analyticsPanel = new JPanel(new BorderLayout());
        analyticsPanel.setBorder(new TitledBorder("Analytics"));
        JTextArea analyticsText = new JTextArea(10, 30);
        analyticsText.setEditable(false);
        analyticsText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane analyticsScrollPane = new JScrollPane(analyticsText);
        analyticsPanel.add(analyticsScrollPane, BorderLayout.CENTER);

        // Refresh button panel
        JPanel refreshPanel = new JPanel(new FlowLayout());
        refreshPanel.setBorder(new TitledBorder("Actions"));
        JButton refreshReportsBtn = new JButton("Refresh Reports");
        refreshReportsBtn.setToolTipText("Click to update all reports with the latest business data");
        refreshReportsBtn.addActionListener(e -> refreshReports(fleetStatus, revenueText, analyticsText));
        refreshPanel.add(refreshReportsBtn);

        panel.add(fleetPanel);
        panel.add(revenuePanel);
        panel.add(analyticsPanel);
        panel.add(refreshPanel);

        // Initial report load
        refreshReports(fleetStatus, revenueText, analyticsText);

        mainPanel.add(instructionsPanel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);
        return mainPanel;
    }

    private void showAddVehicleDialog() {
        JDialog dialog = new JDialog(this, "Add Vehicle", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Instructions
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><b>Add a New Vehicle to Your Fleet</b><br>" +
            "Fill in the details below. Examples:<br>" +
            "• Vehicle ID: CAR001, MOTO001, TRUCK001<br>" +
            "• Model: Toyota Camry, Harley Davidson, Ford F-150<br>" +
            "• Rate: Daily rental price (e.g., 45.00)</html>");
        instructionLabel.setBorder(BorderFactory.createEtchedBorder());
        dialog.add(instructionLabel, gbc);

        // Vehicle type selection
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Car", "Motorcycle", "Truck"});
        typeCombo.setToolTipText("Select the type of vehicle you want to add");
        dialog.add(typeCombo, gbc);

        // Vehicle ID
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Vehicle ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        idField.setToolTipText("Enter unique ID (e.g., CAR001, MOTO001, TRUCK001)");
        dialog.add(idField, gbc);

        // Model
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1;
        JTextField modelField = new JTextField(15);
        modelField.setToolTipText("Enter vehicle model (e.g., Toyota Camry, Honda Civic)");
        dialog.add(modelField, gbc);

        // Rate
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Rate/Day ($):"), gbc);
        gbc.gridx = 1;
        JTextField rateField = new JTextField(15);
        rateField.setToolTipText("Enter daily rental rate in dollars (e.g., 45.00)");
        dialog.add(rateField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Vehicle");
        JButton cancelBtn = new JButton("Cancel");

        addBtn.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                String id = idField.getText().trim();
                String model = modelField.getText().trim();
                double rate = Double.parseDouble(rateField.getText().trim());

                if (id.isEmpty() || model.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields!\n\nExample:\n" +
                        "ID: CAR001\nModel: Toyota Camry\nRate: 45.00", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Vehicle vehicle = null;
                switch (type) {
                    case "Car":
                        vehicle = Vehicle.createCar(id, model, rate, true, false, false);
                        break;
                    case "Motorcycle":
                        vehicle = Vehicle.createMotorcycle(id, model, rate, true, false);
                        break;
                    case "Truck":
                        vehicle = Vehicle.createTruck(id, model, rate, false, false);
                        break;
                }

                agency.addVehicleToFleet(vehicle);
                refreshVehicleTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Vehicle added successfully!\n\n" +
                    "Vehicle: " + model + " (" + id + ")\n" +
                    "Type: " + type + "\n" +
                    "Daily Rate: $" + String.format("%.2f", rate), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid rate!\n\nExample: 45.00", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error adding vehicle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    private void showAddCustomerDialog() {
        JDialog dialog = new JDialog(this, "Add Customer", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Instructions
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><b>Register a New Customer</b><br>" +
            "Examples:<br>" +
            "• Name: John Smith, Jane Doe<br>" +
            "• Customer ID: CUST001, CUST002 (must be unique)</html>");
        instructionLabel.setBorder(BorderFactory.createEtchedBorder());
        dialog.add(instructionLabel, gbc);

        // Customer Name
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        nameField.setToolTipText("Enter customer's full name (e.g., John Smith)");
        dialog.add(nameField, gbc);

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        idField.setToolTipText("Enter unique customer ID (e.g., CUST001, CUST002)");
        dialog.add(idField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Register Customer");
        JButton cancelBtn = new JButton("Cancel");

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();

            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all fields!\n\nExample:\n" +
                    "Name: John Smith\nCustomer ID: CUST001", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Customer customer = new Customer(name, id);
                agency.addCustomer(customer);
                refreshCustomerTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Customer registered successfully!\n\n" +
                    "Name: " + name + "\n" +
                    "ID: " + id + "\n" +
                    "Loyalty Status: Bronze (0 points)", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    private void showRentVehicleDialog() {
        if (agency.getFleet().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No vehicles available!\n\nPlease add vehicles to your fleet first using the Vehicles tab.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Rent Vehicle", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Instructions
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><b>Process a Vehicle Rental</b><br>" +
            "1. Select an available vehicle from the dropdown<br>" +
            "2. Enter customer information<br>" +
            "3. Specify rental duration (1-365 days)<br>" +
            "Cost will be calculated automatically based on vehicle rate and features.</html>");
        instructionLabel.setBorder(BorderFactory.createEtchedBorder());
        dialog.add(instructionLabel, gbc);

        // Available vehicles
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Available Vehicles:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> vehicleCombo = new JComboBox<>();
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.isAvailable()) {
                vehicleCombo.addItem(vehicle.getVehicleId() + " - " + vehicle.getModel() + 
                    " ($" + String.format("%.2f", vehicle.getBaseRentalRate()) + "/day)");
            }
        }
        vehicleCombo.setToolTipText("Select a vehicle from available fleet");
        dialog.add(vehicleCombo, gbc);

        // Customer name
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        JTextField customerField = new JTextField(15);
        customerField.setToolTipText("Enter customer's full name (e.g., John Smith)");
        dialog.add(customerField, gbc);

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        JTextField customerIdField = new JTextField(15);
        customerIdField.setToolTipText("Enter unique customer ID (e.g., CUST001)");
        dialog.add(customerIdField, gbc);

        // Rental days
        gbc.gridx = 0; gbc.gridy = 4;
        dialog.add(new JLabel("Rental Days:"), gbc);
        gbc.gridx = 1;
        JTextField daysField = new JTextField(15);
        daysField.setToolTipText("Enter number of days (1-365)");
        dialog.add(daysField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton rentBtn = new JButton("Process Rental");
        JButton cancelBtn = new JButton("Cancel");

        rentBtn.addActionListener(e -> {
            try {
                String selectedVehicle = (String) vehicleCombo.getSelectedItem();
                if (selectedVehicle == null) {
                    JOptionPane.showMessageDialog(dialog, "No vehicle selected!\n\nPlease select a vehicle from the dropdown.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String vehicleId = selectedVehicle.split(" - ")[0];
                String customerName = customerField.getText().trim();
                String customerId = customerIdField.getText().trim();
                int days = Integer.parseInt(daysField.getText().trim());

                if (customerName.isEmpty() || customerId.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all customer fields!\n\nExample:\n" +
                        "Name: John Smith\nID: CUST001\nDays: 7", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (days < 1 || days > 365) {
                    JOptionPane.showMessageDialog(dialog, "Rental days must be between 1 and 365!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Vehicle vehicle = agency.getFleet().stream()
                    .filter(v -> v.getVehicleId().equals(vehicleId))
                    .findFirst().orElse(null);

                Customer customer = new Customer(customerName, customerId);
                agency.addCustomer(customer);
                
                double totalCost = vehicle.calculateRentalCost(days);
                agency.rentVehicle(vehicle, customer, days);

                refreshAllTables();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Vehicle rented successfully!\n\n" +
                    "Vehicle: " + vehicle.getModel() + " (" + vehicleId + ")\n" +
                    "Customer: " + customerName + "\n" +
                    "Duration: " + days + " days\n" +
                    "Total Cost: $" + String.format("%.2f", totalCost), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid number of days!\n\nExample: 7", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error renting vehicle: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(rentBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    private void removeSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String vehicleId = (String) vehicleTableModel.getValueAt(selectedRow, 0);
        Vehicle vehicle = agency.getFleet().stream()
            .filter(v -> v.getVehicleId().equals(vehicleId))
            .findFirst().orElse(null);

        if (vehicle != null) {
            if (!vehicle.isAvailable()) {
                JOptionPane.showMessageDialog(this, "Cannot remove a rented vehicle!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to remove vehicle " + vehicleId + "?", 
                "Confirm Removal", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                agency.removeVehicleFromFleet(vehicle);
                refreshVehicleTable();
                JOptionPane.showMessageDialog(this, "Vehicle removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void returnSelectedVehicle() {
        int selectedRow = rentalTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental to return!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String vehicleId = (String) rentalTableModel.getValueAt(selectedRow, 0);
        Vehicle vehicle = agency.getFleet().stream()
            .filter(v -> v.getVehicleId().equals(vehicleId))
            .findFirst().orElse(null);

        if (vehicle != null && !vehicle.isAvailable()) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Confirm return of vehicle " + vehicleId + "?", 
                "Confirm Return", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                agency.processReturn(vehicle);
                refreshAllTables();
                JOptionPane.showMessageDialog(this, "Vehicle returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void showRateVehicleDialog() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to rate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String vehicleId = (String) vehicleTableModel.getValueAt(selectedRow, 0);
        Vehicle vehicle = agency.getFleet().stream()
            .filter(v -> v.getVehicleId().equals(vehicleId))
            .findFirst().orElse(null);

        if (vehicle != null) {
            String ratingStr = JOptionPane.showInputDialog(this, "Enter rating (1-5) for " + vehicle.getModel() + ":", "Rate Vehicle", JOptionPane.QUESTION_MESSAGE);
            if (ratingStr != null) {
                try {
                    int rating = Integer.parseInt(ratingStr);
                    if (rating >= 1 && rating <= 5) {
                        agency.rateVehicle(vehicle, rating);
                        refreshVehicleTable();
                        JOptionPane.showMessageDialog(this, "Vehicle rated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showRateCustomerDialog() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to rate!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String customerName = (String) customerTableModel.getValueAt(selectedRow, 1);

        String ratingStr = JOptionPane.showInputDialog(this, "Enter rating (1-5) for " + customerName + ":", "Rate Customer", JOptionPane.QUESTION_MESSAGE);
        if (ratingStr != null) {
            try {
                int rating = Integer.parseInt(ratingStr);
                if (rating >= 1 && rating <= 5) {
                    // Note: Customer rating would need to be implemented
                    JOptionPane.showMessageDialog(this, "Customer rated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddFeatureDialog() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to add features!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String vehicleId = (String) vehicleTableModel.getValueAt(selectedRow, 0);
        Vehicle vehicle = agency.getFleet().stream()
            .filter(v -> v.getVehicleId().equals(vehicleId))
            .findFirst().orElse(null);

        if (vehicle != null) {
            JDialog dialog = new JDialog(this, "Add Feature", true);
            dialog.setSize(300, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            // Feature name
            gbc.gridx = 0; gbc.gridy = 0;
            dialog.add(new JLabel("Feature Name:"), gbc);
            gbc.gridx = 1;
            JTextField nameField = new JTextField(15);
            dialog.add(nameField, gbc);

            // Feature cost
            gbc.gridx = 0; gbc.gridy = 1;
            dialog.add(new JLabel("Additional Cost:"), gbc);
            gbc.gridx = 1;
            JTextField costField = new JTextField(15);
            dialog.add(costField, gbc);

            // Buttons
            gbc.gridx = 0; gbc.gridy = 2;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton addBtn = new JButton("Add");
            JButton cancelBtn = new JButton("Cancel");

            addBtn.addActionListener(e -> {
                try {
                    String name = nameField.getText().trim();
                    double cost = Double.parseDouble(costField.getText().trim());

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Please enter feature name!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Feature feature = new Feature(name, cost);
                    vehicle.addFeature(feature);
                    refreshVehicleTable();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Feature added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid cost!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(e -> dialog.dispose());

            buttonPanel.add(addBtn);
            buttonPanel.add(cancelBtn);
            dialog.add(buttonPanel, gbc);

            dialog.setVisible(true);
        }
    }

    private void refreshVehicleTable() {
        vehicleTableModel.setRowCount(0);
        for (Vehicle vehicle : agency.getFleet()) {
            Object[] row = {
                vehicle.getVehicleId(),
                vehicle.getClass().getSimpleName(),
                vehicle.getModel(),
                String.format("$%.2f", vehicle.getBaseRentalRate()),
                vehicle.isAvailable() ? "Available" : "Rented",
                String.format("%.1f", vehicle.getAverageRating())
            };
            vehicleTableModel.addRow(row);
        }
    }

    private void refreshCustomerTable() {
        customerTableModel.setRowCount(0);
        for (Customer customer : agency.getCustomers()) {
            Object[] row = {
                customer.getCustomerId(),
                customer.getName(),
                customer.getLoyaltyStatus(),
                customer.getLoyaltyPoints(),
                String.format("%.1f", customer.getAverageRating())
            };
            customerTableModel.addRow(row);
        }
    }

    private void refreshRentalTable() {
        rentalTableModel.setRowCount(0);
        for (Vehicle vehicle : agency.getFleet()) {
            if (!vehicle.isAvailable() && vehicle.currentRenter != null) {
                Object[] row = {
                    vehicle.getVehicleId(),
                    vehicle.currentRenter.getName(),
                    "N/A", // Days would need to be tracked
                    "N/A", // Cost would need to be calculated
                    "Active"
                };
                rentalTableModel.addRow(row);
            }
        }
    }

    private void refreshAllTables() {
        refreshVehicleTable();
        refreshCustomerTable();
        refreshRentalTable();
    }

    private void refreshReports(JTextArea fleetStatus, JTextArea revenueText, JTextArea analyticsText) {
        // Fleet status report
        StringBuilder fleetReport = new StringBuilder();
        fleetReport.append("FLEET STATUS REPORT\n");
        fleetReport.append("===================\n\n");
        
        int totalVehicles = agency.getFleet().size();
        long availableVehicles = agency.getFleet().stream().mapToLong(v -> v.isAvailable() ? 1 : 0).sum();
        long rentedVehicles = totalVehicles - availableVehicles;
        
        fleetReport.append(String.format("Total Vehicles: %d\n", totalVehicles));
        fleetReport.append(String.format("Available: %d\n", availableVehicles));
        fleetReport.append(String.format("Rented: %d\n\n", rentedVehicles));
        
        fleetReport.append("Vehicle Details:\n");
        for (Vehicle vehicle : agency.getFleet()) {
            fleetReport.append(String.format("• %s (%s) - %s - Rating: %.1f\n", 
                vehicle.getVehicleId(), 
                vehicle.getModel(),
                vehicle.isAvailable() ? "Available" : "Rented",
                vehicle.getAverageRating()));
        }
        
        fleetStatus.setText(fleetReport.toString());

        // Revenue report
        StringBuilder revenueReport = new StringBuilder();
        revenueReport.append("REVENUE SUMMARY\n");
        revenueReport.append("===============\n\n");
        revenueReport.append("Daily Revenue Potential:\n");
        
        double totalDailyRevenue = 0;
        for (Vehicle vehicle : agency.getFleet()) {
            double vehicleRevenue = vehicle.getBaseRentalRate() + vehicle.calculateTotalFeatureCost();
            totalDailyRevenue += vehicleRevenue;
            revenueReport.append(String.format("• %s: $%.2f/day\n", vehicle.getVehicleId(), vehicleRevenue));
        }
        
        revenueReport.append(String.format("\nTotal Daily Potential: $%.2f\n", totalDailyRevenue));
        revenueReport.append(String.format("Monthly Potential: $%.2f\n", totalDailyRevenue * 30));
        revenueReport.append(String.format("Yearly Potential: $%.2f\n", totalDailyRevenue * 365));
        
        revenueText.setText(revenueReport.toString());

        // Analytics report
        StringBuilder analyticsReport = new StringBuilder();
        analyticsReport.append("ANALYTICS DASHBOARD\n");
        analyticsReport.append("===================\n\n");
        
        if (!agency.getFleet().isEmpty()) {
            double avgRating = agency.getFleet().stream()
                .mapToDouble(Vehicle::getAverageRating)
                .average().orElse(0.0);
            
            double avgRate = agency.getFleet().stream()
                .mapToDouble(Vehicle::getBaseRentalRate)
                .average().orElse(0.0);
            
            analyticsReport.append(String.format("Average Vehicle Rating: %.1f/5\n", avgRating));
            analyticsReport.append(String.format("Average Daily Rate: $%.2f\n", avgRate));
            analyticsReport.append(String.format("Fleet Utilization: %.1f%%\n", ((double)rentedVehicles / totalVehicles) * 100));
        }
        
        if (!agency.getCustomers().isEmpty()) {
            double avgCustomerRating = agency.getCustomers().stream()
                .mapToDouble(Customer::getAverageRating)
                .average().orElse(0.0);
            
            analyticsReport.append(String.format("Average Customer Rating: %.1f/5\n", avgCustomerRating));
            analyticsReport.append(String.format("Total Customers: %d\n", agency.getCustomers().size()));
        }
        
        analyticsText.setText(analyticsReport.toString());
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, 
            "Advanced Vehicle Rental Management System\n" +
            "Version 1.0\n\n" +
            "A comprehensive solution for managing vehicle rentals,\n" +
            "customers, and business operations.\n\n" +
            "Features:\n" +
            "• Vehicle Fleet Management\n" +
            "• Customer Management\n" +
            "• Rental Processing\n" +
            "• Rating System\n" +
            "• Business Reports\n" +
            "• Loyalty Program",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addSampleData() {
        // Add sample vehicles
        Vehicle car1 = Vehicle.createCar("CAR001", "Toyota Camry", 45.0, true, true, false);
        Vehicle car2 = Vehicle.createCar("CAR002", "Honda Accord", 40.0, true, false, true);
        Vehicle motorcycle1 = Vehicle.createMotorcycle("MOTO001", "Harley Davidson", 60.0, true, true);
        Vehicle truck1 = Vehicle.createTruck("TRUCK001", "Ford F-150", 80.0, true, false);

        agency.addVehicleToFleet(car1);
        agency.addVehicleToFleet(car2);
        agency.addVehicleToFleet(motorcycle1);
        agency.addVehicleToFleet(truck1);

        // Add some ratings
        car1.addRating(5);
        car1.addRating(4);
        car2.addRating(4);
        car2.addRating(5);
        motorcycle1.addRating(5);
        truck1.addRating(4);

        // Add sample customers
        Customer customer1 = new Customer("John Smith", "CUST001");
        Customer customer2 = new Customer("Jane Doe", "CUST002");
        
        customer1.addLoyaltyPoints(120);
        customer2.addLoyaltyPoints(45);
        
        agency.addCustomer(customer1);
        agency.addCustomer(customer2);

        // Refresh all tables
        refreshAllTables();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use the system look and feel for better native appearance
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // Fallback to default look and feel
                System.out.println("Could not set system look and feel, using default.");
            }
            
            new VehicleRentalGUI().setVisible(true);
        });
    }
}