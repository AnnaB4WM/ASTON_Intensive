FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy built jar (produced by mvn package)
ARG JAR_FILE=target/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Expose service port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
