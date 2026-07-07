# GongCraft Java Desktop - Implementation Guide
## Complete Technical Guide untuk Java Native Development

---

## 📚 Daftar Isi

1. [Setup Awal](#setup-awal)
2. [Maven Project Configuration](#maven-project-configuration)
3. [Spring Boot Integration](#spring-boot-integration)
4. [JavaFX GUI Architecture](#javafx-gui-architecture)
5. [Database Layer (JPA)](#database-layer-jpa)
6. [Service Layer](#service-layer)
7. [GUI Best Practices](#gui-best-practices)
8. [Testing Strategy](#testing-strategy)
9. [Packaging JAR](#packaging-jar)
10. [Deployment Guide](#deployment-guide)

---

## Setup Awal

### 1. Prerequisites Installation

#### Windows
```bash
# Install JDK 17+ (via Chocolatey atau manual)
choco install openjdk17

# Install Maven
choco install maven

# Install MySQL
choco install mysql

# Verify installation
java -version
mvn -version
mysql --version
```

#### Mac
```bash
# Install JDK 17+
brew install openjdk@17

# Install Maven
brew install maven

# Install MySQL
brew install mysql

# Set JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

#### Linux (Ubuntu/Debian)
```bash
# Install JDK 17+
sudo apt update
sudo apt install openjdk-17-jdk

# Install Maven
sudo apt install maven

# Install MySQL
sudo apt install mysql-server

# Verify
java -version
mvn -version
```

### 2. IDE Setup (IntelliJ IDEA Recommended)

```
1. Download IntelliJ IDEA Community Edition (free)
2. Install & launch
3. File → New → Project from Version Control
4. Clone repository
5. Wait for Maven to sync dependencies
6. Configure JDK: File → Project Structure → Project SDK → Select JDK 17+
```

### 3. Database Setup

```sql
-- Create database
CREATE DATABASE gongcraft CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'gongcraft'@'localhost' IDENTIFIED BY 'gongcraft123';
GRANT ALL PRIVILEGES ON gongcraft.* TO 'gongcraft'@'localhost';
FLUSH PRIVILEGES;

-- Run schema script
mysql -u gongcraft -p gongcraft < sql/01_GongCraft_Database_Schema.sql
```

---

## Maven Project Configuration

### 1. Project Structure

```
gongcraft-desktop/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/gongcraft/
│   │   └── resources/
│   └── test/
│       ├── java/
│       └── resources/
├── sql/
└── docs/
```

### 2. Complete pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- Parent POM -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <!-- Project Coordinates -->
    <groupId>com.gongcraft</groupId>
    <artifactId>gongcraft-desktop</artifactId>
    <version>1.0.0</version>
    <name>GongCraft Desktop</name>
    <description>Java Desktop Application untuk Pemesanan & Monitoring Produksi Gamelan</description>

    <!-- Properties -->
    <properties>
        <java.version>17</java.version>
        <javafx.version>21</javafx.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <!-- Dependencies -->
    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Connection Pooling -->
        <dependency>
            <groupId>com.zaxxer</groupId>
            <artifactId>HikariCP</artifactId>
        </dependency>

        <!-- Caching -->
        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>caffeine</artifactId>
        </dependency>

        <!-- JSON Processing -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>

        <!-- JavaFX (Desktop GUI) -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-graphics</artifactId>
            <version>${javafx.version}</version>
        </dependency>

        <!-- PDF Export -->
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itextpdf</artifactId>
            <version>5.5.13</version>
        </dependency>

        <!-- Excel Export -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.3</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </dependency>

        <!-- Lombok (Optional, untuk reduce boilerplate) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>7.8.1</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- Build Plugins -->
    <build>
        <plugins>
            <!-- Spring Boot Maven Plugin -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.gongcraft.GongCraftApplication</mainClass>
                </configuration>
            </plugin>

            <!-- Shade Plugin untuk Fat JAR (Important!) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <finalName>gongcraft-desktop-${project.version}-jar-with-dependencies</finalName>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>com.gongcraft.GongCraftApplication</mainClass>
                                </transformer>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ApacheCommonsLoggingResourceTransformer"/>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!-- Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>

            <!-- Surefire Plugin (untuk testing) -->
            <plugin>
                <groupId>org.apache.maven.surefire.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                </configuration>
            </plugin>

            <!-- JavaFX Maven Plugin (untuk development) -->
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>0.0.8</version>
                <configuration>
                    <mainClass>com.gongcraft.GongCraftApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 3. Application Properties

**src/main/resources/application.properties:**
```properties
# Spring Boot Configuration
spring.application.name=GongCraft Desktop
spring.profiles.active=dev

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/gongcraft
spring.datasource.username=gongcraft
spring.datasource.password=gongcraft123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Connection Pooling
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# Logging
logging.level.root=INFO
logging.level.com.gongcraft=DEBUG
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

# File Upload
app.file.upload.dir=./uploads
app.file.max-size=10485760

# Application Settings
app.currency.symbol=Rp
app.date.format=dd/MM/yyyy
```

**src/main/resources/application-prod.properties:**
```properties
spring.profiles.active=prod
spring.datasource.url=jdbc:mysql://prod-server:3306/gongcraft
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.gongcraft=INFO
```

---

## Spring Boot Integration

### 1. Main Application Class

**GongCraftApplication.java:**
```java
package com.gongcraft;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class GongCraftApplication extends Application {
    
    private static ConfigurableApplicationContext springContext;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        
        // Setup window
        stage.setTitle("GongCraft - Sistem Pemesanan & Monitoring Produksi Gamelan");
        stage.setWidth(1400);
        stage.setHeight(800);
        
        // Load main window dari Spring context
        var mainWindow = springContext.getBean(MainWindow.class);
        Scene scene = new Scene(mainWindow.getRoot());
        
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        springContext.close();
    }

    public static void main(String[] args) {
        // Mulai Spring Boot context dulu
        springContext = SpringApplication.run(GongCraftApplication.class, args);
        
        // Kemudian launch JavaFX
        Application.launch(args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
```

### 2. Spring Configuration

**config/AppConfig.java:**
```java
package com.gongcraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
```

**config/DatabaseConfig.java:**
```java
package com.gongcraft.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }
    
    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }
}
```

---

## JavaFX GUI Architecture

### 1. Main Window Structure

**ui/MainWindow.java:**
```java
package com.gongcraft.ui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;
import com.gongcraft.ui.panels.*;

@Component
public class MainWindow {
    
    private final BorderPane root;
    private TabPane mainTabPane;
    
    // Inject panels
    private final DashboardPanel dashboardPanel;
    private final CustomerPanel customerPanel;
    private final OrderPanel orderPanel;
    private final ProductPanel productPanel;
    private final ProductionPanel productionPanel;
    private final PaymentPanel paymentPanel;
    private final ReportPanel reportPanel;
    private final SettingsPanel settingsPanel;
    
    public MainWindow(
            DashboardPanel dashboardPanel,
            CustomerPanel customerPanel,
            OrderPanel orderPanel,
            ProductPanel productPanel,
            ProductionPanel productionPanel,
            PaymentPanel paymentPanel,
            ReportPanel reportPanel,
            SettingsPanel settingsPanel) {
        
        this.dashboardPanel = dashboardPanel;
        this.customerPanel = customerPanel;
        this.orderPanel = orderPanel;
        this.productPanel = productPanel;
        this.productionPanel = productionPanel;
        this.paymentPanel = paymentPanel;
        this.reportPanel = reportPanel;
        this.settingsPanel = settingsPanel;
        
        this.root = new BorderPane();
        initializeUI();
    }
    
    private void initializeUI() {
        // Top: Menu bar
        root.setTop(createMenuBar());
        
        // Center: Tab pane dengan semua panels
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        addTabs();
        
        root.setCenter(mainTabPane);
        
        // Bottom: Status bar
        root.setBottom(createStatusBar());
        
        // Styling
        root.setStyle("-fx-background-color: #f0f0f0;");
    }
    
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(exitItem);
        
        // Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem settingsItem = new MenuItem("Settings");
        editMenu.getItems().addAll(settingsItem);
        
        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().addAll(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);
        return menuBar;
    }
    
    private void addTabs() {
        Tab dashboardTab = new Tab("Dashboard", dashboardPanel.getRoot());
        dashboardTab.setClosable(false);
        
        Tab customerTab = new Tab("Customers", customerPanel.getRoot());
        customerTab.setClosable(false);
        
        Tab productTab = new Tab("Products", productPanel.getRoot());
        productTab.setClosable(false);
        
        Tab orderTab = new Tab("Orders", orderPanel.getRoot());
        orderTab.setClosable(false);
        
        Tab productionTab = new Tab("Production", productionPanel.getRoot());
        productionTab.setClosable(false);
        
        Tab paymentTab = new Tab("Payments", paymentPanel.getRoot());
        paymentTab.setClosable(false);
        
        Tab reportTab = new Tab("Reports", reportPanel.getRoot());
        reportTab.setClosable(false);
        
        Tab settingsTab = new Tab("Settings", settingsPanel.getRoot());
        settingsTab.setClosable(false);
        
        mainTabPane.getTabs().addAll(
            dashboardTab, customerTab, productTab, orderTab,
            productionTab, paymentTab, reportTab, settingsTab
        );
    }
    
    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-border-color: #ccc;");
        
        Label statusLabel = new Label("Ready");
        HBox.setHgrow(statusLabel, Priority.ALWAYS);
        
        Label userLabel = new Label("User: Admin");
        
        statusBar.getChildren().addAll(statusLabel, userLabel);
        return statusBar;
    }
    
    public Parent getRoot() {
        return root;
    }
}
```

### 2. Sample Panel (Customer Panel)

**ui/panels/CustomerPanel.java:**
```java
package com.gongcraft.ui.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.springframework.stereotype.Component;
import com.gongcraft.dto.CustomerDTO;
import com.gongcraft.service.CustomerService;
import com.gongcraft.ui.components.CustomTable;

import java.util.List;

@Component
public class CustomerPanel {
    
    private final VBox root;
    private final CustomerService customerService;
    private final CustomTable<CustomerDTO> customerTable;
    
    private TextField searchField;
    private ObservableList<CustomerDTO> customerData;
    
    public CustomerPanel(CustomerService customerService) {
        this.customerService = customerService;
        this.root = new VBox(10);
        this.customerTable = new CustomTable<>();
        
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        root.setPadding(new Insets(15));
        
        // Top: Search bar & buttons
        HBox topBar = createTopBar();
        root.getChildren().add(topBar);
        
        // Center: Table
        setupTable();
        VBox.setVgrow(customerTable.getTableView(), Priority.ALWAYS);
        root.getChildren().add(customerTable.getTableView());
    }
    
    private HBox createTopBar() {
        HBox topBar = new HBox(10);
        
        searchField = new TextField();
        searchField.setPromptText("Search customers...");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        Button addBtn = new Button("Add Customer");
        addBtn.setStyle("-fx-padding: 8px 15px;");
        addBtn.setOnAction(e -> showAddCustomerDialog());
        
        Button editBtn = new Button("Edit");
        editBtn.setOnAction(e -> editSelectedCustomer());
        
        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-text-fill: white; -fx-background-color: #d9534f;");
        deleteBtn.setOnAction(e -> deleteSelectedCustomer());
        
        topBar.getChildren().addAll(
            new Label("Search:"), searchField,
            addBtn, editBtn, deleteBtn
        );
        
        return topBar;
    }
    
    private void setupTable() {
        // Define columns
        customerTable.addColumn("ID", "id", 60);
        customerTable.addColumn("Name", "name", 150);
        customerTable.addColumn("Email", "email", 180);
        customerTable.addColumn("Phone", "phone", 120);
        customerTable.addColumn("Address", "address", 200);
        
        customerTable.getTableView().setPrefHeight(400);
    }
    
    private void loadData() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        customerData = FXCollections.observableArrayList(customers);
        customerTable.getTableView().setItems(customerData);
    }
    
    private void showAddCustomerDialog() {
        Dialog<CustomerDTO> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter customer information");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextArea addressArea = new TextArea();
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressArea, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(
            ButtonType.OK, ButtonType.CANCEL
        );
        
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(nameField.getText());
                customer.setEmail(emailField.getText());
                customer.setPhone(phoneField.getText());
                customer.setAddress(addressArea.getText());
                return customer;
            }
            return null;
        });
        
        dialog.showAndWait().ifPresent(customer -> {
            try {
                customerService.createCustomer(customer);
                loadData();
                showAlert("Success", "Customer added successfully");
            } catch (Exception e) {
                showAlert("Error", "Failed to add customer: " + e.getMessage());
            }
        });
    }
    
    private void editSelectedCustomer() {
        CustomerDTO selected = customerTable.getTableView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a customer to edit");
            return;
        }
        
        // Show edit dialog...
    }
    
    private void deleteSelectedCustomer() {
        CustomerDTO selected = customerTable.getTableView().getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a customer to delete");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setContentText("Are you sure you want to delete this customer?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    customerService.deleteCustomer(selected.getId());
                    loadData();
                    showAlert("Success", "Customer deleted successfully");
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete customer: " + e.getMessage());
                }
            }
        });
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public Parent getRoot() {
        return root;
    }
}
```

### 3. Custom Components

**ui/components/CustomTable.java:**
```java
package com.gongcraft.ui.components;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomTable<T> {
    
    private final TableView<T> tableView;
    
    public CustomTable() {
        this.tableView = new TableView<>();
        this.tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }
    
    public void addColumn(String headerText, String propertyName, double width) {
        TableColumn<T, String> column = new TableColumn<>(headerText);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        column.setPrefWidth(width);
        tableView.getColumns().add(column);
    }
    
    public TableView<T> getTableView() {
        return tableView;
    }
}
```

---

## Database Layer (JPA)

### 1. Entity Example

**entity/Customer.java:**
```java
package com.gongcraft.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Customer name is required")
    @Column(nullable = false)
    private String name;
    
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    private String address;
    
    private String city;
    
    private String province;
    
    private String postalCode;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Enumerated(EnumType.STRING)
    private CustomerStatus status = CustomerStatus.ACTIVE;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // ... more getters/setters
    
    public enum CustomerStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}
