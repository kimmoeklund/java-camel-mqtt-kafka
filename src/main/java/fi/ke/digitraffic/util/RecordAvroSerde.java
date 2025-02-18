package fi.ke.digitraffic.util;


import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class RecordAvroSerde<T> {
    private final Schema schema;
    private final Map<String, Method> accessorMethods;

    public RecordAvroSerde(Class<T> type) {
        this.schema = ReflectData.get().getSchema(type);
        // Create a map of field names to their accessor methods
        this.accessorMethods = Arrays.stream(type.getRecordComponents())
                .collect(Collectors.toMap(
                        RecordComponent::getName,
                        RecordComponent::getAccessor
                ));
    }

    public byte[] serialize(T object) throws IOException {
        GenericRecord avroRecord = new GenericData.Record(schema);
        // Use reflection to set all fields
        for (Schema.Field field : schema.getFields()) {
            String fieldName = field.name();
            try {
                Method accessor = accessorMethods.get(fieldName);
                Object value = accessor.invoke(object);
                avroRecord.put(fieldName, value);
            } catch (ReflectiveOperationException e) {
                throw new IOException("Failed to access field: " + fieldName, e);
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        DatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
        writer.write(avroRecord, encoder);
        encoder.flush();
        return outputStream.toByteArray();
    }

    public T deserialize(byte[] bytes) throws IOException {
        DatumReader<T> reader = ReflectData.get().createDatumReader(schema);
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
        return reader.read(null, decoder);
    }

    // Static factory method
    public static <T> RecordAvroSerde<T> create(Class<T> type) {
        return new RecordAvroSerde<>(type);
    }
}