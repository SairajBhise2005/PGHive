# PGHive - PG Management System

A comprehensive Java-based Paying Guest (PG) Management System that demonstrates various Object-Oriented Programming concepts. This project serves as an educational tool to showcase OOP principles including inheritance, polymorphism, encapsulation, and abstraction.

## Project Overview

PGHive is a console-based application that helps PG owners manage their accommodations, tenants, and rental payments efficiently. The system implements various features while focusing on clean code architecture and OOP best practices.

## Key OOP Concepts Demonstrated

### 1. Inheritance

- `User` abstract class serves as the base for `PGOwner` and `Tenant` classes
- Multiple tenant types (`DailyTenant`, `WeeklyTenant`, etc.) inherit from base `Tenant` class
- Demonstrates both single and hierarchical inheritance

### 2. Polymorphism

- Method overriding in different tenant types for rent calculation
- Method overloading in constructors and utility methods
- Runtime polymorphism through tenant type implementations

### 3. Encapsulation

- Private fields with public getters/setters
- Data hiding in all major classes
- Protected access for inherited members

### 4. Abstraction

- Abstract `User` class
- Interface implementation (`Loggable`)
- Clear separation of concerns between classes

### 5. Interface Implementation

- `Loggable` interface for logging functionality
- Default method implementation in interfaces

## Features

### For PG Owner

- Tenant management (Add/Edit/Delete)
- Room management
- Room assignment
- Rent optimization
- Payment tracking
- Report generation
- Bulk payment generation

### For Tenants

- Profile management
- Rent payment history
- Document management
- Password management

## Class Structure

### Core Classes

- `PGHive`: Main application class
- `User`: Abstract base class
- `PGOwner`: Owner management
- `Tenant`: Base tenant class
- `Room`: Room management
- `Payment`: Payment tracking

### Specialized Classes

- Various tenant types (Daily/Weekly/Monthly etc.)
- `RentOptimizer`: Rent calculation utility
- `RoomAssignmentException`: Custom exception

## Design Patterns

- Factory Pattern in tenant creation
- Singleton pattern potential in PGOwner
- Strategy Pattern in rent calculation

## Security Features

- Password-based authentication
- Account lockout after failed attempts
- Session management

## Future Enhancements

1. Database integration
2. GUI implementation
3. Payment gateway integration
4. Advanced reporting features
5. Mobile app integration

## Learning Outcomes

This project helps understand:

- OOP principles in real-world applications
- Java programming best practices
- Exception handling
- User authentication and authorization
- Business logic implementation

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Any Java IDE (Eclipse, IntelliJ IDEA, etc.)

### Running the Application

1. Clone the repository
2. Compile the Java files
3. Run the PGHive class
4. Use default credentials:
   - Owner: owner@pg.com / admin123
   - Tenant: john@example.com / password123

## Testing

The system includes sample data for testing:

- Pre-configured rooms and tenants
- Various tenant types
- Different room configurations
