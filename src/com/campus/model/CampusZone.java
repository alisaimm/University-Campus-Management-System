package com.campus.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CampusZone implements Serializable {
    private static final long serialVersionUID = 1L;
    private String zoneName;
    private String zoneId;
    private String zoneLocation;
    private ArrayList<Facility> facilities;
    private ArrayList<ServiceUnit> serviceUnits;

    public CampusZone(){
        this.facilities = new ArrayList<>();
        this.serviceUnits = new ArrayList<>();
    }

    public CampusZone(String zoneName, String zoneId, String zoneLocation, ArrayList<Facility> facilities, ArrayList<ServiceUnit> serviceUnits){
        setZoneName(zoneName);
        setZoneId(zoneId);
        setZoneLocation(zoneLocation);
        setFacilities(facilities);
        setServiceUnits(serviceUnits);
    }

    public String displayZoneInfo() {
        String result = String.format("=====ZONE INFO=====%n" +
                "Zone: %s%n" +
                "Zone ID: %s%n" +
                "Location: %s%n" +
                "---------------------%n" +
                "=====FACILITIES=====%n", zoneName, zoneId, zoneLocation);

        for (Facility f : facilities) {
            result += String.format("  > %s Status: %s%n", f.getName(), (f.isOpen() ? "Open" : "Closed"));
        }

        result += String.format("=====SERVICE UNITS=====%n");

        for (ServiceUnit s : serviceUnits) {
            result += String.format("  > %s Status: %s%n", s.getName(), (s.isOperational() ? "Operational" : "Closed"));
        }
        result += String.format("=====================%n");
        
        return result;
    }

    public String addFacility(Facility facility){
        if (facility == null){
            return String.format("Facility cannot be null. Set again.%n");
        }
        if (facilities.contains(facility)){
            return String.format("This facility already exists.%n");
        }
        facilities.add(facility);
        return String.format("Facility added successfully.%n");
    }

    public String removeFacility(Facility facility){
        if (facility == null){
            return String.format("Facility cannot be null. Remove again.%n");
        }

        if (facilities.contains(facility)){
            facilities.remove(facility);
            return String.format("Facility removed successfully.%n");
        } else {
            return String.format("Facility does not exist in database.%n");
        }
    }

    public String addServiceUnit(ServiceUnit serviceUnit){
        if (serviceUnit == null){
            return String.format("Service Unit cannot be null. Set again.%n");
        }

        if (serviceUnits.contains(serviceUnit)){
            return String.format("This service unit already exists.%n");
        }

        serviceUnits.add(serviceUnit);
        return String.format("Service Unit added successfully.%n");
    }

    public String removeServiceUnit(ServiceUnit serviceUnit){
        if (serviceUnit == null){
            return String.format("Service unit cannot be null.%n");
        }

        if (serviceUnits.contains(serviceUnit)){
            serviceUnits.remove(serviceUnit);
            return String.format("Service unit removed successfully.%n");
        } else {
            return String.format("Service unit does not exist in database.%n");
        }
    }

    public void setZoneId(String zoneId) {
        if (zoneId != null && !zoneId.isEmpty()){
            this.zoneId = zoneId;
        } else {
            System.out.println("Zone ID cannot be empty.");
        }
    }

    public void setZoneName(String zoneName) {
        if (zoneName != null && !zoneName.isEmpty()){
            this.zoneName = zoneName;
        } else {
            System.out.println("Zone name cannot be empty.");
        }
    }

    public void setZoneLocation(String zoneLocation) {
        if (zoneLocation != null && !zoneLocation.isEmpty()){
            this.zoneLocation = zoneLocation;
        } else {
            System.out.println("Zone location cannot be empty.");
        }
    }

    public void setFacilities(ArrayList<Facility> facilities) {
        if (facilities == null){
            System.out.println("Facilities cannot be null. Set again.");
            this.facilities = new ArrayList<>();
            return;
        }

        this.facilities = facilities;
    }

    public void setServiceUnits(ArrayList<ServiceUnit> serviceUnits) {
        if (serviceUnits == null){
            System.out.println("Service Units cannot be null. Set again.");
            this.serviceUnits = new ArrayList<>();
            return;
        }

        this.serviceUnits = serviceUnits;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String getZoneLocation() {
        return zoneLocation;
    }

    public String getZoneName() {
        return zoneName;
    }

    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    public ArrayList<ServiceUnit> getServiceUnits() {
        return serviceUnits;
    }

    @Override
    public String toString() {
        return String.format("Zone Name: %s%nZone ID: %s%nZone Location: %s%nTotal Facilities: %d%nTotal Service Units: %d%n", zoneName, zoneId, zoneLocation, facilities.size(), serviceUnits.size());
    }
}
