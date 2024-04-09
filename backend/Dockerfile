FROM openjdk:17-alpine

WORKDIR /app

COPY target/back-0.0.1-SNAPSHOT.jar /app/backend.jar

COPY .env /app/.env

CMD ["java", "-jar", "backend.jar"]
