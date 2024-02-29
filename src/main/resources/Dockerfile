# Use an OpenJDK runtime as a base image
FROM openjdk:17-jdk-alpine
LABEL maintainer="Daniel Silva"

# Define a working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/sw-planetes-api.jar /app

# Expose the port the app runs on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["java", "-jar", "sw-planetes-api.jar"]