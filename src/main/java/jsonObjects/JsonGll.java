package jsonObjects;

/**
 * Created by vrettos on 04.11.2016.
 */
public class JsonGll {
    private String vin;
    private String ts;
    private double latitude;
    private double longitude;
    private double altitude;


    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getTimestamp() {
        return ts;
    }

    public void setTimestamp(String timestamp) {
        this.ts = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

}
