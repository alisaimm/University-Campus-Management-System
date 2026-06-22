package com.campus.model;

import java.io.Serializable;

public abstract class CampusEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String entityID;
    private String name;
    private String location;
    private boolean isBusy;

    public CampusEntity(){

    }

    public CampusEntity(String entityID, String name, String location, boolean isBusy){
        setEntityID(entityID);
        setName(name);
        setLocation(location);
        setIsBusy(isBusy);
    }

    public abstract double calculateOperationalCost();

    public void setName(String name) {
        if (name != null && !name.isEmpty()){
            this.name = name;
        } else {
            System.out.println("Name cannot be empty.");
        }
    }

    public void setEntityID(String entityID) {
        if (entityID != null && !entityID.isEmpty()){
            this.entityID = entityID;
        } else {
            System.out.println("Entity ID cannot be empty.");
        }
    }

    public void setLocation(String location) {
        if (location != null && !location.isEmpty()){
            this.location = location;
        } else {
            System.out.println("Location cannot be empty.");
        }
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public String getEntityID() {
        return entityID;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public boolean getIsBusy() {
        return isBusy;
    }

    @Override
    public String toString() {
        return String.format("Entity ID: %s%nEntity Name: %s%nEntity Location: %s%nBusy: %b%n", entityID, name, location, isBusy);
    }
}
