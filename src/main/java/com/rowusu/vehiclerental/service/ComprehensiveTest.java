package com.rowusu.vehiclerental.service;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.exceptions.*;

public class ComprehensiveTest {
    private static int testsPassed = 0;
    private static int testsTotal = 0;
    
    public static void main(String[] args) {
        System.out.println("🧪 COMPREHENSIVE FEATURE TESTING");
        System.out.println("=".repeat(50));
        
        testVehicleCreation();
        testVehicleFeatures();
        testCustomerManagement();
        testRentalWorkflow();
        testReturnWorkflow();
        testRatingSystem();
        testLoyaltyProgram();
        testErrorHandling();
        testBusinessRules();
        testReportGeneration();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.printf("📊 TEST RESULTS: %d/%d tests passed (%.1f%%)\n", 
            testsPassed, testsTotal, (testsPassed * 100.0) / testsTotal);
        
        if (testsPassed == testsTotal) {
            System.out.println("🎉 ALL TESTS PASSED! Your system is working perfectly!");
        } else {
            System.out.printf("⚠️ %d tests failed. Review the output above.\n", testsTotal - testsPassed);
        }
    }
    
    private static void testVehicleCreation() {
        System.out.println("\n🚗 Testing Vehicle Creation...");
        
        // Test Car creation
        try {
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
            assert car.getVehicleId().equals("CAR001");
            assert car.getModel().equals("Toyota Camry");
            assert car.getBaseRentalRate() == 45.0;
            assert car.hasGPS() == true;
            assert car.hasChildSeat() == false;
            assert car.hasSunroof() == true;
            assert car.isAvailable() == true;
            passTest("Car creation with valid parameters");
        } catch (Exception e) {
            failTest("Car creation", e);
        }
        
        // Test Motorcycle creation
        try {
            Motorcycle bike = new Motorcycle("BIKE001", "Yamaha R6", 35.0, true, true);
            assert bike.getVehicleId().equals("BIKE001");
            assert bike.getModel().equals("Yamaha R6");
            assert bike.getBaseRentalRate() == 35.0;
            passTest("Motorcycle creation with valid parameters");
        } catch (Exception e) {
            failTest("Motorcycle creation", e);
        }
        
        // Test Truck creation
        try {
            Truck truck = new Truck("TRUCK001", "Ford F-150", 80.0, true, false);
            assert truck.getVehicleId().equals("TRUCK001");
            assert truck.getModel().equals("Ford F-150");
            assert truck.getBaseRentalRate() == 80.0;
            passTest("Truck creation with valid parameters");
        } catch (Exception e) {
            failTest("Truck creation", e);
        }
        
        // Test invalid vehicle creation
        try {
            new Car("", "Invalid Car", 45.0, true, false, true);
            failTest("Car creation with empty ID should fail");
        } catch (IllegalArgumentException e) {
            passTest("Car creation validation (empty ID rejected)");
        } catch (Exception e) {
            failTest("Car creation validation", e);
        }
        
        try {
            new Car("CAR001", "Valid Car", -10.0, true, false, true);
            failTest("Car creation with negative rate should fail");
        } catch (IllegalArgumentException e) {
            passTest("Car creation validation (negative rate rejected)");
        } catch (Exception e) {
            failTest("Car creation validation", e);
        }
    }
    
    private static void testVehicleFeatures() {
        System.out.println("\n⚡ Testing Vehicle Features...");
        
        try {
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
            Feature gpsFeature = new Feature("GPS Navigation", 5.0);
            Feature luxuryFeature = new Feature("Luxury Package", 15.0);
            
            car.addFeature(gpsFeature);
            car.addFeature(luxuryFeature);
            
            assert car.getFeatures().size() == 2;
            assert car.calculateTotalFeatureCost() == 20.0;
            
            double totalCost = car.calculateRentalCost(3);
            assert totalCost == (45.0 + 20.0) * 3;
            
            passTest("Feature addition and cost calculation");
        } catch (Exception e) {
            failTest("Feature management", e);
        }
    }
    
