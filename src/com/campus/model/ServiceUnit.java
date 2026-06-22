package com.campus.model;

public abstract class ServiceUnit extends CampusEntity {
    private static final long serialVersionUID = 1L;
    private int staffCount;
    private String businessHours;
    private double hoursPerDay;
    private double costPerHour;
    private boolean isOperational;

    public ServiceUnit(){

    }

    public ServiceUnit(String entityID, String name, String location, boolean isBusy, int staffCount, String businessHours, double hoursPerDay, double costPerHour, boolean isOperational){
        super(entityID, name, location, isBusy);
        setStaffCount(staffCount);
        setBusinessHours(businessHours);
        setHoursPerDay(hoursPerDay);
        setCostPerHour(costPerHour);
        setIsOperational(isOperational);
    }

    @Override
    public abstract double calculateOperationalCost();

    public double calculateStaffCost(){
        return staffCount * costPerHour * hoursPerDay;
    }

    public void setStaffCount(int staffCount) {
        if (staffCount > 0){
            this.staffCount = staffCount;
        } else {
            System.out.println("Staff count cannot negative or zero.");
        }
    }

    public void setBusinessHours(String businessHours) {
        if (businessHours != null && !businessHours.isEmpty()){
            this.businessHours = businessHours;
        } else {
            System.out.println("Business hours cannot be empty.");
        }
    }

    public void setHoursPerDay(double hoursPerDay) {
        if (hoursPerDay > 0 && hoursPerDay <= 24){
            this.hoursPerDay = hoursPerDay;
        } else {
            System.out.println("Open Hours per day must be within range of more than 0 to 24 hours.");
        }
    }

    public void setCostPerHour(double costPerHour) {
        if (costPerHour > 0){
            this.costPerHour = costPerHour;
        } else {
            System.out.println("Cost per hour cannot be negative or zero.");
        }
    }

    public void setIsOperational(boolean isOperational) {
        this.isOperational = isOperational;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public double getCostPerHour() {
        return costPerHour;
    }

    public double getHoursPerDay() {
        return hoursPerDay;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public boolean isOperational() {
        return isOperational;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Staff Count: %d%nBusiness Hours: %s%nHours Per Day: %.2f%nCost Per Hour: %.2f%nOperational Status: %b%n", staffCount, businessHours, hoursPerDay, costPerHour, isOperational);
    }
}
