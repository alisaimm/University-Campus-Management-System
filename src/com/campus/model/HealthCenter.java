package com.campus.model;

import com.campus.interfaces.Notifiable;

import java.util.ArrayList;

public class HealthCenter extends ServiceUnit implements Notifiable {
    private static final long serialVersionUID = 1L;
    private int noOfDoctors;
    private int noOfBeds;
    private int occupiedBeds;
    private double medicineBudget;
    private ArrayList<String> availableMedicines;

    public HealthCenter(){
        availableMedicines = new ArrayList<>();
    }

    public HealthCenter(String entityID, String name, String location, boolean isBusy, int staffCount, String businessHours, double hoursPerDay, double costPerHour, boolean isOperational, int noOfDoctors, int noOfBeds, int occupiedBeds, double medicineBudget, ArrayList<String> availableMedicines){
        super(entityID, name, location, isBusy, staffCount, businessHours, hoursPerDay, costPerHour, isOperational);
        setNoOfDoctors(noOfDoctors);
        setNoOfBeds(noOfBeds);
        setOccupiedBeds(occupiedBeds);
        setMedicineBudget(medicineBudget);
        setAvailableMedicines(availableMedicines);
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("HEALTH CENTER ALERT ~ " + message);
    }

    public String addAvailableMedicine(String medicineName){
        if (medicineName != null && !medicineName.isEmpty()){
            availableMedicines.add(medicineName);
            return String.format("Medicine Added Successfully.%n");
        } else {
            return String.format("Medicine name cannot be empty.%n");
        }
    }

    public void setNoOfDoctors(int noOfDoctors) {
        if (noOfDoctors > 0){
            this.noOfDoctors = noOfDoctors;
        } else {
            System.out.println("Doctors cannot be zero or negative.");
        }
    }

    public void setMedicineBudget(double medicineBudget) {
        if (medicineBudget > 0){
            this.medicineBudget = medicineBudget;
        } else {
            System.out.println("Medicine budget cannot be zero or negative.");
        }
    }

    public void setNoOfBeds(int noOfBeds) {
        if (noOfBeds > 0){
            this.noOfBeds = noOfBeds;
        } else {
            System.out.println("Beds cannot be zero or negative.");
        }
    }

    public void setOccupiedBeds(int occupiedBeds) {
        if (occupiedBeds >= 0 && occupiedBeds <= noOfBeds){
            this.occupiedBeds = occupiedBeds;
        } else {
            System.out.println("Occupied beds cannot be negative or more than total beds.");
        }
    }

    public void setAvailableMedicines(ArrayList<String> availableMedicines) {
        if (availableMedicines == null){
            System.out.println("Available medicines cannot be null. Set again.");
            this.availableMedicines = new ArrayList<>();
            return;
        }

        this.availableMedicines = availableMedicines;
    }

    public int getNoOfDoctors() {
        return noOfDoctors;
    }

    public int getNoOfBeds() {
        return noOfBeds;
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public ArrayList<String> getAvailableMedicines() {
        return availableMedicines;
    }

    public int getAvailableBeds(){
        return (noOfBeds - occupiedBeds);
    }

    @Override
    public double calculateOperationalCost() {
        return calculateStaffCost() + (noOfDoctors * 1000) + (occupiedBeds * 500) + ((noOfBeds - occupiedBeds) * 200) + medicineBudget;
    }

    public double getMedicineBudget() {
        return medicineBudget;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Number of Doctors: %d%nNumber of Beds: %d%nOccupied Beds: %d%nMedicine Budget: %.2f%nNo of Available Medicines: %d%n", noOfDoctors, noOfBeds, occupiedBeds, medicineBudget, availableMedicines.size());
    }

    public void displayAvailableMedicines(){
        System.out.println("---------AVAILABLE MEDICINES---------");
        for (String medicine : availableMedicines){
            System.out.println("Medicine: " + medicine);
        }
        System.out.println("---------------------------");
    }
}