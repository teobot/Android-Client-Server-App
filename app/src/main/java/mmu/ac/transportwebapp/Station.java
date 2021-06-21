package mmu.ac.transportwebapp;

/**
 * Here is my station object,
 * I use these objects to store the needed information about each station
 * Including the:
 * StationName
 * Latitude
 * Longitude
 */
public class Station {
    private String StationName;
    private double Latitude;
    private double Longitude;

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
