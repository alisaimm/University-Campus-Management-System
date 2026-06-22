package com.campus.model;

import com.campus.interfaces.Schedulable;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Schedulable, Serializable {
    private static final long serialVersionUID = 1L;
    private String courseID;
    private String courseName;
    private String teacherName;
    private String schedule;
    private Classroom assignedClassroom;
    private int creditHours;
    private ArrayList<Student> enrolledStudents;
    private ArrayList<Assignment> assignments;
    private static int maximumAssignments = 4;
    private static int maximumStudents = 200;
    private static int courseCount = 0;

    public Course(){
        enrolledStudents = new ArrayList<>();
        assignments = new ArrayList<>();
        courseCount++;
    }

    public Course(String courseID, String courseName, String teacherName, String schedule, Classroom assignedClassroom, int creditHours, ArrayList<Student> enrolledStudents, ArrayList<Assignment> assignments){
        setCourseID(courseID);
        setCourseName(courseName);
        setCreditHours(creditHours);
        setEnrolledStudents(enrolledStudents);
        setAssignments(assignments);
        setSchedule(schedule);
        setAssignedClassroom(assignedClassroom);
        setTeacherName(teacherName);
        courseCount++;
    }

    public static void setCourseCount(int count){
        courseCount = count;
    }

    @Override
    public String generateSchedule() {
        String report = "";
        report += String.format("====COURSE SCHEDULE====%n");
        report += String.format("Course Name: %s%n", courseName);
        report += String.format("Course ID: %s%n", courseID);
        report += String.format("Teacher Name: %s%n", teacherName);
        report += String.format("Classroom: %s%n", getClassroomName());
        report += String.format("Schedule: %s%n", schedule);
        report += String.format("Credit Hours: %d%n", creditHours);
        report += String.format("Number of Students: %d%n", enrolledStudents.size());
        report += String.format("----------------------------%n");

        report += String.format("====ASSIGNMENTS SCHEDULES===%n");
        if (!assignments.isEmpty()){
            for (int i = 0; i < assignments.size(); i++){
                report += String.format("Name: %s - Deadline: %s%n", assignments.get(i).getAssignmentName(), assignments.get(i).getDeadline());
            }
        } else {
            report += String.format("No assignments yet%n");
        }

        report += String.format("==========================%n");
        return report;
    }

    public void addAssignment(Assignment assignment){
        if (assignment == null){
            System.out.println("Assignment cannot be null. Add again.");
            return;
        }

        for (Assignment storedAssignment : assignments){
            if (assignment == storedAssignment){
                System.out.println("This assignment already exists.");
                return;
            }
        }

        if (assignments.size() >= maximumAssignments){
            System.out.printf("Maximum assignments (%d) are already assigned. Cannot assign more assignments.%n", maximumAssignments);
            return;
        }

        assignments.add(assignment);
        System.out.println("Assignment successfully added.");
    }

    public void addStudent(Student student){
        if (student == null){
            System.out.println("Student cannot be null. Add again.");
            return;
        }

        for (Student storedStudent : enrolledStudents){
            if (storedStudent == student){
                System.out.println("This student is already enrolled.");
                return;
            }
        }

        if (enrolledStudents.size() >= maximumStudents){
            System.out.printf("Maximum students (%d) are already enrolled. Cannot enroll more students.%n", maximumStudents);
            return;
        }

        enrolledStudents.add(student);
        System.out.println("Student successfully enrolled.");
    }

    public void setCourseID(String courseID) {
        if (courseID != null && !courseID.isEmpty()){
            this.courseID = courseID;
        } else {
            System.out.println("Course ID cannot be empty.");
        }
    }

    public void setCourseName(String courseName) {
        if (courseName != null && !courseName.isEmpty()){
            this.courseName = courseName;
        } else {
            System.out.println("Course name cannot be empty.");
        }
    }

    public void setTeacherName(String teacherName) {
        if (teacherName != null && !teacherName.isEmpty()){
            this.teacherName = teacherName;
        } else {
            System.out.println("Teacher name cannot be empty.");
        }
    }

    public void setSchedule(String schedule) {
        if (schedule != null && !schedule.isEmpty()){
            this.schedule = schedule;
        } else {
            System.out.println("Schedule cannot be empty.");
        }
    }

    public void setCreditHours(int creditHours) {
        if (creditHours > 0){
            this.creditHours = creditHours;
        } else {
            System.out.println("Credit hours cannot be zero or negative.");
        }
    }

    public void setEnrolledStudents(ArrayList<Student> enrolledStudents) {
        if (enrolledStudents == null){
            System.out.println("Enrolled students cannot be null. Set again.");
            this.enrolledStudents = new ArrayList<>();
            return;
        }

        if (enrolledStudents.size() > maximumStudents){
            System.out.println("Cannot register more than " + maximumStudents + " students in this course.");
            this.enrolledStudents = new ArrayList<>();
            return;
        }

        this.enrolledStudents = enrolledStudents;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        if (assignments == null){
            System.out.println("Assignments cannot be null. Set again.");
            this.assignments = new ArrayList<>();
            return;
        }

        if (assignments.size() > maximumAssignments){
            System.out.println("Cannot assign more than " + maximumAssignments + " assignments in this course.");
            this.assignments = new ArrayList<>();
            return;
        }

        this.assignments = assignments;
    }

    public void setAssignedClassroom(Classroom assignedClassroom) {
        // Release old classroom first so it becomes available again
        if (this.assignedClassroom != null) {
            this.assignedClassroom.setAvailable(true);
        }

        if (assignedClassroom == null){
            System.out.println("Classroom cannot be null. Set again.");
            return;
        }

        if (assignedClassroom.isAvailable()){
            this.assignedClassroom = assignedClassroom;
            assignedClassroom.setAvailable(false);
        } else {
            System.out.println("This classroom is not available. Set again.");
        }
    }



    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public ArrayList<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public static int getMaximumAssignments() {
        return maximumAssignments;
    }

    public String getCourseID() {
        return courseID;
    }

    public static int getCourseCount() {
        return courseCount;
    }

    public static int getMaximumStudents() {
        return maximumStudents;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getClassroomName(){
        if (assignedClassroom != null){
            return assignedClassroom.getName();
        } else {
            return "No classroom assigned";
        }
    }

    public Classroom getAssignedClassroom() {
        return assignedClassroom;
    }

    public void displayEnrolledStudents(){
        System.out.printf("====ALL STUDENTS ENROLLED IN %s COURSE====%n", courseName);

        System.out.println("----------------------------");
        for (Student student : enrolledStudents){
            System.out.println(student.getName() + " - " + student.getStudentID());
        }
        System.out.println("----------------------------");
    }

    @Override
    public String toString() {
        return String.format("Course ID: %s%nCourse Name: %s%nTeacher Name: %s%nSchedule: %s%nClassroom: %s%nCredit Hours: %d%nMaximum Students: %d%n%nEnrolled Students: %d%nMaximum Assignments: %d%n%nNumber of Assignments: %d%n", courseID, courseName, teacherName, schedule, getClassroomName(), creditHours, maximumStudents, enrolledStudents.size(), maximumAssignments, assignments.size());
    }
}
