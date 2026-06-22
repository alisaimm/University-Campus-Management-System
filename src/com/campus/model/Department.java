package com.campus.model;

import com.campus.interfaces.Reportable;

import java.util.ArrayList;

public class Department extends AcademicUnit implements Reportable {
    private static final long serialVersionUID = 1L;
    private String deptCode;
    private ArrayList<Student> students;
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Classroom> classrooms;
    private int numberOfEquipment;
    private int maxCourseCapacity;

    public Department(){
        students = new ArrayList<>();
        courses = new ArrayList<>();
        classrooms = new ArrayList<>();
    }

    public Department(String entityID, String name, String location, boolean isBusy, int capacity, String headName, boolean isActive, String deptCode, ArrayList<Student> students, ArrayList<Course> courses, int numberOfEquipment, int maxCourseCapacity){
        super(entityID, name, location, isBusy, capacity, headName, isActive);
        setDeptCode(deptCode);
        setStudents(students);
        setMaxCourseCapacity(maxCourseCapacity);
        setCourses(courses);
        setNumberOfEquipment(numberOfEquipment);
        this.classrooms = new ArrayList<>();
    }

    public boolean hasConflict (Course course){

        if (course == null){
            System.out.println("Course is null.");
            return false;
        }

        if (course.getAssignedClassroom() == null){
            System.out.println("Classroom is null. Finding any available classroom...");
            resolveConflict(course);
            return false;
        }

        for (int i = 0; i < courses.size(); i++){
            if (courses.get(i) == course){
                continue;
            }

            if (courses.get(i).getAssignedClassroom() == null){
                System.out.println("Null classroom detected in Course: " + courses.get(i).getCourseName());
                continue;
            }

            if (courses.get(i).getSchedule() == null || course.getSchedule() == null) {
                continue;
            }

            if (courses.get(i).getSchedule().equalsIgnoreCase(course.getSchedule()) && courses.get(i).getAssignedClassroom().getName().equalsIgnoreCase(course.getAssignedClassroom().getName())){
                System.out.println("Conflict detected in Course: " + course.getCourseName() + " with Course: " + courses.get(i).getCourseName());
                return true;
            }
        }

        return false;
    }

    public String resolveConflict(Course conflictedCourse){
        String[] availableSlots = {"Mon/Wed 8:00-9:30", "Mon/Wed 9:30-11:00", "Mon/Wed 11:00-12:30", "Tue/Thu 8:00-9:30", "Tue/Thu 9:30-11:00", "Tue/Thu 11:00-12:30"};

        for (String slot : availableSlots){
            boolean slotAvailable = true;

            for (Course existingCourse : courses){
                if (existingCourse.getSchedule().equalsIgnoreCase(slot)){
                    slotAvailable = false;
                    break;
                }
            }

            if (slotAvailable){
                conflictedCourse.setSchedule(slot);
                return String.format("Course rescheduled to: %s%n", conflictedCourse.getSchedule());
            }
        }

        conflictedCourse.setSchedule("N/A");
        return String.format("No free schedule available.%n");
    }

    public String rescheduleClassrooms(){
        String report = "";
        boolean isFree = false;

        for (int i = 0; i < courses.size(); i++){
            isFree = false;

            if (courses.get(i).getAssignedClassroom() != null && !courses.get(i).getAssignedClassroom().isActive()){
                for (int j = 0; j < classrooms.size(); j++){
                    if (classrooms.get(j).isAvailable() && classrooms.get(j).isActive()){
                        isFree = true;
                        courses.get(i).setAssignedClassroom(classrooms.get(j));
                        report += String.format("Course rescheduled to Classroom: %s%n", courses.get(i).getAssignedClassroom().getName());
                        break;
                    }
                }

                if (!isFree){
                    report += String.format("No available classrooms for course: %s%n", courses.get(i).getCourseName());
                }
            }
        }
        if (report.isEmpty()){
            return "No rescheduling needed.\n";
        } else {
            return report;
        }
    }

    @Override
    public String generateReport() {
        return String.format("=====DEPARTMENT REPORT=====%n" +
                " >Department Name: %s%n" +
                " >Department Code: %s%n" +
                " >Location: %s%n" +
                "--------------------------------%n" +
                " >Total Students: %d%n" +
                " >Total Courses: %d%n" +
                " >Maximum Course Capacity: %d%n" +
                " >Number of Equipment: %d%n" +
                "--------------------------------%n" +
                " >Operational Cost: %.2f%n" +
                "=================================%n",
                getName(), getDeptCode(), getLocation(), getStudents().size(), getCourses().size(),
                getMaxCourseCapacity(), getNumberOfEquipment(), calculateOperationalCost());
    }

