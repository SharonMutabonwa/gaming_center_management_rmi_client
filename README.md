# 🎮 Gaming Center Management System – Client Application

## 📌 Overview

This project is the **Client-side application** of the Gaming Center Management System. It is a **Java Swing desktop application** used by staff and administrators to interact with the system.

The client focuses purely on **presentation and user interaction**. All business logic, validations, database operations, and notifications are handled by the **Server Application** via **Java RMI**.

---

## 🎯 Responsibilities

* Provide graphical user interfaces for staff and admins
* Collect and validate basic user input (required fields, formats)
* Communicate with the server using Java RMI
* Display results, confirmations, and error messages

---

## 🏗️ Architecture Role

**Tier:** Presentation Tier (Client)

**Architecture Pattern:** MVC

* **View:** Swing frames and panels
* **Controller:** Handles UI events and calls server methods
* **Model:** Shared DTOs received from the server

---

## 🖥️ Technology Stack

* Java SE
* Java Swing (`javax.swing`)
* Java RMI
* MVC Design Pattern

---

## 📂 Project Structure

```
client/
│
├── view/                # Swing UI screens
│   ├── LoginFrame.java
│   ├── DashboardFrame.java
│   ├── BookingManagementFrame.java
│   ├── CustomerManagementFrame.java
│   ├── TournamentFrame.java
│   └── ReportFrame.java
│
├── controller/          # Event handling & coordination
│   ├── LoginController.java
│   ├── BookingController.java
│   ├── CustomerController.java
│   └── TournamentController.java
│
├── model/               # Shared data objects (DTOs)
├── rmi/                 # RMI connection utilities
│   └── RMIClient.java
│
└── utils/               # UI and validation helpers
```

---

## 🔐 Authentication Flow (OTP-Based)

1. User enters username and password
2. Server generates a 6-digit OTP
3. OTP is sent to the user via email
4. User enters OTP
5. Server validates and returns authenticated session

---

## 🔄 Client–Server Communication

* Protocol: **Java RMI**
* Default Port: **3500**

Example:

```java
RemoteService service = RMIClient.getService();
Booking booking = service.createBooking(...);
```

The method call appears local but is executed remotely on the server.

---

## ▶️ How to Run the Client

1. Ensure the **Server Application is running**
2. Configure RMI host and port in `RMIClient`
3. Run `Main.java` or `LoginFrame.java`
4. Log in using valid staff/admin credentials

---

## 🚫 What the Client Does NOT Do

* No direct database access
* No business rule enforcement
* No email or notification sending

---

## 👥 User Roles Supported

* **Admin:** Full access (reports, configuration, management)
* **Staff:** Daily operations (bookings, customers, tournaments)

---

## ✅ Summary

The Client Application provides a clean, user-friendly interface for managing a gaming center while delegating all critical logic and data handling to the server for security, scalability, and maintainability.

