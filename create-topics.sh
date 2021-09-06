#!/bin/bash

kafka-topics --create --bootstrap-server kafka:9092 --partitions 6 --replication-factor 1 --topic car-metrics
kafka-topics --create --bootstrap-server kafka:9092 --partitions 6 --replication-factor 1 --topic car-locations
kafka-topics --create --bootstrap-server kafka:9092 --partitions 3 --replication-factor 1 --topic weather
kafka-topics --create --bootstrap-server kafka:9092 --partitions 3 --replication-factor 1 --topic driver-notifications