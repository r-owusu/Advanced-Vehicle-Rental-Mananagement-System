package com.rowusu.vehiclerental.customers;

import com.rowusu.vehiclerental.interfaces.LoyaltyProgram;
import com.rowusu.vehiclerental.interfaces.Ratable;
import com.rowusu.vehiclerental.model.Vehicle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Customer implements LoyaltyProgram, Ratable {
    private final String name;
    private final String customerId;
    private final List<Vehicle> rentalHistory; // Rental history as strings (or RentalTransaction if available)
    private final  Map<Vehicle, Integer> currentRentals; // Current rentals: Vehicle -> Rental days
    private int rentalLimit = 2; // Max number of concurrent rentals allowed
    private List<Integer> ratings; // List to store all ratings
    private boolean eligibleForRental;

    // Constructor
    public Customer(String name, String customerId) {
        this.name = name;
        this.customerId = customerId;
        this.rentalHistory = new ArrayList<>();
        this.currentRentals = new HashMap<>();
        this.ratings = new ArrayList<>();
        this.eligibleForRental = true; // Default to eligible

    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    // Rental History Management
    public void addToRentalHistory(Vehicle transactionDetails) {
        rentalHistory.add(transactionDetails);
    }

    public List<Vehicle> getRentalHistory() {
        return rentalHistory;
    }

    // Current Rentals Management
    public void addCurrentRental(Vehicle vehicle, int days) {
        if (!isEligibleForRental()) {
            throw new IllegalStateException("Customer is not eligible for new rentals.");
        }
        if (currentRentals.size() >= rentalLimit) {
            throw new IllegalStateException("Customer has exceeded rental limit.");
        }
        currentRentals.put(vehicle, days);
    }

    public void removeCurrentRental(Vehicle vehicle) {
        System.out.println("Removing rental: " + vehicle.getVehicleId());
        currentRentals.remove(vehicle);
    }

    public Map<Vehicle, Integer> getCurrentRentals() {
        return currentRentals;
    }

    // Rental Eligibility Check
    /*public boolean isEligibleForRental() {
        // Example criteria: No more than `rentalLimit` active rentals
        return currentRentals.size() < rentalLimit;
    }*/
    public boolean isEligibleForRental() {
        return eligibleForRental;
    }
    private int loyaltyPoints;  // Track customer's points

    @Override
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    @Override
    public void addLoyaltyPoints(int points) {
        if (points<0){
            throw new IllegalArgumentException("Loyalty points cannot be negative.");
        }
        loyaltyPoints += points;
    }

    @Override
    public String getLoyaltyStatus() {
        if (loyaltyPoints >= 100) return "Gold";
        if (loyaltyPoints >= 50) return "Silver";
        return "Bronze";
    }

    public void addRental(Vehicle vehicle, String transactionDetails, int days) {
        if (!isEligibleForRental()) {
            throw new IllegalStateException("Customer is not eligible for new rentals.");
        }

        // Add to the current rentals
        addCurrentRental(vehicle, days);

        // Add to the rental history
        addToRentalHistory(vehicle);
    }

    // Method to add a rating
    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        ratings.add(rating);
    }

    // Method to calculate the average rating
    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0; // Default if no ratings
        }
        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same reference check
        if (o == null || getClass() != o.getClass()) return false; // Class type comparison
        Customer customer = (Customer) o; // Cast and compare customerId
        return customerId.equals(customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId); // Use customerId for generating hash code
    }
    public void setEligibleForRental(boolean eligibleForRental) {
        this.eligibleForRental = eligibleForRental;
    }
}

