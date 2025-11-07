# Use Maven with Eclipse Temurin JDK 21
FROM maven:3.9.9-eclipse-temurin-21

# Set working directory
WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy Maven files first for better layer caching
COPY pom.xml ./

# Download dependencies first
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests -B

# Expose the application port
EXPOSE 8080

# Add a health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Find and run the JAR file
RUN find target -name "*.jar" -exec mv {} app.jar \;

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]