```

### 2. Repository

**repository/CustomerRepository.java:**
```java
package com.gongcraft.repository;

import com.gongcraft.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    Optional<Customer> findByPhone(String phone);
    
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    List<Customer> findByStatus(Customer.CustomerStatus status);
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> searchCustomers(String searchTerm);
}
```

---

## Service Layer

### 1. Service Example

**service/CustomerService.java:**
```java
package com.gongcraft.service;

import com.gongcraft.dto.CustomerDTO;
import com.gongcraft.entity.Customer;
import com.gongcraft.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    
    @Cacheable("customers")
    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<CustomerDTO> searchCustomers(String searchTerm) {
        log.info("Searching customers with term: {}", searchTerm);
        return customerRepository.searchCustomers(searchTerm)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return toDTO(customer);
    }
    
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerDTO createCustomer(CustomerDTO dto) {
        log.info("Creating new customer: {}", dto.getName());
        
        // Validate unique email
        if (customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setProvince(dto.getProvince());
        customer.setPostalCode(dto.getPostalCode());
        customer.setStatus(Customer.CustomerStatus.ACTIVE);
        
        Customer saved = customerRepository.save(customer);
        log.info("Customer created successfully with id: {}", saved.getId());
        
        return toDTO(saved);
    }
    
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        log.info("Updating customer with id: {}", id);
        
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setProvince(dto.getProvince());
        customer.setPostalCode(dto.getPostalCode());
        customer.setUpdatedAt(LocalDateTime.now());
        
        Customer updated = customerRepository.save(customer);
        return toDTO(updated);
    }
    
    @CacheEvict(value = "customers", allEntries = true)
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with id: {}", id);
        customerRepository.deleteById(id);
    }
    
    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setProvince(customer.getProvince());
        dto.setPostalCode(customer.getPostalCode());
        dto.setStatus(customer.getStatus().toString());
        return dto;
    }
}
```

---

## GUI Best Practices

### 1. Threading (untuk long-running operations)

```java
// DON'T block UI thread!
// Use Task untuk background operations

