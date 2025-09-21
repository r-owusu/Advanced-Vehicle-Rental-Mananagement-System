# 🌐 Advanced Vehicle Rental Management System - Web Version

## 📋 Overview

The Advanced Vehicle Rental Management System has been transformed into a modern web application featuring:

- **🚗 Vehicle Fleet Management** - Add, remove, and rate vehicles (Cars, Motorcycles, Trucks)
- **👤 Customer Management** - Register customers and track loyalty programs
- **📋 Rental Processing** - Process rentals and returns with real-time cost calculation
- **📊 Business Analytics** - Comprehensive reports and dashboard metrics
- **💻 Modern Web Interface** - Responsive design with intuitive navigation

## 🏗️ Architecture

### Backend (Java)
- **Servlets**: Handle HTTP requests and business logic
  - `VehicleServlet` - Vehicle management operations
  - `CustomerServlet` - Customer registration and management
  - `RentalServlet` - Rental processing and returns
  - `ReportsServlet` - Business analytics and reporting
- **Model Classes**: Vehicle, Customer, RentalAgency (reused from desktop version)
- **Embedded Jetty Server**: For development and testing

### Frontend (HTML5/CSS3/JavaScript)
- **Single Page Application**: Dynamic content loading without page refreshes
- **Responsive Design**: Works on desktop, tablet, and mobile devices
- **Real-time Updates**: Live dashboard and statistics
- **Modern UI Components**: Cards, tabs, forms, and interactive elements

## 🚀 Getting Started

### Prerequisites

1. **Java Development Kit (JDK) 11 or higher**
   ```bash
   java -version
   ```

2. **Apache Maven 3.6+ (recommended)**
   ```bash
   mvn -version
   ```

### Installation & Setup

#### Option 1: Using Maven (Recommended)

1. **Install Maven** from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

2. **Clone or navigate to the project directory**
   ```bash
   cd Advanced-Vehicle-Rental-Mananagement-System
   ```

3. **Compile the project**
   ```bash
   mvn clean compile
   ```

4. **Run the web server**
   ```bash
   mvn exec:java -Dexec.mainClass="com.rowusu.vehiclerental.web.WebServer"
   ```

