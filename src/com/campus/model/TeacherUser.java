package com.campus.model;

import java.util.ArrayList;

public class TeacherUser extends User {
    private static final long serialVersionUID = 1L;
    private ArrayList<Course> assignedCourses;

    public TeacherUser(){
        assignedCourses = new ArrayList<>();
    }

    public TeacherUser (String username, String password, String fullName, ArrayList<Course> assignedCourses){
        super(username, password, fullName, "TEACHER");
        setAssignedCourses(assignedCourses);
    }

    public void setAssignedCourses(ArrayList<Course> assignedCourses) {
        if (assignedCourses != null){
            this.assignedCourses = assignedCourses;
        } else {
            System.out.println("Courses cannot be empty.");
            this.assignedCourses = new ArrayList<>();
        }
    }

    public ArrayList<Course> getAssignedCourses() {
        return new ArrayList<>(assignedCourses);
    }

    @Override
    public String toString() {
        if (assignedCourses != null){
            return super.toString() + String.format("Assigned Courses: %d%n", assignedCourses.size());
        } else {
            return super.toString() + String.format("Assigned Courses: N/A%n");
        }
    }
}