Task<List<CustomerDTO>> loadTask = new Task<List<CustomerDTO>>() {
    @Override
    protected List<CustomerDTO> call() throws Exception {
        return customerService.getAllCustomers(); // Heavy operation
    }
};

loadTask.setOnSucceeded(event -> {
    customerData = FXCollections.observableArrayList(loadTask.getValue());
    tableView.setItems(customerData);
});

loadTask.setOnFailed(event -> {
    showAlert("Error", "Failed to load data");
});

new Thread(loadTask).start();
```

### 2. Input Validation

```java
private boolean validateCustomerForm(String name, String email, String phone) {
    if (name.isEmpty()) {
        showAlert("Validation Error", "Name is required");
        return false;
    }
    
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        showAlert("Validation Error", "Invalid email format");
        return false;
    }
    
    if (phone.length() < 10) {
        showAlert("Validation Error", "Phone number must be at least 10 digits");
        return false;
    }
    
    return true;
}
```

### 3. Exception Handling

```java
try {
    CustomerDTO newCustomer = customerService.createCustomer(customerDTO);
    loadData(); // Refresh table
    showAlert("Success", "Customer added successfully");
} catch (DataIntegrityViolationException e) {
    showAlert("Error", "Email already exists in system");
} catch (Exception e) {
    log.error("Unexpected error while creating customer", e);
    showAlert("Error", "An unexpected error occurred: " + e.getMessage());
}
```

---

## Testing Strategy

### 1. Unit Tests untuk Service

**test/java/com/gongcraft/service/CustomerServiceTest.java:**
```java
package com.gongcraft.service;

