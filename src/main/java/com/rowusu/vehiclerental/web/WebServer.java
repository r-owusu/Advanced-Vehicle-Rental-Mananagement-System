package com.rowusu.vehiclerental.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.rowusu.vehiclerental.servlet.*;

/**
 * Embedded Jetty server for testing the web application
 */
public class WebServer {
    
    public static void main(String[] args) throws Exception {
        // Create Jetty server on port 8080
        Server server = new Server(8080);
        
        // Create servlet context handler
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        
        // Add servlets
        context.addServlet(new ServletHolder(new VehicleServlet()), "/vehicles");
        context.addServlet(new ServletHolder(new CustomerServlet()), "/customers");
        context.addServlet(new ServletHolder(new RentalServlet()), "/rentals");
        context.addServlet(new ServletHolder(new ReportsServlet()), "/reports");
        
        // Set resource base for static files
        context.setResourceBase("src/main/webapp");
        
        server.setHandler(context);
        
        try {
            server.start();
            System.out.println("🚀 Advanced Vehicle Rental Management System Web Server Started!");
            System.out.println("📱 Access the application at: http://localhost:8080");
            System.out.println("⚡ Server running on port 8080");
            System.out.println("🛑 Press Ctrl+C to stop the server");
            
            server.join();
        } catch (Exception e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}