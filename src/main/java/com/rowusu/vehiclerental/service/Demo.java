package com.rowusu.vehiclerental.service;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.exceptions.*;

public class Demo {
    public static void main(String[] args) {
        System.out.println("🚗 Welcome to Advanced Vehicle Rental Management System - DEMO MODE 🚗");
        System.out.println("====================================================================");
        
        RentalAgency agency = new RentalAgency();
        
        // Initialize sample data
        initializeSampleData(agency);
        
        // Run all demonstrations
        demonstrateVehicleManagement(agency);
        demonstrateRentalProcess(agency);
        demonstrateReturnProcess(agency);
        demonstrateRatingSystem(agency);
        demonstrateLoyaltyProgram(agency);
        generateReports(agency);
        demonstrateErrorHandling(agency);
        
        System.out.println("\n🎉 Demo completed! Your Vehicle Rental Management System is fully functional!");
        System.out.println("You can run the interactive version using the Main class.");
    }
    
    private static void initializeSampleData(RentalAgency agency) {
        System.out.println("\n🔧 Initializing sample data...");
        
        // Create various vehicles
        Car car1 = new Car("CAR001", "Toyota Camry 2023", 45.0, true, false, true);
        Car car2 = new Car("CAR002", "Honda Civic 2023", 40.0, false, true, false);
        Motorcycle bike1 = new Motorcycle("BIKE001", "Yamaha R6 2023", 35.0, true, true);
        Truck truck1 = new Truck("TRUCK001", "Ford F-150 2023", 80.0, true, false);
        
        // Add features to vehicles
        Feature gpsFeature = new Feature("GPS Navigation", 5.0);
        Feature childSeatFeature = new Feature("Child Safety Seat", 3.0);
        Feature helmetFeature = new Feature("Safety Helmet", 2.0);
        Feature cargoLiftFeature = new Feature("Hydraulic Cargo Lift", 15.0);
        
        car1.addFeature(gpsFeature);
        car2.addFeature(childSeatFeature);
        bike1.addFeature(helmetFeature);
        truck1.addFeature(cargoLiftFeature);
        
        // Add vehicles to fleet
        agency.addVehicleToFleet(car1);
        agency.addVehicleToFleet(car2);
        agency.addVehicleToFleet(bike1);
        agency.addVehicleToFleet(truck1);
        
        System.out.println("✅ Sample data initialized successfully!");
        System.out.println("   - 4 vehicles added to fleet");
        System.out.println("   - Various features configured");
    }
    
    private static void demonstrateVehicleManagement(RentalAgency agency) {
        System.out.println("\n🚗 VEHICLE FLEET MANAGEMENT DEMO");
        System.out.println("=".repeat(40));
        
        // Display current fleet
        System.out.println("\n📋 Current Fleet Status:");
        agency.generateFleetReport();
        
        // Demonstrate adding a new vehicle
        System.out.println("\n➕ Adding a new vehicle to fleet...");
        Car newCar = new Car("CAR003", "BMW X5 2023", 75.0, true, true, true);
        Feature luxuryPackage = new Feature("Luxury Package", 20.0);
        newCar.addFeature(luxuryPackage);
        agency.addVehicleToFleet(newCar);
        
        // Show updated fleet
        System.out.println("\n📋 Updated Fleet Status:");
        agency.generateFleetReport();
        
        // Demonstrate vehicle details
        System.out.println("\n🔍 Vehicle Details:");
        for (Vehicle vehicle : agency.getFleet()) {
            System.out.println(vehicle.toString());
            System.out.printf("   💰 Base Rate: $%.2f/day\n", vehicle.getBaseRentalRate());
            System.out.printf("   ⚡ Features Cost: $%.2f/day\n", vehicle.calculateTotalFeatureCost());
            System.out.printf("   📊 Average Rating: %.1f/5\n", vehicle.getAverageRating());
            System.out.println();
        }
    }
    
