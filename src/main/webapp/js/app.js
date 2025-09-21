// Advanced Vehicle Rental Management System - JavaScript Functions

// Global variables
let vehicles = [];
let customers = [];
let rentals = [];
let rentalIdCounter = 1;

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    showTab('vehicles');
    loadDataFromServer();
});

// Initialize application
function initializeApp() {
    // Set up event listeners
    setupEventListeners();
    
    // Update all displays
    updateDashboard();
    updateVehicleTable();
    updateCustomerTable();
    updateRentalTable();
    updateDropdowns();
}

// Load data from server instead of sample data
function loadDataFromServer() {
    // Load vehicles
    fetch('/api/vehicles?action=list')
        .then(response => response.json())
        .then(data => {
            if (data.vehicles) {
                vehicles = data.vehicles;
                updateVehicleTable();
                updateDropdowns();
            }
        })
        .catch(error => {
            console.error('Error loading vehicles:', error);
            loadSampleData(); // Fallback to sample data
        });
    
    // Load customers
    fetch('/api/customers?action=list')
        .then(response => response.json())
        .then(data => {
            if (data.customers) {
                customers = data.customers;
                updateCustomerTable();
                updateDropdowns();
            }
        })
        .catch(error => {
            console.error('Error loading customers:', error);
            loadSampleData(); // Fallback to sample data
        });
    
    // Load rentals
    fetch('/api/rentals?action=list')
        .then(response => response.json())
        .then(data => {
            if (data.rentals) {
                rentals = data.rentals;
                updateRentalTable();
            }
        })
        .catch(error => {
            console.error('Error loading rentals:', error);
        });
    
    // Update dashboard
    updateDashboardFromServer();
}

// Load sample data for demonstration
function loadSampleData() {
    if (vehicles.length === 0) {
        vehicles = [
            { id: 'CAR001', type: 'Car', model: 'Toyota Camry', rate: 45.00, status: 'Available', rating: 4.5 },
            { id: 'CAR002', type: 'Car', model: 'Honda Civic', rate: 40.00, status: 'Available', rating: 4.2 },
            { id: 'MOTO001', type: 'Motorcycle', model: 'Harley Davidson', rate: 75.00, status: 'Available', rating: 4.8 },
            { id: 'TRUCK001', type: 'Truck', model: 'Ford F-150', rate: 85.00, status: 'Available', rating: 4.3 }
        ];
    }
    
    if (customers.length === 0) {
        customers = [
            { id: 'CUST001', name: 'John Smith', email: 'john@example.com', phone: '(555) 123-4567', loyaltyPoints: 120, rating: 4.5 },
            { id: 'CUST002', name: 'Sarah Johnson', email: 'sarah@example.com', phone: '(555) 987-6543', loyaltyPoints: 85, rating: 4.8 },
            { id: 'CUST003', name: 'Mike Davis', email: 'mike@example.com', phone: '(555) 456-7890', loyaltyPoints: 45, rating: 4.2 }
        ];
    }
    
    updateAllDisplays();
}

// Setup event listeners
function setupEventListeners() {
    // Rental cost calculator
    document.getElementById('rental-vehicle').addEventListener('change', calculateRentalCost);
    document.getElementById('rental-days').addEventListener('input', calculateRentalCost);
}

// Tab navigation
function showTab(tabName) {
    // Hide all tabs
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.classList.add('hidden'));
    
    // Remove active class from nav tabs
    const navTabs = document.querySelectorAll('.nav-tab');
    navTabs.forEach(tab => tab.classList.remove('active'));
    
    // Show selected tab
    document.getElementById(tabName + '-tab').classList.remove('hidden');
    
    // Add active class to nav tab
    event.target.classList.add('active');
}

// Vehicle Management Functions
function addVehicle(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    
    // Send to server
    fetch('/api/vehicles?action=add', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showAlert(data.message, 'success');
            event.target.reset();
            loadDataFromServer(); // Refresh data
        } else {
            showAlert(data.error || 'Failed to add vehicle', 'error');
        }
    })
    .catch(error => {
        console.error('Error adding vehicle:', error);
        showAlert('Error adding vehicle', 'error');
    });
}

