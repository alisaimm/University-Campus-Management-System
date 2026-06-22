package com.campus.model;

public class StudentUser extends User {
    private static final long serialVersionUID = 1L;
    private Student studentRecord;

    public StudentUser(){

    }

    public StudentUser(String username, String password, String fullName, Student studentRecord){
        super(username, password, fullName, "STUDENT");
        setStudentRecord(studentRecord);
    }

    public void setStudentRecord(Student studentRecord) {
        if (studentRecord == null){
            System.out.println("Student cannot be empty.");
            return;
        }

        this.studentRecord = studentRecord;
    }

    public Student getStudentRecord() {
        return studentRecord;
    }

    @Override
    public String toString() {
        if (studentRecord != null){
            return super.toString() + String.format("Student ID: %s%n", studentRecord.getStudentID());
        } else {
            return super.toString() + String.format("Student ID: N/A%n");
        }
    }
}
