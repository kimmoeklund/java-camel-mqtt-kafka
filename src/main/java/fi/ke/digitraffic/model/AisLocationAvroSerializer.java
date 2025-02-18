package fi.ke.digitraffic.model;

import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class AisLocationAvroSerializer implements Serializer<AisLocation> {
    private static final Logger LOG = LogManager.getLogger(AisLocationAvroSerializer.class);
    final private AvroMapper avroMapper;
    final private AvroSchema avroSchema;

    public AisLocationAvroSerializer() {
        this.avroMapper = AvroSerdes.mapper;
        this.avroSchema = AvroSerdes.locationSchema;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, AisLocation data) {
        if (data == null) {
            return null;
        }
        try {
            return avroMapper.writer(avroSchema).writeValueAsBytes(data);
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
