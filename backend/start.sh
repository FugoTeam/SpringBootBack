#!/bin/bash
cd backend/ || exit 1

echo "DATABASE_URL=${DATABASE_URL}" >> .env
echo "DATABASE_USERNAME=${DATABASE_USERNAME}" >> .env
echo "DATABASE_PASSWORD=${DATABASE_PASSWORD}" >> .env
echo "PORT=${PORT}" >> .env

if [ -z "$PORT" ]; then
exit 1
fi

echo "Stopping process running on port $PORT..."
sudo lsof -t -i:"$PORT" -sTCP:LISTEN | xargs -r kill

echo "Starting your application..."
git checkout main
git pull
rm -rf target
mvn clean package -DskipTests
docker build -t fugoworld_backend .
docker run -p "${PORT}":"${PORT}" -e PORT="${PORT}" fugoworld_backend