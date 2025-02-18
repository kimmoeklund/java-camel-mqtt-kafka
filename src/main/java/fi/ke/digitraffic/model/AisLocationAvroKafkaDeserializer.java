package fi.ke.digitraffic.model;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

public class AisLocationAvroKafkaDeserializer implements Deserializer<AisLocation> {
    private static final Logger LOG = LoggerFactory.getLogger(AisLocationAvroKafkaDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    @Override
    public AisLocation deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return AvroSerdes.aisLocationSerde.deserialize(data);
        } catch (Exception e) {
            LOG.error("Error deserializing AisLocation: {}", e.getMessage(), e);
            throw new RuntimeException("Error deserializing AisLocation", e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up
    }
}
