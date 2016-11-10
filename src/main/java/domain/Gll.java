package domain;

import jsonObjects.JsonGll;

import javax.persistence.Entity;

/**
 * Created by vrettos on 14.10.2016.
 */

@Entity
public class Gll extends PersistentObject{
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

    public JsonGll toJson(){
        JsonGll jg = new JsonGll();
        jg.setVin(this.getVin());
        jg.setTimestamp(this.getTimestamp());
        jg.setLatitude(this.getLatitude());
        jg.setLongitude(this.getLongitude());
        jg.setAltitude(this.getAltitude());
        return jg;
    }
}


