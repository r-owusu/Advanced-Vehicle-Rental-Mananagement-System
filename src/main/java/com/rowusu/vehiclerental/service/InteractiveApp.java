package com.rowusu.vehiclerental.service;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;

import java.util.Scanner;

public class InteractiveApp {
    private static RentalAgency agency;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("🚗 Welcome to Advanced Vehicle Rental Management System 🚗");
        System.out.println("=========================================================");
        System.out.println("📝 This version allows you to input your own data!");
        
        agency = new RentalAgency();
        scanner = new Scanner(System.in);
        
        // Start interactive menu
        runInteractiveMenu();
        
        scanner.close();
    }
    
    private static void runInteractiveMenu() {
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            int choice = getChoice();
            
            switch (choice) {
                case 1:
                    manageVehicles();
                    break;
                case 2:
                    manageCustomers();
                    break;
                case 3:
                    processRental();
                    break;
                case 4:
                    processReturn();
                    break;
                case 5:
                    rateVehicleOrCustomer();
                    break;
                case 6:
                    manageLoyaltyProgram();
                    break;
                case 7:
                    generateReports();
                    break;
                case 8:
                    System.out.println("\n👋 Thank you for using the Vehicle Rental Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🏢 VEHICLE RENTAL MANAGEMENT SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. 🚗 Manage Vehicles");
        System.out.println("2. 👤 Manage Customers");
        System.out.println("3. 📝 Process Rental");
        System.out.println("4. 🔄 Process Return");
        System.out.println("5. ⭐ Rate Vehicle/Customer");
        System.out.println("6. 🏆 Manage Loyalty Program");
        System.out.println("7. 📊 Generate Reports");
        System.out.println("8. 🚪 Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (1-8): ");
    }
    
    private static int getChoice() {
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ Please enter a number.");
                return -1;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input. Please enter a valid number.");
            return -1;
        }
    }
    
    private static int getChoiceWithRange(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.print("❌ Please enter a number (" + min + "-" + max + "): ");
                    continue;
                }
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.print("❌ Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input. Please enter a number (" + min + "-" + max + "): ");
            }
        }
    }
    
    private static String getValidString(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty() && !allowEmpty) {
                System.out.println("❌ Input cannot be empty. Please try again.");
                continue;
            }
            
            if (input.length() > 100) {
                System.out.println("❌ Input too long (max 100 characters). Please try again.");
                continue;
            }
            
            return input;
        }
    }
    
    private static double getValidDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("❌ Please enter a number.");
                    continue;
                }
                
                double value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.printf("❌ Please enter a number between %.2f and %.2f.\n", min, max);
                    continue;
                }
                
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number format. Please try again.");
            }
        }
    }
    
    private static int getValidInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("❌ Please enter a number.");
                    continue;
                }
                
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.printf("❌ Please enter a number between %d and %d.\n", min, max);
                    continue;
                }
                
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number format. Please try again.");
            }
        }
    }
    
    private static boolean getYesNoChoice(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("❌ Please enter 'y' for yes or 'n' for no.");
            }
        }
    }
    
    private static boolean isValidVehicleId(String vehicleId) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            return false;
        }
        
        // Check if ID already exists in fleet
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.getVehicleId().equalsIgnoreCase(vehicleId.trim())) {
                return false;
            }
        }
        
        // Basic format validation (alphanumeric, 3-20 characters)
        return vehicleId.trim().matches("^[A-Za-z0-9]{3,20}$");
    }
    
    private static void manageVehicles() {
        System.out.println("\n🚗 VEHICLE MANAGEMENT");
        System.out.println("=".repeat(30));
        System.out.println("1. Add Vehicle to Fleet");
        System.out.println("2. Remove Vehicle from Fleet");
        System.out.println("3. View Fleet Status");
        System.out.print("Choose option (1-3): ");
        
        int choice = getChoiceWithRange(1, 3);
        
        switch (choice) {
            case 1:
                addVehicleToFleet();
                break;
            case 2:
                removeVehicleFromFleet();
                break;
            case 3:
                viewFleetStatus();
                break;
        }
    }
    
    private static void addVehicleToFleet() {
        System.out.println("\n➕ ADD VEHICLE TO FLEET");
        System.out.println("-".repeat(25));
        
        System.out.println("Select vehicle type:");
        System.out.println("1. Car");
        System.out.println("2. Motorcycle");
        System.out.println("3. Truck");
        System.out.print("Enter choice (1-3): ");
        
        int vehicleType = getChoiceWithRange(1, 3);
        
        // Get and validate Vehicle ID
        String vehicleId;
        while (true) {
            vehicleId = getValidString("Enter Vehicle ID (3-20 alphanumeric characters): ", false);
            if (isValidVehicleId(vehicleId)) {
                break;
            } else {
                if (vehicleId.length() < 3 || vehicleId.length() > 20) {
                    System.out.println("❌ Vehicle ID must be 3-20 characters long.");
                } else if (!vehicleId.matches("^[A-Za-z0-9]+$")) {
                    System.out.println("❌ Vehicle ID must contain only letters and numbers.");
                } else {
                    System.out.println("❌ Vehicle ID already exists. Please choose a different ID.");
                }
            }
        }
        
        String model = getValidString("Enter Vehicle Model: ", false);
        double rate = getValidDouble("Enter Base Rental Rate (per day): $", 1.0, 10000.0);
        
        Vehicle vehicle = null;
        
        try {
            switch (vehicleType) {
                case 1:
                    vehicle = createCar(vehicleId, model, rate);
                    break;
                case 2:
                    vehicle = createMotorcycle(vehicleId, model, rate);
                    break;
                case 3:
                    vehicle = createTruck(vehicleId, model, rate);
                    break;
            }
            
            // Ask for features
            addFeaturesToVehicle(vehicle);
            
            agency.addVehicleToFleet(vehicle);
            System.out.println("✅ Vehicle added successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Error adding vehicle: " + e.getMessage());
        }
    }
    
    private static Car createCar(String vehicleId, String model, double rate) {
        System.out.println("\n🚗 CAR FEATURES:");
        
        boolean hasGPS = getYesNoChoice("Has GPS?");
        boolean hasChildSeat = getYesNoChoice("Has Child Seat?");
        boolean hasSunroof = getYesNoChoice("Has Sunroof?");
        
        return new Car(vehicleId, model, rate, hasGPS, hasChildSeat, hasSunroof);
    }
    
    private static Motorcycle createMotorcycle(String vehicleId, String model, double rate) {
        System.out.println("\n🏍️ MOTORCYCLE FEATURES:");
        
        boolean hasHelmet = getYesNoChoice("Has Helmet?");
        boolean hasLuggageStorage = getYesNoChoice("Has Luggage Storage?");
        
        return new Motorcycle(vehicleId, model, rate, hasHelmet, hasLuggageStorage);
    }
    
    private static Truck createTruck(String vehicleId, String model, double rate) {
        System.out.println("\n🚚 TRUCK FEATURES:");
        
        boolean hasCargoLift = getYesNoChoice("Has Cargo Lift?");
        boolean hasRefrigeratedStorage = getYesNoChoice("Has Refrigerated Storage?");
        
        return new Truck(vehicleId, model, rate, hasCargoLift, hasRefrigeratedStorage);
    }
    
    private static void addFeaturesToVehicle(Vehicle vehicle) {
        System.out.println("\n⚡ ADD ADDITIONAL FEATURES:");
        
        if (!getYesNoChoice("Do you want to add additional features?")) {
            return;
        }
        
        while (true) {
            String featureName = getValidString("Enter feature name (or 'done' to finish): ", false);
            
            if (featureName.toLowerCase().equals("done")) {
                break;
            }
            
            double cost = getValidDouble("Enter feature cost per day: $", 0.0, 1000.0);
            vehicle.addFeature(new Feature(featureName, cost));
            System.out.println("✅ Feature '" + featureName + "' added!");
        }
    }
    
    private static void removeVehicleFromFleet() {
        if (agency.getFleet().isEmpty()) {
            System.out.println("❌ No vehicles in fleet.");
            return;
        }
        
        System.out.println("\n➖ REMOVE VEHICLE FROM FLEET");
        System.out.println("-".repeat(30));
        
        viewFleetStatus();
        
        String vehicleId = getValidString("Enter Vehicle ID to remove: ", false);
        
        Vehicle toRemove = null;
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.getVehicleId().equalsIgnoreCase(vehicleId)) {
                toRemove = vehicle;
                break;
            }
        }
        
        if (toRemove != null) {
            if (!toRemove.isAvailable()) {
                System.out.println("❌ Cannot remove vehicle - it is currently rented.");
                return;
            }
            
            if (getYesNoChoice("Are you sure you want to remove " + toRemove.getModel() + "?")) {
                agency.removeVehicleFromFleet(toRemove);
                System.out.println("✅ Vehicle removed successfully!");
            } else {
                System.out.println("❌ Removal cancelled.");
            }
        } else {
            System.out.println("❌ Vehicle not found.");
        }
    }
    
    private static void viewFleetStatus() {
        System.out.println("\n📋 FLEET STATUS");
        System.out.println("-".repeat(20));
        
        if (agency.getFleet().isEmpty()) {
            System.out.println("❌ No vehicles in fleet.");
            return;
        }
        
        agency.generateFleetReport();
        
        System.out.println("\n🔍 DETAILED VEHICLE INFORMATION:");
        for (Vehicle vehicle : agency.getFleet()) {
            System.out.println(vehicle.toString());
            System.out.printf("   💰 Base Rate: $%.2f/day\n", vehicle.getBaseRentalRate());
            System.out.printf("   ⚡ Features Cost: $%.2f/day\n", vehicle.calculateTotalFeatureCost());
            System.out.printf("   📊 Average Rating: %.1f/5\n", vehicle.getAverageRating());
            System.out.println();
        }
    }
    
    private static void manageCustomers() {
        System.out.println("\n👤 CUSTOMER MANAGEMENT");
        System.out.println("=".repeat(25));
        System.out.println("1. Create New Customer");
        System.out.println("2. View Customer Information");
        System.out.print("Choose option (1-2): ");
        
        int choice = getChoiceWithRange(1, 2);
        
        switch (choice) {
            case 1:
                createNewCustomer();
                break;
            case 2:
                viewCustomerInfo();
                break;
        }
    }
    
    private static Customer createNewCustomer() {
        System.out.println("\n👤 CREATE NEW CUSTOMER");
        System.out.println("-".repeat(25));
        
        System.out.print("Enter Customer Name: ");
        String name = getValidString("Customer name", false);
        
        System.out.print("Enter Customer ID: ");
        String customerId = getValidString("Customer ID", false);
        
        try {
            Customer customer = new Customer(name, customerId);
            System.out.println("✅ Customer created successfully!");
            System.out.printf("   Name: %s\n", customer.getName());
            System.out.printf("   ID: %s\n", customer.getCustomerId());
            System.out.printf("   Loyalty Status: %s\n", customer.getLoyaltyStatus());
            System.out.printf("   Loyalty Points: %d\n", customer.getLoyaltyPoints());
            return customer;
        } catch (Exception e) {
            System.out.println("❌ Error creating customer: " + e.getMessage());
            return null;
        }
    }
    
    private static void viewCustomerInfo() {
        System.out.println("\n👤 CUSTOMER INFORMATION");
        System.out.println("-".repeat(25));
        System.out.print("Enter Customer ID: ");
        String customerId = getValidString("Customer ID", false);
        
        // Note: In a real system, you'd store customers in a repository
        System.out.println("ℹ️ Customer lookup feature requires customer storage implementation.");
        System.out.println("   Currently, customers are created per session.");
        System.out.println("   Customer ID entered: " + customerId);
    }
    
    private static void processRental() {
        if (agency.getFleet().isEmpty()) {
            System.out.println("❌ No vehicles available. Please add vehicles first.");
            return;
        }
        
        System.out.println("\n📝 PROCESS VEHICLE RENTAL");
        System.out.println("-".repeat(30));
        
        // Show available vehicles
        System.out.println("📋 AVAILABLE VEHICLES:");
        boolean hasAvailable = false;
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.isAvailable()) {
                System.out.printf("   🚗 %s - %s ($%.2f/day)\n", 
                    vehicle.getVehicleId(), vehicle.getModel(), vehicle.getBaseRentalRate());
                hasAvailable = true;
            }
        }
        
        if (!hasAvailable) {
            System.out.println("❌ No vehicles currently available for rental.");
            return;
        }
        
        // Get rental details
        System.out.print("\nEnter Vehicle ID to rent: ");
        String vehicleId = getValidString("Vehicle ID", false);
        
        Vehicle selectedVehicle = null;
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailable()) {
                selectedVehicle = vehicle;
                break;
            }
        }
        
        if (selectedVehicle == null) {
            System.out.println("❌ Vehicle not found or not available.");
            return;
        }
        
        // Create or get customer
        System.out.println("\n👤 CUSTOMER INFORMATION:");
        Customer customer = createNewCustomer();
        if (customer == null) return;
        
        // Get rental period
        System.out.print("\nEnter rental period (days): ");
        int days = getValidInt("Rental period", 1, 365);
        
        // Calculate and confirm cost
        double totalCost = selectedVehicle.calculateRentalCost(days);
        System.out.printf("\n💰 RENTAL SUMMARY:\n");
        System.out.printf("   Vehicle: %s\n", selectedVehicle.getModel());
        System.out.printf("   Customer: %s\n", customer.getName());
        System.out.printf("   Period: %d days\n", days);
        System.out.printf("   Base Cost: $%.2f/day\n", selectedVehicle.getBaseRentalRate());
        System.out.printf("   Features Cost: $%.2f/day\n", selectedVehicle.calculateTotalFeatureCost());
        System.out.printf("   TOTAL COST: $%.2f\n", totalCost);
        
        System.out.print("\nConfirm rental? (y/n): ");
        if (!getYesNoChoice("Confirm rental")) {
            System.out.println("❌ Rental cancelled.");
            return;
        }
        
        // Process rental
        try {
            agency.rentVehicle(selectedVehicle, customer, days);
            customer.addLoyaltyPoints(days * 10); // Award loyalty points
            System.out.println("✅ Rental processed successfully!");
            System.out.printf("   %s earned %d loyalty points!\n", customer.getName(), days * 10);
        } catch (Exception e) {
            System.out.println("❌ Rental failed: " + e.getMessage());
        }
    }
    
    private static void processReturn() {
        System.out.println("\n🔄 PROCESS VEHICLE RETURN");
        System.out.println("-".repeat(30));
        
        // Show rented vehicles
        System.out.println("📋 CURRENTLY RENTED VEHICLES:");
        boolean hasRented = false;
        for (Vehicle vehicle : agency.getFleet()) {
            if (!vehicle.isAvailable()) {
                System.out.printf("   🚗 %s - %s\n", vehicle.getVehicleId(), vehicle.getModel());
                hasRented = true;
            }
        }
        
        if (!hasRented) {
            System.out.println("ℹ️ No vehicles are currently rented.");
            return;
        }
        
        System.out.print("\nEnter Vehicle ID to return: ");
        String vehicleId = getValidString("Vehicle ID", false);
        
        Vehicle vehicleToReturn = null;
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.getVehicleId().equals(vehicleId) && !vehicle.isAvailable()) {
                vehicleToReturn = vehicle;
                break;
            }
        }
        
        if (vehicleToReturn == null) {
            System.out.println("❌ Vehicle not found or not currently rented.");
            return;
        }
        
        try {
            agency.processReturn(vehicleToReturn);
            System.out.println("✅ Vehicle returned successfully!");
        } catch (Exception e) {
            System.out.println("❌ Return failed: " + e.getMessage());
        }
    }
    
    private static void rateVehicleOrCustomer() {
        System.out.println("\n⭐ RATING SYSTEM");
        System.out.println("=".repeat(20));
        System.out.println("1. Rate a Vehicle");
        System.out.println("2. Rate a Customer");
        System.out.print("Choose option (1-2): ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                rateVehicle();
                break;
            case 2:
                System.out.println("ℹ️ Customer rating requires customer storage implementation.");
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }
    
    private static void rateVehicle() {
        if (agency.getFleet().isEmpty()) {
            System.out.println("❌ No vehicles in fleet.");
            return;
        }
        
        System.out.println("\n⭐ RATE VEHICLE");
        System.out.println("-".repeat(15));
        
        viewFleetStatus();
        
        System.out.print("Enter Vehicle ID to rate: ");
        String vehicleId = getValidString("Vehicle ID", false);
        
        Vehicle vehicleToRate = null;
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                vehicleToRate = vehicle;
                break;
            }
        }
        
        if (vehicleToRate == null) {
            System.out.println("❌ Vehicle not found.");
            return;
        }
        
        System.out.print("Enter rating (1-5): ");
        int rating = getValidInt("Rating", 1, 5);
        agency.rateVehicle(vehicleToRate, rating);
        System.out.printf("✅ Vehicle rated successfully! New average: %.1f/5\n", 
            vehicleToRate.getAverageRating());
    }
    
    private static void manageLoyaltyProgram() {
        System.out.println("\n🏆 LOYALTY PROGRAM MANAGEMENT");
        System.out.println("-".repeat(35));
        System.out.println("ℹ️ Loyalty program information:");
        System.out.println("   🥉 Bronze (0-49 points): Standard service");
        System.out.println("   🥈 Silver (50-99 points): 5% discount + priority booking");
        System.out.println("   🥇 Gold (100+ points): 10% discount + premium support + free upgrades");
        System.out.println("\n💡 Points are automatically awarded during rentals (10 points per day).");
    }
    
    private static void generateReports() {
        System.out.println("\n📊 SYSTEM REPORTS");
        System.out.println("=".repeat(20));
        System.out.println("1. Fleet Report");
        System.out.println("2. Active Rentals Report");
        System.out.println("3. Financial Summary");
        System.out.print("Choose option (1-3): ");
        
        int choice = getChoice();
        
        switch (choice) {
            case 1:
                System.out.println("\n📋 FLEET REPORT:");
                agency.generateFleetReport();
                break;
            case 2:
                System.out.println("\n🚗 ACTIVE RENTALS REPORT:");
                agency.generateActiveRentalsReport();
                break;
            case 3:
                generateFinancialSummary();
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }
    
    private static void generateFinancialSummary() {
        System.out.println("\n💰 FINANCIAL SUMMARY:");
        
        double totalRevenue = 0;
        int totalRentals = 0;
        
        for (Vehicle vehicle : agency.getFleet()) {
            if (!vehicle.isAvailable()) {
                totalRevenue += vehicle.calculateRentalCost(5); // Assume 5-day average
                totalRentals++;
            }
        }
        
        System.out.printf("💵 Total Active Revenue: $%.2f\n", totalRevenue);
        System.out.printf("📈 Active Rentals Count: %d\n", totalRentals);
        
        if (agency.getFleet().size() > 0) {
            System.out.printf("🚗 Fleet Utilization: %.1f%%\n", 
                (totalRentals * 100.0) / agency.getFleet().size());
        }
    }
}