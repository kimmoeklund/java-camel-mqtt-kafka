package fi.ke.digitraffic.util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
class MqttHeadersTest {

    @Test
    @DisplayName("Should extract MMSI from valid location topic")
    void shouldExtractMmsiFromValidLocationTopic() {
        // Given
        String validTopic = "vessels-v2/123456789/location";

        // When
        Optional<String> result = MqttHeaders.parseMmsiFromTopic(validTopic, "location");

        // Then
        assertTrue(result.isPresent());
        assertEquals("123456789", result.get());
    }

    @Test
    @DisplayName("Should extract MMSI from valid metadata topic")
    void shouldExtractMmsiFromValidMetadataTopic() {
        // Given
        String validTopic = "vessels-v2/987654321/metadata";

        // When
        Optional<String> result = MqttHeaders.parseMmsiFromTopic(validTopic, "metadata");

        // Then
        assertTrue(result.isPresent());
        assertEquals("987654321", result.get());
    }

    @ParameterizedTest
    @MethodSource("invalidTopicsProvider")
    @DisplayName("Should return empty Optional for invalid topics")
    void shouldReturnEmptyForInvalidTopics(String invalidTopic, String topicSuffix) {
        // When
        Optional<String> result = MqttHeaders.parseMmsiFromTopic(invalidTopic, topicSuffix);

        // Then
        assertTrue(result.isEmpty());
    }

    private static Stream<Arguments> invalidTopicsProvider() {
        return Stream.of(
                Arguments.of("vessels-v2/12345678/location", "location"),    // 8 digits
                Arguments.of("vessels-v2/1234567890/location", "location"),  // 10 digits
                Arguments.of("vessels-v2/abc123456/location", "location"),   // non-numeric
                Arguments.of("vessels-v2/123456789", "location"),           // missing /location
                Arguments.of("vessels-v2//location", "location"),           // missing MMSI
                Arguments.of("wrong-prefix/123456789/location", "location"), // wrong prefix
                Arguments.of("vessels-v2/12345678/metadata", "metadata"),    // 8 digits
                Arguments.of("vessels-v2/1234567890/metadata", "metadata"),  // 10 digits
                Arguments.of("vessels-v2/abc123456/metadata", "metadata"),   // non-numeric
                Arguments.of("vessels-v2/123456789", "metadata"),           // missing /metadata
                Arguments.of("vessels-v2//metadata", "metadata"),           // missing MMSI
                Arguments.of("wrong-prefix/123456789/metadata", "metadata")  // wrong prefix
        );
    }

    @ParameterizedTest
    @CsvSource({
            "'', location",
            "'', metadata",
            "vessels-v2/123456789/location, ''",
            "vessels-v2/123456789/metadata, ''"
    })
    @DisplayName("Should return empty Optional for empty strings")
    void shouldReturnEmptyForEmptyStrings(String topic, String suffix) {
        // When
        Optional<String> result = MqttHeaders.parseMmsiFromTopic(topic, suffix);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty Optional for null inputs")
    void shouldReturnEmptyForNullInputs() {
        // When & Then
        assertTrue(MqttHeaders.parseMmsiFromTopic(null, "location").isEmpty());
        assertTrue(MqttHeaders.parseMmsiFromTopic("vessels-v2/123456789/location", null).isEmpty());
        assertTrue(MqttHeaders.parseMmsiFromTopic(null, null).isEmpty());
    }

    @Test
    @DisplayName("Should pick first if multiple valid patterns in topic")
    void shouldHandleMultipleValidPatternsInTopic() {
        // Given
        String topicWithMultiplePatterns = "vessels-v2/123456789/location/vessels-v2/987654321/location";

        // When
        Optional<String> result = MqttHeaders.parseMmsiFromTopic(topicWithMultiplePatterns, "location");

        // Then
        assertTrue(result.isPresent());
        assertEquals("123456789", result.get()); // Should return first match
    }

    @Test
    @DisplayName("Should handle mixed location and metadata patterns")
    void shouldHandleMixedPatterns() {
        // Given
        String mixedTopic = "vessels-v2/123456789/metadata/vessels-v2/987654321/location";

        // When
        Optional<String> metadataResult = MqttHeaders.parseMmsiFromTopic(mixedTopic, "metadata");
        Optional<String> locationResult = MqttHeaders.parseMmsiFromTopic(mixedTopic, "location");

        // Then
        assertTrue(metadataResult.isPresent());
        assertEquals("123456789", metadataResult.get());

        assertTrue(locationResult.isPresent());
        assertEquals("987654321", locationResult.get());
    }
}
