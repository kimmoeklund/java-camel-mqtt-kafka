package fi.ke.digitraffic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class AisSerializationTest {

    @Nested
    class AisLocationSerializationTest {
        private AisLocationAvroSerializer serializer;
        private AisLocationAvroDeserializer deserializer;
        private AisLocation testLocation;

        @BeforeEach
        void setUp() {
            serializer = new AisLocationAvroSerializer();
            deserializer = new AisLocationAvroDeserializer();

            testLocation = new AisLocation(
                    "123456789",    // mmsi
                    1613494800000L, // time (epoch millis)
                    12.5,          // sog (speed over ground)
                    45.0,          // cog (course over ground)
                    0,             // navStat
                    0,             // rot (rate of turn)
                    true,          // posAcc (position accuracy)
                    false,         // raim
                    90,            // heading
                    24.9567,       // lon
                    60.1567        // lat
            );
        }

        @Test
        void shouldSerializeAndDeserializeLocation() {
            // Serialize
            byte[] serialized = serializer.serialize("test-topic", testLocation);
            assertNotNull(serialized);
            assertTrue(serialized.length > 0);

            // Deserialize
            AisLocation deserialized = deserializer.deserialize("test-topic", serialized);

            // Verify all fields match
            assertEquals(testLocation.mmsi(), deserialized.mmsi());
            assertEquals(testLocation.time(), deserialized.time());
            assertEquals(testLocation.sog(), deserialized.sog(), 0.0001);
            assertEquals(testLocation.cog(), deserialized.cog(), 0.0001);
            assertEquals(testLocation.navStat(), deserialized.navStat());
            assertEquals(testLocation.rot(), deserialized.rot());
            assertEquals(testLocation.posAcc(), deserialized.posAcc());
            assertEquals(testLocation.raim(), deserialized.raim());
            assertEquals(testLocation.heading(), deserialized.heading());
            assertEquals(testLocation.lon(), deserialized.lon(), 0.0001);
            assertEquals(testLocation.lat(), deserialized.lat(), 0.0001);
        }

        @Test
        void shouldHandleNullInput() {
            byte[] serialized = serializer.serialize("test-topic", null);
            assertNull(serialized);
        }

        @Test
        void shouldHandleWithMmsiMethod() {
            String newMmsi = "987654321";
            AisLocation modifiedLocation = testLocation.withMmsi(newMmsi);

            byte[] serialized = serializer.serialize("test-topic", modifiedLocation);
            AisLocation deserialized = deserializer.deserialize("test-topic", serialized);

            assertEquals(newMmsi, deserialized.mmsi());
            // Verify other fields remained unchanged
            assertEquals(testLocation.time(), deserialized.time());
            assertEquals(testLocation.sog(), deserialized.sog(), 0.0001);
        }
    }

    @Nested
    class AisMetadataSerializationTest {
        private AisMetadataAvroSerializer serializer;
        private AisMetadataAvroDeserializer deserializer;
        private AisMetadata testMetadata;

        @BeforeEach
        void setUp() {
            serializer = new AisMetadataAvroSerializer();
            deserializer = new AisMetadataAvroDeserializer();

            testMetadata = new AisMetadata(
                    "123456789",       // mmsi
                    1613494800000L,    // timestamp
                    "HELSINKI",        // destination
                    "TEST VESSEL",     // name
                    45,               // draught
                    1234,            // eta
                    1,               // posType
                    10,              // refA
                    20,              // refB
                    5,               // refC
                    5,               // refD
                    "ABC123",        // callSign
                    9876543,         // imo
                    80               // type
            );
        }

        @Test
        void shouldSerializeAndDeserializeMetadata() {
            // Serialize
            byte[] serialized = serializer.serialize("test-topic", testMetadata);
            assertNotNull(serialized);
            assertTrue(serialized.length > 0);

            // Deserialize
            AisMetadata deserialized = deserializer.deserialize("test-topic", serialized);

            // Verify all fields match
            assertEquals(testMetadata.mmsi(), deserialized.mmsi());
            assertEquals(testMetadata.timestamp(), deserialized.timestamp());
            assertEquals(testMetadata.destination(), deserialized.destination());
            assertEquals(testMetadata.name(), deserialized.name());
            assertEquals(testMetadata.draught(), deserialized.draught());
            assertEquals(testMetadata.eta(), deserialized.eta());
            assertEquals(testMetadata.posType(), deserialized.posType());
            assertEquals(testMetadata.refA(), deserialized.refA());
            assertEquals(testMetadata.refB(), deserialized.refB());
            assertEquals(testMetadata.refC(), deserialized.refC());
            assertEquals(testMetadata.refD(), deserialized.refD());
            assertEquals(testMetadata.callSign(), deserialized.callSign());
            assertEquals(testMetadata.imo(), deserialized.imo());
            assertEquals(testMetadata.type(), deserialized.type());
        }

        @Test
        void shouldHandleNullInput() {
            byte[] serialized = serializer.serialize("test-topic", null);
            assertNull(serialized);
        }

        @Test
        void shouldHandleWithMmsiMethod() {
            String newMmsi = "987654321";
            AisMetadata modifiedMetadata = testMetadata.withMmsi(newMmsi);

            byte[] serialized = serializer.serialize("test-topic", modifiedMetadata);
            AisMetadata deserialized = deserializer.deserialize("test-topic", serialized);

            assertEquals(newMmsi, deserialized.mmsi());
            // Verify other fields remained unchanged
            assertEquals(testMetadata.timestamp(), deserialized.timestamp());
            assertEquals(testMetadata.destination(), deserialized.destination());
            assertEquals(testMetadata.name(), deserialized.name());
        }
    }

    @Nested
    class AvroSerdesTest {

        @Test
        void shouldCreateLocationSerde() {
            var serde = AvroSerdes.AisLocation();
            assertNotNull(serde);
            assertNotNull(serde.serializer());
            assertNotNull(serde.deserializer());
        }

        @Test
        void shouldCreateMetadataSerde() {
            var serde = AvroSerdes.AisMetadata();
            assertNotNull(serde);
            assertNotNull(serde.serializer());
            assertNotNull(serde.deserializer());
        }
    }
}