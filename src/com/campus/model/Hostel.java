package com.campus.model;

import java.util.ArrayList;

public class Hostel extends Facility{
    private static final long serialVersionUID = 1L;
    private int totalRooms;
    private int occupiedRooms;
    private String hostelType;
    private double rentPerRoom;
    private ArrayList<Student> students;

    public Hostel(){
        super();
        this.students = new ArrayList<>();
    }

    public Hostel(String entityID, String name, String location, boolean isBusy, double maintenanceCost, int usageFrequency, String operatingHours, boolean isOpen, int totalRooms, int occupiedRooms,String hostelType, double rentPerRoom, ArrayList<Student> students){
        super(entityID, name, location, isBusy, maintenanceCost, usageFrequency, operatingHours, isOpen);
        setTotalRooms(totalRooms);
        setOccupiedRooms(occupiedRooms);
        setHostelType(hostelType);
        setRentPerRoom(rentPerRoom);
        setStudents(students);
    }

    @Override
    public double calculateOperationalCost() {
        double costPerStudent = 5000;
        return ((getMaintenanceCost()* getUsageFrequency()) + (students.size()*costPerStudent));
    }

    public String assignRoom(Student s){
        if (s == null){
            return String.format("Invalid student%n");
        }

        if (occupiedRooms == totalRooms){
            return String.format("All rooms are occupied%n");
        }

        if (students.contains(s)){
            return String.format("Student already residing in the hostel%n");
        }

        students.add(s);
        occupiedRooms++;
        return String.format("Room assigned to student %s%n", s.getName());
    }



    public String unassignRoom(Student s){
        if (s == null){
            return String.format("Invalid student%n");
        }

        if (occupiedRooms == 0){
            return String.format("No room assigned yet%n");
        }

        if (students.contains(s)){
            students.remove(s);
            occupiedRooms--;
            return String.format("Room unassigned from student %s%n", s.getName());
        } else {
            return String.format("Student not residing in the hostel%n");
        }

    }

    public void setTotalRooms(int totalRooms) {
        if (totalRooms >= 0){
            this.totalRooms = totalRooms;
        } else {
            this.totalRooms = 0;
            System.out.println("Invalid Rooms Count");
        }
    }

    public void setOccupiedRooms(int occupiedRooms) {
        if (occupiedRooms >= 0 && occupiedRooms <= totalRooms){
            this.occupiedRooms = occupiedRooms;
        } else {
            this.occupiedRooms = 0;
            System.out.println("Invalid Rooms Count");
        }
    }

    public void setHostelType(String hostelType) {
        if (hostelType != null && !hostelType.isEmpty()){
            this.hostelType = hostelType;
        } else  {
            System.out.println("Invalid Hostel Type");
        }
    }

    public void setRentPerRoom(double rentPerRoom) {
        if (rentPerRoom >= 0){
            this.rentPerRoom = rentPerRoom;
        } else {
            this.rentPerRoom = 0;
            System.out.println("Invalid Room Rent");
        }
    }

    public void setStudents(ArrayList<Student> students) {
        if(students != null){
            this.students = new ArrayList<>(students);
        }else{
            System.out.println("Invalid Student List");
            this.students = new ArrayList<>();
        }
    }

    public int getTotalRooms(){
        return totalRooms;
    }

    public int getOccupiedRooms(){
        return occupiedRooms;
    }

    public String getHostelType(){
        return hostelType;
    }

    public double getRentPerRoom(){
        return rentPerRoom;
    }

    public int getAvailableRooms(){
        return totalRooms - occupiedRooms;
    }

    public ArrayList<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public String toString(){
        return super.toString() + String.format("Total Rooms in hostel: %d\nOccupied Rooms: %d\nAvailable Rooms: %d\nHostel Type: %s\nRent Per Room: %.2f\n", getTotalRooms(), getOccupiedRooms(),getAvailableRooms(), getHostelType(), getRentPerRoom());
    }
}
