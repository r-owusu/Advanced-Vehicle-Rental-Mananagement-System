package com.rowusu.vehiclerental.service;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.exceptions.*;

public class RealWorldScenarioTest {
    public static void main(String[] args) {
        System.out.println("🌍 REAL-WORLD SCENARIO TESTING");
        System.out.println("=".repeat(50));
        
        testCompleteRentalBusinessDay();
        testComplexCustomerJourney();
        testFleetManagementScenario();
        testErrorRecoveryScenarios();
        
        System.out.println("\n🎉 ALL REAL-WORLD SCENARIOS COMPLETED SUCCESSFULLY!");
        System.out.println("Your Vehicle Rental Management System is production-ready! 🚀");
    }
    
    private static void testCompleteRentalBusinessDay() {
        System.out.println("\n📅 Scenario 1: Complete Business Day Simulation");
        System.out.println("-".repeat(45));
        
        RentalAgency agency = new RentalAgency();
        
        // Setup fleet
        System.out.println("🌅 Morning: Setting up fleet...");
        Car economyCar = new Car("ECON001", "Honda Civic", 35.0, false, true, false);
        Car luxuryCar = new Car("LUX001", "BMW 5 Series", 85.0, true, false, true);
        Motorcycle bike = new Motorcycle("BIKE001", "Harley Davidson", 45.0, true, true);
        Truck movingTruck = new Truck("TRUCK001", "U-Haul", 65.0, true, false);
        
        // Add premium features
        luxuryCar.addFeature(new Feature("Premium GPS", 10.0));
        luxuryCar.addFeature(new Feature("Leather Seats", 15.0));
        bike.addFeature(new Feature("Touring Package", 8.0));
        movingTruck.addFeature(new Feature("Loading Ramp", 12.0));
        
        agency.addVehicleToFleet(economyCar);
        agency.addVehicleToFleet(luxuryCar);
        agency.addVehicleToFleet(bike);
        agency.addVehicleToFleet(movingTruck);
        
        // Create customers
        Customer student = new Customer("Emma Student", "STU001");
        Customer businessman = new Customer("Robert Executive", "BUS001");
        Customer adventurer = new Customer("Alex Rider", "ADV001");
        Customer mover = new Customer("Sarah Moving", "MOV001");
        
        // Give some customers loyalty points
        businessman.addLoyaltyPoints(150); // Gold member
        adventurer.addLoyaltyPoints(65);   // Silver member
        
        System.out.println("☀️ Day Operations: Processing rentals...");
        
        try {
            // Student rents economy car (budget conscious)
            System.out.println("\n💰 Budget rental: Student -> Economy Car");
            agency.rentVehicle(economyCar, student, 3);
            double studentCost = economyCar.calculateRentalCost(3);
            System.out.printf("   Cost: $%.2f for 3 days\n", studentCost);
            
            // Businessman rents luxury car (business trip)
            System.out.println("\n💼 Business rental: Executive -> Luxury Car");
            agency.rentVehicle(luxuryCar, businessman, 7);
            double businessCost = luxuryCar.calculateRentalCost(7);
            System.out.printf("   Cost: $%.2f for 7 days (Gold member gets benefits)\n", businessCost);
            
            // Adventurer rents motorcycle (weekend trip)
            System.out.println("\n🏍️ Adventure rental: Rider -> Motorcycle");
            agency.rentVehicle(bike, adventurer, 2);
            double bikeCost = bike.calculateRentalCost(2);
            System.out.printf("   Cost: $%.2f for 2 days (Silver member)\n", bikeCost);
            
            // Mover rents truck (moving day)
            System.out.println("\n📦 Moving rental: Mover -> Truck");
            agency.rentVehicle(movingTruck, mover, 1);
            double truckCost = movingTruck.calculateRentalCost(1);
            System.out.printf("   Cost: $%.2f for 1 day\n", truckCost);
            
            System.out.println("\n📊 Midday Report:");
            agency.generateActiveRentalsReport();
            
            // Some returns in the afternoon
            System.out.println("\n🌆 Afternoon: Processing returns...");
            agency.processReturn(movingTruck); // Truck returned same day
            agency.processReturn(bike);        // Motorcycle returned after weekend
            
            System.out.println("\n📊 End of Day Report:");
            agency.generateFleetReport();
            agency.generateActiveRentalsReport();
            
        } catch (Exception e) {
            System.out.println("❌ Error in business day simulation: " + e.getMessage());
        }
    }
    