function removeVehicle(vehicleId) {
    if (confirm('Are you sure you want to remove this vehicle?')) {
        fetch(`/api/vehicles?action=delete&id=${vehicleId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showAlert(data.message, 'success');
                loadDataFromServer(); // Refresh data
            } else {
                showAlert(data.error || 'Failed to remove vehicle', 'error');
            }
        })
        .catch(error => {
            console.error('Error removing vehicle:', error);
            showAlert('Error removing vehicle', 'error');
        });
    }
}

function rateVehicle(vehicleId) {
    const rating = prompt('Rate this vehicle (1-5 stars):');
    if (rating && rating >= 1 && rating <= 5) {
        const formData = new FormData();
        formData.append('action', 'rate');
        formData.append('id', vehicleId);
        formData.append('rating', rating);
        
        fetch('/api/vehicles', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showAlert(data.message, 'success');
                loadDataFromServer(); // Refresh data
            } else {
                showAlert(data.error || 'Failed to rate vehicle', 'error');
            }
        })
        .catch(error => {
            console.error('Error rating vehicle:', error);
            showAlert('Error rating vehicle', 'error');
        });
    }
}

// Customer Management Functions
function addCustomer(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    
    // Send to server
    fetch('/api/customers?action=add', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showAlert(data.message, 'success');
            event.target.reset();
            loadDataFromServer(); // Refresh data
        } else {
            showAlert(data.error || 'Failed to add customer', 'error');
        }
    })
    .catch(error => {
        console.error('Error adding customer:', error);
        showAlert('Error adding customer', 'error');
    });
}

function removeCustomer(customerId) {
    if (confirm('Are you sure you want to remove this customer?')) {
        fetch(`/api/customers?action=delete&id=${customerId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showAlert(data.message, 'success');
                loadDataFromServer(); // Refresh data
            } else {
                showAlert(data.error || 'Failed to remove customer', 'error');
            }
        })
        .catch(error => {
            console.error('Error removing customer:', error);
            showAlert('Error removing customer', 'error');
        });
    }
}

function rateCustomer(customerId) {
    const rating = prompt('Rate this customer (1-5 stars):');
    if (rating && rating >= 1 && rating <= 5) {
        const formData = new FormData();
        formData.append('action', 'rate');
        formData.append('id', customerId);
        formData.append('rating', rating);
        
        fetch('/api/customers', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showAlert(data.message, 'success');
                loadDataFromServer(); // Refresh data
            } else {
                showAlert(data.error || 'Failed to rate customer', 'error');
            }
        })
        .catch(error => {
            console.error('Error rating customer:', error);
            showAlert('Error rating customer', 'error');
        });
    }
}

// Rental Management Functions
function processRental(event) {
    event.preventDefault();
    
    const formData = new FormData(event.target);
    
    // Send to server
    fetch('/api/rentals?action=process', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showAlert(`${data.message} Rental ID: ${data.rentalId}`, 'success');
            event.target.reset();
            document.getElementById('rental-cost').textContent = 'Select vehicle and duration to see cost';
            loadDataFromServer(); // Refresh data
        } else {
            showAlert(data.error || 'Failed to process rental', 'error');
        }
    })
    .catch(error => {
        console.error('Error processing rental:', error);
        showAlert('Error processing rental', 'error');
    });
}

function returnVehicle(rentalId) {
    if (confirm('Process vehicle return?')) {
        // Extract vehicle ID from rental ID (assuming format RENT{vehicleId})
        const vehicleId = rentalId.replace('RENT', '');
        
        const formData = new FormData();
        formData.append('action', 'return');
        formData.append('vehicleId', vehicleId);
        
        fetch('/api/rentals', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                showAlert(data.message, 'success');
                loadDataFromServer(); // Refresh data
            } else {
                showAlert(data.error || 'Failed to return vehicle', 'error');
            }
        })
        .catch(error => {
            console.error('Error returning vehicle:', error);
            showAlert('Error returning vehicle', 'error');
        });
    }
}

function calculateRentalCost() {
    const vehicleId = document.getElementById('rental-vehicle').value;
    const days = parseInt(document.getElementById('rental-days').value) || 0;
    
    if (vehicleId && days > 0) {
        const vehicle = vehicles.find(v => v.id === vehicleId);
        if (vehicle) {
            const totalCost = vehicle.rate * days;
            document.getElementById('rental-cost').textContent = `$${totalCost.toFixed(2)} (${days} days × $${vehicle.rate}/day)`;
        }
    } else {
        document.getElementById('rental-cost').textContent = 'Select vehicle and duration to see cost';
    }
}

// Display Update Functions
function updateAllDisplays() {
    updateDashboard();
    updateVehicleTable();
    updateCustomerTable();
    updateRentalTable();
    updateDropdowns();
}

