# JobPulse 📊

> A clean, straightforward Spring Boot application to track job applications and interview rounds.

JobPulse helps you organize your job hunt. It keeps track of the roles you've applied for, the companies you're interviewing with, and the status of each interview round—all in one place.

## 📸 Screenshots

*(Replace the placeholders below with actual images of your app)*

| Dashboard View | Application Details |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/84afc3d2-f19c-4a97-88e4-de8f12f01f24" alt="Dashboard" width="400"/> | <img src="https://github.com/user-attachments/assets/2e72b5fa-503b-44e2-a4aa-4584a4857ef6" alt="Application Details" width="400"/> |
<img width="1226" height="749" alt="image" src="https://github.com/user-attachments/assets/419f4f4f-f32b-4d96-8c5d-e0b62862e22f" />


## 🚀 Tech Stack

- **Backend:** Java 17, Spring Boot (Web MVC, Data JPA)
- **Database:** MySQL
- **Frontend:** HTML, Vanilla JavaScript, CSS (Served directly from Spring Boot)
- **Build Tool:** Maven

## ⚙️ Local Setup

Running this project locally is very simple.

### Prerequisites
- Java 17 installed
- MySQL running on port 3306

### Steps to Run

1. **Create the Database**  
   Open your MySQL terminal or workbench and run:
   ```sql
   CREATE DATABASE jobpulse_db;
   ```

2. **Clone the Repository**
   ```bash
   git clone https://github.com/akshayst369/jobpulse.git
   cd jobpulse
   ```

3. **Run the Application**  
   Since `ddl-auto=update` is enabled, Spring Boot will automatically create the required database tables for you.
   ```bash
   ./mvnw spring-boot:run
   ```
   *(If you have maven installed globally, you can also use `mvn spring-boot:run`)*

4. **Access the App**  
   Open your browser and navigate to:  
   `http://localhost:8080`

## 📂 Key Features

- **Job Application Tracking:** Add, view, and update job applications (Company, Role, Status, Priority).
- **Interview Round Management:** Track individual interview rounds (HR, Technical, Managerial) tied to specific applications.
- **RESTful API:** Cleanly separated endpoints to manage both Jobs and Rounds.
- **Pagination:** Handles large amounts of data seamlessly with paginated API responses.

## 🤝 Contributing
Contributions, issues, and feature requests are welcome!

---
*Built with ❤️ for a better job hunt.*
