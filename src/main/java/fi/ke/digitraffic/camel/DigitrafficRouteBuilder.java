package fi.ke.digitraffic.camel;

import fi.ke.digitraffic.model.AisLocation;
import fi.ke.digitraffic.model.AisMetadata;
import fi.ke.digitraffic.util.MqttHeaders;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.Optional;

public class DigitrafficRouteBuilder extends RouteBuilder {

    public void configure() {
        from("paho:vessels-v2/+/location?" +
               "brokerUrl={{mqtt.server}}&" +
               "clientId=fi.ke")
                .routeId("mqtt-location")
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    AisLocation location = exchange.getIn().getBody(AisLocation.class);
                    Optional<String> mmsi = MqttHeaders.parseMmsiFromTopic(exchange.getIn().getHeader("CamelMqttTopic", String.class), "location");
                    exchange.getIn().setBody(mmsi.map(location::withMmsi).orElse(location));
                })
                .log(LoggingLevel.DEBUG, "Received location: ${body}")
                .setHeader(KafkaConstants.KEY, simple("${body.mmsi}"))
                .to("kafka:vessel-location?brokers={{kafka.bootstrap.brokers}}&valueSerializer=fi.ke.digitraffic.model.AisLocationAvroSerializer");

        from("paho:vessels-v2/+/metadata?" +
                "brokerUrl=wss://meri-test.digitraffic.fi&" +
                "clientId=fi.ke")
                .routeId("mqtt-metadata")
                .unmarshal().json(JsonLibrary.Jackson)
                .process(exchange -> {
                    AisMetadata metadata = exchange.getIn().getBody(AisMetadata.class);
                    Optional<String> mmsi = MqttHeaders.parseMmsiFromTopic(exchange.getIn().getHeader("CamelMqttTopic", String.class), "metadata");
                    exchange.getIn().setBody(mmsi.map(metadata::withMmsi).orElse(metadata));
                })
                .log(LoggingLevel.DEBUG, "Received metadata: ${body}")
                .setHeader(KafkaConstants.KEY, simple("${body.mmsi}"))
                .to("kafka:vessel-metadata?brokers={{kafka.bootstrap.brokers}}&valueSerializer=fi.ke.digitraffic.model.AisMetadataAvroSerializer");


        CamelContext ctx = getContext();
        ctx.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
    }
}