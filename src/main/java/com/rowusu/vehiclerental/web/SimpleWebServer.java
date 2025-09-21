package com.rowusu.vehiclerental.web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;
import com.rowusu.vehiclerental.model.*;
import com.rowusu.vehiclerental.customers.Customer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple HTTP server for the vehicle rental web application
 * Uses Java's built-in HttpServer (no external dependencies required)
 */
public class SimpleWebServer {
    
    private static RentalAgency agency = new RentalAgency();
    private static final int PORT = 8080;
    
    public static void main(String[] args) throws IOException {
        // Initialize with sample data
        initializeSampleData();
        
        // Create HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Set up route handlers
        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/vehicles", new VehicleHandler());
        server.createContext("/api/customers", new CustomerHandler());
        server.createContext("/api/rentals", new RentalHandler());
        server.createContext("/api/reports", new ReportsHandler());
        
        // Start server
        server.setExecutor(null);
        server.start();
        
        System.out.println("🚀 Advanced Vehicle Rental Management System Started!");
        System.out.println("📱 Access the application at: http://localhost:" + PORT);
        System.out.println("⚡ Server running on port " + PORT);
        System.out.println("🛑 Press Ctrl+C to stop the server");
    }
    
    private static void initializeSampleData() {
        try {
            // Add sample vehicles
            Vehicle car1 = new Car("CAR001", "Toyota Camry", 45.0, true, false, false);
            Vehicle car2 = new Car("CAR002", "Honda Civic", 40.0, false, true, false);
            Vehicle moto1 = new Motorcycle("MOTO001", "Harley Davidson", 75.0, true, false);
            Vehicle truck1 = new Truck("TRUCK001", "Ford F-150", 85.0, false, false);
            
            agency.addVehicleToFleet(car1);
            agency.addVehicleToFleet(car2);
            agency.addVehicleToFleet(moto1);
            agency.addVehicleToFleet(truck1);
            
            // Add sample customers
            Customer customer1 = new Customer("John Smith", "CUST001");
            Customer customer2 = new Customer("Sarah Johnson", "CUST002");
            Customer customer3 = new Customer("Mike Davis", "CUST003");
            
            customer1.addLoyaltyPoints(120);
            customer2.addLoyaltyPoints(85);
            customer3.addLoyaltyPoints(45);
            
            agency.addCustomer(customer1);
            agency.addCustomer(customer2);
            agency.addCustomer(customer3);
            
            System.out.println("✅ Sample data initialized successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error initializing sample data: " + e.getMessage());
        }
    }
    
