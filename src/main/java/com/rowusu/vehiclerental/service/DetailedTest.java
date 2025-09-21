package com.rowusu.vehiclerental.service;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.exceptions.*;

public class DetailedTest {
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("🔬 DETAILED FEATURE TESTING & DEBUGGING");
        System.out.println("=".repeat(50));
        
        testRatingSystemDetailed();
        testBusinessRulesDetailed();
        testEdgeCases();
        testSpecificFeatures();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.printf("📊 DETAILED TEST RESULTS: %d/%d tests passed (%.1f%%)\n", 
            testsPassed, testsTotal, (testsPassed * 100.0) / testsTotal);
    }
    
    private static void testRatingSystemDetailed() {
        System.out.println("\n⭐ DETAILED Rating System Testing...");
        
        // Test direct vehicle rating (without RentalAgency wrapper)
        try {
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
            
            // Valid ratings should work
            car.addRating(5);
            car.addRating(4);
            car.addRating(3);
            assert car.getAverageRating() == 4.0;
            passTest("Direct vehicle rating - valid ratings");
            
            // Invalid rating should throw exception
            try {
                car.addRating(10);
                failTest("Direct vehicle rating should reject invalid rating (10)");
            } catch (IllegalArgumentException e) {
                passTest("Direct vehicle rating validation (rating > 5 rejected)");
            }
            
            try {
                car.addRating(0);
                failTest("Direct vehicle rating should reject invalid rating (0)");
            } catch (IllegalArgumentException e) {
                passTest("Direct vehicle rating validation (rating < 1 rejected)");
            }
            
        } catch (Exception e) {
            failTest("Direct vehicle rating", e);
        }
        
        // Test RentalAgency rating wrapper behavior
        try {
            RentalAgency agency = new RentalAgency();
            Car car = new Car("CAR002", "Honda Civic", 40.0, true, false, true);
            
            System.out.println("🧪 Testing RentalAgency.rateVehicle with invalid rating (should handle gracefully):");
            agency.rateVehicle(car, 10); // This catches exception internally
            
            // The vehicle should still have no ratings since invalid rating was caught
            assert car.getAverageRating() == 0.0;
            passTest("RentalAgency rating validation (graceful error handling)");
            
        } catch (Exception e) {
            failTest("RentalAgency rating system", e);
        }
    }
    
    private static void testBusinessRulesDetailed() {
        System.out.println("\n📋 DETAILED Business Rules Testing...");
        
        try {
            Customer customer = new Customer("Test Customer", "TEST001");
            Car car1 = new Car("CAR001", "Car 1", 45.0, true, false, true);
            Car car2 = new Car("CAR002", "Car 2", 45.0, true, false, true);
            Car car3 = new Car("CAR003", "Car 3", 45.0, true, false, true);
            
            System.out.println("🔍 Initial customer state:");
            System.out.println("   - Current rentals: " + customer.getCurrentRentals().size());
            System.out.println("   - Eligible for rental: " + customer.isEligibleForRental());
            
            // Rent first car
            car1.rent(customer, 3);
            System.out.println("🔍 After first rental:");
            System.out.println("   - Current rentals: " + customer.getCurrentRentals().size());
            System.out.println("   - Eligible for rental: " + customer.isEligibleForRental());
            
            // Rent second car
            car2.rent(customer, 3);
            System.out.println("🔍 After second rental:");
            System.out.println("   - Current rentals: " + customer.getCurrentRentals().size());
            System.out.println("   - Eligible for rental: " + customer.isEligibleForRental());
            
            assert customer.getCurrentRentals().size() == 2;
            assert !customer.isEligibleForRental();
            passTest("Customer rental limit reached (2 vehicles)");
            
            // Try to rent third car - should fail
            try {
                car3.rent(customer, 3);
                failTest("Third rental should fail due to limit");
            } catch (IllegalStateException e) {
                passTest("Rental limit enforcement (third rental rejected)");
                System.out.println("   ✅ Expected error: " + e.getMessage());
            }
            
        } catch (Exception e) {
            failTest("Business rules detailed testing", e);
        }
    }
    
    private static void testEdgeCases() {
        System.out.println("\n🧪 Testing Edge Cases...");
        
        // Test customer with 0 loyalty points
        try {
            Customer customer = new Customer("Zero Points", "ZERO001");
            assert customer.getLoyaltyPoints() == 0;
            assert customer.getLoyaltyStatus().equals("Bronze");
            passTest("Customer with zero loyalty points");
        } catch (Exception e) {
            failTest("Zero loyalty points edge case", e);
        }
        
        // Test exactly 50 loyalty points (boundary)
        try {
            Customer customer = new Customer("Boundary Test", "BOUND001");
            customer.addLoyaltyPoints(50);
            assert customer.getLoyaltyStatus().equals("Silver");
            passTest("Loyalty boundary test (exactly 50 points = Silver)");
        } catch (Exception e) {
            failTest("Loyalty boundary test", e);
        }
        
        // Test exactly 100 loyalty points (boundary)
        try {
            Customer customer = new Customer("Boundary Test 2", "BOUND002");
            customer.addLoyaltyPoints(100);
            assert customer.getLoyaltyStatus().equals("Gold");
            passTest("Loyalty boundary test (exactly 100 points = Gold)");
        } catch (Exception e) {
            failTest("Loyalty boundary test", e);
        }
        
        // Test rental cost calculation with no features
        try {
            Car car = new Car("CAR001", "Basic Car", 50.0, false, false, false);
            double cost = car.calculateRentalCost(3);
            assert cost == 150.0; // 50 * 3
            passTest("Rental cost calculation (no features)");
        } catch (Exception e) {
            failTest("Rental cost edge case", e);
        }
        
        // Test vehicle with 0 rating
        try {
            Car car = new Car("CAR001", "Unrated Car", 50.0, false, false, false);
            assert car.getAverageRating() == 0.0;
            passTest("Vehicle with no ratings");
        } catch (Exception e) {
            failTest("Zero rating edge case", e);
        }
    }
    
    private static void testSpecificFeatures() {
        System.out.println("\n🔧 Testing Specific Features...");
        
        // Test each vehicle type's unique features
        try {
            // Car specific features
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, true, true);
            assert car.hasGPS();
            assert car.hasChildSeat();
            assert car.hasSunroof();
            
            car.setGPS(false);
            car.setChildSeat(false);
            car.setSunroof(false);
            assert !car.hasGPS();
            assert !car.hasChildSeat();
            assert !car.hasSunroof();
            passTest("Car feature toggles");
            
        } catch (Exception e) {
            failTest("Car specific features", e);
        }
        
        try {
            // Motorcycle specific features
            Motorcycle bike = new Motorcycle("BIKE001", "Yamaha R6", 35.0, true, true);
            System.out.println("🏍️ Motorcycle toString: " + bike.toString());
            passTest("Motorcycle feature display");
            
        } catch (Exception e) {
            failTest("Motorcycle specific features", e);
        }
        
        try {
            // Truck specific features  
            Truck truck = new Truck("TRUCK001", "Ford F-150", 80.0, true, false);
            System.out.println("🚚 Truck toString: " + truck.toString());
            passTest("Truck feature display");
            
        } catch (Exception e) {
            failTest("Truck specific features", e);
        }
        
        // Test return vehicle workflow
        try {
            Car car = new Car("CAR001", "Test Car", 45.0, true, false, true);
            Customer customer = new Customer("Return Customer", "RET001");
            
            // Rent the car
            car.rent(customer, 5);
            assert !car.isAvailable();
            assert customer.getCurrentRentals().size() == 1;
            
            // Return the car
            car.returnVehicle();
            assert car.isAvailable();
            assert customer.getCurrentRentals().size() == 0;
            passTest("Vehicle return workflow");
            
        } catch (Exception e) {
            failTest("Return workflow", e);
        }
        
        // Test feature cost calculation
        try {
            Car car = new Car("CAR001", "Feature Car", 50.0, true, false, true);
            Feature premium = new Feature("Premium Package", 25.0);
            Feature insurance = new Feature("Full Insurance", 10.0);
            
            car.addFeature(premium);
            car.addFeature(insurance);
            
            double featureCost = car.calculateTotalFeatureCost();
            assert featureCost == 35.0;
            
            double totalCost = car.calculateRentalCost(2);
            assert totalCost == (50.0 + 35.0) * 2; // (base + features) * days
            passTest("Complex feature cost calculation");
            
        } catch (Exception e) {
            failTest("Feature cost calculation", e);
        }
    }
    
    private static void passTest(String testName) {
        testsTotal++;
        testsPassed++;
        System.out.println("✅ " + testName);
    }
    
    private static void failTest(String testName) {
        testsTotal++;
        System.out.println("❌ " + testName + " - FAILED");
    }
    
    private static void failTest(String testName, Exception e) {
        testsTotal++;
        System.out.println("❌ " + testName + " - FAILED: " + e.getMessage());
        e.printStackTrace();
    }
}