# Use the official OpenJDK 17 image as a base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Package the application
COPY target/*.jar post-crud.jar

# Expose the port the application runs on
EXPOSE 2020

# Run the application
ENTRYPOINT ["java", "-jar", "post-crud.jar"]