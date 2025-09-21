package com.rowusu.vehiclerental.servlet;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for generating business reports and analytics
 */
@WebServlet("/reports")
public class ReportsServlet extends HttpServlet {
    
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
        
        String reportType = request.getParameter("type");
        HttpSession session = request.getSession();
        RentalAgency agency = getRentalAgency(session);
        
        try {
            if ("fleet".equals(reportType)) {
                generateFleetReport(agency, out);
            } else if ("revenue".equals(reportType)) {
                generateRevenueReport(agency, out);
            } else if ("customer".equals(reportType)) {
                generateCustomerReport(agency, out);
            } else if ("utilization".equals(reportType)) {
                generateUtilizationReport(agency, out);
            } else if ("dashboard".equals(reportType)) {
                generateDashboardReport(agency, out);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Invalid report type\"}");
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private void generateFleetReport(RentalAgency agency, PrintWriter out) {
        List<Vehicle> vehicles = agency.getFleet();
        int total = vehicles.size();
        int available = 0;
        int rented = 0;
        int maintenance = 0;
        double totalRating = 0;
        
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable()) {
                available++;
            } else {
                rented++;
            }
            totalRating += vehicle.getAverageRating();
        }
        
        double avgRating = total > 0 ? totalRating / total : 0;
        double utilization = total > 0 ? ((double) rented / total) * 100 : 0;
        
        StringBuilder json = new StringBuilder();
        json.append("{")
            .append("\"totalVehicles\":").append(total).append(",")
            .append("\"available\":").append(available).append(",")
            .append("\"rented\":").append(rented).append(",")
            .append("\"maintenance\":").append(maintenance).append(",")
            .append("\"utilization\":").append(String.format("%.1f", utilization)).append(",")
            .append("\"avgRating\":").append(String.format("%.1f", avgRating))
            .append("}");
        
        out.print(json.toString());
    }
    
    private void generateRevenueReport(RentalAgency agency, PrintWriter out) {
        double totalRevenue = 0;
        double todayRevenue = 0;
        int totalRentals = 0;
        int activeRentals = 0;
        
        for (Customer customer : agency.getCustomers()) {
            for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                Vehicle vehicle = entry.getKey();
                Integer days = entry.getValue();
                double cost = vehicle.calculateRentalCost(days);
                
                totalRevenue += cost;
                todayRevenue += cost; // Simplified - treating all as today's revenue
                totalRentals++;
                activeRentals++;
            }
        }
        
        double avgPerRental = totalRentals > 0 ? totalRevenue / totalRentals : 0;
        
        StringBuilder json = new StringBuilder();
        json.append("{")
            .append("\"totalRevenue\":").append(String.format("%.2f", totalRevenue)).append(",")
            .append("\"todayRevenue\":").append(String.format("%.2f", todayRevenue)).append(",")
            .append("\"totalRentals\":").append(totalRentals).append(",")
            .append("\"activeRentals\":").append(activeRentals).append(",")
            .append("\"avgPerRental\":").append(String.format("%.2f", avgPerRental))
            .append("}");
        
        out.print(json.toString());
    }
    
    private void generateCustomerReport(RentalAgency agency, PrintWriter out) {
        List<Customer> customers = agency.getCustomers();
        int total = customers.size();
        int gold = 0;
        int silver = 0;
        int bronze = 0;
        double totalRating = 0;
        int returningCustomers = 0;
        
        for (Customer customer : customers) {
            int points = customer.getLoyaltyPoints();
            if (points >= 100) gold++;
            else if (points >= 50) silver++;
            else bronze++;
            
            totalRating += customer.getAverageRating();
            
            if (points > 0) returningCustomers++;
        }
        
        double avgRating = total > 0 ? totalRating / total : 0;
        double retentionRate = total > 0 ? ((double) returningCustomers / total) * 100 : 0;
        
        StringBuilder json = new StringBuilder();
        json.append("{")
            .append("\"totalCustomers\":").append(total).append(",")
            .append("\"goldMembers\":").append(gold).append(",")
            .append("\"silverMembers\":").append(silver).append(",")
            .append("\"bronzeMembers\":").append(bronze).append(",")
            .append("\"avgRating\":").append(String.format("%.1f", avgRating)).append(",")
            .append("\"retentionRate\":").append(String.format("%.1f", retentionRate))
            .append("}");
        
        out.print(json.toString());
    }
    
    private void generateUtilizationReport(RentalAgency agency, PrintWriter out) {
        List<Vehicle> vehicles = agency.getFleet();
        Map<String, Integer> typeCounts = new HashMap<>();
        Map<String, Integer> typeRented = new HashMap<>();
        
        int totalDays = 0;
        int totalRentals = 0;
        
        for (Vehicle vehicle : vehicles) {
            String type = getVehicleType(vehicle);
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
            
            if (!vehicle.isAvailable()) {
                typeRented.put(type, typeRented.getOrDefault(type, 0) + 1);
            }
        }
        
        for (Customer customer : agency.getCustomers()) {
            for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                totalDays += entry.getValue();
                totalRentals++;
            }
        }
        
        double avgRentalDuration = totalRentals > 0 ? (double) totalDays / totalRentals : 0;
        double utilization = vehicles.size() > 0 ? ((double) typeRented.values().stream().mapToInt(Integer::intValue).sum() / vehicles.size()) * 100 : 0;
        
        String peakDemandType = getMostRentedVehicleType(agency);
        
        StringBuilder json = new StringBuilder();
        json.append("{")
            .append("\"utilization\":").append(String.format("%.1f", utilization)).append(",")
            .append("\"avgRentalDuration\":").append(String.format("%.1f", avgRentalDuration)).append(",")
            .append("\"totalRentalDays\":").append(totalDays).append(",")
            .append("\"peakDemandType\":\"").append(peakDemandType).append("\",")
            .append("\"totalActiveRentals\":").append(totalRentals)
            .append("}");
        
        out.print(json.toString());
    }
    
    private void generateDashboardReport(RentalAgency agency, PrintWriter out) {
        List<Vehicle> vehicles = agency.getFleet();
        List<Customer> customers = agency.getCustomers();
        
        int activeRentals = 0;
        double todayRevenue = 0;
        
        for (Customer customer : customers) {
            for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                Vehicle vehicle = entry.getKey();
                Integer days = entry.getValue();
                activeRentals++;
                todayRevenue += vehicle.calculateRentalCost(days);
            }
        }
        
        StringBuilder json = new StringBuilder();
        json.append("{")
            .append("\"vehicleCount\":").append(vehicles.size()).append(",")
            .append("\"customerCount\":").append(customers.size()).append(",")
            .append("\"activeRentals\":").append(activeRentals).append(",")
            .append("\"todayRevenue\":").append(String.format("%.2f", todayRevenue))
            .append("}");
        
        out.print(json.toString());
    }
    
    private String getMostRentedVehicleType(RentalAgency agency) {
        Map<String, Integer> typeCounts = new HashMap<>();
        
        for (Customer customer : agency.getCustomers()) {
            for (Vehicle vehicle : customer.getCurrentRentals().keySet()) {
                String type = getVehicleType(vehicle);
                typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
            }
        }
        
        return typeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
    }
    
    private String getVehicleType(Vehicle vehicle) {
        if (vehicle instanceof Car) return "Car";
        if (vehicle instanceof Motorcycle) return "Motorcycle";
        if (vehicle instanceof Truck) return "Truck";
        return "Unknown";
    }
}