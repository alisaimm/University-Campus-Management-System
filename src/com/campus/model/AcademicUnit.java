package com.campus.model;

public abstract class AcademicUnit extends CampusEntity {
    private static final long serialVersionUID = 1L;
    private int capacity;
    private String headName;
    private boolean isActive;

    public AcademicUnit(){

    }

    public AcademicUnit(String entityID, String name, String location, boolean isBusy, int capacity, String headName, boolean isActive){
        super(entityID, name, location, isBusy);
        setCapacity(capacity);
        setHeadName(headName);
        setActive(isActive);
    }

    @Override
    public abstract double calculateOperationalCost();

    public void setCapacity(int capacity) {
        if (capacity > 0){
            this.capacity = capacity;
        } else {
            System.out.println("Capacity cannot be zero or negative.");
        }
    }

    public void setHeadName(String headName) {
        if (headName != null && !headName.isEmpty()){
            this.headName = headName;
        } else {
            System.out.println("Head Name cannot be empty.");
        }
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public String getHeadName() {
        return headName;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Entity Type: Academic Unit%nCapacity: %d%nHead Name: %s%nActive Status: %b%n", capacity, headName, isActive);
    }
}
