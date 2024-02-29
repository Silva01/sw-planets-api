# Use an OpenJDK runtime as a base image
FROM openjdk:17-oracle
LABEL maintainer="Daniel Silva"

# Define a working directory inside the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/sw-planets-api-0.0.1-SNAPSHOT.jar /app

# Expose the port the app runs on
EXPOSE 8080

# Specify the command to run on container startup
CMD ["java", "-jar", "sw-planets-api-0.0.1-SNAPSHOT.jar"]