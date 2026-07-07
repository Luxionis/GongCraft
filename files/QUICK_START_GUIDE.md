# GongCraft Java Desktop - Quick Start Guide
## Mulai development dalam 30 menit!

---

## 🎯 Tujuan
Setup development environment dan run aplikasi pertama kali.

**Waktu estimasi:** 30 menit

---

## ✅ Step 1: Install Prerequisites (15 menit)

### Windows
```bash
# Install Chocolatey (if not installed)
# https://chocolatey.org/install

# Install JDK 17
choco install openjdk17

# Install Maven
choco install maven

# Install MySQL
choco install mysql

# Install Git
choco install git

# Verify installation
java -version
mvn -version
mysql --version
```

### macOS
```bash
# Install Homebrew (if not installed)
# https://brew.sh

# Install JDK 17
brew install openjdk@17

# Install Maven
brew install maven

# Install MySQL
brew install mysql

# Install Git
brew install git

# Add Java to PATH
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc

# Verify
java -version
mvn -version
```

### Linux (Ubuntu/Debian)
```bash
# Update package manager
sudo apt update

# Install JDK 17
sudo apt install openjdk-17-jdk

# Install Maven
sudo apt install maven

# Install MySQL Server
sudo apt install mysql-server

# Install Git
sudo apt install git

# Verify
java -version
mvn -version
```

---

## ✅ Step 2: Setup Database (10 menit)

### 2a. Start MySQL Service

**Windows:**
```bash
# MySQL should auto-start
# Check if running:
mysql --version
```

**macOS:**
```bash
# Start MySQL
brew services start mysql

# Or manual start
mysqld_safe &
```

**Linux:**
```bash
# Start MySQL
sudo service mysql start

# Or
sudo systemctl start mysql
```

### 2b. Create Database & User

```bash
# Connect to MySQL (prompt for password)
mysql -u root -p

# Paste these commands dalam MySQL CLI:
```

```sql
-- Create database
CREATE DATABASE gongcraft CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'gongcraft'@'localhost' IDENTIFIED BY 'gongcraft123';

-- Grant privileges
GRANT ALL PRIVILEGES ON gongcraft.* TO 'gongcraft'@'localhost';

-- Flush to apply changes
FLUSH PRIVILEGES;

-- Exit
EXIT;
```

### 2c. Import Schema

```bash
# Navigate ke project directory
cd gongcraft

# Run schema script
mysql -u gongcraft -p gongcraft < sql/01_GongCraft_Database_Schema.sql

# When prompted, enter password: gongcraft123

# Verify (optional)
mysql -u gongcraft -p gongcraft -e "SHOW TABLES;"
```

**Result:**
```
+----------------------------+
| Tables_in_gongcraft        |
+----------------------------+
| audit_logs                 |
| customers                  |
| craftsmen                  |
| inventory                  |
| notifications              |
| order_items                |
| orders                     |
| payments                   |
| production_progress        |
| products                   |
| quality_checks             |
| roles                      |
| users                      |
| ... (17 tables total)      |
+----------------------------+
```

✅ Database ready!

---

## ✅ Step 3: Setup Java Project (5 menit)

### 3a. Create Project Directory

```bash
# Create project directory
mkdir gongcraft-workspace
cd gongcraft-workspace

# Create Maven project
mvn archetype:generate \
  -DgroupId=com.gongcraft \
  -DartifactId=gongcraft-desktop \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### 3b. Copy Files

```bash
cd gongcraft-desktop

# Replace pom.xml with the provided template
# Copy the pom.xml we provided to this directory
# File: pom.xml (from outputs)

# Create directories
mkdir -p src/main/java/com/gongcraft
mkdir -p src/main/java/com/gongcraft/ui
mkdir -p src/main/java/com/gongcraft/ui/panels
mkdir -p src/main/java/com/gongcraft/ui/components
mkdir -p src/main/java/com/gongcraft/service
mkdir -p src/main/java/com/gongcraft/repository
mkdir -p src/main/java/com/gongcraft/entity
mkdir -p src/main/java/com/gongcraft/dto
mkdir -p src/main/java/com/gongcraft/config
mkdir -p src/main/java/com/gongcraft/util
mkdir -p src/main/resources
mkdir -p sql

