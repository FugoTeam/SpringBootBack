#!/bin/bash
cd backend/
 
echo "DATABASE_URL=${DATABASE_URL}" >> .env
echo "DATABASE_USERNAME=${DATABASE_USERNAME}" >> .env
echo "DATABASE_PASSWORD=${DATABASE_PASSWORD}" >> .env
echo "PORT=${PORT}" >> .env
 
if [ -z "$PORT" ]; then
    exit 1
fi
 
echo "Stopping process running on port $PORT..."
sudo lsof -t -i:$PORT -sTCP:LISTEN | xargs -r kill
 
 
echo "Starting application..."
git checkout main
git pull
 
rm -rf target
 
mvn clean package -DskipTests
 
nohup java -jar target/backend-0.0.1-SNAPSHOT.jar > nohup.out 2>&1 &
 
check_application_started() {
    if grep -q "Started BackendApplication" nohup.out; then
        echo "Spring Boot application started successfully."
        exit 0
    fi
}
 
while ! grep -q "Started BackendApplication" nohup.out; do
    echo "Waiting for the Spring Boot application to start..."
    sleep 1
done
 
echo "Spring Boot application started successfully."
