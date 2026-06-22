package com.campus.model;

import com.campus.interfaces.Notifiable;

import java.util.ArrayList;

public class SecurityService extends ServiceUnit implements Notifiable {
    private static final long serialVersionUID = 1L;
    private int numberOfSecurityGuards;
    private String shift;
    private String securityLevel;
    private ArrayList<Notifiable> emergencyContacts;

    public SecurityService(){
        emergencyContacts = new ArrayList<>();
    }

    public SecurityService (String entityID, String name, String location, boolean isBusy, int staffCount, String businessHours, double hoursPerDay, double costPerHour, boolean isOperational, int numberOfSecurityGuards, String shift, String securityLevel, ArrayList<Notifiable> emergencyContacts){
        super(entityID, name, location, isBusy, staffCount, businessHours, hoursPerDay, costPerHour, isOperational);
        setNumberOfSecurityGuards(numberOfSecurityGuards);
        setShift(shift);
        setSecurityLevel(securityLevel);
        setEmergencyContacts(emergencyContacts);
    }

    public void handleMedicalEmergency(String location, String emergencyDescription){
        if (location == null || location.isEmpty()){
            System.out.println("Location cannot be empty.");
            return;
        }

        if (emergencyDescription == null || emergencyDescription.isEmpty()){
            System.out.println("Emergency description cannot be empty.");
            return;
        }

        String message = String.format("Medical emergency at: %s%nDescription: %s%n", location, emergencyDescription);

        for (Notifiable contact : emergencyContacts){
            contact.sendNotification(message);
        }

        System.out.println("Notification sent to all emergency contacts.");
    }

    @Override
    public double calculateOperationalCost() {
        double costPerSecurityGuard = 120.0;
        return (costPerSecurityGuard * numberOfSecurityGuards) + calculateStaffCost();
    }

    @Override
    public void sendNotification(String message) {
        System.out.println("SECURITY ALERT ~ " + message);
    }

    public void setNumberOfSecurityGuards(int numberOfSecurityGuards) {
        if (numberOfSecurityGuards > 0){
            this.numberOfSecurityGuards = numberOfSecurityGuards;
        } else {
            System.out.println("Number of security guards cannot be zero or negative.");
        }
    }

    public void setShift(String shift) {
        if (shift != null && !shift.isEmpty()){
            if (shift.equalsIgnoreCase("Morning") || shift.equalsIgnoreCase("Afternoon") || shift.equalsIgnoreCase("Evening") || shift.equalsIgnoreCase("Night")){
                this.shift = shift;
            } else {
                System.out.println("Invalid shift. Shifts: Morning, Afternoon, Evening or Night.");
            }
        } else {
            System.out.println("Shift cannot be empty.");
        }
    }

    public void setSecurityLevel(String securityLevel) {
        if (securityLevel != null && !securityLevel.isEmpty()){
            if (securityLevel.equalsIgnoreCase("Low") || securityLevel.equalsIgnoreCase("Medium") || securityLevel.equalsIgnoreCase("High")){
                this.securityLevel = securityLevel;
            } else {
                System.out.println("Invalid security level. Levels: Low, Medium or High.");
            }
        } else {
            System.out.println("Security level cannot be empty.");
        }
    }

    public void setEmergencyContacts(ArrayList<Notifiable> emergencyContacts) {
        if (emergencyContacts == null){
            System.out.println("Emergency Contacts cannot be null. Set again.");
            this.emergencyContacts = new ArrayList<>();
            return;
        }

        this.emergencyContacts = new ArrayList<>(emergencyContacts);
    }

    public String addEmergencyContact(Notifiable emergencyContact){
        if (emergencyContact == null){
            return String.format("Emergency Contact cannot be null. Add again.%n");
        }

        if (emergencyContacts.contains(emergencyContact)){
            return String.format("This emergency contact is already registered.%n");
        }

        emergencyContacts.add(emergencyContact);
        return String.format("Emergency Contact added successfully.%n");
    }

    public int getNumberOfSecurityGuards() {
        return numberOfSecurityGuards;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public String getShift() {
        return shift;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Entity Type: Security Service%nNumber of Security Guards: %d%nShift: %s%nSecurity Level: %s%n", numberOfSecurityGuards, shift, securityLevel);
    }
}
