package com.campus.model;

import java.util.ArrayList;

public class Cafeteria extends Facility{
    private static final long serialVersionUID = 1L;
    private ArrayList<String> menuItems;
    private int seatingCapacity;
    private int staffCount;
    private int salaryPerStaff;

    public Cafeteria(){
        super();
        this.menuItems = new ArrayList<>();
    }

    public Cafeteria(String entityID, String name, String location, boolean isBusy, double maintenanceCost, int usageFrequency, String operatingHours, boolean isOpen, ArrayList<String> menuItems, int seatingCapacity, int staffCount, int salaryPerStaff) {
        super(entityID, name, location, isBusy, maintenanceCost, usageFrequency, operatingHours, isOpen);
        setMenuItems(menuItems);
        setSeatingCapacity(seatingCapacity);
        setStaffCount(staffCount);
        setSalaryPerStaff(salaryPerStaff);
    }

    @Override
    public double calculateOperationalCost() {
        return ((getMaintenanceCost()*getUsageFrequency()) + (getStaffCount()* getSalaryPerStaff()));
    }

    public String addMenuItem(String menuItem){
        if (menuItem == null || menuItem.isEmpty()){
            return String.format("Menu Item is Empty%n");
        } else if (menuItems.contains(menuItem)){
            return String.format("Menu Item already exists%n");
        }
        menuItems.add(menuItem);
        return String.format("Menu Item added successfully%n");
    }

    public String removeMenuItem(String menuItem){
        if (menuItem == null || menuItem.isEmpty()) {
            return String.format("Menu Item is Empty%n");
        }

        if (menuItems.contains(menuItem)){
            menuItems.remove(menuItem);
            return String.format("Menu Item removed successfully%n");
        } else {
            return String.format("Menu Item does not exist%n");
        }
    }

    public void setMenuItems(ArrayList<String> menuItems) {
        if (menuItems == null){
            this.menuItems = new ArrayList<>();
            System.out.println("Menu Items cannot be null");
        } else {
            this.menuItems = new ArrayList<>(menuItems);
        }
    }

    public void setSeatingCapacity(int seatingCapacity) {
        if  (seatingCapacity > 0){
            this.seatingCapacity = seatingCapacity;
        } else {
            this.seatingCapacity = 0;
            System.out.println("Invalid Seating Capacity");
        }
    }

    public void setStaffCount(int staffCount) {
        if (staffCount > 0){
            this.staffCount = staffCount;
        } else {
            this.staffCount = 0;
            System.out.println("Invalid Staff Count");
        }
    }

    public void setSalaryPerStaff(int salaryPerStaff) {
        if (salaryPerStaff > 0){
            this.salaryPerStaff = salaryPerStaff;
        } else {
            this.salaryPerStaff = 0;
            System.out.println("Invalid Salary Per Staff");
        }
    }

    public ArrayList<String> getMenuItems() {
        return new ArrayList<>(menuItems);
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public int getSalaryPerStaff() {
        return salaryPerStaff;
    }

    public void displayMenu(){
        System.out.println("----- MENU -----");
        for (String menuItem : menuItems){
            System.out.println(menuItem);
        }
    }

    public String toString(){
        return super.toString() + String.format("Total Menu Items: %d\nSeating Capacity: %d\nNumber of Staff: %d\nSalary Per-Staff: %d\n",menuItems.size(), getSeatingCapacity(), getStaffCount(), getSalaryPerStaff());
    }
}