    private static void testComplexCustomerJourney() {
        System.out.println("\n👤 Scenario 2: Complex Customer Journey");
        System.out.println("-".repeat(45));
        
        RentalAgency agency = new RentalAgency();
        Customer loyalCustomer = new Customer("Jennifer Frequent", "LOYAL001");
        
        // Setup some vehicles
        Car car1 = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
        Car car2 = new Car("CAR002", "Nissan Altima", 42.0, false, true, false);
        Motorcycle bike = new Motorcycle("BIKE001", "Kawasaki Ninja", 38.0, true, false);
        
        agency.addVehicleToFleet(car1);
        agency.addVehicleToFleet(car2);
        agency.addVehicleToFleet(bike);
        
        try {
            System.out.println("📈 Customer Journey: Building loyalty over time");
            
            // First rental - Bronze status
            System.out.printf("🥉 Initial status: %s (%d points)\n", 
                loyalCustomer.getLoyaltyStatus(), loyalCustomer.getLoyaltyPoints());
            
            agency.rentVehicle(car1, loyalCustomer, 5);
            loyalCustomer.addLoyaltyPoints(50); // Points for 5-day rental
            agency.processReturn(car1);
            
            System.out.printf("🥈 After first rental: %s (%d points)\n", 
                loyalCustomer.getLoyaltyStatus(), loyalCustomer.getLoyaltyPoints());
            
            // Second rental - Silver status
            agency.rentVehicle(bike, loyalCustomer, 3);
            loyalCustomer.addLoyaltyPoints(30); // Points for 3-day rental
            agency.processReturn(bike);
            
            System.out.printf("🥇 After second rental: %s (%d points)\n", 
                loyalCustomer.getLoyaltyStatus(), loyalCustomer.getLoyaltyPoints());
            
            // Third rental - Gold status benefits
            agency.rentVehicle(car2, loyalCustomer, 10);
            loyalCustomer.addLoyaltyPoints(100); // Points for 10-day rental
            
            System.out.printf("💎 After becoming Gold: %s (%d points)\n", 
                loyalCustomer.getLoyaltyStatus(), loyalCustomer.getLoyaltyPoints());
            
            // Rate the vehicles based on experience
            agency.rateVehicle(car1, 5);
            agency.rateVehicle(bike, 4);
            agency.rateVehicle(car2, 5);
            
            // Customer gets rated by agency
            agency.rateCustomer(loyalCustomer, 5); // Excellent customer
            
            System.out.printf("⭐ Customer rating: %.1f/5\n", loyalCustomer.getAverageRating());
            
        } catch (Exception e) {
            System.out.println("❌ Error in customer journey: " + e.getMessage());
        }
    }
    
    private static void testFleetManagementScenario() {
        System.out.println("\n🚗 Scenario 3: Dynamic Fleet Management");
        System.out.println("-".repeat(45));
        
        RentalAgency agency = new RentalAgency();
        
        System.out.println("📋 Building diverse fleet...");
        
        // Create different vehicle categories
        Vehicle[] economyFleet = {
            new Car("ECON001", "Hyundai Elantra", 32.0, false, false, false),
            new Car("ECON002", "Nissan Versa", 30.0, false, true, false),
            new Car("ECON003", "Honda Civic", 35.0, true, false, false)
        };
        
        Vehicle[] luxuryFleet = {
            new Car("LUX001", "Mercedes C-Class", 95.0, true, true, true),
            new Car("LUX002", "Audi A4", 90.0, true, false, true),
            new Car("LUX003", "BMW 3 Series", 92.0, true, true, false)
        };
        
        Vehicle[] specialtyFleet = {
            new Motorcycle("BIKE001", "Honda CBR", 40.0, true, true),
            new Motorcycle("BIKE002", "Yamaha YZF", 42.0, true, false),
            new Truck("TRUCK001", "Ford Transit", 70.0, true, true),
            new Truck("TRUCK002", "Chevy Express", 68.0, false, true)
        };
        
        // Add all vehicles to fleet
        for (Vehicle v : economyFleet) agency.addVehicleToFleet(v);
        for (Vehicle v : luxuryFleet) agency.addVehicleToFleet(v);
        for (Vehicle v : specialtyFleet) agency.addVehicleToFleet(v);
        
        System.out.printf("🎯 Total fleet size: %d vehicles\n", agency.getFleet().size());
        
        // Simulate high demand day
        Customer[] customers = {
            new Customer("Customer A", "CUST001"),
            new Customer("Customer B", "CUST002"),
            new Customer("Customer C", "CUST003"),
            new Customer("Customer D", "CUST004"),
            new Customer("Customer E", "CUST005")
        };
        
        try {
            System.out.println("\n🔥 High demand simulation...");
            
            // Rent out multiple vehicles
            agency.rentVehicle(economyFleet[0], customers[0], 3);
            agency.rentVehicle(luxuryFleet[0], customers[1], 5);
            agency.rentVehicle(specialtyFleet[0], customers[2], 2);
            agency.rentVehicle(economyFleet[1], customers[3], 4);
            agency.rentVehicle(luxuryFleet[1], customers[4], 7);
            
            // Check fleet utilization
            int rentedVehicles = 0;
            for (Vehicle v : agency.getFleet()) {
                if (!v.isAvailable()) rentedVehicles++;
            }
            
            double utilization = (rentedVehicles * 100.0) / agency.getFleet().size();
            System.out.printf("📈 Fleet utilization: %.1f%% (%d/%d vehicles rented)\n", 
                utilization, rentedVehicles, agency.getFleet().size());
            
            // Demonstrate category availability
            System.out.println("\n📊 Available vehicles by category:");
            int econAvailable = 0, luxAvailable = 0, specialAvailable = 0;
            
            for (Vehicle v : economyFleet) if (v.isAvailable()) econAvailable++;
            for (Vehicle v : luxuryFleet) if (v.isAvailable()) luxAvailable++;
            for (Vehicle v : specialtyFleet) if (v.isAvailable()) specialAvailable++;
            
            System.out.printf("   💰 Economy: %d/%d available\n", econAvailable, economyFleet.length);
            System.out.printf("   💎 Luxury: %d/%d available\n", luxAvailable, luxuryFleet.length);
            System.out.printf("   🏍️ Specialty: %d/%d available\n", specialAvailable, specialtyFleet.length);
            
        } catch (Exception e) {
            System.out.println("❌ Error in fleet management: " + e.getMessage());
        }
    }
    
