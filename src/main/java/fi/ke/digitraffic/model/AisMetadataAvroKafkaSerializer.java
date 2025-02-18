package fi.ke.digitraffic.model;

import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AisMetadataAvroKafkaSerializer implements Serializer<AisMetadata> {
    private static final Logger LOG = LogManager.getLogger(AisMetadataAvroKafkaSerializer.class);

    @Override
    public byte[] serialize(String topic, AisMetadata data) {
        if (data == null) {
            return null;
        }
        try {
            return AvroSerdes.aisMetadataSerde.serialize(data);
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