function updateDashboard() {
    const activeRentals = rentals.filter(r => r.status === 'Active').length;
    const todayRevenue = rentals
        .filter(r => r.startDate === new Date().toISOString().split('T')[0])
        .reduce((sum, r) => sum + r.totalCost, 0);
    
    document.getElementById('vehicle-count').textContent = vehicles.length;
    document.getElementById('customer-count').textContent = customers.length;
    document.getElementById('active-rentals').textContent = activeRentals;
    document.getElementById('revenue').textContent = `$${todayRevenue.toFixed(2)}`;
}

function updateVehicleTable() {
    const tbody = document.getElementById('vehicles-tbody');
    tbody.innerHTML = '';
    
    vehicles.forEach(vehicle => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${vehicle.id}</td>
            <td>${getVehicleIcon(vehicle.type)} ${vehicle.type}</td>
            <td>${vehicle.model}</td>
            <td>$${vehicle.rate.toFixed(2)}</td>
            <td><span class="status ${vehicle.status.toLowerCase()}">${vehicle.status}</span></td>
            <td>${vehicle.rating.toFixed(1)} ⭐</td>
            <td>
                <button onclick="rateVehicle('${vehicle.id}')" class="btn btn-sm btn-warning">Rate</button>
                <button onclick="removeVehicle('${vehicle.id}')" class="btn btn-sm btn-danger">Remove</button>
            </td>
        `;
        tbody.appendChild(row);
    });
    
    // Update fleet stats
    const available = vehicles.filter(v => v.status === 'Available').length;
    const rented = vehicles.filter(v => v.status === 'Rented').length;
    const avgRating = vehicles.length > 0 ? vehicles.reduce((sum, v) => sum + v.rating, 0) / vehicles.length : 0;
    
    document.getElementById('fleet-total').textContent = vehicles.length;
    document.getElementById('fleet-available').textContent = available;
    document.getElementById('fleet-rented').textContent = rented;
    document.getElementById('fleet-rating').textContent = avgRating.toFixed(1);
}

function updateCustomerTable() {
    const tbody = document.getElementById('customers-tbody');
    tbody.innerHTML = '';
    
    customers.forEach(customer => {
        const row = document.createElement('tr');
        const loyaltyStatus = getLoyaltyStatus(customer.loyaltyPoints);
        
        row.innerHTML = `
            <td>${customer.id}</td>
            <td>${customer.name}</td>
            <td>${customer.email}</td>
            <td>${customer.phone}</td>
            <td><span class="loyalty ${loyaltyStatus.toLowerCase()}">${loyaltyStatus}</span></td>
            <td>${customer.loyaltyPoints}</td>
            <td>${customer.rating.toFixed(1)} ⭐</td>
            <td>
                <button onclick="rateCustomer('${customer.id}')" class="btn btn-sm btn-warning">Rate</button>
                <button onclick="removeCustomer('${customer.id}')" class="btn btn-sm btn-danger">Remove</button>
            </td>
        `;
        tbody.appendChild(row);
    });
    
    // Update customer stats
    const gold = customers.filter(c => c.loyaltyPoints >= 100).length;
    const silver = customers.filter(c => c.loyaltyPoints >= 50 && c.loyaltyPoints < 100).length;
    const bronze = customers.filter(c => c.loyaltyPoints < 50).length;
    
    document.getElementById('customer-total').textContent = customers.length;
    document.getElementById('gold-members').textContent = gold;
    document.getElementById('silver-members').textContent = silver;
    document.getElementById('bronze-members').textContent = bronze;
}

function updateRentalTable() {
    const tbody = document.getElementById('rentals-tbody');
    tbody.innerHTML = '';
    
    rentals.forEach(rental => {
        const vehicle = vehicles.find(v => v.id === rental.vehicleId);
        const customer = customers.find(c => c.id === rental.customerId);
        
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${rental.id}</td>
            <td>${vehicle ? vehicle.model : 'Unknown'}</td>
            <td>${customer ? customer.name : 'Unknown'}</td>
            <td>${rental.startDate}</td>
            <td>${rental.days} days</td>
            <td>$${rental.totalCost.toFixed(2)}</td>
            <td><span class="status ${rental.status.toLowerCase()}">${rental.status}</span></td>
            <td>
                ${rental.status === 'Active' ? 
                    `<button onclick="returnVehicle('${rental.id}')" class="btn btn-sm btn-success">Return</button>` :
                    '<span class="text-muted">Completed</span>'
                }
            </td>
        `;
        tbody.appendChild(row);
    });
    
    // Update rental stats
    const activeRentalsCount = rentals.filter(r => r.status === 'Active').length;
    const todayRentals = rentals.filter(r => r.startDate === new Date().toISOString().split('T')[0]).length;
    const todayRevenue = rentals
        .filter(r => r.startDate === new Date().toISOString().split('T')[0])
        .reduce((sum, r) => sum + r.totalCost, 0);
    const avgDays = rentals.length > 0 ? rentals.reduce((sum, r) => sum + r.days, 0) / rentals.length : 0;
    
    document.getElementById('active-rentals-count').textContent = activeRentalsCount;
    document.getElementById('today-rentals').textContent = todayRentals;
    document.getElementById('today-revenue').textContent = todayRevenue.toFixed(2);
    document.getElementById('avg-rental-days').textContent = avgDays.toFixed(1);
}

