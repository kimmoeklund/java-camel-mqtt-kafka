package fi.ke.digitraffic.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class AvroSerdes {
    public final static AvroMapper mapper = new AvroMapper();
    public final static AvroSchema locationSchema;
    public final static AvroSchema metadataSchema;

    static {
        try {
            locationSchema = mapper.schemaFor(AisLocation.class);
            metadataSchema = mapper.schemaFor(AisMetadata.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class AisLocationSerde extends Serdes.WrapperSerde<AisLocation> {
        public AisLocationSerde() {
            super(new AisLocationAvroSerializer(), new AisLocationAvroDeserializer());
        }
    }

    public static final class AisMetadataSerde extends Serdes.WrapperSerde<AisMetadata> {
        public AisMetadataSerde() {
            super(new AisMetadataAvroSerializer(), new AisMetadataAvroDeserializer());
        }
    }

    public static Serde<AisLocation> AisLocation() {
        return new AisLocationSerde();
    }

    public static Serde<AisMetadata> AisMetadata() {
        return new AisMetadataSerde();
    }
}