# Copy database schema
# Copy: sql/01_GongCraft_Database_Schema.sql
```

### 3c. Create Configuration File

**Create: `src/main/resources/application.properties`**

```properties
# Spring Boot
spring.application.name=GongCraft Desktop
spring.profiles.active=dev

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/gongcraft
spring.datasource.username=gongcraft
spring.datasource.password=gongcraft123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# HikariCP Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Logging
logging.level.root=INFO
logging.level.com.gongcraft=DEBUG
logging.pattern.console=%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## ✅ Step 4: Create Main Application Class (5 menit)

**Create: `src/main/java/com/gongcraft/GongCraftApplication.java`**

```java
package com.gongcraft;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GongCraftApplication extends Application {
    
    private static ConfigurableApplicationContext springContext;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        
        stage.setTitle("GongCraft - Sistem Pemesanan & Monitoring Produksi Gamelan");
        stage.setWidth(1200);
        stage.setHeight(700);
        
        // Simple welcome screen
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");
        
        Label title = new Label("Selamat Datang di GongCraft");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label subtitle = new Label("Sistem Pemesanan & Monitoring Produksi Gamelan");
        subtitle.setStyle("-fx-font-size: 14px;");
        
        Label message = new Label("✅ Database connection successful!\n\nApplicationStarting...");
        message.setStyle("-fx-font-size: 12px;");
        
        root.getChildren().addAll(title, subtitle, message);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (springContext != null) {
            springContext.close();
        }
    }

    public static void main(String[] args) {
        springContext = SpringApplication.run(GongCraftApplication.class, args);
        Application.launch(args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }
}
```

---

## ✅ Step 5: Build & Run (5 menit)

### Option A: Run dari IDE

**Using IntelliJ IDEA (Recommended):**

1. Open IntelliJ IDEA
2. `File → Open` → select `gongcraft-desktop` directory
3. Wait for Maven to sync dependencies
4. Right-click `GongCraftApplication.java`
5. Click `Run 'GongCraftApplication.main()'`
6. Application window akan muncul

**Using Eclipse:**

1. Open Eclipse
2. `File → Import → Maven → Existing Maven Projects`
3. Browse to `gongcraft-desktop` folder
4. Click Finish
5. Right-click project → `Run As → Java Application`
6. Select `GongCraftApplication`

**Using VS Code:**

1. Install Extension Pak for Java
2. Open `gongcraft-desktop` folder
3. Open `GongCraftApplication.java`
4. Click `Run` (above main method)

### Option B: Run dari Terminal

```bash
cd gongcraft-desktop

# Download dependencies & compile
mvn clean compile

# Run application
mvn spring-boot:run

# Or run directly from IDE Run menu
```

### Option C: Create Executable JAR

```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/gongcraft-desktop-1.0.0-jar-with-dependencies.jar
```

---

## 🎉 Success!

Jika Anda melihat:
1. ✅ No database connection errors
2. ✅ Application window appears
3. ✅ Welcome message displays

Maka development environment Anda sudah siap!

---

## 📝 Langkah Berikutnya

### Week 1: Database Layer
- [ ] Create all entity classes
- [ ] Create repository interfaces
- [ ] Test database operations

### Week 2: Service Layer
- [ ] Create service classes
- [ ] Implement business logic
- [ ] Add validation & error handling

### Week 3-4: GUI Implementation
- [ ] Create main window structure
- [ ] Build individual panels
- [ ] Connect GUI dengan services

### Week 5: Integration & Testing
- [ ] Full integration testing
- [ ] GUI testing
- [ ] Performance optimization

---

## 🛠️ Troubleshooting

### Problem: "MySQL Connection Refused"

**Solution:**
```bash
# Check MySQL is running
mysql --version

# If Windows:
# Services > MySQL → Start

# If macOS:
brew services start mysql

# If Linux:
sudo systemctl start mysql

# Check connection
mysql -u gongcraft -p gongcraft -e "SELECT 1;"
```

