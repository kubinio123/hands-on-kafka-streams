#!/bin/bash

kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --topic car-speed
kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --topic car-engine
kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --topic car-location
kafka-topics --create --bootstrap-server kafka:9092 --partitions 1 --topic location-data --config "cleanup.policy=compact"
kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --topic driver-notification

sbt "project avro" "run car.avro.RegisterAvroSchemas"