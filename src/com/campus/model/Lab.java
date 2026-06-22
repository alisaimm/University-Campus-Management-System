package com.campus.model;

public class Lab extends AcademicUnit {
    private static final long serialVersionUID = 1L;
    private String labType;
    private String technicianName;
    private int numberOfEquipment;

    public Lab(){

    }

    public Lab(String entityID, String name, String location, boolean isBusy, int capacity, String headName, boolean isActive, String labType, String technicianName, int numberOfEquipment){
        super(entityID, name, location, isBusy, capacity, headName, isActive);
        setLabType(labType);
        setTechnicianName(technicianName);
        setNumberOfEquipment(numberOfEquipment);
    }

    @Override
    public double calculateOperationalCost() {
        double costPerCapacity = 75.0;
        double costPerEquipment = 250.0;

        return (costPerCapacity * getCapacity()) + (costPerEquipment * getNumberOfEquipment());
    }

    public void setLabType(String labType) {
        if (labType != null && !labType.isEmpty()) {
            this.labType = labType;
        } else {
            System.out.println("Lab type cannot be empty.");
        }
    }

    public void setTechnicianName(String technicianName) {
        if (technicianName != null && !technicianName.isEmpty()) {
            this.technicianName = technicianName;
        } else {
            System.out.println("Technician name cannot be empty.");
        }
    }

    public void setNumberOfEquipment(int numberOfEquipment) {
        if (numberOfEquipment > 0) {
            this.numberOfEquipment = numberOfEquipment;
        } else {
            System.out.println("Number of equipment cannot be zero or negative.");
        }
    }

    public String getLabType() {
        return labType;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public int getNumberOfEquipment() {
        return numberOfEquipment;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Entity Type: Lab%nLab Type: %s%nTechnician Name: %s%nNumber of Equipment: %d%n", labType, technicianName, numberOfEquipment);
    }
}
