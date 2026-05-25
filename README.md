# SYNQ 📱
> A secure, cloud-synced, enterprise-grade contact management engine built with modern full-stack web architectures.

![Spring Boot](https://spring.io/)
![Tailwind CSS](https://tailwindcss.com/)
![Cloudinary](https://cloudinary.com/)
![License](LICENSE)

---

## 🚀 Key Value Propositions

Most contact tools treat portfolios as trivial CRUD tables. SYNQ scales data integrity and secure communication by focusing on real-world engineering constraints:

* Robust OAuth2 Ecosystem: Native seamless profile sync pipelines utilizing Spring Security 6 mapping hooks for Google accounts, handling background registration and session sync without data mismatches.
* Preventing IDOR Vulnerabilities: Complete data isolation context. Resource endpoints natively inspect authentication parameters, ensuring users can never enumerate, view, or delete other users' contact data through direct URL modification.
* High-Fidelity Client-Side Previews: Zero-latency asset updates. Integrated HTML5 FileReader background pipelines allow immediate verification of custom images before triggering cloud storage execution.
* Cloud-Native Asset Pipelines: Decoupled media workflows leveraging Cloudinary API integrations to handle heavy payload mutations, dynamic processing transformations, and responsive adjustments away from the main server threads.

---

## 🛠️ Tech Stack & Architecture

### Backend Core
* Framework: Spring Boot 3.x (Spring MVC, Spring Data JPA)
* Security Architecture: Spring Security 6.x (Form Login, OAuth2 Client, BCrypt Encryption)
* Database Schema Container: PostgreSQL / MySQL 
* Templating Engine Logic: Thymeleaf 3.1 (Layout Dialect, Fragment Decoupling)

### Frontend Engine
* Styling System: Tailwind CSS
* Interactive UI Assets: Flowbite Components & SweetAlert2 Confirmation Dialogs
* Asynchronous Communications Layer: Native JavaScript Async/Await Fetch API (Handling Event Bubbling Overrides)

---

## ⚙️ Local Development Setup

Follow these streamlined instructions to clone, build, and run SYNQ locally on your development environment:

### Prerequisites
* Java Development Kit (JDK 17 or higher)
* Node.js & npm (for Tailwind configuration)
* A Cloudinary Developer account API Key

1. Database & Cloud Configuration
Create an application.properties or application.yml file inside your src/main/resources folder and configure your system profile environment properties:

```properties
# Server Configuration
server.port=8081

# Data Source Connection Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/synq_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Spring Security OAuth2 Configuration Registration Strings
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=profile,email

# Cloudinary Integration Parameter Envs
cloudinary.cloud-name=YOUR_CLOUD_NAME
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET

2. Frontend Assets Generation

# Run from your project directory root containing your package.json
npm install


3. Build and Run the Boot App

# Using the embedded Maven Wrapper script
./mvnw clean spring-boot:run
