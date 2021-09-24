#!/bin/bash

kafka-topics --delete --bootstrap-server kafka:9092 --topic car-speed
kafka-topics --delete --bootstrap-server kafka:9092 --topic car-engine
kafka-topics --delete --bootstrap-server kafka:9092 --topic car-location
kafka-topics --delete --bootstrap-server kafka:9092 --topic location-data
kafka-topics --delete --bootstrap-server kafka:9092 --topic driver-notification

curl -X DELETE http://schema-registry:8081/subjects/car-speed-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/car-speed-value?permanent=true

curl -X DELETE http://schema-registry:8081/subjects/car-engine-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/car-engine-value?permanent=true

curl -X DELETE http://schema-registry:8081/subjects/car-location-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/car-location-value?permanent=true

curl -X DELETE http://schema-registry:8081/subjects/location-data-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/location-data-value?permanent=true

curl -X DELETE http://schema-registry:8081/subjects/driver-notification-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/driver-notification-value?permanent=true