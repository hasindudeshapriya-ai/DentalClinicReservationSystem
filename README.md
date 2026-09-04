# Online Dental Clinic Reservation System

A web-based **Online Dental Clinic Reservation System** developed to manage patients, dentists, appointments, billing, payments, users, and clinic reports through a centralized system.

## 📌 Project Overview

The system digitizes the main operations of a dental clinic and replaces manual reservation and record-management processes with a structured web application backed by MySQL.

### User Roles
- **Admin** – Full system access
- **Cashier** – Clinic operations, appointments, billing, payments and reports; no User Management
- **Dentist** – View own appointments and permitted patient/billing information; cannot create appointments or manage bills

---

## ✨ Main Features

### 🔐 Authentication & User Management
- User login and session management
- Role-based access control
- User registration
- Admin user management
- Password, email and phone validation
- Invalid-login handling

### 👤 Patient Management
- Register patients
- View patients
- Edit patient details
- Delete patient records

### 🦷 Dentist Management
- Register dentists
- View, edit and delete dentist records
- Link dentist accounts with dentist profiles

### 📅 Appointment Management
- Create appointments
- Select registered patients and dentists
- Set date, time, treatment and status
- View, edit and delete appointments according to role
- Prevent dentist double-booking
- Dentists can view only their own appointments

### 💰 Billing & Payment Management
- Create bills for completed appointments
- Treatment charge and discount calculation
- Record payments
- Calculate balance
- Track payment status
- View and print bills

### 📊 Reports
- Appointment summaries
- Patient summaries
- Billing summaries
- Recent billing information

---

# 📅 Appointment & Billing Rules

Appointments use these statuses:

- `Scheduled`
- `Completed`
- `Cancelled`

| Appointment Status | Create Bill |
|---|---|
| Scheduled | ❌ Not allowed |
| Completed | ✅ Allowed |
| Cancelled | ❌ Not allowed |

**Important rules:**
1. A bill can only be generated for a `Completed` appointment.
2. Scheduled appointments must be completed before billing.
3. Cancelled appointments cannot be billed.
4. Duplicate bills for the same appointment are prevented.
5. Only **Admin** and **Cashier** can create and print bills.
6. Dentists cannot create appointments.

---

# 🛠️ Technology Stack

### Frontend
- HTML5
- CSS3
- JavaScript

### Backend
- Java
- Jakarta Servlets
- Jakarta EE 10
- Apache Tomcat 10

### Database
- MySQL
- JDBC
- MySQL Connector/J

### Build & Development
- Apache Maven
- NetBeans
- Visual Studio Code
- MySQL Workbench / phpMyAdmin

---

# 🏗️ System Architecture

The project follows a **3-Tier Architecture**:

```text
┌─────────────────────────────────────────────┐
│              PRESENTATION LAYER             │
│                                             │
│          HTML + CSS + JavaScript            │
│       Forms • Pages • User Interface        │
└──────────────────────┬──────────────────────┘
                       │ HTTP
                       ▼
┌─────────────────────────────────────────────┐
│              APPLICATION LAYER              │
│                                             │
│  Jakarta Servlets • DAO • Business Logic   │
│  Authentication • Sessions • Role Access    │
└──────────────────────┬──────────────────────┘
                       │ JDBC
                       ▼
┌─────────────────────────────────────────────┐
│                  DATA LAYER                 │
│                                             │
│                    MySQL                    │
│                                             │
│ users • patients • dentists                 │
│ appointments • bills • bill_payments        │
└─────────────────────────────────────────────┘
```

---

# 📂 Project Structure

```text
DentalClinicReservationSystem/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ...
│       │
│       └── webapp/
│           ├── *.html
│           ├── *.css
│           ├── *.js
│           └── WEB-INF/
│
├── database/
│   └── *.sql
│
├── pom.xml
├── README.md
└── FEATURE_UPDATES.md
```

Important backend components include:

```text
Servlets
 ├── LoginServlet
 ├── RegisterUserServlet
 ├── AppointmentServlet
 ├── ViewAppointmentsServlet
 ├── EditAppointmentServlet
 ├── UpdateAppointmentServlet
 ├── DeleteAppointmentServlet
 ├── CreateBillServlet
 ├── RecordPaymentServlet
 ├── PrintBillServlet
 └── ReportServlet

DAO
 ├── UserDAO
 ├── PatientDAO
 ├── DentistDAO
 ├── AppointmentDAO
 ├── BillDAO
 └── ReportDAO
```

---