    @Override
    public double calculateOperationalCost(){
        double costPerStudent = 400.0;
        double costPerEquipment = 200.0;
        int numberOfStudents = students.size();

        return (numberOfStudents * costPerStudent) + (costPerEquipment * getNumberOfEquipment());
    }

    public String addStudent(Student s) {
        if (s == null) {
            return String.format("Student cannot be null.%n");
        }

        if (students.size() >= getCapacity()){
            return String.format("Student capacity reached. Cannot add more students.%n");
        }

        for (Student storedStudent : students) {
            if (storedStudent == s) {
                return String.format("This student already exists.%n");
            }
        }

        students.add(s);
        return String.format("Student successfully added.%n");
    }

    public String addCourse(Course c){
        if (c == null){
            return String.format("Course cannot be null%n");
        }


        if (courses.size() >= maxCourseCapacity){
            return String.format("Course capacity reached. Cannot add more courses.%n");
        }

        for (Course storedCourse : courses){
            if (storedCourse == c){
                return String.format("This course already exists.%n");
            }
        }

        if (hasConflict(c)){
            String conflictMessage = String.format("Schedule Conflict Detected. Resolving schedule conflict of newly added course...%n");
            resolveConflict(c);
            courses.add(c);
            return conflictMessage + String.format("Course successfully added.%n");
        }

        courses.add(c);
        return String.format("");
    }

    public void setDeptCode(String deptCode) {
        if (deptCode != null && !deptCode.isEmpty()){
            this.deptCode = deptCode;
        } else {
            System.out.println("Department code cannot be empty.");
        }
    }

    public void setStudents(ArrayList<Student> students) {
        if (students == null){
            System.out.println("Students List cannot be null. Set again.");
            this.students = new ArrayList<>();
            return;
        }

        if (students.size() > getCapacity()){
            System.out.println("Students List cannot exceed department capacity. Set again.");
            this.students = new ArrayList<>();
            return;
        }


        this.students = students;
    }

    public void setCourses(ArrayList<Course> courses) {
        if (courses == null){
            System.out.println("Courses List cannot be null. Set again.");
            this.courses = new ArrayList<>();
            return;
        }

        if (courses.size() > maxCourseCapacity){
            System.out.println("Courses List cannot exceed course capacity. Set again.");
            this.courses = new ArrayList<>();
            return;
        }

        this.courses = courses;
    }

    public void setClassrooms(ArrayList<Classroom> classrooms) {
        if (classrooms == null){
            System.out.println("Classrooms List cannot be null. Set again.");
            this.classrooms = new ArrayList<>();
            return;
        }

        this.classrooms = new ArrayList<>(classrooms);
    }

    public void setMaxCourseCapacity(int maxCourseCapacity) {
        if (maxCourseCapacity > 0){
            this.maxCourseCapacity = maxCourseCapacity;
        } else {
            System.out.println("Max course capacity cannot be zero or negative.");
        }
    }

    public void setNumberOfEquipment(int numberOfEquipment) {
        if (numberOfEquipment > 0){
            this.numberOfEquipment = numberOfEquipment;
        } else {
            System.out.println("Number of equipment cannot be zero or negative.");
        }
    }

    public ArrayList<Student> getStudents() {
        if (students == null) {
            students = new ArrayList<>();
        }
        return students;
    }

    public ArrayList<Course> getCourses() {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        return courses;
    }

    public ArrayList<Classroom> getClassrooms() {
        if (classrooms == null) {
            classrooms = new ArrayList<>();
        }
        return classrooms;
    }

    public int getNumberOfEquipment() {
        return numberOfEquipment;
    }

    public int getMaxCourseCapacity() {
        return maxCourseCapacity;
    }

    public String getDeptCode() {
        return deptCode;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Department Code: %s%nNumber of Students: %d%nMax Course Capacity: %d%nNumber of Courses: %d%nNumber of Classrooms: %d%nNumber of Equipment: %d%n", deptCode, getStudents().size(), getMaxCourseCapacity(), getCourses().size(), getClassrooms().size(), numberOfEquipment);
    }
}
