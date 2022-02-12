#!/bin/bash

docker compose up -d

sleep 5 # wait until broker is ready to accept requests

echo "Creating kafka topics..."
docker exec -it streams-app-tools-1 ./create-topics.sh

echo "Listing kafka topics..."
docker exec -it streams-app-tools-1 ./list-topics.sh