package fi.ke.digitraffic.model;

public record AisMetadata(
        String mmsi,
        long timestamp,
        String destination,
        String name,
        int draught,
        int eta,
        int posType,
        int refA,
        int refB,
        int refC,
        int refD,
        String callSign,
        int imo,
        int type
) {
    public AisMetadata withMmsi(String newMmsi) {
        return new AisMetadata(newMmsi, timestamp, destination, name, draught, eta, posType, refA, refB, refC, refD, callSign, imo, type);
    }

}