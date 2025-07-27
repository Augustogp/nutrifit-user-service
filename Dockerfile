FROM eclipse-temurin:21-jdk-jammy

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/nutrifit-user-account-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4000
EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]