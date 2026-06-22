package com.campus.repository;

import com.campus.model.*;

import java.util.ArrayList;

public class DataManager {
    private static final String SYSTEM_FILE_LOCATION = "data/system_state.dat";

    private CampusRepository<Student> studentRepo;
    private CampusRepository<Course> courseRepo;
    private CampusRepository<Department> departmentRepo;
    private CampusRepository<Library> libraryRepo;
    private CampusRepository<Cafeteria> cafeteriaRepo;
    private CampusRepository<Hostel> hostelRepo;
    private CampusRepository<HealthCenter> healthCenterRepo;
    private CampusRepository<SecurityService> securityRepo;
    private CampusRepository<TransportService> transportRepo;
    private CampusRepository<CampusZone> zoneRepo;
    private CampusRepository<User> userRepo;
    private CampusRepository<Lab> labRepo;
    private CampusRepository<Classroom> classroomRepo;


    public DataManager() {
        studentRepo = new CampusRepository<>("Student Repository");
        courseRepo = new CampusRepository<>("Course Repository");
        departmentRepo = new CampusRepository<>("Department Repository");
        libraryRepo = new CampusRepository<>("Library Repository");
        cafeteriaRepo = new CampusRepository<>("Cafeteria Repository");
        hostelRepo = new CampusRepository<>("Hostel Repository");
        healthCenterRepo = new CampusRepository<>("Health Center Repository");
        securityRepo = new CampusRepository<>("Security Repository");
        transportRepo = new CampusRepository<>("Transport Repository");
        zoneRepo = new CampusRepository<>("Campus Zone Repository");
        userRepo = new CampusRepository<>("Users Repository");
        labRepo = new CampusRepository<>("Lab Repository");
        classroomRepo = new CampusRepository<>("Classroom Repository");
    }

    public void saveAllData(){
        ArrayList<Object> allData = new ArrayList<>();
        allData.add(0, studentRepo);
        allData.add(1, courseRepo);
        allData.add(2, departmentRepo);
        allData.add(3, libraryRepo);
        allData.add(4, cafeteriaRepo);
        allData.add(5, hostelRepo);
        allData.add(6, healthCenterRepo);
        allData.add(7, securityRepo);
        allData.add(8, transportRepo);
        allData.add(9, zoneRepo);
        allData.add(10, userRepo);
        allData.add(11, labRepo);
        allData.add(12, classroomRepo);

        FileManagement.saveToFile(SYSTEM_FILE_LOCATION, allData);
    }

    public void loadAllData(){
        Object temp = FileManagement.loadFromFile(SYSTEM_FILE_LOCATION);

        if (temp instanceof ArrayList) {
            ArrayList<Object> allData = (ArrayList<Object>) temp;

            if (allData.size() > 0 && allData.get(0) instanceof CampusRepository) {
                studentRepo = (CampusRepository<Student>) allData.get(0);
                Student.setStudentCount(studentRepo.getSize());
            }

            if (allData.size() > 1 && allData.get(1) instanceof CampusRepository) {
                courseRepo = (CampusRepository<Course>) allData.get(1);
                Course.setCourseCount(courseRepo.getSize());
            }

            if (allData.size() > 2 && allData.get(2) instanceof CampusRepository) {
                departmentRepo = (CampusRepository<Department>) allData.get(2);
            }

            if (allData.size() > 3 && allData.get(3) instanceof CampusRepository) {
                libraryRepo = (CampusRepository<Library>) allData.get(3);
            }

            if (allData.size() > 4 && allData.get(4) instanceof CampusRepository) {
                cafeteriaRepo = (CampusRepository<Cafeteria>) allData.get(4);
            }

            if (allData.size() > 5 && allData.get(5) instanceof CampusRepository) {
                hostelRepo = (CampusRepository<Hostel>) allData.get(5);
            }

            if (allData.size() > 6 && allData.get(6) instanceof CampusRepository) {
                healthCenterRepo = (CampusRepository<HealthCenter>) allData.get(6);
            }

            if (allData.size() > 7 && allData.get(7) instanceof CampusRepository) {
                securityRepo = (CampusRepository<SecurityService>) allData.get(7);
            }

            if (allData.size() > 8 && allData.get(8) instanceof CampusRepository) {
                transportRepo = (CampusRepository<TransportService>) allData.get(8);
            }

            if (allData.size() > 9 && allData.get(9) instanceof CampusRepository) {
                zoneRepo = (CampusRepository<CampusZone>) allData.get(9);
            }

            if (allData.size() > 10 && allData.get(10) instanceof CampusRepository) {
                userRepo = (CampusRepository<User>) allData.get(10);
            }

            if (allData.size() > 11 && allData.get(11) instanceof CampusRepository) {
                labRepo = (CampusRepository<Lab>) allData.get(11);
            }

            if (allData.size() > 12 && allData.get(12) instanceof CampusRepository) {
                classroomRepo = (CampusRepository<Classroom>) allData.get(12);
            }

            Facility.setFacilityUsageCount(libraryRepo.getSize() + cafeteriaRepo.getSize() + hostelRepo.getSize());

            System.out.println("Data loaded successfully.");
        } else {
            System.out.println("No valid system state found. Starting fresh.");
        }
    }

    public CampusRepository<Student> getStudentRepo() {
        return studentRepo;
    }

    public CampusRepository<Course> getCourseRepo() {
        return courseRepo;
    }

    public CampusRepository<Department> getDepartmentRepo() {
        return departmentRepo;
    }

    public CampusRepository<Library> getLibraryRepo() {
        return libraryRepo;
    }

    public CampusRepository<Cafeteria> getCafeteriaRepo() {
        return cafeteriaRepo;
    }

    public CampusRepository<Hostel> getHostelRepo() {
        return hostelRepo;
    }

    public CampusRepository<HealthCenter> getHealthCenterRepo() {
        return healthCenterRepo;
    }

    public CampusRepository<SecurityService> getSecurityRepo() {
        return securityRepo;
    }

    public CampusRepository<TransportService> getTransportRepo() {
        return transportRepo;
    }

    public CampusRepository<CampusZone> getZoneRepo() {
        return zoneRepo;
    }

    public CampusRepository<User> getUserRepo() {
        return userRepo;
    }

    public CampusRepository<Lab> getLabRepo() {
        return labRepo;
    }

    public CampusRepository<Classroom> getClassroomRepo() {
        return classroomRepo;
    }

    public void seedDefaultUsers() {
        if (userRepo.getSize() == 0) {
            userRepo.addItem(new AdminUser("admin", "admin123", "System Admin"));
            userRepo.addItem(new TeacherUser("teacher1", "pass123", "Dr. Tariq", new ArrayList<Course>()));
            // For StudentUser, link to an existing Student if available
        }
    }

}
