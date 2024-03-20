FROM eclipse-temurin:17.0.8.1_1-jdk-alpine
LABEL mainitainer="Code2AM"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]