# 🗄️ Database

The application uses MySQL.

Main tables:

```text
users
patients
dentists
appointments
bills
bill_payments
```

Logical relationship:

```text
Users
  │
  └── Dentist Profile

Patients ────── Appointments ────── Dentists
                    │
                    ▼
                   Bills
                    │
                    ▼
              Bill Payments
```

Database scripts are provided in the `database` directory.

---

# ⚙️ Installation & Setup

## 1. Requirements

Install:

- JDK 21
- Apache Maven
- Apache Tomcat 10
- MySQL Server
- MySQL Workbench or phpMyAdmin
- NetBeans or another Java IDE

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

## 2. Clone the Repository

```bash
git clone https://github.com/hasindudeshapriya-ai/DentalClinicReservationSystem.git
cd DentalClinicReservationSystem
```

## 3. Configure MySQL

Create the database and import the SQL scripts from the `database` folder.

Example:

```sql
CREATE DATABASE dental_clinic;
```

Configure the project's database connection with your local:

```text
Host: localhost
Port: 3306
Database: dental_clinic
Username: your_mysql_username
Password: your_mysql_password
```

> Do not commit database passwords or other secrets to GitHub.

---

# ▶️ Running the Project

### Using NetBeans

1. Open the project in NetBeans.
2. Configure Apache Tomcat 10.
3. Make sure MySQL is running.
4. Configure the database connection.
5. Clean and Build the project.
6. Run the application on Tomcat.

The deployed URL will normally be similar to:

```text
http://localhost:8080/DentalClinicReservationSystem/
```

The exact context path may vary depending on the Tomcat deployment configuration.

---

# 🔒 Security & Access Control

The application uses both frontend and server-side authorization.

Security-related functionality includes:

- Session-based authentication
- Role-based authorization
- Protected servlet endpoints
- Server-side permission checks
- Input validation
- Appointment validation
- Billing status validation
- Duplicate-bill prevention
- Restricted bill printing

Frontend menu hiding is used for usability, while important permissions are enforced on the server.

---

# 🧪 Testing Areas

### Authentication
- Valid login
- Invalid login
- Session management
- Role-based navigation

### Patient Management
- Add
- View
- Edit
- Delete
- Validation

### Dentist Management
- Add
- View
- Edit
- Delete

### Appointment Management
- Create appointment
- Required-field validation
- Date/time validation
- Dentist double-booking prevention
- Status validation
- Dentist access restrictions

### Billing
- Bill for completed appointment
- Block scheduled appointment billing
- Block cancelled appointment billing
- Prevent duplicate bills
- Record payment
- Calculate balance
- Restrict bill printing

### User Management
- Registration validation
- Role restrictions
- Unauthorized-access prevention

---

# 📋 Business Rules

1. Only authenticated users can access protected clinic functions.
2. Admin has full access.
3. Cashier cannot access User Management.
4. Dentist cannot create appointments.
5. Dentist can view only their own appointments.
6. Dentist cannot edit or delete appointments.
7. Only Admin and Cashier can create bills.
8. Bills can only be generated for `Completed` appointments.
9. Scheduled appointments cannot be billed.
10. Cancelled appointments cannot be billed.
11. An appointment cannot have multiple bills.
12. Only authorized Admin/Cashier users can print bills.
13. Dentist time-slot conflicts are prevented.
14. Patient and dentist records must exist before creating an appointment.

---

# 🎯 Project Objectives

- Digitize dental clinic reservation processes.
- Reduce manual appointment management.
- Improve patient and dentist record management.
- Provide role-based access control.
- Reduce appointment scheduling conflicts.
- Improve billing and payment management.
- Provide printable billing information.
- Provide clinic reports.
- Improve data organization and operational efficiency.

---

# 🚀 Future Enhancements

- Online patient registration
- Email/SMS appointment notifications
- Online payment gateway
- Dentist availability calendar
- Appointment reminders
- Advanced reporting dashboards
- Audit logs
- Password reset via email
- Mobile application
- Cloud deployment
- Selenium automated testing
- REST API integration

---

# 👨‍💻 Developer

**Hasindu Deshapriya**

Software Engineering Undergraduate

---

# 📚 Academic Project

This project demonstrates:

- Web application development
- Java Servlet development
- JDBC and MySQL
- CRUD operations
- Authentication
- Role-based access control
- Appointment management
- Billing and payment processing
- Software testing
- 3-tier architecture

---

# 📜 License

This project is intended primarily for academic and educational purposes.
