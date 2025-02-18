package fi.ke.digitraffic.kafka;

import fi.ke.digitraffic.model.*;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KStreamApp {
    private static final Logger LOG = LoggerFactory.getLogger(KStreamApp.class);

    public static void main(final String[] args) {
        Properties props = loadProperties();
        StreamsBuilder builder = new StreamsBuilder();
        createTopology(builder);
        // Build and start the streams
        try (KafkaStreams streams = new KafkaStreams(builder.build(), props)) {
            streams.start();
            // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        }
    }

    public static Properties loadProperties() {
        try {
            Configurations configs = new Configurations();
            Configuration config = configs.properties("application.properties");
            Properties kafkaProps = new Properties();
            kafkaProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
            kafkaProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                    config.getString("kafka.bootstrap.brokers"));
            kafkaProps.put(StreamsConfig.CLIENT_ID_CONFIG,
                    config.getString("kafka.streams.client.id"));
            kafkaProps.put(StreamsConfig.APPLICATION_ID_CONFIG,
                    config.getString("kafka.streams.application.id"));
            return kafkaProps;
        } catch (ConfigurationException e) {
            throw new RuntimeException("Could not load application.properties", e);
        }
    }

    public static void createTopology(StreamsBuilder builder) {
        // Create KTable for Metadata
        KTable<String, AisMetadata> metadataTable = builder.table(
                "vessel-metadata",
                Consumed.with(Serdes.String(), AvroSerdes.AisMetadata())
        );
        // Create KTable for Location
        KTable<String, AisLocation> locationTable = builder.table(
                "vessel-location",
                Consumed.with(Serdes.String(), AvroSerdes.AisLocation())
        );
        KTable<String, VesselState> joined = locationTable.join(metadataTable,
                VesselState::new
        );
        joined.toStream().foreach((key, value) -> LOG.info("meta key={}, value={}", key, value.toString()));
    }
}