    // Static file handler for serving HTML, CSS, JS files
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) {
                path = "/index.jsp";
            }
            
            // Map file paths
            String filePath = "src/main/webapp" + path;
            File file = new File(filePath);
            
            if (file.exists() && file.isFile()) {
                // Determine content type
                String contentType = getContentType(path);
                
                // Read file content
                byte[] content = Files.readAllBytes(file.toPath());
                
                // Send response
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, content.length);
                OutputStream os = exchange.getResponseBody();
                os.write(content);
                os.close();
            } else {
                // File not found
                String response = "404 - File Not Found: " + path;
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
        
        private String getContentType(String path) {
            if (path.endsWith(".html") || path.endsWith(".jsp")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            return "text/plain";
        }
    }
    
    // Vehicle API handler
    static class VehicleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            
            String response = "";
            int statusCode = 200;
            
            try {
                if ("GET".equals(method)) {
                    String action = params.get("action");
                    if ("list".equals(action)) {
                        response = getVehicleList();
                    } else if ("available".equals(action)) {
                        response = getAvailableVehicles();
                    } else {
                        response = "{\"error\":\"Invalid action\"}";
                        statusCode = 400;
                    }
                } else if ("POST".equals(method)) {
                    // Handle POST requests (add vehicle, rate vehicle)
                    String body = readRequestBody(exchange);
                    Map<String, String> postParams = parseFormData(body);
                    
                    String action = postParams.get("action");
                    if ("add".equals(action)) {
                        response = addVehicle(postParams);
                    } else if ("rate".equals(action)) {
                        response = rateVehicle(postParams);
                    } else {
                        response = "{\"error\":\"Invalid action\"}";
                        statusCode = 400;
                    }
                } else if ("DELETE".equals(method)) {
                    response = removeVehicle(params.get("id"));
                }
            } catch (Exception e) {
                response = "{\"error\":\"" + e.getMessage() + "\"}";
                statusCode = 500;
            }
            
            // Send response
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        private String getVehicleList() {
            StringBuilder json = new StringBuilder();
            json.append("{\"vehicles\":[");
            
            boolean first = true;
            for (Vehicle v : agency.getFleet()) {
                if (!first) json.append(",");
                first = false;
                
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
            return json.toString();
        }
        
        private String getAvailableVehicles() {
            StringBuilder json = new StringBuilder();
            json.append("{\"vehicles\":[");
            
            boolean first = true;
            for (Vehicle v : agency.getFleet()) {
                if (v.isAvailable()) {
                    if (!first) json.append(",");
                    first = false;
                    
                    json.append("{")
                        .append("\"id\":\"").append(v.getVehicleId()).append("\",")
                        .append("\"type\":\"").append(getVehicleType(v)).append("\",")
                        .append("\"model\":\"").append(v.getModel()).append("\",")
                        .append("\"rate\":").append(v.getBaseRentalRate())
                        .append("}");
                }
            }
            
            json.append("]}");
            return json.toString();
        }
        
        private String addVehicle(Map<String, String> params) {
            try {
                String vehicleId = params.get("id");
                String type = params.get("type");
                String model = params.get("model");
                double rate = Double.parseDouble(params.get("rate"));
                
                Vehicle vehicle = createVehicle(type, vehicleId, model, rate);
                agency.addVehicleToFleet(vehicle);
                
                return "{\"success\":true,\"message\":\"Vehicle added successfully\"}";
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
        private String rateVehicle(Map<String, String> params) {
            try {
                String vehicleId = params.get("id");
                int rating = Integer.parseInt(params.get("rating"));
                
                Vehicle vehicle = findVehicle(vehicleId);
                if (vehicle != null) {
                    agency.rateVehicle(vehicle, rating);
                    return "{\"success\":true,\"message\":\"Vehicle rated successfully\"}";
                } else {
                    return "{\"error\":\"Vehicle not found\"}";
                }
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
        private String removeVehicle(String vehicleId) {
            try {
                Vehicle vehicle = findVehicle(vehicleId);
                if (vehicle != null) {
                    agency.removeVehicleFromFleet(vehicle);
                    return "{\"success\":true,\"message\":\"Vehicle removed successfully\"}";
                } else {
                    return "{\"error\":\"Vehicle not found\"}";
                }
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
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
        
        private Vehicle findVehicle(String vehicleId) {
            for (Vehicle vehicle : agency.getFleet()) {
                if (vehicle.getVehicleId().equals(vehicleId)) {
                    return vehicle;
                }
            }
            return null;
        }
        
        private String getVehicleType(Vehicle vehicle) {
            if (vehicle instanceof Car) return "Car";
            if (vehicle instanceof Motorcycle) return "Motorcycle";
            if (vehicle instanceof Truck) return "Truck";
            return "Unknown";
        }
    }
    
    // Customer API handler
    static class CustomerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            
            String response = "";
            int statusCode = 200;
            
            try {
                if ("GET".equals(method)) {
                    String action = params.get("action");
                    if ("list".equals(action)) {
                        response = getCustomerList();
                    } else {
                        response = "{\"error\":\"Invalid action\"}";
                        statusCode = 400;
                    }
                } else if ("POST".equals(method)) {
                    String body = readRequestBody(exchange);
                    Map<String, String> postParams = parseFormData(body);
                    
                    String action = postParams.get("action");
                    if ("add".equals(action)) {
                        response = addCustomer(postParams);
                    } else if ("rate".equals(action)) {
                        response = rateCustomer(postParams);
                    } else {
                        response = "{\"error\":\"Invalid action\"}";
                        statusCode = 400;
                    }
                } else if ("DELETE".equals(method)) {
                    response = removeCustomer(params.get("id"));
                }
            } catch (Exception e) {
                response = "{\"error\":\"" + e.getMessage() + "\"}";
                statusCode = 500;
            }
            
            // Send response
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        private String getCustomerList() {
            StringBuilder json = new StringBuilder();
            json.append("{\"customers\":[");
            
            boolean first = true;
            for (Customer c : agency.getCustomers()) {
                if (!first) json.append(",");
                first = false;
                
                json.append("{")
                    .append("\"id\":\"").append(c.getCustomerId()).append("\",")
                    .append("\"name\":\"").append(c.getName()).append("\",")
                    .append("\"email\":\"").append("").append("\",")
                    .append("\"phone\":\"").append("").append("\",")
                    .append("\"loyaltyPoints\":").append(c.getLoyaltyPoints()).append(",")
                    .append("\"rating\":").append(c.getAverageRating())
                    .append("}");
            }
            
            json.append("]}");
            return json.toString();
        }
        
        private String addCustomer(Map<String, String> params) {
            try {
                String customerId = params.get("id");
                String name = params.get("name");
                
                Customer customer = new Customer(name, customerId);
                agency.addCustomer(customer);
                
                return "{\"success\":true,\"message\":\"Customer added successfully\"}";
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
        private String rateCustomer(Map<String, String> params) {
            try {
                String customerId = params.get("id");
                int rating = Integer.parseInt(params.get("rating"));
                
                Customer customer = findCustomer(customerId);
                if (customer != null) {
                    agency.rateCustomer(customer, rating);
                    return "{\"success\":true,\"message\":\"Customer rated successfully\"}";
                } else {
                    return "{\"error\":\"Customer not found\"}";
                }
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
        private String removeCustomer(String customerId) {
            try {
                Customer customer = findCustomer(customerId);
                if (customer != null) {
                    agency.getCustomers().remove(customer);
                    return "{\"success\":true,\"message\":\"Customer removed successfully\"}";
                } else {
                    return "{\"error\":\"Customer not found\"}";
                }
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
        private Customer findCustomer(String customerId) {
            for (Customer customer : agency.getCustomers()) {
                if (customer.getCustomerId().equals(customerId)) {
                    return customer;
                }
            }
            return null;
        }
    }
    
    // Rental API handler
    static class RentalHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            
            String response = "";
            int statusCode = 200;
            
            try {
                if ("GET".equals(method)) {
                    String action = params.get("action");
                    if ("list".equals(action)) {
                        response = getRentalList();
                    } else if ("stats".equals(action)) {
                        response = getRentalStats();
                    } else {
                        response = "{\"error\":\"Invalid action\"}";
                        statusCode = 400;
                    }
                } else if ("POST".equals(method)) {
                    String body = readRequestBody(exchange);
                    Map<String, String> postParams = parseFormData(body);
                    
                    String action = postParams.get("action");
                    if ("process".equals(action)) {
                        response = processRental(postParams);
                    } else if ("return".equals(action)) {
                        response = returnVehicle(postParams);
                    } else {
                        response = "{\"error\":\"Invalid action\"}";
                        statusCode = 400;
                    }
                }
            } catch (Exception e) {
                response = "{\"error\":\"" + e.getMessage() + "\"}";
                statusCode = 500;
            }
            
            // Send response
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        private String getRentalList() {
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
            return json.toString();
        }
        
        private String getRentalStats() {
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
            
            return json.toString();
        }
        
        private String processRental(Map<String, String> params) {
            try {
                String vehicleId = params.get("vehicle");
                String customerId = params.get("customer");
                int days = Integer.parseInt(params.get("days"));
                
                Vehicle vehicle = findVehicle(vehicleId);
                Customer customer = findCustomer(customerId);
                
                if (vehicle == null) {
                    return "{\"error\":\"Vehicle not found\"}";
                }
                
                if (customer == null) {
                    return "{\"error\":\"Customer not found\"}";
                }
                
                agency.rentVehicle(vehicle, customer, days);
                String rentalId = "RENT" + vehicleId;
                return "{\"success\":true,\"message\":\"Rental processed successfully\",\"rentalId\":\"" + rentalId + "\"}";
                
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
        private String returnVehicle(Map<String, String> params) {
            try {
                String vehicleId = params.get("vehicleId");
                Vehicle vehicle = findVehicle(vehicleId);
                
                if (vehicle != null) {
                    agency.processReturn(vehicle);
                    return "{\"success\":true,\"message\":\"Vehicle returned successfully\"}";
                } else {
                    return "{\"error\":\"Vehicle not found\"}";
                }
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage() + "\"}";
            }
        }
        
        private Vehicle findVehicle(String vehicleId) {
            for (Vehicle vehicle : agency.getFleet()) {
                if (vehicle.getVehicleId().equals(vehicleId)) {
                    return vehicle;
                }
            }
            return null;
        }
        
        private Customer findCustomer(String customerId) {
            for (Customer customer : agency.getCustomers()) {
                if (customer.getCustomerId().equals(customerId)) {
                    return customer;
                }
            }
            return null;
        }
    }
    
    // Reports API handler
    static class ReportsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(query);
            String reportType = params.get("type");
            
            String response = "";
            int statusCode = 200;
            
            try {
                if ("dashboard".equals(reportType)) {
                    response = getDashboardReport();
                } else if ("fleet".equals(reportType)) {
                    response = getFleetReport();
                } else if ("revenue".equals(reportType)) {
                    response = getRevenueReport();
                } else if ("customer".equals(reportType)) {
                    response = getCustomerReport();
                } else if ("utilization".equals(reportType)) {
                    response = getUtilizationReport();
                } else {
                    response = "{\"error\":\"Invalid report type\"}";
                    statusCode = 400;
                }
            } catch (Exception e) {
                response = "{\"error\":\"" + e.getMessage() + "\"}";
                statusCode = 500;
            }
            
            // Send response
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        private String getDashboardReport() {
            int vehicleCount = agency.getFleet().size();
            int customerCount = agency.getCustomers().size();
            
            int activeRentals = 0;
            double todayRevenue = 0;
            
            for (Customer customer : agency.getCustomers()) {
                for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                    Vehicle vehicle = entry.getKey();
                    Integer days = entry.getValue();
                    activeRentals++;
                    todayRevenue += vehicle.calculateRentalCost(days);
                }
            }
            
            return String.format("{\"vehicleCount\":%d,\"customerCount\":%d,\"activeRentals\":%d,\"todayRevenue\":%.2f}",
                vehicleCount, customerCount, activeRentals, todayRevenue);
        }
        
        private String getFleetReport() {
            int total = agency.getFleet().size();
            int available = 0;
            int rented = 0;
            double totalRating = 0;
            
            for (Vehicle vehicle : agency.getFleet()) {
                if (vehicle.isAvailable()) {
                    available++;
                } else {
                    rented++;
                }
                totalRating += vehicle.getAverageRating();
            }
            
            double avgRating = total > 0 ? totalRating / total : 0;
            double utilization = total > 0 ? ((double) rented / total) * 100 : 0;
            
            return String.format("{\"totalVehicles\":%d,\"available\":%d,\"rented\":%d,\"maintenance\":0,\"utilization\":\"%.1f\",\"avgRating\":\"%.1f\"}",
                total, available, rented, utilization, avgRating);
        }
        
        private String getRevenueReport() {
            double totalRevenue = 0;
            int totalRentals = 0;
            
            for (Customer customer : agency.getCustomers()) {
                for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                    Vehicle vehicle = entry.getKey();
                    Integer days = entry.getValue();
                    totalRevenue += vehicle.calculateRentalCost(days);
                    totalRentals++;
                }
            }
            
            double avgPerRental = totalRentals > 0 ? totalRevenue / totalRentals : 0;
            
            return String.format("{\"totalRevenue\":\"%.2f\",\"todayRevenue\":\"%.2f\",\"totalRentals\":%d,\"activeRentals\":%d,\"avgPerRental\":\"%.2f\"}",
                totalRevenue, totalRevenue, totalRentals, totalRentals, avgPerRental);
        }
        
        private String getCustomerReport() {
            int total = agency.getCustomers().size();
            int gold = 0;
            int silver = 0;
            int bronze = 0;
            double totalRating = 0;
            
            for (Customer customer : agency.getCustomers()) {
                int points = customer.getLoyaltyPoints();
                if (points >= 100) gold++;
                else if (points >= 50) silver++;
                else bronze++;
                
                totalRating += customer.getAverageRating();
            }
            
            double avgRating = total > 0 ? totalRating / total : 0;
            
            return String.format("{\"totalCustomers\":%d,\"goldMembers\":%d,\"silverMembers\":%d,\"bronzeMembers\":%d,\"avgRating\":\"%.1f\",\"retentionRate\":\"85.0\"}",
                total, gold, silver, bronze, avgRating);
        }
        
        private String getUtilizationReport() {
            int totalDays = 0;
            int totalRentals = 0;
            
            for (Customer customer : agency.getCustomers()) {
                for (Map.Entry<Vehicle, Integer> entry : customer.getCurrentRentals().entrySet()) {
                    totalDays += entry.getValue();
                    totalRentals++;
                }
            }
            
            double avgDuration = totalRentals > 0 ? (double) totalDays / totalRentals : 0;
            double utilization = agency.getFleet().size() > 0 ? ((double) totalRentals / agency.getFleet().size()) * 100 : 0;
            
            return String.format("{\"utilization\":\"%.1f\",\"avgRentalDuration\":\"%.1f\",\"totalRentalDays\":%d,\"peakDemandType\":\"Car\",\"totalActiveRentals\":%d}",
                utilization, avgDuration, totalDays, totalRentals);
        }
    }
    
    // Utility methods
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }
    
    private static Map<String, String> parseFormData(String body) {
        Map<String, String> params = new HashMap<>();
        if (body != null) {
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        params.put(keyValue[0], java.net.URLDecoder.decode(keyValue[1], "UTF-8"));
                    } catch (Exception e) {
                        params.put(keyValue[0], keyValue[1]);
                    }
                }
            }
        }
        return params;
    }
    
    private static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toString("UTF-8");
    }
}