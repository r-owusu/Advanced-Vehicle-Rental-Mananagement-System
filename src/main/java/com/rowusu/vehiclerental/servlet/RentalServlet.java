package com.rowusu.vehiclerental.servlet;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.Vehicle;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.exceptions.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Servlet for handling rental operations
 */
@WebServlet("/rentals")
public class RentalServlet extends HttpServlet {
    
    private RentalAgency getRentalAgency(HttpSession session) {
        RentalAgency agency = (RentalAgency) session.getAttribute("rentalAgency");
        if (agency == null) {
            agency = new RentalAgency();
            session.setAttribute("rentalAgency", agency);
        }
        return agency;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        RentalAgency agency = getRentalAgency(session);
        
        try {
            if ("list".equals(action)) {
                // Return list of all active rentals
                StringBuilder json = new StringBuilder();
                json.append("{\"rentals\":[");
                
                boolean first = true;
                for (Customer customer : agency.getCustomers()) {
                    for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                        Vehicle vehicle = entry.getKey();
                        Integer days = entry.getValue();
                        
                        if (!first) json.append(",");
                        first = false;
                        
                        json.append("{")
                            .append("\"id\":\"RENT").append(vehicle.getVehicleId()).append("\",")
                            .append("\"vehicleId\":\"").append(vehicle.getVehicleId()).append("\",")
                            .append("\"vehicleModel\":\"").append(vehicle.getModel()).append("\",")
                            .append("\"customerId\":\"").append(customer.getCustomerId()).append("\",")
                            .append("\"customerName\":\"").append(customer.getName()).append("\",")
                            .append("\"days\":").append(days).append(",")
                            .append("\"totalCost\":").append(vehicle.calculateRentalCost(days)).append(",")
                            .append("\"startDate\":\"").append(java.time.LocalDate.now()).append("\",")
                            .append("\"status\":\"Active\"")
                            .append("}");
                    }
                }
                
                json.append("]}");
                out.print(json.toString());
                
            } else if ("stats".equals(action)) {
                // Return rental statistics
                int totalRentals = 0;
                double totalRevenue = 0.0;
                int totalDays = 0;
                
                for (Customer customer : agency.getCustomers()) {
                    for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                        Vehicle vehicle = entry.getKey();
                        Integer days = entry.getValue();
                        totalRentals++;
                        totalRevenue += vehicle.calculateRentalCost(days);
                        totalDays += days;
                    }
                }
                
                double avgDays = totalRentals > 0 ? (double) totalDays / totalRentals : 0;
                
                StringBuilder json = new StringBuilder();
                json.append("{")
                    .append("\"activeRentals\":").append(totalRentals).append(",")
                    .append("\"todayRentals\":").append(totalRentals).append(",")
                    .append("\"todayRevenue\":").append(totalRevenue).append(",")
                    .append("\"avgRentalDays\":").append(String.format("%.1f", avgDays))
                    .append("}");
                
                out.print(json.toString());
                
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid action\"}");
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        RentalAgency agency = getRentalAgency(session);
        
        try {
            if ("process".equals(action)) {
                // Process new rental
                String vehicleId = request.getParameter("vehicle");
                String customerId = request.getParameter("customer");
                int days = Integer.parseInt(request.getParameter("days"));
                
                Vehicle vehicle = findVehicle(agency, vehicleId);
                Customer customer = findCustomer(agency, customerId);
                
                if (vehicle == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Vehicle not found\"}");
                    return;
                }
                
                if (customer == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Customer not found\"}");
                    return;
                }
                
                try {
                    agency.rentVehicle(vehicle, customer, days);
                    String rentalId = "RENT" + vehicleId;
                    out.print("{\"success\":true,\"message\":\"Rental processed successfully\",\"rentalId\":\"" + rentalId + "\"}");
                } catch (VehicleNotAvailable | CustomerNotEligible | InvalidRentalPeriod e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\":\"" + e.getMessage() + "\"}");
                }
                
            } else if ("return".equals(action)) {
                // Process vehicle return
                String vehicleId = request.getParameter("vehicleId");
                
                Vehicle vehicle = findVehicle(agency, vehicleId);
                if (vehicle != null) {
                    agency.processReturn(vehicle);
                    out.print("{\"success\":true,\"message\":\"Vehicle returned successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Vehicle not found\"}");
                }
                
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid action\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Invalid number format\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Helper method to find vehicle by ID
     */
    private Vehicle findVehicle(RentalAgency agency, String vehicleId) {
        for (Vehicle vehicle : agency.getFleet()) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                return vehicle;
            }
        }
        return null;
    }
    
    /**
     * Helper method to find customer by ID
     */
    private Customer findCustomer(RentalAgency agency, String customerId) {
        for (Customer customer : agency.getCustomers()) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
}