<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Advanced Vehicle Rental Management System</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="icon" type="image/x-icon" href="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTguOTIgNi4wMUM4LjMzIDYuMDEgMS4wMSA5LjMzIDEuMDEgMTAuOTFWMTVIMkMzLjEgMTUgNCAxNC4xIDQgMTNINUMzLjkgMTMgMyAxMi4xIDMgMTFWMTAuOTFDMyAxMC4zOCA4LjY5IDcuOTkgMTguOTIgNy45OUM5LjIgNy45OSAzIDEwLjM4IDMgMTAuOTFWMTFDMyAxMi4xIDMuOSAxMyA1IDEzSDE5QzIwLjEgMTMgMjEgMTIuMSAyMSAxMVY5LjA5QzIxIDguMzQgMTYuMzEgNi4wMSA5LjA4IDYuMDFIMTguOTJaIiBmaWxsPSIjMDA3YmZmIi8+PC9zdmc+">
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="header">
            <h1>🚗 Advanced Vehicle Rental Management System</h1>
            <p>Your complete solution for managing vehicle rentals, customers, and business operations</p>
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-number" id="vehicle-count">0</div>
                    <div class="stat-label">Total Vehicles</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number" id="customer-count">0</div>
                    <div class="stat-label">Registered Customers</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number" id="active-rentals">0</div>
                    <div class="stat-label">Active Rentals</div>
                </div>
                <div class="stat-card">
                    <div class="stat-number" id="revenue">$0</div>
                    <div class="stat-label">Today's Revenue</div>
                </div>
            </div>
        </div>

        <!-- Navigation -->
        <div class="nav-tabs">
            <a href="#vehicles" class="nav-tab active" onclick="showTab('vehicles')">🚗 Vehicles</a>
            <a href="#customers" class="nav-tab" onclick="showTab('customers')">👤 Customers</a>
            <a href="#rentals" class="nav-tab" onclick="showTab('rentals')">📋 Rentals</a>
            <a href="#reports" class="nav-tab" onclick="showTab('reports')">📊 Reports</a>
        </div>

        <!-- Vehicles Tab -->
        <div id="vehicles-tab" class="tab-content">
            <div class="card">
                <h2>Vehicle Management</h2>
                <div class="instructions">
                    <h4>📋 Instructions:</h4>
                    <ul>
                        <li>View all vehicles in your fleet in the table below</li>
                        <li>Click "Add New Vehicle" to register cars, motorcycles, or trucks</li>
                        <li>Use action buttons to edit, remove, or rate vehicles</li>
                        <li>Track vehicle status: Available, Rented, or Under Maintenance</li>
                    </ul>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <h3>Add New Vehicle</h3>
                        <form id="vehicle-form" onsubmit="addVehicle(event)">
                            <div class="form-group">
                                <label for="vehicle-type">Vehicle Type:</label>
                                <select id="vehicle-type" name="type" class="form-control" required>
                                    <option value="">Select Type</option>
                                    <option value="Car">🚗 Car</option>
                                    <option value="Motorcycle">🏍️ Motorcycle</option>
                                    <option value="Truck">🚛 Truck</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="vehicle-id">Vehicle ID:</label>
                                <input type="text" id="vehicle-id" name="id" class="form-control" 
                                       placeholder="e.g., CAR001, MOTO001, TRUCK001" required>
                            </div>
                            <div class="form-group">
                                <label for="vehicle-model">Model:</label>
                                <input type="text" id="vehicle-model" name="model" class="form-control" 
                                       placeholder="e.g., Toyota Camry, Honda Civic" required>
                            </div>
                            <div class="form-group">
                                <label for="vehicle-rate">Daily Rate ($):</label>
                                <input type="number" id="vehicle-rate" name="rate" class="form-control" 
                                       placeholder="e.g., 45.00" step="0.01" min="1" required>
                            </div>
                            <button type="submit" class="btn btn-success">Add Vehicle</button>
                        </form>
                    </div>
                    <div class="col-md-6">
                        <h3>Fleet Overview</h3>
                        <div id="fleet-stats">
                            <p><strong>Total Vehicles:</strong> <span id="fleet-total">0</span></p>
                            <p><strong>Available:</strong> <span id="fleet-available">0</span></p>
                            <p><strong>Rented:</strong> <span id="fleet-rented">0</span></p>
                            <p><strong>Average Rating:</strong> <span id="fleet-rating">0.0</span>/5</p>
                        </div>
                    </div>
                </div>

                <h3>Vehicle Fleet</h3>
                <div class="table-container">
                    <table class="table" id="vehicles-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Type</th>
                                <th>Model</th>
                                <th>Rate/Day</th>
                                <th>Status</th>
                                <th>Rating</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="vehicles-tbody">
                            <!-- Vehicle data will be loaded here -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Customers Tab -->
        <div id="customers-tab" class="tab-content hidden">
            <div class="card">
                <h2>Customer Management</h2>
                <div class="instructions">
                    <h4>📋 Instructions:</h4>
                    <ul>
                        <li>Register new customers with their personal information</li>
                        <li>Track customer loyalty status and points</li>
                        <li>View customer rental history and ratings</li>
                        <li>Loyalty tiers: Bronze (0-49 pts), Silver (50-99 pts), Gold (100+ pts)</li>
                    </ul>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <h3>Register New Customer</h3>
                        <form id="customer-form" onsubmit="addCustomer(event)">
                            <div class="form-group">
                                <label for="customer-name">Full Name:</label>
                                <input type="text" id="customer-name" name="name" class="form-control" 
                                       placeholder="e.g., John Smith" required>
                            </div>
                            <div class="form-group">
                                <label for="customer-id">Customer ID:</label>
                                <input type="text" id="customer-id" name="id" class="form-control" 
                                       placeholder="e.g., CUST001, CUST002" required>
                            </div>
                            <div class="form-group">
                                <label for="customer-email">Email:</label>
                                <input type="email" id="customer-email" name="email" class="form-control" 
                                       placeholder="e.g., john@example.com">
                            </div>
                            <div class="form-group">
                                <label for="customer-phone">Phone:</label>
                                <input type="tel" id="customer-phone" name="phone" class="form-control" 
                                       placeholder="e.g., (555) 123-4567">
                            </div>
                            <button type="submit" class="btn btn-success">Register Customer</button>
                        </form>
                    </div>
                    <div class="col-md-6">
                        <h3>Customer Statistics</h3>
                        <div id="customer-stats">
                            <p><strong>Total Customers:</strong> <span id="customer-total">0</span></p>
                            <p><strong>Gold Members:</strong> <span id="gold-members">0</span></p>
                            <p><strong>Silver Members:</strong> <span id="silver-members">0</span></p>
                            <p><strong>Bronze Members:</strong> <span id="bronze-members">0</span></p>
                        </div>
                    </div>
                </div>

                <h3>Customer Directory</h3>
                <div class="table-container">
                    <table class="table" id="customers-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Loyalty Status</th>
                                <th>Points</th>
                                <th>Rating</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="customers-tbody">
                            <!-- Customer data will be loaded here -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Rentals Tab -->
        <div id="rentals-tab" class="tab-content hidden">
            <div class="card">
                <h2>Rental Management</h2>
                <div class="instructions">
                    <h4>📋 Instructions:</h4>
                    <ul>
                        <li>Process new vehicle rentals by selecting available vehicles</li>
                        <li>Choose existing customers or register new ones</li>
                        <li>Set rental duration and review total costs</li>
                        <li>Process returns and generate rental receipts</li>
                    </ul>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <h3>Process New Rental</h3>
                        <form id="rental-form" onsubmit="processRental(event)">
                            <div class="form-group">
                                <label for="rental-vehicle">Select Vehicle:</label>
                                <select id="rental-vehicle" name="vehicle" class="form-control" required>
                                    <option value="">Choose Available Vehicle</option>
                                    <!-- Options will be populated by JavaScript -->
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="rental-customer">Select Customer:</label>
                                <select id="rental-customer" name="customer" class="form-control" required>
                                    <option value="">Choose Customer</option>
                                    <!-- Options will be populated by JavaScript -->
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="rental-days">Rental Duration (Days):</label>
                                <input type="number" id="rental-days" name="days" class="form-control" 
                                       placeholder="e.g., 7" min="1" max="365" required>
                            </div>
                            <div class="form-group">
                                <label>Estimated Total Cost:</label>
                                <div id="rental-cost" class="form-control" style="background: #f8f9fa;">
                                    Select vehicle and duration to see cost
                                </div>
                            </div>
                            <button type="submit" class="btn btn-success">Process Rental</button>
                        </form>
                    </div>
                    <div class="col-md-6">
                        <h3>Rental Statistics</h3>
                        <div id="rental-stats">
                            <p><strong>Active Rentals:</strong> <span id="active-rentals-count">0</span></p>
                            <p><strong>Today's Rentals:</strong> <span id="today-rentals">0</span></p>
                            <p><strong>Revenue Today:</strong> $<span id="today-revenue">0.00</span></p>
                            <p><strong>Average Rental Days:</strong> <span id="avg-rental-days">0</span></p>
                        </div>
                    </div>
                </div>

                <h3>Active Rentals</h3>
                <div class="table-container">
                    <table class="table" id="rentals-table">
                        <thead>
                            <tr>
                                <th>Rental ID</th>
                                <th>Vehicle</th>
                                <th>Customer</th>
                                <th>Start Date</th>
                                <th>Duration</th>
                                <th>Total Cost</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="rentals-tbody">
                            <!-- Rental data will be loaded here -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Reports Tab -->
        <div id="reports-tab" class="tab-content hidden">
            <div class="card">
                <h2>Business Reports & Analytics</h2>
                <div class="instructions">
                    <h4>📋 Instructions:</h4>
                    <ul>
                        <li>View real-time business performance metrics</li>
                        <li>Analyze fleet utilization and revenue trends</li>
                        <li>Monitor customer satisfaction and loyalty programs</li>
                        <li>Export reports for business planning and tax purposes</li>
                    </ul>
                </div>

                <button onclick="refreshReports()" class="btn btn-warning mb-3">🔄 Refresh Reports</button>

                <div class="row">
                    <div class="col-md-6">
                        <h3>Fleet Performance</h3>
                        <div id="fleet-report" class="card p-3">
                            <div class="loading">Loading fleet data...</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <h3>Revenue Analytics</h3>
                        <div id="revenue-report" class="card p-3">
                            <div class="loading">Loading revenue data...</div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6">
                        <h3>Customer Analytics</h3>
                        <div id="customer-report" class="card p-3">
                            <div class="loading">Loading customer data...</div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <h3>Utilization Metrics</h3>
                        <div id="utilization-report" class="card p-3">
                            <div class="loading">Loading utilization data...</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="footer">
            <p>&copy; 2025 Advanced Vehicle Rental Management System. All rights reserved.</p>
            <p>Built with Java, HTML5, CSS3, and JavaScript</p>
        </div>
    </div>

    <!-- Include JavaScript -->
    <script src="js/app.js"></script>
</body>
</html>