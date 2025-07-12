package com.rowusu.vehiclerental.rentalagency;
import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.Car;
import com.rowusu.vehiclerental.model.Truck;
import com.rowusu.vehiclerental.model.Vehicle;
import com.rowusu.vehiclerental.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RentalAgencyTest {

    private RentalAgency rentalAgency;
    private Customer customer1;
    private Customer customer2;
    private Vehicle car1;
    private Vehicle truck1;

    @BeforeEach
    public void setUp() {
        rentalAgency = new RentalAgency();

        // Create customers
        customer1 = new Customer("John Doe", "C123");
        customer2 = new Customer("Jane Smith", "C124");

        // Create vehicles
        car1 = new Car("V001", "Toyota Camry", 100.0, true, true, false);
        truck1 = new Truck("V002", "Ford F150", 150.0, true, false);

        // Add vehicles to the fleet
        rentalAgency.addVehicleToFleet(car1);
        rentalAgency.addVehicleToFleet(truck1);
    }

    // Test that vehicles are correctly added to the fleet
    @Test
    public void testAddVehicleToFleet() {
        assertTrue(rentalAgency.getFleet().contains(car1));
        assertTrue(rentalAgency.getFleet().contains(truck1));
    }

    // Test that vehicles are correctly removed from the fleet
    @Test
    public void testRemoveVehicleFromFleet() {
        rentalAgency.removeVehicleFromFleet(car1);
        assertFalse(rentalAgency.getFleet().contains(car1));
    }

    // Test that rental process works for eligible customers and available vehicles
    @Test
    public void testProcessRental_Success() {
        rentalAgency.processRental(customer1, car1, 5);
        assertFalse(car1.isAvailable()); // The car should be rented
    }

    // Test that rental fails for unavailable vehicles
    @Test
    public void testProcessRental_Failure_VehicleUnavailable() {
        rentalAgency.processRental(customer1, car1, 5);
        rentalAgency.processRental(customer2, car1, 3); // Car is already rented
        assertFalse(car1.isAvailable()); // The car should still be rented
    }

    // Test that rental fails for ineligible customers
  /*  @Test
   public void testProcessRental_Failure_CustomerNotEligible() {
        customer1.setEligibleForRental(false); // Make customer ineligible
        rentalAgency.processRental(customer1, car1, 5);
        assertTrue(car1.isAvailable()); // The car should remain available
    }*/
    @Test
    public void testProcessRental_Failure_CustomerNotEligible() {
        Vehicle car2 = new Car("V003", "Honda Accord", 90.0, true, true, false);
        rentalAgency.addVehicleToFleet(car2);

        // Fill up customer1's rental limit
        rentalAgency.processRental(customer1, car1, 3);
        rentalAgency.processRental(customer1, car2, 2);

        // Now try to rent a third vehicle
        Vehicle car3 = new Car("V004", "Nissan Altima", 95.0, true, true, false);
        rentalAgency.addVehicleToFleet(car3);

        assertThrows(CustomerNotEligible.class, () -> rentalAgency.rentVehicle(car3, customer1, 1));
    }

    // Test that rental period must be greater than zero
    @Test
    public void testProcessRental_Failure_InvalidRentalPeriod() {
        assertThrows(InvalidRentalPeriod.class, () -> rentalAgency.rentVehicle(car1, customer1, 0), "Rental period must be greater than zero.");
    }

    // Test that customer and vehicle rating works correctly
    @Test
    public void testRateVehicle_Success() {
        rentalAgency.rateVehicle(car1, 5);
        assertEquals(5, car1.getAverageRating());
    }

    // Test that rating a customer works correctly
    @Test
    public void testRateCustomer_Success() {
        rentalAgency.rateCustomer(customer1, 4);
        assertEquals(4, customer1.getAverageRating());
    }

    // Test that the fleet report generates correctly
    @Test
    public void testGenerateFleetReport() {
        rentalAgency.generateFleetReport(); // Should print out the fleet
    }

    // Test that the active rentals report generates correctly
    @Test
    public void testGenerateActiveRentalsReport() {
        rentalAgency.processRental(customer1, car1, 5);
        rentalAgency.generateActiveRentalsReport(); // Should print out active rentals
    }

    // Test that rental fails for a vehicle that is not available for rental
    // Test that rental fails for unavailable vehicles
    @Test
    public void testRentVehicle_Failure_VehicleNotAvailable() {
        // Rent the vehicle first
        rentalAgency.processRental(customer1, car1, 5);

        // Now attempt to rent the same vehicle again (it should throw VehicleNotAvailable exception)
        assertThrows(VehicleNotAvailable.class, () -> rentalAgency.rentVehicle(car1, customer2, 3), "Vehicle is not available for rental: " + car1.getModel());
    }

    // Test that rental fails for an ineligible customer
   /* @Test
    public void testRentVehicle_Failure_CustomerNotEligible() {
        customer1.setEligibleForRental(false); // Make customer ineligible
        assertThrows(CustomerNotEligible.class, () -> rentalAgency.rentVehicle(car1, customer1, 5), "Customer is not eligible for rental.");
    }*/
    @Test
    public void testRentVehicle_Failure_CustomerNotEligible() {
        // Create additional vehicles to reach rental limit
        Vehicle car2 = new Car("V003", "Honda Accord", 90.0, true, true, false);
        Vehicle car3 = new Car("V004", "Nissan Altima", 95.0, true, true, false);

        rentalAgency.addVehicleToFleet(car2);
        rentalAgency.addVehicleToFleet(car3);

        // Fill up customer1's rental limit
        rentalAgency.processRental(customer1, car1, 3);
        rentalAgency.processRental(customer1, car2, 2);

        // Attempt to rent a third vehicle — should fail
        assertThrows(CustomerNotEligible.class, () -> rentalAgency.rentVehicle(car3, customer1, 1),
                "Customer is not eligible for rental.");
    }

    // Test that invalid rental period throws exception
    @Test
    public void testRentVehicle_Failure_InvalidRentalPeriod() {
        assertThrows(InvalidRentalPeriod.class, () -> rentalAgency.rentVehicle(car1, customer1, -5), "Rental period must be greater than zero.");
    }

    // Test for rental when vehicle is unavailable
    @Test
    public void testRentVehicle_Failure_VehicleUnavailable() {
        rentalAgency.processRental(customer1, car1, 5);
        assertThrows(VehicleNotAvailable.class, () -> rentalAgency.rentVehicle(car1, customer2, 3), "Vehicle is not available for rental.");
    }

    // Test vehicle return after rental
    @Test
    public void testProcessReturn_Success() {
        rentalAgency.processRental(customer1, car1, 5);
        rentalAgency.processReturn(car1); // Return the car
        assertTrue(car1.isAvailable()); // The car should be available after return
    }

    // Test return of a vehicle that is not rented
    @Test
    public void testProcessReturn_Failure_VehicleNotRented() {
        rentalAgency.processReturn(car1); // The car is not rented yet
        assertTrue(car1.isAvailable()); // The car should remain available
    }

    // Test that the rental agency handles multiple vehicles and customers
    @Test
    public void testMultipleRentals() {
        rentalAgency.processRental(customer1, car1, 5);
        rentalAgency.processRental(customer2, truck1, 3);

        assertFalse(car1.isAvailable()); // The car should be rented by customer1
        assertFalse(truck1.isAvailable()); // The truck should be rented by customer2
    }
}


