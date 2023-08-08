FROM openjdk:20-jdk-slim
WORKDIR /app
COPY target/drones-api-1.0.jar .
EXPOSE 8080
ENTRYPOINT ["java","-jar","drones-api-1.0.jar"]