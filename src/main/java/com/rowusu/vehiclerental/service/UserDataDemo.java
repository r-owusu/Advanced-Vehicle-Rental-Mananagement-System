package com.rowusu.vehiclerental.service;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;

public class UserDataDemo {
    public static void main(String[] args) {
        System.out.println("🚗 Vehicle Rental System - USER DATA DEMO 🚗");
        System.out.println("============================================");
        System.out.println("📝 This demo shows how to use YOUR OWN data instead of dummy data!");
        
        RentalAgency agency = new RentalAgency();
        
        // Demo 1: User creates their own vehicles
        System.out.println("\n🔧 Demo 1: Creating Your Own Vehicles");
        System.out.println("-".repeat(40));
        
        // User-defined car
        System.out.println("Creating your car with your specifications:");
        Car userCar = new Car("USER001", "My Toyota Prius", 55.0, true, true, false);
        userCar.addFeature(new Feature("Premium Sound System", 8.0));
        userCar.addFeature(new Feature("Backup Camera", 5.0));
        agency.addVehicleToFleet(userCar);
        
        // User-defined motorcycle
        System.out.println("Creating your motorcycle:");
        Motorcycle userBike = new Motorcycle("BIKE_USER", "My Kawasaki Ninja", 42.0, true, false);
        userBike.addFeature(new Feature("Racing Kit", 12.0));
        agency.addVehicleToFleet(userBike);
        
        // User-defined truck
        System.out.println("Creating your truck:");
        Truck userTruck = new Truck("TRUCK_USER", "My Ford Transit", 75.0, false, true);
        userTruck.addFeature(new Feature("GPS Tracking", 6.0));
        agency.addVehicleToFleet(userTruck);
        
        System.out.println("✅ Your vehicles added to fleet!");
        
        // Demo 2: User creates their own customers
        System.out.println("\n👤 Demo 2: Creating Your Own Customers");
        System.out.println("-".repeat(40));
        
        Customer customer1 = new Customer("John Smith", "JS001");
        Customer customer2 = new Customer("Sarah Williams", "SW002");
        Customer customer3 = new Customer("Mike Johnson", "MJ003");
        
        // Give customers different loyalty levels based on your business
        customer1.addLoyaltyPoints(25);   // Bronze
        customer2.addLoyaltyPoints(75);   // Silver  
        customer3.addLoyaltyPoints(120);  // Gold
        
        System.out.printf("Created customer: %s - %s status (%d points)\n", 
            customer1.getName(), customer1.getLoyaltyStatus(), customer1.getLoyaltyPoints());
        System.out.printf("Created customer: %s - %s status (%d points)\n", 
            customer2.getName(), customer2.getLoyaltyStatus(), customer2.getLoyaltyPoints());
        System.out.printf("Created customer: %s - %s status (%d points)\n", 
            customer3.getName(), customer3.getLoyaltyStatus(), customer3.getLoyaltyPoints());
        
        // Demo 3: User-defined rental scenarios
        System.out.println("\n📝 Demo 3: Your Rental Scenarios");
        System.out.println("-".repeat(35));
        
        double cost1 = 0, cost2 = 0, cost3 = 0;
        
        try {
            // Scenario 1: Business trip rental
            System.out.println("Scenario 1: John needs the car for a 3-day business trip");
            agency.rentVehicle(userCar, customer1, 3);
            cost1 = userCar.calculateRentalCost(3);
            System.out.printf("   Total cost: $%.2f for 3 days\n", cost1);
            
            // Scenario 2: Weekend adventure
            System.out.println("\nScenario 2: Sarah wants the motorcycle for a weekend adventure");
            agency.rentVehicle(userBike, customer2, 2);
            cost2 = userBike.calculateRentalCost(2);
            System.out.printf("   Total cost: $%.2f for 2 days (Silver member benefits)\n", cost2);
            
            // Scenario 3: Moving day
            System.out.println("\nScenario 3: Mike needs the truck for moving day");
            agency.rentVehicle(userTruck, customer3, 1);
            cost3 = userTruck.calculateRentalCost(1);
            System.out.printf("   Total cost: $%.2f for 1 day (Gold member benefits)\n", cost3);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // Demo 4: User rates their experience
        System.out.println("\n⭐ Demo 4: Your Ratings and Reviews");
        System.out.println("-".repeat(35));
        
        // Rate vehicles based on user experience
        agency.rateVehicle(userCar, 5);      // Excellent car
        agency.rateVehicle(userCar, 4);      // Another customer's rating
        
        agency.rateVehicle(userBike, 5);     // Perfect bike
        agency.rateVehicle(userBike, 5);     // Another excellent rating
        
        agency.rateVehicle(userTruck, 4);    // Good truck
        agency.rateVehicle(userTruck, 3);    // Decent service
        
        System.out.printf("Your car rating: %.1f/5\n", userCar.getAverageRating());
        System.out.printf("Your bike rating: %.1f/5\n", userBike.getAverageRating());
        System.out.printf("Your truck rating: %.1f/5\n", userTruck.getAverageRating());
        
        // Demo 5: Your business reports
        System.out.println("\n📊 Demo 5: Your Business Reports");
        System.out.println("-".repeat(30));
        
        System.out.println("Current Fleet Status:");
        agency.generateFleetReport();
        
        System.out.println("\nActive Rentals:");
        agency.generateActiveRentalsReport();
        
        // Calculate your revenue
        double totalRevenue = cost1 + cost2 + cost3;
        System.out.printf("\nYour total revenue from these rentals: $%.2f\n", totalRevenue);
        
        int activeRentals = 0;
        for (Vehicle v : agency.getFleet()) {
            if (!v.isAvailable()) activeRentals++;
        }
        System.out.printf("Fleet utilization: %.1f%% (%d/%d vehicles rented)\n", 
            (activeRentals * 100.0) / agency.getFleet().size(), activeRentals, agency.getFleet().size());
        
        System.out.println("\n🎉 Demo Complete!");
        System.out.println("=".repeat(50));
        System.out.println("📋 How to use YOUR data:");
        System.out.println("1. Run: java -cp compiled com.rowusu.vehiclerental.service.InteractiveApp");
        System.out.println("2. Follow the prompts to enter YOUR vehicles, customers, and rentals");
        System.out.println("3. The system will use YOUR data instead of dummy data!");
        System.out.println("4. You can add real vehicles from your fleet");
        System.out.println("5. Create real customers with their actual information");
        System.out.println("6. Process real rental transactions");
        System.out.println("7. Generate reports based on YOUR business data");
    }
}