    private static void demonstrateRentalProcess(RentalAgency agency) {
        System.out.println("\n📝 RENTAL PROCESS DEMO");
        System.out.println("=".repeat(40));
        
        // Create a customer
        Customer customer = new Customer("Alice Brown", "CUST004");
        System.out.println("👤 Customer: " + customer.getName() + " (ID: " + customer.getCustomerId() + ")");
        System.out.println("🏆 Loyalty Status: " + customer.getLoyaltyStatus());
        System.out.println("⭐ Loyalty Points: " + customer.getLoyaltyPoints());
        
        // Find an available vehicle
        Vehicle availableVehicle = null;
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.isAvailable()) {
                availableVehicle = vehicle;
                break;
            }
        }
        
        if (availableVehicle != null) {
            try {
                System.out.println("\n🚗 Selected Vehicle: " + availableVehicle.getModel());
                System.out.printf("💰 Daily Rate: $%.2f\n", availableVehicle.getBaseRentalRate());
                
                int rentalDays = 5;
                System.out.println("📅 Rental Period: " + rentalDays + " days");
                
                double totalCost = availableVehicle.calculateRentalCost(rentalDays);
                System.out.printf("💵 Total Cost: $%.2f\n", totalCost);
                
                // Process rental
                agency.rentVehicle(availableVehicle, customer, rentalDays);
                System.out.println("✅ Rental processed successfully!");
                
                // Show updated customer info
                System.out.println("\n👤 Updated Customer Info:");
                System.out.println("🏆 Loyalty Status: " + customer.getLoyaltyStatus());
                System.out.println("⭐ Loyalty Points: " + customer.getLoyaltyPoints());
                System.out.println("🚗 Current Rentals: " + customer.getCurrentRentals().size());
                
            } catch (Exception e) {
                System.out.println("❌ Rental failed: " + e.getMessage());
            }
        } else {
            System.out.println("❌ No vehicles available for rental.");
        }
    }
    
    private static void demonstrateReturnProcess(RentalAgency agency) {
        System.out.println("\n🔄 VEHICLE RETURN DEMO");
        System.out.println("=".repeat(40));
        
        // Find a rented vehicle
        Vehicle rentedVehicle = null;
        for (Vehicle vehicle : agency.getFleet()) {
            if (!vehicle.isAvailable()) {
                rentedVehicle = vehicle;
                break;
            }
        }
        
        if (rentedVehicle != null) {
            System.out.println("🚗 Returning vehicle: " + rentedVehicle.getModel());
            System.out.println("👤 Current renter: " + (rentedVehicle.currentRenter != null ? 
                rentedVehicle.currentRenter.getName() : "Unknown"));
            
            try {
                agency.processReturn(rentedVehicle);
                System.out.println("✅ Vehicle returned successfully!");
                
                // Show updated fleet status
                System.out.println("\n📋 Updated Fleet Status:");
                agency.generateFleetReport();
                
            } catch (Exception e) {
                System.out.println("❌ Return failed: " + e.getMessage());
            }
        } else {
            System.out.println("ℹ️ No vehicles are currently rented.");
        }
    }
    
    private static void demonstrateRatingSystem(RentalAgency agency) {
        System.out.println("\n⭐ RATING SYSTEM DEMO");
        System.out.println("=".repeat(40));
        
        // Rate vehicles
        System.out.println("📊 Rating vehicles...");
        for (Vehicle vehicle : agency.getFleet()) {
            // Simulate random ratings
            int[] ratings = {4, 5, 3, 5, 4};
            for (int rating : ratings) {
                agency.rateVehicle(vehicle, rating);
            }
            System.out.printf("🚗 %s - Average Rating: %.1f/5\n", 
                vehicle.getModel(), vehicle.getAverageRating());
        }
        
        // Rate customers (simulate)
        Customer sampleCustomer = new Customer("Rating Demo Customer", "DEMO001");
        System.out.println("\n👤 Rating customer: " + sampleCustomer.getName());
        agency.rateCustomer(sampleCustomer, 5);
        agency.rateCustomer(sampleCustomer, 4);
        agency.rateCustomer(sampleCustomer, 5);
        System.out.printf("⭐ Customer Average Rating: %.1f/5\n", sampleCustomer.getAverageRating());
    }
    
    private static void demonstrateLoyaltyProgram(RentalAgency agency) {
        System.out.println("\n🏆 LOYALTY PROGRAM DEMO");
        System.out.println("=".repeat(40));
        
        // Create customers with different loyalty levels
        Customer bronzeCustomer = new Customer("Bronze Member", "BRONZE001");
        Customer silverCustomer = new Customer("Silver Member", "SILVER001");
        Customer goldCustomer = new Customer("Gold Member", "GOLD001");
        
        // Add loyalty points
        bronzeCustomer.addLoyaltyPoints(25);
        silverCustomer.addLoyaltyPoints(75);
        goldCustomer.addLoyaltyPoints(150);
        
        System.out.println("👤 Customer Loyalty Status:");
        System.out.printf("🥉 %s: %s (%d points)\n", 
            bronzeCustomer.getName(), bronzeCustomer.getLoyaltyStatus(), bronzeCustomer.getLoyaltyPoints());
        System.out.printf("🥈 %s: %s (%d points)\n", 
            silverCustomer.getName(), silverCustomer.getLoyaltyStatus(), silverCustomer.getLoyaltyPoints());
        System.out.printf("🥇 %s: %s (%d points)\n", 
            goldCustomer.getName(), goldCustomer.getLoyaltyStatus(), goldCustomer.getLoyaltyPoints());
        
        System.out.println("\n💡 Loyalty Benefits:");
        System.out.println("🥉 Bronze (0-49 points): Standard service");
        System.out.println("🥈 Silver (50-99 points): 5% discount + priority booking");
        System.out.println("🥇 Gold (100+ points): 10% discount + premium support + free upgrades");
    }
    
    private static void generateReports(RentalAgency agency) {
        System.out.println("\n📊 SYSTEM REPORTS");
        System.out.println("=".repeat(40));
        
        // Fleet report
        System.out.println("📋 FLEET REPORT:");
        agency.generateFleetReport();
        
        // Active rentals report
        System.out.println("\n🚗 ACTIVE RENTALS REPORT:");
        agency.generateActiveRentalsReport();
        
        // Financial summary (simulate)
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
        System.out.printf("🚗 Fleet Utilization: %.1f%%\n", 
            (totalRentals * 100.0) / agency.getFleet().size());
    }
    
    private static void demonstrateErrorHandling(RentalAgency agency) {
        System.out.println("\n⚠️ ERROR HANDLING DEMO");
        System.out.println("=".repeat(40));
        
        Customer customer = new Customer("Error Demo Customer", "ERROR001");
        
        // Test 1: Invalid rental period
        System.out.println("🧪 Test 1: Invalid Rental Period");
        try {
            Vehicle testVehicle = agency.getFleet().get(0);
            agency.rentVehicle(testVehicle, customer, -1);
        } catch (InvalidRentalPeriod e) {
            System.out.println("✅ Caught InvalidRentalPeriod: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✅ Caught Exception: " + e.getMessage());
        }
        
        // Test 2: Invalid rating
        System.out.println("\n🧪 Test 2: Invalid Rating");
        try {
            agency.rateVehicle(agency.getFleet().get(0), 10); // Invalid rating
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Caught IllegalArgumentException: " + e.getMessage());
        }
        
        System.out.println("\n✅ Error handling demonstration completed!");
    }
}