
#STAGE 1: Compile Frontend (Tailwind CSS)

FROM node:20-alpine AS frontend-builder
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci

# Copy the rest of the application files and compile Tailwind
COPY . .
RUN npm run build

# STAGE 2: Build Production JAR (Maven + JDK)

FROM maven:3.9.6-eclipse-temurin-21-alpine AS backend-builder
WORKDIR /app

# 1. Copy the compiled Tailwind CSS directly from the frontend-builder stage
# This places the minified CSS/JS exactly where Maven expects it for static assets
COPY --from=frontend-builder /app/src/main/resources/static ./src/main/resources/static

# 2. Copy the backend configuration and source files
COPY pom.xml .
COPY src ./src

# 3. Package the application into a production-ready JAR file
RUN mvn clean package -DskipTests


# STAGE 3: Final Production Runtime Image

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Secure container by creating a non-root system user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy ONLY the finalized execution JAR from Stage 2
COPY --from=backend-builder /app/target/*.jar app.jar

# Document that the app listens on port 8081
EXPOSE 8081

# Execute the application
ENTRYPOINT ["java", "-jar", "app.jar"]