package fi.ke.digitraffic.model;

import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AisMetadataAvroSerializer implements Serializer<AisMetadata> {
    private static final Logger LOG = LogManager.getLogger(AisMetadataAvroSerializer.class);
    final private AvroMapper avroMapper;
    final private AvroSchema avroSchema;

    public AisMetadataAvroSerializer() {
        this.avroMapper = AvroSerdes.mapper;
        this.avroSchema = AvroSerdes.metadataSchema;
    }

    @Override
    public byte[] serialize(String topic, AisMetadata data) {
        if (data == null) {
            return null;
        }
        try {
            return avroMapper.writer(avroSchema).writeValueAsBytes(data);
        } catch (Exception e) {
            LOG.error("Error serializing AisMetadata: {}", e.getMessage(), e);
            throw new RuntimeException("Error serializing AisMetadata", e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up
    }
}