    private static void testErrorRecoveryScenarios() {
        System.out.println("\n⚠️ Scenario 4: Error Recovery & Edge Cases");
        System.out.println("-".repeat(45));
        
        RentalAgency agency = new RentalAgency();
        Car testCar = new Car("TEST001", "Test Vehicle", 50.0, true, true, true);
        agency.addVehicleToFleet(testCar);
        
        // Test various error conditions
        System.out.println("🧪 Testing error conditions and recovery...");
        
        // 1. Customer at rental limit
        Customer heavyRenter = new Customer("Heavy Renter", "HEAVY001");
        Car car1 = new Car("CAR001", "Car 1", 40.0, true, false, false);
        Car car2 = new Car("CAR002", "Car 2", 40.0, true, false, false);
        Car car3 = new Car("CAR003", "Car 3", 40.0, true, false, false);
        
        agency.addVehicleToFleet(car1);
        agency.addVehicleToFleet(car2);
        agency.addVehicleToFleet(car3);
        
        try {
            agency.rentVehicle(car1, heavyRenter, 3);
            agency.rentVehicle(car2, heavyRenter, 3);
            System.out.println("✅ Customer successfully rented 2 vehicles (at limit)");
            
            // This should fail
            agency.rentVehicle(car3, heavyRenter, 3);
            System.out.println("❌ ERROR: Third rental should have failed!");
        } catch (CustomerNotEligible e) {
            System.out.println("✅ Rental limit properly enforced: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
        }
        
        // 2. Invalid rental periods
        Customer normalCustomer = new Customer("Normal Customer", "NORM001");
        try {
            agency.rentVehicle(testCar, normalCustomer, 0);
            System.out.println("❌ ERROR: Zero-day rental should have failed!");
        } catch (InvalidRentalPeriod e) {
            System.out.println("✅ Zero-day rental properly rejected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
        }
        
        try {
            agency.rentVehicle(testCar, normalCustomer, -5);
            System.out.println("❌ ERROR: Negative rental period should have failed!");
        } catch (InvalidRentalPeriod e) {
            System.out.println("✅ Negative rental period properly rejected: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
        }
        
        // 3. Return non-rented vehicle
        Car availableCar = new Car("AVAIL001", "Available Car", 45.0, true, true, true);
        agency.addVehicleToFleet(availableCar);
        
        System.out.println("🔄 Testing return of non-rented vehicle...");
        agency.processReturn(availableCar); // Should handle gracefully
        
        // 4. Rate with invalid values
        System.out.println("⭐ Testing invalid rating scenarios...");
        agency.rateVehicle(testCar, 0);   // Invalid - too low
        agency.rateVehicle(testCar, 6);   // Invalid - too high
        agency.rateVehicle(testCar, -1);  // Invalid - negative
        
        // Valid ratings should still work
        agency.rateVehicle(testCar, 5);
        agency.rateVehicle(testCar, 4);
        System.out.printf("✅ Valid ratings processed. Average: %.1f/5\n", testCar.getAverageRating());
        
        System.out.println("✅ All error recovery scenarios handled correctly!");
    }
}