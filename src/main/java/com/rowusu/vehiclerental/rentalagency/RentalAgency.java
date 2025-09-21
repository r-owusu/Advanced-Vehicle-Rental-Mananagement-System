package com.rowusu.vehiclerental.rentalagency;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.Vehicle;
import com.rowusu.vehiclerental.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RentalAgency {

    // List to manage the fleet of vehicles
    private List<Vehicle> vehicleFleet;

    // Map to track currently rented vehicles and their customers
    private Map<Vehicle, Customer> activeRentals;
    
    // List to manage customers
    private List<Customer> customers;

    // Constructor
    public RentalAgency() {
        this.vehicleFleet = new ArrayList<>();
        this.activeRentals = new HashMap<>();
        this.customers = new ArrayList<>();
    }
    
    public List<Vehicle> getFleet() {
        return vehicleFleet;
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    // Add a customer
    public void addCustomer(Customer customer) {
        customers.add(customer);
        System.out.println("Customer added: " + customer.getName());
    }
    // Add a vehicle to the fleet
    public void addVehicleToFleet(Vehicle vehicle) {
        vehicleFleet.add(vehicle);
        System.out.println("Vehicle added to fleet: " + vehicle.getModel());
    }

    // Remove a vehicle from the fleet
    public void removeVehicleFromFleet(Vehicle vehicle) {
        if (vehicleFleet.remove(vehicle)) {
            System.out.println("Vehicle removed from fleet: " + vehicle.getModel());
        } else {
            System.out.println("Vehicle not found in fleet.");
        }
    }

    // Process a rental
    public void processRental(Customer customer, Vehicle vehicle, int days) {
        if (!vehicleFleet.contains(vehicle)) {
            System.out.println("Vehicle is not part of the fleet.");
            return;
        }

        if (!vehicle.isAvailable()) {
            System.out.println("Vehicle is currently unavailable.");
            return;
        }

        if (!customer.isEligibleForRental()) {
            System.out.println("Customer is not eligible for rental.");
            return;
        }

        // Rent the vehicle
        vehicle.rent(customer, days);

        // Update active rentals
        activeRentals.put(vehicle, customer);

        System.out.println("Rental processed successfully for vehicle: " + vehicle.getModel());
// Inside processRental in RentalAgency:
        customer.addLoyaltyPoints(days * 10); // Award points for rental days

    }

    // Process a return
    public void processReturn(Vehicle vehicle) {
        if (!activeRentals.containsKey(vehicle)) {
            System.out.println("Vehicle is not currently rented.");
            return;
        }

        // Return the vehicle
        vehicle.returnVehicle();

        // Update active rentals
        Customer customer = activeRentals.remove(vehicle);
        customer.removeCurrentRental(vehicle);

        // Add to the customer's rental history
       customer.addToRentalHistory(vehicle);

        System.out.println("Vehicle returned successfully: " );
                //+ vehicle.getModel());
    }

    // Generate a report of all vehicles in the fleet
    public void generateFleetReport() {
        System.out.println("\nFleet Report:");
        for (Vehicle vehicle : vehicleFleet) {
            System.out.println(vehicle.getModel() + " - " + (vehicle.isAvailable() ? "Available" : "Rented"));
        }
    }

    // Generate a report of all active rentals
   /* public void generateActiveRentalsReport() {
        System.out.println("\nActive Rentals Report:");
        activeRentals.forEach((vehicle, customer) -> {
            System.out.println("Vehicle: " + vehicle.getModel() + ", Rented by: " + customer.getName());
        });
    }*/public void generateActiveRentalsReport() {
        System.out.println("Active Rentals Report:");
        if (activeRentals.isEmpty()) {
            System.out.println("No active rentals.");
            return;
        }

        for (Map.Entry<Vehicle, Customer> entry : activeRentals.entrySet()) {
            Vehicle vehicle = entry.getKey();
            Customer customer = entry.getValue();
            System.out.println(vehicle.getModel() + " rented by " + customer.getName());
        }
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, int days)
            throws VehicleNotAvailable, CustomerNotEligible, InvalidRentalPeriod {

        // Check if the rental period is valid
        if (days <= 0) {
            throw new InvalidRentalPeriod("Rental period must be greater than zero.");
        }
        // Check if the customer is eligible for rental
        if (!customer.isEligibleForRental()) {
            throw new CustomerNotEligible("Customer is not eligible for rental.");
        }
        // Check if the vehicle is available
        if (!vehicle.isAvailableForRental()) {
            throw new VehicleNotAvailable("Vehicle is not available for rental: " + vehicle.getModel());
        }

        vehicle.setAvailableForRental(false); // Mark vehicle as rented
        // Proceed with rental if all checks pass
        vehicle.rent(customer, days);
        String transactionDetails = "Rented for " + days + " days";
        customer.addRental(vehicle, transactionDetails, days);
        activeRentals.put(vehicle, customer);
    }
    // Method to rate a vehicle
    public void rateVehicle(Vehicle vehicle, int rating) {
        try {
            vehicle.addRating(rating);
            System.out.println("Vehicle " + vehicle.getModel() + " rated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error rating vehicle: " + e.getMessage());
        }
    }

    // Method to rate a customer
    public void rateCustomer(Customer customer, int rating) {
        try {
            customer.addRating(rating);
            System.out.println("Customer " + customer.getName() + " rated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error rating customer: " + e.getMessage());
        }
    }
}

