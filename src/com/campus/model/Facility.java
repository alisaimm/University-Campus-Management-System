package com.campus.model;

public abstract class Facility extends CampusEntity{
    private static final long serialVersionUID = 1L;
    private static int totalFacilityUsage;
    private double maintenanceCost;
    private int usageFrequency;
    private String operatingHours;
    private boolean isOpen;

    public Facility(){
        totalFacilityUsage++;
    }

    public Facility(String entityId, String name, String location, boolean isBusy, double maintenanceCost, int usageFrequency, String operatingHours, boolean isOpen){
        super(entityId, name, location, isBusy);
        setMaintenanceCost(maintenanceCost);
        setUsageFrequency(usageFrequency);
        setOperatingHours(operatingHours);
        setIsOpen(isOpen);
        totalFacilityUsage++;
    }

    public static void setFacilityUsageCount(int count){
        totalFacilityUsage = count;
    }

    @Override
    public abstract double calculateOperationalCost();

    public void setMaintenanceCost(double maintenanceCost){
        if (maintenanceCost > 0){
            this.maintenanceCost = maintenanceCost;
        } else {
            System.out.println("Maintenance Cost not valid");
            this.maintenanceCost = 0;
        }
    }

    public void setUsageFrequency(int usageFrequency){
        if (usageFrequency > 0){
            this.usageFrequency = usageFrequency;
        } else{
            System.out.println("Usage Frequency not valid");
            this.usageFrequency = 0;
        }
    }

    public void setOperatingHours(String operatingHours){
        if (operatingHours != null && !operatingHours.isEmpty()){
            this.operatingHours = operatingHours;
        } else {
            System.out.println("Operating Hours not valid");
        }
    }

    public static int getTotalFacilityUsage(){
        return totalFacilityUsage;
    }

    public void setIsOpen(boolean isOpen){
        this.isOpen = isOpen;
    }

    public double getMaintenanceCost(){
        return maintenanceCost;
    }

    public int getUsageFrequency(){
        return usageFrequency;
    }

    public String getOperatingHours(){
        return operatingHours;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public String toString(){
        return super.toString() + String.format("Maintenance Cost: %.2f\nUsage Frequency: %d\nOperational Hours: %s\nOpen Status: %b\n", maintenanceCost, usageFrequency, operatingHours, isOpen);
    }
}
