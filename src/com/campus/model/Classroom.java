package com.campus.model;

public class Classroom extends AcademicUnit{
    private static final long serialVersionUID = 1L;
    private String roomType;
    private boolean hasProjector;
    private boolean isAvailable;

    public Classroom(){

    }

    public Classroom(String entityID, String name, String location, boolean isBusy, int capacity, String headName, boolean isActive, String roomType, boolean hasProjector, boolean isAvailable){
        super(entityID, name, location, isBusy, capacity, headName, isActive);
        setRoomType(roomType);
        setHasProjector(hasProjector);
        setAvailable(isAvailable);
    }

    @Override
    public double calculateOperationalCost(){
        double costPerCapacity = 50.0;
        double projectorCost = 1000.0;

        if (hasProjector){
            return (costPerCapacity * getCapacity()) + projectorCost;
        } else {
            return costPerCapacity * getCapacity();
        }

    }

    public void setRoomType(String roomType) {
        if (roomType != null && !roomType.isEmpty()){
            this.roomType = roomType;
        } else {
            System.out.println("Room type cannot be empty.");
        }
    }

    public void setHasProjector(boolean hasProjector) {
        this.hasProjector = hasProjector;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean hasProjector() {
        return hasProjector;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Room Type: %s%nHas Projector: %b%nAvailablity: %b%n", roomType, hasProjector, isAvailable);
    }
}
