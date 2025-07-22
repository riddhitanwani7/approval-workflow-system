# Approval Workflow System

A Spring Boot-based RESTful API with an Angular frontend for managing approval workflows in an organizational hierarchy.

## Features

- Authentication with JWT tokens
- Employee management (create, update, delete, toggle availability)
- Supervisor assignment
- Approval workflows (submit, approve, reject, escalate)
- Dashboard with tabs for approvers to view pending requests
- Role-based access control

## Prerequisites

- Java 17 or higher
- Node.js and npm
- MySQL Database
- Maven

## Database Setup

1. Create a MySQL database named `test`
2. Use the following credentials:
   - Username: `root`
   - Password: `root`

## Backend Setup (Spring Boot)

1. Clone the repository
2. Navigate to the project directory
3. Run the Spring Boot application using Maven:

```
mvn spring-boot:run
```

The application will start on port 8080.

## Frontend Setup (Angular)

1. Navigate to the Angular project directory:

```
cd src/main/resources/static
```

2. Install dependencies:

```
npm install
```

3. Start the Angular development server:

```
ng serve
```

The Angular application will be available on http://localhost:4200.

## Default User Accounts

The system comes with three default users:

1. **Supervisor**
   - Email: alice@example.com
   - Password: alice123
   - Role: SUPERVISOR

2. **Manager**
   - Email: bob@example.com
   - Password: bob123
   - Role: MANAGER

3. **Employee**
   - Email: charlie@example.com
   - Password: charlie123
   - Role: EMPLOYEE

## Usage Guide

### Login

1. Access the login page at (http://localhost:9090/)
2. Enter your email and password
3. Click "Login"

### Dashboard

The dashboard displays tabs for each employee who can approve requests. Each tab shows the pending requests for that employee.

- View requests assigned to you
- Approve or reject requests using the respective buttons
- Escalate requests to a higher approver if needed

### Employee Management (Admin only)

- View all employees
- Create new employees
- Update employee details
- Delete employees
- Toggle employee availability
- Assign or change supervisors

### Submit Requests

- Fill out the request form
- Select the requester (self or others if permitted)
- Provide a description
- Submit the request

## API Endpoints

### Authentication

- `POST /api/auth/login`: Authenticate a user

### Employees

- `GET /api/employees`: Get all employees
- `GET /api/employees/{id}`: Get employee by ID
- `POST /api/employees`: Create a new employee
- `PUT /api/employees/{id}`: Update an employee
- `DELETE /api/employees/{id}`: Delete an employee
- `PUT /api/employees/{id}/supervisor`: Update an employee's supervisor
- `PUT /api/employees/{id}/toggle-availability`: Toggle an employee's availability

### Approval Requests

- `GET /api/approvals`: Get all approval requests
- `GET /api/approvals/{id}`: Get approval request by ID
- `GET /api/approvals/employee/{employeeId}`: Get approval requests by employee
- `GET /api/approvals/approver/{approverId}`: Get approval requests by approver
- `POST /api/approvals`: Submit a new approval request
- `PUT /api/approvals/{id}/approve`: Approve a request
- `PUT /api/approvals/{id}/reject`: Reject a request
- `PUT /api/approvals/{id}/escalate`: Escalate a request

## Security

The application uses JWT tokens for authentication. All API endpoints (except for authentication) are protected and require a valid JWT token in the Authorization header.

## Troubleshooting

- If you encounter database connection issues, ensure your MySQL server is running and the credentials are correct.
- For JWT token issues, check that the token hasn't expired (default expiration is 24 hours).
- If the Angular application can't connect to the backend, verify that the Spring Boot application is running and CORS is properly configured. 
