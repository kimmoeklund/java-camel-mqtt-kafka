package fi.ke.digitraffic.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class AisLocationAvroDeserializer implements Deserializer<AisLocation> {
    final private AvroMapper avroMapper;
    final private AvroSchema avroSchema;

    public AisLocationAvroDeserializer() {
        this.avroMapper = AvroSerdes.mapper.copy();
        this.avroSchema = AvroSerdes.locationSchema;
    }
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    @Override
    public AisLocation deserialize(String s, byte[] bytes) {
        try {
            return avroMapper.reader(avroSchema)
                    .with(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                    .readValue(bytes, AisLocation.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        // No resources to clean up
    }
}
