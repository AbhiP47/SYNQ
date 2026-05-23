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



## 🛠️ System Architecture

```mermaid
flowchart TD

subgraph group_runtime["Runtime"]
   node_synq_app(("Synq<br/>Spring Boot app"))
end

subgraph group_web["Web"]
   node_root_controller["Root<br/>MVC controller"]
   node_page_controller["Pages<br/>MVC controller"]
   node_auth_controller["Auth<br/>MVC controller"]
   node_user_controller["User<br/>MVC controller"]
   node_contact_controller["Contacts<br/>MVC controller"]
   node_api_controller["API<br/>MVC controller<br/>[ApiController.java]"]
end

subgraph group_domain["Domain"]
   node_user_entity[("User<br/>JPA entity<br/>[User.java]")]
   node_contact_entity[("Contact<br/>JPA entity<br/>[Contact.java]")]
   node_social_link[("SocialLink<br/>JPA entity<br/>[SocialLink.java]")]
   node_user_form["UserForm<br/>request form<br/>[UserForm.java]"]
   node_contact_form["ContactForm<br/>request form<br/>[ContactForm.java]"]
   node_contact_search_form["SearchForm<br/>request form"]
end

subgraph group_services["Services"]
   node_user_service{{"UserSvc<br/>[UserService.java]"}}
   node_contact_service{{"ContactSvc"}}
   node_email_service{{"EmailSvc<br/>[EmailService.java]"}}
   node_image_service{{"ImageSvc<br/>[ImageService.java]"}}
end

subgraph group_data["Data"]
   node_user_repo[("UserRepo<br/>JPA repository<br/>[UserRepo.java]")]
   node_contact_repo[("ContactRepo<br/>JPA repository<br/>[ContactRepo.java]")]
end

subgraph group_infra["Infrastructure"]
   node_security_config["Security"]
   node_oauth_service["OAuth2<br/>auth adapter"]
   node_success_handler["Success<br/>auth handler"]
   node_failure_handler["Failure<br/>auth handler"]
   node_details_service["Details<br/>security adapter"]
   node_security_user_service["AuthUserSvc<br/>security service"]
   node_app_config["AppConfig<br/>framework config<br/>[AppConfig.java]"]
   node_file_validator["FileValidator<br/>[FileValidator.java]"]
   node_valid_file["ValidFile<br/>annotation<br/>[ValidFile.java]"]
end

node_synq_app -->|"bootstraps"| node_root_controller
node_synq_app -->|"configures"| node_security_config
node_root_controller -->|"routes"| node_page_controller
node_root_controller -->|"routes"| node_auth_controller
node_root_controller -->|"routes"| node_user_controller
node_root_controller -->|"routes"| node_contact_controller
node_root_controller -->|"routes"| node_api_controller

node_auth_controller -->|"uses"| node_user_service
node_user_controller -->|"uses"| node_user_service
node_contact_controller -->|"uses"| node_contact_service
node_api_controller -->|"uses"| node_contact_service

node_user_service -->|"persists"| node_user_repo
node_contact_service -->|"persists"| node_contact_repo
node_user_service -->|"delegates"| node_email_service
node_contact_service -->|"validates via"| node_file_validator
node_file_validator -->|"implements"| node_valid_file

node_security_config -->|"uses"| node_oauth_service
node_security_config -->|"uses"| node_success_handler
node_security_config -->|"uses"| node_failure_handler
node_security_config -->|"uses"| node_details_service
node_details_service -->|"backed by"| node_security_user_service
node_security_user_service -->|"reads"| node_user_repo

node_user_service -->|"manages"| node_user_entity
node_contact_service -->|"manages"| node_contact_entity
node_contact_service -->|"manages"| node_social_link

node_page_controller -->|"binds"| node_user_form
node_contact_controller -->|"binds"| node_contact_form
node_contact_controller -->|"binds"| node_contact_search_form

node_user_controller -.->|"uses"| node_app_config
node_contact_service -->|"uploads to"| node_image_service

click node_synq_app "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/SynqApplication.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/SynqApplication.java)"
click node_root_controller "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/RootController.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/RootController.java)"
click node_page_controller "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/PageController.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/PageController.java)"
click node_auth_controller "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/AuthController.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/AuthController.java)"
click node_user_controller "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/UserController.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/UserController.java)"
click node_contact_controller "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/ContactController.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/ContactController.java)"
click node_api_controller "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/ApiController.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/controller/ApiController.java)"
click node_user_entity "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/entity/User.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/entity/User.java)"
click node_contact_entity "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/entity/Contact.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/entity/Contact.java)"
click node_social_link "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/entity/SocialLink.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/entity/SocialLink.java)"
click node_user_form "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/forms/UserForm.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/forms/UserForm.java)"
click node_contact_form "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/forms/ContactForm.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/forms/ContactForm.java)"
click node_contact_search_form "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/forms/ContactSearchForm.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/forms/ContactSearchForm.java)"
click node_user_service "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/UserService.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/UserService.java)"
click node_contact_service "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/ContactService.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/ContactService.java)"
click node_email_service "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/EmailService.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/EmailService.java)"
click node_image_service "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/ImageService.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/ImageService.java)"
click node_user_repo "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/repository/UserRepo.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/repository/UserRepo.java)"
click node_contact_repo "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/repository/ContactRepo.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/repository/ContactRepo.java)"
click node_security_config "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/SecurityConfig.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/SecurityConfig.java)"
click node_oauth_service "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/CustomOAuth2UserService.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/CustomOAuth2UserService.java)"
click node_success_handler "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/OAuthenticationSuccessHandler.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/OAuthenticationSuccessHandler.java)"
click node_failure_handler "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/AuthFailureHandler.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/AuthFailureHandler.java)"
click node_details_service "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/UserDetailsImpl.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/UserDetailsImpl.java)"
click node_security_user_service "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/serviceImplementation/SecurityCustomDetailService.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/service/serviceImplementation/SecurityCustomDetailService.java)"
click node_app_config "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/AppConfig.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/config/AppConfig.java)"
click node_file_validator "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/validators/FileValidator.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/validators/FileValidator.java)"
click node_valid_file "[https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/validators/ValidFile.java](https://github.com/abhip47/synq/blob/main/src/main/java/com/synq/validators/ValidFile.java)"

classDef toneNeutral fill:#f8fafc,stroke:#334155,stroke-width:1.5px,color:#0f172a
classDef toneBlue fill:#dbeafe,stroke:#2563eb,stroke-width:1.5px,color:#172554
classDef toneAmber fill:#fef3c7,stroke:#d97706,stroke-width:1.5px,color:#78350f
classDef toneMint fill:#dcfce7,stroke:#16a34a,stroke-width:1.5px,color:#14532d
classDef toneRose fill:#ffe4e6,stroke:#e11d48,stroke-width:1.5px,color:#881337
classDef toneIndigo fill:#e0e7ff,stroke:#4f46e5,stroke-width:1.5px,color:#312e81
classDef toneTeal fill:#ccfbf1,stroke:#0f766e,stroke-width:1.5px,color:#134e4a

class node_synq_app toneBlue
class node_root_controller,node_page_controller,node_auth_controller,node_user_controller,node_contact_controller,node_api_controller toneAmber
class node_user_entity,node_contact_entity,node_social_link,node_user_form,node_contact_form,node_contact_search_form toneMint
class node_user_service,node_contact_service,node_email_service,node_image_service toneRose
class node_user_repo,node_contact_repo toneIndigo
class node_security_config,node_oauth_service,node_success_handler,node_failure_handler,node_details_service,node_security_user_service,node_app_config,node_file_validator,node_valid_file toneTeal
