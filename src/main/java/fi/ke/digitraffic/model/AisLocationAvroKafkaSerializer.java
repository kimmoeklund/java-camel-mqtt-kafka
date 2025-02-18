package fi.ke.digitraffic.model;

import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AisLocationAvroKafkaSerializer implements Serializer<AisLocation> {
    private static final Logger LOG = LogManager.getLogger(AisLocationAvroKafkaSerializer.class);

    @Override
    public byte[] serialize(String topic, AisLocation data) {
        if (data == null) {
            return null;
        }
        try {
            return AvroSerdes.aisLocationSerde.serialize(data);
        } catch (Exception e) {
            LOG.error("Error serializing AisLocation: {}", e.getMessage(), e);
            throw new RuntimeException("Error serializing AisLocation", e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up
    }
}