import com.gongcraft.dto.CustomerDTO;
import com.gongcraft.entity.Customer;
import com.gongcraft.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private CustomerService customerService;
    
    private Customer testCustomer;
    
    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john@example.com");
        testCustomer.setPhone("081234567890");
    }
    
    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(testCustomer));
        
        var result = customerService.getAllCustomers();
        
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(customerRepository, times(1)).findAll();
    }
    
    @Test
    void testCreateCustomer() {
        CustomerDTO dto = new CustomerDTO();
        dto.setName("Jane Doe");
        dto.setEmail("jane@example.com");
        dto.setPhone("081987654321");
        
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);
        
        var result = customerService.createCustomer(dto);
        
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}
```

---

## Packaging JAR

### Build Command

```bash
# Full clean build with JAR
mvn clean package -DskipTests

# Build dengan executing tests
mvn clean package

# Build hanya ter JAR (tanpa shade)
mvn spring-boot:build-image
```

### Run Executable JAR

```bash
# Direct
java -jar target/gongcraft-desktop-1.0.0-jar-with-dependencies.jar

# With VM options
java -Xmx1024m -Xms512m -jar target/gongcraft-desktop-1.0.0-jar-with-dependencies.jar

# With different profile
java -Dspring.profiles.active=prod -jar gongcraft-desktop.jar
```

---

## Deployment Guide

### 1. Pre-deployment Checklist

- [ ] Database schema created & tested
- [ ] All unit tests passing
- [ ] Integration tests passing
- [ ] GUI tested on target machine
- [ ] Documentation complete
- [ ] Version bumped in pom.xml
- [ ] CHANGELOG updated
- [ ] JAR file created & tested

### 2. Installation Steps untuk End-user

1. Install Java 17+
2. Download `gongcraft-desktop-1.0.0-jar-with-dependencies.jar`
3. Setup MySQL database
4. Configure database connection in `application-prod.properties`
5. Run: `java -jar gongcraft-desktop-1.0.0-jar-with-dependencies.jar`

### 3. Auto-launcher (Optional)

**Windows - create run.bat:**
```batch
@echo off
cd /d %~dp0
java -jar gongcraft-desktop-1.0.0-jar-with-dependencies.jar
pause
```

**Linux/Mac - create run.sh:**
```bash
#!/bin/bash
cd "$(dirname "$0")"
java -jar gongcraft-desktop-1.0.0-jar-with-dependencies.jar
```

---

## 🎯 Development Workflow

```bash
# 1. Clone & setup
git clone <repo>
cd gongcraft
mvn clean install

# 2. Run during development
mvn spring-boot:run

# 3. Make changes...

# 4. Run tests
mvn test

# 5. Build JAR
mvn clean package -DskipTests

# 6. Test JAR
java -jar target/gongcraft-desktop-1.0.0-jar-with-dependencies.jar

# 7. Push to repo
git add .
git commit -m "Feature/fix description"
git push
```

---

**Happy coding! 🚀**

For more details, refer to:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [JavaFX Documentation](https://openjfx.io/)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
