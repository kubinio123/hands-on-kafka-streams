#!/bin/bash

kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --replication-factor 1 --topic car-speed
kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --replication-factor 1 --topic car-engine
kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --replication-factor 1 --topic car-location
kafka-topics --create --bootstrap-server kafka:9092 --partitions 1 --replication-factor 1 --topic location-data --config "cleanup.policy=compact"
kafka-topics --create --bootstrap-server kafka:9092 --partitions 2 --replication-factor 1 --topic driver-notification