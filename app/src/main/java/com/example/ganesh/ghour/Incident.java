package com.example.ganesh.ghour;

/**
 * Created by Ganesh on 03-Oct-17.
 */

public class Incident {
    private String details;
    private String name;


    public Incident() {

    }

    public Incident(String details, String name) {
        this.details = details;
        this.name = name;
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
}
