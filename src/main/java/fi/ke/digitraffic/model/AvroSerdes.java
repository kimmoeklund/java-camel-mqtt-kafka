package fi.ke.digitraffic.model;

import fi.ke.digitraffic.util.RecordAvroSerde;

public class AvroSerdes {
    public static RecordAvroSerde<AisLocation> aisLocationSerde = RecordAvroSerde.create(AisLocation.class);
    public static RecordAvroSerde<AisMetadata> aisMetadataSerde = RecordAvroSerde.create(AisMetadata.class);
}
