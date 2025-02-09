<h1>Coworking Spaces - Console Application
</h1>
<h2>Overview</h2>

Coworking Spaces is a console-based Java application that allows users to manage coworking space reservations. The application supports two roles: Admin and Customer. It uses PostgreSQL for database management and Hibernate (HQL queries) for data persistence. The application follows the Spring framework for dependency injection and service management. Additionally, it includes unit tests using Mockito and demonstrates the use of a custom class loader to load a Greeting class dynamically.

<h2>Features</h2>

<h3>User Roles</h3>

Admin:

1 - Add a new workspace

2 - Remove an existing workspace

3 - View all reservations

4 - Update workspace details

5 - Cancel any reservation

Customer:

1 - Browse available coworking spaces

2 - Make a reservation

3 - View personal reservations

4 - Cancel own reservation

<h2>Technologies Used</h2>

Java (Core features, OOP principles)

Spring Framework (Bean configuration, dependency injection)

Hibernate (HQL queries for database interaction)

PostgreSQL (Database for storing workspace and reservation data)

Mockito (Unit testing)

Custom Class Loader (Dynamically loads a Greeting class for demonstration)

<h2>Installation and Setup</h2>

<h3>1. Prerequisites</h3>

Java 17+

PostgreSQL installed and running

Maven installed

<h3>2. Database Configuration</h3>

Create a PostgreSQL database:

CREATE DATABASE coworking_db;

<h3>3. Build and Run the Application</h3>

mvn clean install 
mvn package

<h2>Usage</h2>

<h3>1. Start the Application</h3>

Run the main class (Main.java).

<h3>2. Choose a Role</h3>

Welcome to Coworking Spaces!
1 - Admin
2 - Customer
3 - Exit
Select an option:

<h3>3. Admin Functionalities</h3>

1 - Add workspace
2 - Remove workspace
3 - View all reservations
4 - Update workspace
5 - Cancel any reservation

<h3>4. Customer Functionalities</h3>

1 - Browse available coworking spaces
2 - Make a reservation
3 - View personal reservations
4 - Cancel own reservation

<h2>Testing</h2>

Run unit tests using:

mvn test

Tests are written using Mockito to mock dependencies and ensure business logic correctness.

<h2>Custom Class Loader Usage</h2>

The application demonstrates Java class loading by dynamically loading a Greeting class using a custom class loader.


 
