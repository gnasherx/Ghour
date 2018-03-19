package com.example.ganesh.ghour;

/**
 * Created by Ganesh on 03-Oct-17.
 */

public class Incident {
    private String details;
    private String name;
    private String image;
    private String placeId;

    public Incident() {

    }

    public Incident(String details, String name, String image, String placeId) {
        this.details = details;
        this.name = name;
        this.image = image;
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String description) {
        this.details = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
