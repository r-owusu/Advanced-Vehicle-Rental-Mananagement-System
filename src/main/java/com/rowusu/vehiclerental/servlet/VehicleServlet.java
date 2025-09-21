package com.rowusu.vehiclerental.servlet;

import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.exceptions.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

/**
 * Servlet for handling vehicle management operations
 */
@WebServlet("/vehicles")
public class VehicleServlet extends HttpServlet {
    
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
                // Return list of all vehicles
                List<Vehicle> vehicles = agency.getFleet();
                StringBuilder json = new StringBuilder();
                json.append("{\"vehicles\":[");
                
                for (int i = 0; i < vehicles.size(); i++) {
                    Vehicle v = vehicles.get(i);
                    if (i > 0) json.append(",");
                    json.append("{")
                        .append("\"id\":\"").append(v.getVehicleId()).append("\",")
                        .append("\"type\":\"").append(getVehicleType(v)).append("\",")
                        .append("\"model\":\"").append(v.getModel()).append("\",")
                        .append("\"rate\":").append(v.getBaseRentalRate()).append(",")
                        .append("\"isAvailable\":").append(v.isAvailable()).append(",")
                        .append("\"rating\":").append(v.getAverageRating())
                        .append("}");
                }
                
                json.append("]}");
                out.print(json.toString());
                
            } else if ("available".equals(action)) {
                // Return only available vehicles
                List<Vehicle> allVehicles = agency.getFleet();
                List<Vehicle> availableVehicles = new ArrayList<>();
                for (Vehicle v : allVehicles) {
                    if (v.isAvailable()) {
                        availableVehicles.add(v);
                    }
                }
                
                StringBuilder json = new StringBuilder();
                json.append("{\"vehicles\":[");
                
                for (int i = 0; i < availableVehicles.size(); i++) {
                    Vehicle v = availableVehicles.get(i);
                    if (i > 0) json.append(",");
                    json.append("{")
                        .append("\"id\":\"").append(v.getVehicleId()).append("\",")
                        .append("\"type\":\"").append(getVehicleType(v)).append("\",")
                        .append("\"model\":\"").append(v.getModel()).append("\",")
                        .append("\"rate\":").append(v.getBaseRentalRate())
                        .append("}");
                }
                
                json.append("]}");
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
            if ("add".equals(action)) {
                // Add new vehicle
                String vehicleId = request.getParameter("id");
                String type = request.getParameter("type");
                String model = request.getParameter("model");
                double rate = Double.parseDouble(request.getParameter("rate"));
                
                Vehicle vehicle = createVehicle(type, vehicleId, model, rate);
                agency.addVehicleToFleet(vehicle);
                
                out.print("{\"success\":true,\"message\":\"Vehicle added successfully\"}");
                
            } else if ("rate".equals(action)) {
                // Rate a vehicle
                String vehicleId = request.getParameter("id");
                double rating = Double.parseDouble(request.getParameter("rating"));
                
                Vehicle vehicle = findVehicle(agency, vehicleId);
                if (vehicle != null) {
                    agency.rateVehicle(vehicle, (int)rating);
                    out.print("{\"success\":true,\"message\":\"Vehicle rated successfully\"}");
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
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String vehicleId = request.getParameter("id");
        HttpSession session = request.getSession();
        RentalAgency agency = getRentalAgency(session);
        
        try {
            Vehicle vehicle = findVehicle(agency, vehicleId);
            if (vehicle != null) {
                agency.removeVehicleFromFleet(vehicle);
                out.print("{\"success\":true,\"message\":\"Vehicle removed successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Vehicle not found\"}");
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Helper method to create vehicle instances based on type
     */
    private Vehicle createVehicle(String type, String id, String model, double rate) {
        switch (type.toLowerCase()) {
            case "car":
                return new Car(id, model, rate, false, false, false);
            case "motorcycle":
                return new Motorcycle(id, model, rate, false, false);
            case "truck":
                return new Truck(id, model, rate, false, false);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
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
     * Helper method to get vehicle type string
     */
    private String getVehicleType(Vehicle vehicle) {
        if (vehicle instanceof Car) return "Car";
        if (vehicle instanceof Motorcycle) return "Motorcycle";
        if (vehicle instanceof Truck) return "Truck";
        return "Unknown";
    }
}