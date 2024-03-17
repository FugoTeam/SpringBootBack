#!/bin/bash
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
fi

if [ -z "$PORT" ]; then
    echo "Error: PORT variable is not set in .env file"
    exit 1
fi


echo "Stopping process running on port $PORT..."
sudo lsof -t -i:$PORT -sTCP:LISTEN | xargs -r kill


echo "Starting your application..."
git checkout main
git pull

rm -rf target

mvn clean package -DskipTests

java -jar target/backend-0.0.1-SNAPSHOT.jar