### Problem: "Java version is not correct"

**Solution:**
```bash
# Check Java version
java -version

# Should show: openjdk version "17..." or higher

# If wrong version:
# Windows: choco install openjdk17
# macOS: brew install openjdk@17
# Linux: sudo apt install openjdk-17-jdk
```

### Problem: "Maven build fails"

**Solution:**
```bash
# Clear Maven cache
mvn clean

# Force update dependencies
mvn clean install -U

# Check pom.xml is correct
# Make sure internet connection is working
```

### Problem: "Database schema import failed"

**Solution:**
```bash
# Verify database exists
mysql -u gongcraft -p gongcraft -e "SHOW TABLES;"

# If empty, check errors:
mysql -u gongcraft -p gongcraft < sql/01_GongCraft_Database_Schema.sql 2>&1 | tail

# Re-create database if needed:
mysql -u root -p -e "DROP DATABASE IF EXISTS gongcraft;"
mysql -u root -p < /path/to/schema.sql
```

### Problem: "OutOfMemoryError"

**Solution:**
```bash
# Run with more memory
java -Xmx2048m -Xms512m -jar gongcraft-desktop.jar

# Or edit IntelliJ VM options:
# File → Settings → Build, Execution, Deployment → Compiler
# Set: -Xmx2048m
```

---

## 📚 Learning Resources

If you need to learn the technologies:

### Java
- [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [Java 17 Features](https://www.oracle.com/java/technologies/javase/17-relnotes.html)

### Spring Boot
- [Spring Boot Official Docs](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

### JavaFX
- [JavaFX Official Documentation](https://openjfx.io/javadoc/)
- [JavaFX Tutorials](https://docs.oracle.com/javase/8/javafx/get-started-tutorial/)

### MySQL
- [MySQL Official Documentation](https://dev.mysql.com/doc/)
- [SQL Tutorial](https://www.w3schools.com/sql/)

### Maven
- [Maven Official Guide](https://maven.apache.org/guides/)

---

## ✨ Tips & Best Practices

### 1. Keep IDE Updated
```bash
# IntelliJ IDEA: Help → Check for Updates
# Eclipse: Help → Check for Updates
# VS Code: Extensions → Java Extensions → Update
```

### 2. Use Version Control
```bash
# Initialize Git repository
git init
git add .
git commit -m "Initial commit"

# Create .gitignore for Maven projects
# Add: target/, .idea/, *.class, *.jar, etc.
```

### 3. Regular Testing
```bash
# Test frequently
mvn test

# Before building JAR
mvn clean test package
```

### 4. Code Organization
- Keep packages organized (ui, service, repository, entity)
- Use meaningful class names
- Add JavaDoc comments
- Follow Java naming conventions

### 5. Database Backups
```bash
# Backup database
mysqldump -u gongcraft -p gongcraft > backup.sql

# Restore from backup
mysql -u gongcraft -p gongcraft < backup.sql
```

---

## 🚀 Ready to Code!

Sekarang Anda siap untuk mulai development:

1. ✅ Environment setup complete
2. ✅ Database ready
3. ✅ Maven project created
4. ✅ Application runs

**Next:** Follow the `JAVA_DESKTOP_IMPLEMENTATION_GUIDE.md` untuk detail implementasi!

---

## 📞 Quick Reference

| Task | Command |
|------|---------|
| Run app (IDE) | Right-click GongCraftApplication → Run |
| Run app (Maven) | `mvn spring-boot:run` |
| Build JAR | `mvn clean package` |
| Run JAR | `java -jar gongcraft-desktop.jar` |
| Run tests | `mvn test` |
| Clear build | `mvn clean` |
| Check database | `mysql -u gongcraft -p gongcraft -e "SHOW TABLES;"` |

---

**Good luck! Happy coding! 🎉**

Questions? Check the documentation files:
- `summary-updated.md` - Project overview
- `JAVA_DESKTOP_IMPLEMENTATION_GUIDE.md` - Detailed implementation
- `CHANGELOG_SUMMARY.md` - Architecture changes
- `pom.xml` - Maven configuration
