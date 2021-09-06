#!/bin/bash

kafka-topics --delete --bootstrap-server kafka:9092 --topic car-metrics
kafka-topics --delete --bootstrap-server kafka:9092 --topic car-locations
kafka-topics --delete --bootstrap-server kafka:9092 --topic weather
kafka-topics --delete --bootstrap-server kafka:9092 --topic driver-notifications

curl -X DELETE http://schema-registry:8081/subjects/car-metrics-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/car-metrics-value?permanent=true

curl -X DELETE http://schema-registry:8081/subjects/car-locations-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/car-locations-value?permanent=true

curl -X DELETE http://schema-registry:8081/subjects/weather-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/weather-value?permanent=true

curl -X DELETE http://schema-registry:8081/subjects/driver-notifications-key?permanent=true
curl -X DELETE http://schema-registry:8081/subjects/driver-notifications-value?permanent=true