    private static void testCustomerManagement() {
        System.out.println("\n👤 Testing Customer Management...");
        
        try {
            Customer customer = new Customer("John Smith", "CUST001");
            assert customer.getName().equals("John Smith");
            assert customer.getCustomerId().equals("CUST001");
            assert customer.isEligibleForRental() == true;
            assert customer.getCurrentRentals().size() == 0;
            assert customer.getLoyaltyPoints() == 0;
            assert customer.getLoyaltyStatus().equals("Bronze");
            passTest("Customer creation and initial state");
        } catch (Exception e) {
            failTest("Customer creation", e);
        }
        
        try {
            Customer customer = new Customer("Jane Doe", "CUST002");
            customer.addLoyaltyPoints(75);
            assert customer.getLoyaltyPoints() == 75;
            assert customer.getLoyaltyStatus().equals("Silver");
            
            customer.addLoyaltyPoints(50);
            assert customer.getLoyaltyPoints() == 125;
            assert customer.getLoyaltyStatus().equals("Gold");
            passTest("Loyalty points and status management");
        } catch (Exception e) {
            failTest("Loyalty system", e);
        }
    }
    
    private static void testRentalWorkflow() {
        System.out.println("\n📝 Testing Rental Workflow...");
        
        try {
            RentalAgency agency = new RentalAgency();
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
            Customer customer = new Customer("Alice Brown", "CUST001");
            
            agency.addVehicleToFleet(car);
            
            // Test successful rental
            agency.rentVehicle(car, customer, 5);
            assert !car.isAvailableForRental();
            assert customer.getCurrentRentals().size() == 1;
            assert customer.getCurrentRentals().containsKey(car);
            passTest("Successful vehicle rental");
            
            // Test rental of unavailable vehicle
            Customer customer2 = new Customer("Bob Wilson", "CUST002");
            try {
                agency.rentVehicle(car, customer2, 3);
                failTest("Rental of unavailable vehicle should fail");
            } catch (VehicleNotAvailable e) {
                passTest("Rental validation (unavailable vehicle rejected)");
            }
            
        } catch (Exception e) {
            failTest("Rental workflow", e);
        }
    }
    
    private static void testReturnWorkflow() {
        System.out.println("\n🔄 Testing Return Workflow...");
        
        try {
            RentalAgency agency = new RentalAgency();
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
            Customer customer = new Customer("Alice Brown", "CUST001");
            
            agency.addVehicleToFleet(car);
            agency.rentVehicle(car, customer, 5);
            
            // Test successful return
            agency.processReturn(car);
            assert car.isAvailable();
            assert customer.getCurrentRentals().size() == 0;
            assert customer.getRentalHistory().contains(car);
            passTest("Successful vehicle return");
            
        } catch (Exception e) {
            failTest("Return workflow", e);
        }
    }
    
    private static void testRatingSystem() {
        System.out.println("\n⭐ Testing Rating System...");
        
        try {
            RentalAgency agency = new RentalAgency();
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
            Customer customer = new Customer("Alice Brown", "CUST001");
            
            // Test vehicle rating
            agency.rateVehicle(car, 5);
            agency.rateVehicle(car, 4);
            agency.rateVehicle(car, 5);
            
            double avgRating = car.getAverageRating();
            assert Math.abs(avgRating - 4.67) < 0.1; // Allow for floating point precision
            passTest("Vehicle rating calculation");
            
            // Test customer rating
            agency.rateCustomer(customer, 5);
            agency.rateCustomer(customer, 4);
            
            double custAvgRating = customer.getAverageRating();
            assert custAvgRating == 4.5;
            passTest("Customer rating calculation");
            
            // Test invalid rating
            try {
                agency.rateVehicle(car, 10);
                failTest("Invalid rating should be rejected");
            } catch (IllegalArgumentException e) {
                passTest("Rating validation (invalid rating rejected)");
            }
            
        } catch (Exception e) {
            failTest("Rating system", e);
        }
    }
    
