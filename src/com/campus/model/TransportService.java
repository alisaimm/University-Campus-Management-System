package com.campus.model;

import com.campus.interfaces.Schedulable;

import java.util.ArrayList;

public class TransportService extends ServiceUnit implements Schedulable {
    private static final long serialVersionUID = 1L;
    private int numberOfVehicles;
    private int driverCount;
    private boolean expressRoutesActive = false;
    private ArrayList<String> transportRoutes;
    private ArrayList<String> expressRoutes;

    public TransportService(){
        transportRoutes = new ArrayList<>();

        expressRoutes = new ArrayList<>();
        expressRoutes.add("Express Route - Comsats to Saddar");
        expressRoutes.add("Express Route - Comsats to PWD");
    }

    public TransportService(String entityID, String name, String location, boolean isBusy, int staffCount, String businessHours, double hoursPerDay, double costPerHour, boolean isOperational, int numberOfVehicles, int driverCount, ArrayList<String> transportRoutes, ArrayList<String> expressRoutes){
        super(entityID, name, location, isBusy, staffCount, businessHours, hoursPerDay, costPerHour, isOperational);
        setNumberOfVehicles(numberOfVehicles);
        setTransportRoutes(transportRoutes);
        setDriverCount(driverCount);
        setExpressRoutes(expressRoutes);
    }

    public String adjustForPeakHours(String timeOfDay) {
        String report = "";
        if (timeOfDay.equalsIgnoreCase("Morning") || timeOfDay.equalsIgnoreCase("Evening") || timeOfDay.equalsIgnoreCase("Afternoon")) {
            report += String.format("PEAK HOURS - High demand at: %s%n", timeOfDay);
            report += String.format("Adding extra express routes...%n");

            for (String expressRoute : expressRoutes){
                if (!transportRoutes.contains(expressRoute)){
                    transportRoutes.add(expressRoute);
                    this.expressRoutesActive = true;
                } else {
                    report += String.format("This express route already exists in active routes.%n");
                }
            }

            report += String.format("Express Routes Added for Peak Hours.%n");
        } else {
            report += String.format("OFF PEAK at: %s%n", timeOfDay);
            report += String.format("Transport service is at standard mode.%n");
            report += removeExpressRoutes();
            this.expressRoutesActive = false;
        }
        return report;
    }

    public String removeExpressRoutes(){
        transportRoutes.removeAll(expressRoutes);
        return String.format("Removed all operational express routes.%n");
    }

    @Override
    public double calculateOperationalCost() {
        double costPerVehicle = 100.0;
        double costPerDriver = 50.0;
        double expressRouteCost = 150.0;

        return (numberOfVehicles * costPerVehicle) + (driverCount * costPerDriver) + calculateStaffCost() + (expressRoutes.size() * expressRouteCost) ;
    }

    @Override
    public String generateSchedule() {
        int numberOfRoutes = transportRoutes.size();
        String report = "";
        report += String.format("====== TRANSPORT SCHEDULE ======%n");
        report += String.format("Service Name: %s%n", getName());
        report += String.format("Operational Hours: %s%n", getBusinessHours());
        report += String.format("Express Routes Status: %b%n", isExpressRoutesActive());
        report += String.format("-------------------------------------%n");
        report += String.format("Morning (6 AM - 8 AM): All Routes Active%n");
        report += String.format("Afternoon (12 PM - 1 PM): Routes Reduced (Only Even Routes Operational): %n");

        report += "[";
        for (int i = 1; i <= numberOfRoutes; i++){
            if (i % 2 == 0){
                report += String.format("%d, ", i);
            }
        }
        report += String.format("]%n");

        report += String.format("Evening (4 PM - 6 PM): All Routes Active%n");
        report += String.format("-------------------------------------%n");

        report += displayAllRoutes();

        report += displayExpressRoutes();

        report += String.format("===================================%n");
        return report;
    }

    public String addTransportRoute(String route) {
        if (route == null || route.isEmpty()){
            return String.format("Transport Route cannot be empty or null.%n");
        }

        if (transportRoutes.size() >= numberOfVehicles){
            return String.format("Cannot add more routes due to less vehicles.%n");
        }

        if (transportRoutes.contains(route)){
            return String.format("This route already exists.%n");
        }

        transportRoutes.add(route);
        return String.format("Route added successfully.%n");
    }

    public String addExpressRoute(String expressRoute){
        if (expressRoute == null || expressRoute.isEmpty()){
            return String.format("Express route cannot be empty.%n");
        }

        if (expressRoutes.contains(expressRoute)){
            return String.format("This express route already exists.%n");
        }

        this.expressRoutes.add(expressRoute);
        return String.format("Express Route Added.%n");
    }

    public void setNumberOfVehicles(int numberOfVehicles) {
        if (numberOfVehicles > 0){
            this.numberOfVehicles = numberOfVehicles;
        } else {
            System.out.println("Number of vehicles cannot be zero or negative.");
        }
    }

    public void setDriverCount(int driverCount) {
        if (driverCount > 0){
            this.driverCount = driverCount;
        } else {
            System.out.println("Number of drivers cannot be zero or negative.");
        }
    }

    public void setExpressRoutesActive(boolean expressRoutesActive) {
        this.expressRoutesActive = expressRoutesActive;
    }

    public void setTransportRoutes(ArrayList<String> transportRoutes) {
        if (transportRoutes == null){
            System.out.println("Routes cannot be null. Set again.");
            this.transportRoutes = new ArrayList<>();
            return;
        }

        if (transportRoutes.size() > numberOfVehicles){
            System.out.println("Routes cannot be greater than number of vehicles. Set again.");
            this.transportRoutes = new ArrayList<>();
            return;
        }

        this.transportRoutes = transportRoutes;
    }

    public void setExpressRoutes(ArrayList<String> expressRoutes) {
        if (expressRoutes == null){
            System.out.println("Express Routes cannot be empty. Setting to default 2 routes of Saddar and PWD.");

            this.expressRoutes = new ArrayList<>();
            this.expressRoutes.add("Express Route - Comsats to Saddar");
            this.expressRoutes.add("Express Route - Comsats to PWD");

            return;
        }

        this.expressRoutes = expressRoutes;
    }

    public int getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public int getDriverCount() {
        return driverCount;
    }

    public boolean isExpressRoutesActive() {
        return expressRoutesActive;
    }

    public ArrayList<String> getTransportRoutes() {
        return transportRoutes;
    }

    public ArrayList<String> getExpressRoutes() {
        return expressRoutes;
    }

    public String displayAllRoutes(){
        int count = 1;
        String report = "";
        report += String.format("------------TRANSPORT ROUTES------------%n");
        for (String route : transportRoutes){
            report += String.format("Route %d: %s%n", count++, route);
        }
        report += String.format("------------------------%n");
        return report;
    }

    public String displayExpressRoutes(){
        String report = "";
        report += String.format("------------EXPRESS ROUTES------------%n");
        for (String expressRoute : expressRoutes){
            report += String.format("Express Route - %s%n", expressRoute);
        }
        report += String.format("---------------------------%n");
        return report;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Entity Type: Transport Service%nNumber of Routes: %d%nExpress Routes: %d%nExpress Route Status: %b%nNumber of Vehicles: %d%nNumber of Drivers: %d%n", transportRoutes.size(), expressRoutes.size(), expressRoutesActive, numberOfVehicles, driverCount);
    }
}
