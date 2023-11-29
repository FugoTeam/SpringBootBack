FROM openjdk:8-jdk-alpine
FROM maven:alpine

WORKDIR /usr/src/app/api
COPY . ./
RUN ./mvn clean package -DskipTests
CMD mvn spring-boot:run
EXPOSE 8080
