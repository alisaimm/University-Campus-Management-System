package com.campus.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String studentID;
    private String name;
    private String email;
    private int age;
    private String program;
    private int semester;
    private ArrayList<Course> enrolledCourses;
    private static int studentCount = 0;
    private static int maxCourseAllowed = 6;

    public Student(){
        enrolledCourses = new ArrayList<>();
        studentCount++;
    }

    public Student(String studentID, String name, String email, int age, String program, int semester, ArrayList<Course> enrolledCourses){
        setStudentID(studentID);
        setName(name);
        setEmail(email);
        setAge(age);
        setProgram(program);
        setSemester(semester);
        setEnrolledCourses(enrolledCourses);
        studentCount++;
    }

    public static void setStudentCount(int count){
        studentCount = count;
    }

    public void enrollCourse(Course course){
        if (course == null){
            System.out.println("Course cannot be null. Enrollment failed. Enroll again.");
            return;
        }

        for (Course storedCourse : enrolledCourses){
            if (storedCourse == course){
                System.out.println("This course is already enrolled.");
                return;
            }
        }

        if (enrolledCourses.size() < maxCourseAllowed){
            enrolledCourses.add(course);
            course.addStudent(this);
            System.out.println("Course successfully enrolled.");
        } else {
            System.out.println("Maximum courses are already registered. Cannot register more.");
        }
    }

    public void setStudentID(String studentID) {
        if (studentID != null && !studentID.isEmpty()){
            this.studentID = studentID;
        } else {
            System.out.println("Student ID cannot be empty.");
        }
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()){
            this.name = name;
        } else {
            System.out.println("Name cannot be empty.");
        }
    }

    public void setEmail(String email) {
        if (email != null && !email.isEmpty()){
            this.email = email;
        } else {
            System.out.println("Email cannot be empty.");
        }
    }

    public void setAge(int age) {
        if (age > 0){
            this.age = age;
        } else {
            System.out.println("Age cannot be zero or negative.");
        }
    }

    public void setProgram(String program) {
        if (program != null && !program.isEmpty()){
            this.program = program;
        } else {
            System.out.println("Program cannot be empty.");
        }
    }

    public void setSemester(int semester) {
        if (semester > 0 && semester < 9){
            this.semester = semester;
        } else {
            System.out.println("Semester must be between 1 and 8.");
        }
    }

    public void setEnrolledCourses(ArrayList<Course> enrolledCourses) {
        if (enrolledCourses == null){
            System.out.println("Courses cannot be null. Set again.");
            this.enrolledCourses = new ArrayList<>();
            return;
        }

        if (enrolledCourses.size() > maxCourseAllowed){
            System.out.println("Courses cannot exceed maximum allowed. Set again.");
            this.enrolledCourses = new ArrayList<>();
            return;
        }

        this.enrolledCourses = enrolledCourses;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getProgram() {
        return program;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public static int getStudentCount() {
        return studentCount;
    }

    public static int getMaxCourseAllowed() {
        return maxCourseAllowed;
    }

    @Override
    public String toString() {
        String coursesStr = "";
        if (enrolledCourses.isEmpty()) {
            coursesStr = "None";
        } else {
            for (int i = 0; i < enrolledCourses.size(); i++) {
                coursesStr = coursesStr + enrolledCourses.get(i).getCourseName();
                if (i < enrolledCourses.size() - 1) {
                    coursesStr = coursesStr + ", ";
                }
            }
        }

        return String.format("Student ID: %s%nName: %s%nEmail: %s%nAge: %d%nProgram: %s%nSemester: %d%nEnrolled Courses: %s%nMaximum Courses Allowed: %d%n%nTotal Students in System: %d%n",
                studentID, name, email, age, program, semester, coursesStr.toString(), maxCourseAllowed, studentCount);
    }

    public void displayEnrolledCourses(){
        System.out.println("====ENROLLED COURSES====");
        for (Course course : enrolledCourses){
            System.out.println(course);
        }
        System.out.println("--------------------------");
    }
}
