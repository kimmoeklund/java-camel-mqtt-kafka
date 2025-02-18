package fi.ke.digitraffic.model;

public record AisLocation(
        String mmsi, long time, double sog, double cog, int navStat, int rot, boolean posAcc, boolean raim,
        int heading, double lon, double lat
) {
    public AisLocation withMmsi(String newMmsi) {
        return new AisLocation(newMmsi, time, sog, cog, navStat, rot, posAcc, raim, heading, lon, lat);
    }
}