function updateDropdowns() {
    // Update vehicle dropdown
    const vehicleSelect = document.getElementById('rental-vehicle');
    vehicleSelect.innerHTML = '<option value="">Choose Available Vehicle</option>';
    
    vehicles.filter(v => v.status === 'Available').forEach(vehicle => {
        const option = document.createElement('option');
        option.value = vehicle.id;
        option.textContent = `${vehicle.model} - $${vehicle.rate}/day`;
        vehicleSelect.appendChild(option);
    });
    
    // Update customer dropdown
    const customerSelect = document.getElementById('rental-customer');
    customerSelect.innerHTML = '<option value="">Choose Customer</option>';
    
    customers.forEach(customer => {
        const option = document.createElement('option');
        option.value = customer.id;
        option.textContent = `${customer.name} (${customer.id})`;
        customerSelect.appendChild(option);
    });
}

// Utility Functions
function getVehicleIcon(type) {
    switch (type) {
        case 'Car': return '🚗';
        case 'Motorcycle': return '🏍️';
        case 'Truck': return '🚛';
        default: return '🚗';
    }
}

function getLoyaltyStatus(points) {
    if (points >= 100) return 'Gold';
    if (points >= 50) return 'Silver';
    return 'Bronze';
}

function showAlert(message, type) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    alertDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 5px;
        color: white;
        font-weight: bold;
        z-index: 1000;
        animation: slideIn 0.3s ease-out;
    `;
    
    if (type === 'success') {
        alertDiv.style.backgroundColor = '#28a745';
    } else if (type === 'error') {
        alertDiv.style.backgroundColor = '#dc3545';
    }
    
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
        alertDiv.remove();
    }, 3000);
}

// Reports Functions
function refreshReports() {
    fetch('/api/reports?type=fleet')
        .then(response => response.json())
        .then(data => generateFleetReportFromServer(data))
        .catch(error => {
            console.error('Error loading fleet report:', error);
            generateFleetReport(); // Fallback
        });
    
    fetch('/api/reports?type=revenue')
        .then(response => response.json())
        .then(data => generateRevenueReportFromServer(data))
        .catch(error => {
            console.error('Error loading revenue report:', error);
            generateRevenueReport(); // Fallback
        });
    
    fetch('/api/reports?type=customer')
        .then(response => response.json())
        .then(data => generateCustomerReportFromServer(data))
        .catch(error => {
            console.error('Error loading customer report:', error);
            generateCustomerReport(); // Fallback
        });
    
    fetch('/api/reports?type=utilization')
        .then(response => response.json())
        .then(data => generateUtilizationReportFromServer(data))
        .catch(error => {
            console.error('Error loading utilization report:', error);
            generateUtilizationReport(); // Fallback
        });
}

function updateDashboardFromServer() {
    fetch('/api/reports?type=dashboard')
        .then(response => response.json())
        .then(data => {
            document.getElementById('vehicle-count').textContent = data.vehicleCount || 0;
            document.getElementById('customer-count').textContent = data.customerCount || 0;
            document.getElementById('active-rentals').textContent = data.activeRentals || 0;
            document.getElementById('revenue').textContent = `$${data.todayRevenue || '0.00'}`;
        })
        .catch(error => {
            console.error('Error loading dashboard data:', error);
            updateDashboard(); // Fallback
        });
}

function generateFleetReportFromServer(data) {
    const reportDiv = document.getElementById('fleet-report');
    reportDiv.innerHTML = `
        <h5>Fleet Status Overview</h5>
        <div class="report-metric">
            <strong>Total Vehicles:</strong> ${data.totalVehicles || 0}
        </div>
        <div class="report-metric">
            <strong>Available:</strong> ${data.available || 0} (${((data.available/data.totalVehicles)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Rented:</strong> ${data.rented || 0} (${((data.rented/data.totalVehicles)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Under Maintenance:</strong> ${data.maintenance || 0}
        </div>
        <div class="report-metric">
            <strong>Fleet Utilization:</strong> ${data.utilization || 0}%
        </div>
    `;
}

function generateRevenueReportFromServer(data) {
    const reportDiv = document.getElementById('revenue-report');
    reportDiv.innerHTML = `
        <h5>Revenue Analysis</h5>
        <div class="report-metric">
            <strong>Total Revenue:</strong> $${data.totalRevenue || '0.00'}
        </div>
        <div class="report-metric">
            <strong>Today's Revenue:</strong> $${data.todayRevenue || '0.00'}
        </div>
        <div class="report-metric">
            <strong>Total Rentals:</strong> ${data.totalRentals || 0}
        </div>
        <div class="report-metric">
            <strong>Average per Rental:</strong> $${data.avgPerRental || '0.00'}
        </div>
        <div class="report-metric">
            <strong>Active Rentals:</strong> ${data.activeRentals || 0}
        </div>
    `;
}

function generateCustomerReportFromServer(data) {
    const reportDiv = document.getElementById('customer-report');
    reportDiv.innerHTML = `
        <h5>Customer Insights</h5>
        <div class="report-metric">
            <strong>Total Customers:</strong> ${data.totalCustomers || 0}
        </div>
        <div class="report-metric">
            <strong>Gold Members:</strong> ${data.goldMembers || 0} (${((data.goldMembers/data.totalCustomers)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Silver Members:</strong> ${data.silverMembers || 0} (${((data.silverMembers/data.totalCustomers)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Bronze Members:</strong> ${data.bronzeMembers || 0} (${((data.bronzeMembers/data.totalCustomers)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Average Rating:</strong> ${data.avgRating || 0}/5 ⭐
        </div>
    `;
}

function generateUtilizationReportFromServer(data) {
    const reportDiv = document.getElementById('utilization-report');
    reportDiv.innerHTML = `
        <h5>Utilization Metrics</h5>
        <div class="report-metric">
            <strong>Fleet Utilization:</strong> ${data.utilization || 0}%
        </div>
        <div class="report-metric">
            <strong>Average Rental Duration:</strong> ${data.avgRentalDuration || 0} days
        </div>
        <div class="report-metric">
            <strong>Total Rental Days:</strong> ${data.totalRentalDays || 0}
        </div>
        <div class="report-metric">
            <strong>Peak Demand Vehicle:</strong> ${data.peakDemandType || 'None'}
        </div>
        <div class="report-metric">
            <strong>Customer Retention:</strong> ${data.retentionRate || 0}%
        </div>
    `;
}

function generateFleetReport() {
    const reportDiv = document.getElementById('fleet-report');
    const available = vehicles.filter(v => v.status === 'Available').length;
    const rented = vehicles.filter(v => v.status === 'Rented').length;
    const maintenance = vehicles.filter(v => v.status === 'Maintenance').length;
    
    reportDiv.innerHTML = `
        <h5>Fleet Status Overview</h5>
        <div class="report-metric">
            <strong>Total Vehicles:</strong> ${vehicles.length}
        </div>
        <div class="report-metric">
            <strong>Available:</strong> ${available} (${((available/vehicles.length)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Rented:</strong> ${rented} (${((rented/vehicles.length)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Under Maintenance:</strong> ${maintenance}
        </div>
        <div class="report-metric">
            <strong>Fleet Utilization:</strong> ${((rented/vehicles.length)*100).toFixed(1)}%
        </div>
    `;
}

function generateRevenueReport() {
    const reportDiv = document.getElementById('revenue-report');
    const totalRevenue = rentals.reduce((sum, r) => sum + r.totalCost, 0);
    const todayRevenue = rentals
        .filter(r => r.startDate === new Date().toISOString().split('T')[0])
        .reduce((sum, r) => sum + r.totalCost, 0);
    const avgRental = rentals.length > 0 ? totalRevenue / rentals.length : 0;
    
    reportDiv.innerHTML = `
        <h5>Revenue Analysis</h5>
        <div class="report-metric">
            <strong>Total Revenue:</strong> $${totalRevenue.toFixed(2)}
        </div>
        <div class="report-metric">
            <strong>Today's Revenue:</strong> $${todayRevenue.toFixed(2)}
        </div>
        <div class="report-metric">
            <strong>Total Rentals:</strong> ${rentals.length}
        </div>
        <div class="report-metric">
            <strong>Average per Rental:</strong> $${avgRental.toFixed(2)}
        </div>
        <div class="report-metric">
            <strong>Active Rentals:</strong> ${rentals.filter(r => r.status === 'Active').length}
        </div>
    `;
}

function generateCustomerReport() {
    const reportDiv = document.getElementById('customer-report');
    const gold = customers.filter(c => c.loyaltyPoints >= 100).length;
    const silver = customers.filter(c => c.loyaltyPoints >= 50 && c.loyaltyPoints < 100).length;
    const bronze = customers.filter(c => c.loyaltyPoints < 50).length;
    const avgRating = customers.length > 0 ? customers.reduce((sum, c) => sum + c.rating, 0) / customers.length : 0;
    
    reportDiv.innerHTML = `
        <h5>Customer Insights</h5>
        <div class="report-metric">
            <strong>Total Customers:</strong> ${customers.length}
        </div>
        <div class="report-metric">
            <strong>Gold Members:</strong> ${gold} (${((gold/customers.length)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Silver Members:</strong> ${silver} (${((silver/customers.length)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Bronze Members:</strong> ${bronze} (${((bronze/customers.length)*100).toFixed(1)}%)
        </div>
        <div class="report-metric">
            <strong>Average Rating:</strong> ${avgRating.toFixed(1)}/5 ⭐
        </div>
    `;
}

function generateUtilizationReport() {
    const reportDiv = document.getElementById('utilization-report');
    const totalDays = rentals.reduce((sum, r) => sum + r.days, 0);
    const avgRentalDuration = rentals.length > 0 ? totalDays / rentals.length : 0;
    const utilization = vehicles.length > 0 ? (rentals.filter(r => r.status === 'Active').length / vehicles.length) * 100 : 0;
    
    reportDiv.innerHTML = `
        <h5>Utilization Metrics</h5>
        <div class="report-metric">
            <strong>Fleet Utilization:</strong> ${utilization.toFixed(1)}%
        </div>
        <div class="report-metric">
            <strong>Average Rental Duration:</strong> ${avgRentalDuration.toFixed(1)} days
        </div>
        <div class="report-metric">
            <strong>Total Rental Days:</strong> ${totalDays}
        </div>
        <div class="report-metric">
            <strong>Peak Demand Vehicle:</strong> ${getMostRentedVehicleType()}
        </div>
        <div class="report-metric">
            <strong>Customer Retention:</strong> ${getReturnCustomerRate()}%
        </div>
    `;
}

function getMostRentedVehicleType() {
    const typeCounts = {};
    rentals.forEach(rental => {
        const vehicle = vehicles.find(v => v.id === rental.vehicleId);
        if (vehicle) {
            typeCounts[vehicle.type] = (typeCounts[vehicle.type] || 0) + 1;
        }
    });
    
    let maxType = 'None';
    let maxCount = 0;
    for (const [type, count] of Object.entries(typeCounts)) {
        if (count > maxCount) {
            maxCount = count;
            maxType = type;
        }
    }
    return maxType;
}

function getReturnCustomerRate() {
    if (customers.length === 0) return 0;
    const returningCustomers = customers.filter(c => c.loyaltyPoints > 0).length;
    return ((returningCustomers / customers.length) * 100).toFixed(1);
}

// Local Storage Functions
function saveToStorage() {
    localStorage.setItem('vehicles', JSON.stringify(vehicles));
    localStorage.setItem('customers', JSON.stringify(customers));
    localStorage.setItem('rentals', JSON.stringify(rentals));
    localStorage.setItem('rentalIdCounter', rentalIdCounter.toString());
}

function loadFromStorage() {
    const savedVehicles = localStorage.getItem('vehicles');
    const savedCustomers = localStorage.getItem('customers');
    const savedRentals = localStorage.getItem('rentals');
    const savedCounter = localStorage.getItem('rentalIdCounter');
    
    if (savedVehicles) vehicles = JSON.parse(savedVehicles);
    if (savedCustomers) customers = JSON.parse(savedCustomers);
    if (savedRentals) rentals = JSON.parse(savedRentals);
    if (savedCounter) rentalIdCounter = parseInt(savedCounter);
}