### Description

This is "learner" application to learn basics with Apache Camel and Kafka KStreams API.

CamelApp: Application reads vessel AIS messages from MQTT topic and publishes them to Kafka topics. This application uses Digitraffic's [websocket API](https://www.digitraffic.fi/meriliikenne/#mqtt-websocket--rajapinnat) and their test environment. 
KStreamsApp: Joins vessel location and metadata messages as table.  

### Prerequisites

- maven
- docker-compose
- Kafka tools in path

### Setup

1. Start Kafka cluster with `docker-compose up -d`
2. Create topics
- `kafka-topics.sh --create --topic vessel-location --partitions 6 --bootstrap-server localhost:29092`
- `kafka-topics.sh --create --topic vessel-metadata --partitions 6 --bootstrap-server localhost:29092`

### Run

You can run both applications with `./run.sh`, the script will remain tailing `logs/application.log` which
is used by both applications to log. It will take a moment for app to create streams but after a while you
should see entries like this

```
2025-02-18 18:14:11.494 [ais-processor-client-StreamThread-1] INFO  fi.ke.digitraffic.kafka.KStreamApp - meta key=668116152, value=VesselState[location=AisLocation[mmsi=668116152, time=1739895245, sog=10.2, cog=73.0, navStat=0, rot=0, posAcc=true, raim=false, heading=69, lon=23.3659, lat=59.5468], metadata=AisMetadata[mmsi=668116152, timestamp=1739895226138, destination=RUULU, name=OZANNO, draught=81, eta=170560, posType=1, refA=208, refB=42, refC=16, refD=28, callSign=S9-VW, imo=9394935, type=80]]
```
Use CTRL-C to stop the script which will kill the apps.

### Known issues

Digitraffic API limits connections to 5 per minute based on the IP.