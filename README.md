### Description

This is "learner" application to learn basics with Apache Camel and Kafka KStreams API.

CamelApp: Application reads vessel AIS messages from MQTT topic and publishes them to Kafka topics.
KStreamsApp: Joins vessel location and metadata messages as table 

### Prerequisites

- maven
- docker-compose
- Kafka tools in path

### Setup and Run

1. Start Kafka cluster with `docker-compose up -d`
2. Create topics 
- `kafka-topics --create --topic vessel-location --partitions 5 --bootstrap-server localhost:29092`
- `kafka-topics --create --topic vessel-metadata --partitions 5 --bootstrap-server localhost:29092`

Run following programs on own shells and leave them running 
- mvn camel:run 
- mvn exec:java 