    private static void testLoyaltyProgram() {
        System.out.println("\n🏆 Testing Loyalty Program...");
        
        try {
            Customer bronzeCustomer = new Customer("Bronze User", "BRONZE001");
            Customer silverCustomer = new Customer("Silver User", "SILVER001");
            Customer goldCustomer = new Customer("Gold User", "GOLD001");
            
            // Test Bronze status (0-49 points)
            bronzeCustomer.addLoyaltyPoints(25);
            assert bronzeCustomer.getLoyaltyStatus().equals("Bronze");
            
            // Test Silver status (50-99 points)
            silverCustomer.addLoyaltyPoints(75);
            assert silverCustomer.getLoyaltyStatus().equals("Silver");
            
            // Test Gold status (100+ points)
            goldCustomer.addLoyaltyPoints(150);
            assert goldCustomer.getLoyaltyStatus().equals("Gold");
            
            passTest("Loyalty status calculations");
            
            // Test negative points rejection
            try {
                bronzeCustomer.addLoyaltyPoints(-10);
                failTest("Negative loyalty points should be rejected");
            } catch (IllegalArgumentException e) {
                passTest("Loyalty points validation (negative points rejected)");
            }
            
        } catch (Exception e) {
            failTest("Loyalty program", e);
        }
    }
    
    private static void testErrorHandling() {
        System.out.println("\n⚠️ Testing Error Handling...");
        
        try {
            RentalAgency agency = new RentalAgency();
            Car car = new Car("CAR001", "Toyota Camry", 45.0, true, false, true);
            Customer customer = new Customer("Test Customer", "TEST001");
            
            agency.addVehicleToFleet(car);
            
            // Test invalid rental period
            try {
                agency.rentVehicle(car, customer, -1);
                failTest("Negative rental period should be rejected");
            } catch (InvalidRentalPeriod e) {
                passTest("Invalid rental period validation");
            }
            
            try {
                agency.rentVehicle(car, customer, 0);
                failTest("Zero rental period should be rejected");
            } catch (InvalidRentalPeriod e) {
                passTest("Zero rental period validation");
            }
            
        } catch (Exception e) {
            failTest("Error handling", e);
        }
    }
    
    private static void testBusinessRules() {
        System.out.println("\n📋 Testing Business Rules...");
        
        try {
            RentalAgency agency = new RentalAgency();
            Customer customer = new Customer("Heavy Renter", "HEAVY001");
            
            // Test rental limit enforcement
            Car car1 = new Car("CAR001", "Car 1", 45.0, true, false, true);
            Car car2 = new Car("CAR002", "Car 2", 45.0, true, false, true);
            Car car3 = new Car("CAR003", "Car 3", 45.0, true, false, true);
            
            agency.addVehicleToFleet(car1);
            agency.addVehicleToFleet(car2);
            agency.addVehicleToFleet(car3);
            
            // Should be able to rent 2 cars (rental limit)
            agency.rentVehicle(car1, customer, 3);
            agency.rentVehicle(car2, customer, 3);
            
            assert customer.getCurrentRentals().size() == 2;
            assert !customer.isEligibleForRental();
            
            // Third rental should fail
            try {
                agency.rentVehicle(car3, customer, 3);
                failTest("Rental limit should be enforced");
            } catch (CustomerNotEligible e) {
                passTest("Rental limit enforcement");
            }
            
        } catch (Exception e) {
            failTest("Business rules", e);
        }
    }
    
    private static void testReportGeneration() {
        System.out.println("\n📊 Testing Report Generation...");
        
        try {
            RentalAgency agency = new RentalAgency();
            Car car1 = new Car("CAR001", "Car 1", 45.0, true, false, true);
            Car car2 = new Car("CAR002", "Car 2", 50.0, true, false, true);
            Customer customer = new Customer("Report Customer", "REP001");
            
            agency.addVehicleToFleet(car1);
            agency.addVehicleToFleet(car2);
            
            // Test fleet report generation
            agency.generateFleetReport();
            
            // Rent one vehicle and test active rentals report
            agency.rentVehicle(car1, customer, 5);
            agency.generateActiveRentalsReport();
            
            assert agency.getFleet().size() == 2;
            passTest("Report generation functionality");
            
        } catch (Exception e) {
            failTest("Report generation", e);
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
    }
}