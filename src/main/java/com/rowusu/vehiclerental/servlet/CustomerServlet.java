package com.rowusu.vehiclerental.servlet;

import com.rowusu.vehiclerental.customers.Customer;
import com.rowusu.vehiclerental.rentalagency.RentalAgency;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet for handling customer management operations
 */
@WebServlet("/customers")
public class CustomerServlet extends HttpServlet {
    
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
                // Return list of all customers
                List<Customer> customers = agency.getCustomers();
                StringBuilder json = new StringBuilder();
                json.append("{\"customers\":[");
                
                for (int i = 0; i < customers.size(); i++) {
                    Customer c = customers.get(i);
                    if (i > 0) json.append(",");
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
                // Add new customer
                String customerId = request.getParameter("id");
                String name = request.getParameter("name");
                // Note: Email and phone parameters are ignored as Customer model doesn't support them
                
                Customer customer = new Customer(name, customerId);
                // Note: Email and phone not supported in current Customer model
                
                agency.addCustomer(customer);
                
                out.print("{\"success\":true,\"message\":\"Customer registered successfully\"}");
                
            } else if ("rate".equals(action)) {
                // Rate a customer
                String customerId = request.getParameter("id");
                double rating = Double.parseDouble(request.getParameter("rating"));
                
                Customer customer = findCustomer(agency, customerId);
                if (customer != null) {
                    agency.rateCustomer(customer, (int)rating);
                    out.print("{\"success\":true,\"message\":\"Customer rated successfully\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\":\"Customer not found\"}");
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
        
        String customerId = request.getParameter("id");
        HttpSession session = request.getSession();
        RentalAgency agency = getRentalAgency(session);
        
        try {
            Customer customer = findCustomer(agency, customerId);
            if (customer != null) {
                agency.getCustomers().remove(customer);
                out.print("{\"success\":true,\"message\":\"Customer removed successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Customer not found\"}");
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
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