5. **Access the application**
   - Open your browser and go to: [http://localhost:8080](http://localhost:8080)

#### Option 2: Manual Setup (Without Maven)

1. **Download required JAR files** to a `lib/` directory:
   - `servlet-api-4.0.1.jar`
   - `jetty-server-9.4.48.jar`
   - `jetty-servlet-9.4.48.jar`
   - `jetty-webapp-9.4.48.jar`

2. **Compile Java files**
   ```bash
   javac -cp "lib/*" -d target/classes src/main/java/com/rowusu/vehiclerental/**/*.java
   ```

3. **Run the web server**
   ```bash
   java -cp "target/classes:lib/*" com.rowusu.vehiclerental.web.WebServer
   ```

## 🎯 Features & Usage

### 🚗 Vehicle Management
- **Add Vehicles**: Register cars, motorcycles, and trucks with daily rates
- **Fleet Overview**: View all vehicles with status (Available/Rented)
- **Vehicle Rating**: Rate vehicles on a 1-5 star scale
- **Remove Vehicles**: Delete vehicles from the fleet

### 👤 Customer Management
- **Customer Registration**: Add new customers with unique IDs
- **Loyalty Program**: Automatic point tracking with Bronze/Silver/Gold tiers
- **Customer Rating**: Rate customer reliability
- **Customer Directory**: View all registered customers

### 📋 Rental Operations
- **Process Rentals**: Select available vehicles and customers
- **Cost Calculation**: Real-time rental cost estimation
- **Return Processing**: Mark rentals as completed
- **Rental History**: Track all active and completed rentals

### 📊 Business Reports
- **Fleet Performance**: Utilization rates and vehicle statistics
- **Revenue Analytics**: Total and daily revenue tracking
- **Customer Insights**: Loyalty program distribution and retention
- **Utilization Metrics**: Average rental duration and demand patterns

## 🔧 API Endpoints

### Vehicles API
- `GET /vehicles?action=list` - Get all vehicles
- `GET /vehicles?action=available` - Get available vehicles only
- `POST /vehicles?action=add` - Add new vehicle
- `POST /vehicles?action=rate` - Rate a vehicle
- `DELETE /vehicles?id={vehicleId}` - Remove vehicle

### Customers API
- `GET /customers?action=list` - Get all customers
- `POST /customers?action=add` - Register new customer
- `POST /customers?action=rate` - Rate a customer
- `DELETE /customers?id={customerId}` - Remove customer

### Rentals API
- `GET /rentals?action=list` - Get active rentals
- `GET /rentals?action=stats` - Get rental statistics
- `POST /rentals?action=process` - Process new rental
- `POST /rentals?action=return` - Return vehicle

### Reports API
- `GET /reports?type=fleet` - Fleet performance report
- `GET /reports?type=revenue` - Revenue analytics
- `GET /reports?type=customer` - Customer insights
- `GET /reports?type=utilization` - Utilization metrics
- `GET /reports?type=dashboard` - Dashboard summary

## 📱 User Interface

### Navigation Tabs
- **Vehicles**: Manage fleet and add new vehicles
- **Customers**: Register and manage customer accounts
- **Rentals**: Process rentals and returns
- **Reports**: View business analytics and insights

### Key Features
- **Responsive Design**: Adapts to different screen sizes
- **Real-time Updates**: Dashboard refreshes automatically
- **Form Validation**: Client-side and server-side validation
- **Interactive Elements**: Buttons, dropdowns, and modals
- **Status Indicators**: Color-coded status for vehicles and rentals

## 🛠️ Development

### Project Structure
```
src/main/
├── java/com/rowusu/vehiclerental/
│   ├── servlet/          # Web servlets
│   ├── web/             # Web server launcher
│   ├── model/           # Business model classes
│   ├── customers/       # Customer management
│   ├── rentalagency/    # Rental agency logic
│   └── exceptions/      # Custom exceptions
└── webapp/
    ├── index.jsp        # Main application page
    ├── css/style.css    # Responsive CSS styling
    ├── js/app.js        # JavaScript application logic
    └── WEB-INF/web.xml  # Servlet configuration
```

### Adding New Features

1. **Backend**: Create new servlets in `servlet/` package
2. **Frontend**: Add UI components to `index.jsp` and logic to `app.js`
3. **Styling**: Update `style.css` for new visual elements
4. **API**: Define new endpoints following the existing pattern

## 🔒 Configuration

### Server Settings
- **Port**: 8080 (configurable in `WebServer.java`)
- **Context Path**: `/` (root)
- **Session Management**: Enabled for user state

### Database
- **Storage**: In-memory (session-based)
- **Persistence**: Data lost on server restart
- **Future Enhancement**: Add database integration (MySQL, PostgreSQL)

## 🚦 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   - Change port in `WebServer.java` (line 16)
   - Or stop the process using port 8080

2. **Maven not found**
   - Install Maven from official website
   - Add Maven to your system PATH

3. **Java compilation errors**
   - Ensure JDK 11+ is installed
   - Check JAVA_HOME environment variable

4. **Browser can't access localhost:8080**
   - Check if server started successfully
   - Look for firewall blocking the port
   - Try 127.0.0.1:8080 instead

### Logs and Debugging
- Server logs appear in the console
- Browser Developer Tools for frontend debugging
- Check Network tab for API request/response details

## 🔄 Version History

- **v3.0**: Web-based version with REST API
- **v2.0**: GUI version with Swing interface
- **v1.0**: Console-based application

## 📞 Support

For issues, questions, or contributions:
1. Check the troubleshooting section above
2. Review the console logs for error messages
3. Ensure all dependencies are properly installed
4. Verify Java and Maven versions meet requirements

## 🎉 Success! 

Once running, you should see:
- 🚀 Server startup message with URL
- 📱 Web interface accessible at http://localhost:8080
- 🎯 All features working: vehicles, customers, rentals, reports

**Happy Vehicle Rental Management! 🚗💨**