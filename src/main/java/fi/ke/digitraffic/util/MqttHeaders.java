package fi.ke.digitraffic.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttHeaders {
    static Pattern mmsiPattern = Pattern.compile("vessels-v2/(\\d{9})/location");
    static Pattern metadataPattern = Pattern.compile("vessels-v2/(\\d{9})/metadata");

    public static Optional<String> parseMmsiFromTopic(String topicName, String topicSuffix) {
        if (topicName == null || topicName.isEmpty() || topicSuffix == null || topicSuffix.isEmpty()) {
            return Optional.empty();
        }
        Matcher mmsiMatcher = topicSuffix.equals("location") ? mmsiPattern.matcher(topicName) : metadataPattern.matcher(topicName);
        return mmsiMatcher.find() ? Optional.of(mmsiMatcher.group(1)) : Optional.empty();
    }
}
