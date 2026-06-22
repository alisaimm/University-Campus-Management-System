package com.campus.model;

import java.io.Serializable;
import java.util.Date;

public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String assignmentID;
    private String assignmentName;
    private String description;
    private int totalMarks;
    private Date deadline;

    public Assignment(){
        this.deadline = new Date(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000));
    }

    public Assignment(String assignmentID, String assignmentName, String description, int totalMarks, Date deadline){
        setAssignmentID(assignmentID);
        setAssignmentName(assignmentName);
        setDescription(description);
        setTotalMarks(totalMarks);
        setDeadline(deadline);
    }

    public void setDeadline(Date deadline) {
        if (deadline == null) {
            System.out.println("Deadline cannot be null. Set to 7 days.");
            this.deadline = new Date(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000));
            return;
        }

        if (deadline.before(new Date())){
            System.out.println("Assignment Deadline cannot be in past. Set to 7 days.");
            this.deadline = new Date(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000));
            return;
        }

        this.deadline = deadline;
    }

    public void setAssignmentID(String assignmentID) {
        if (assignmentID != null && !assignmentID.isEmpty()){
            this.assignmentID = assignmentID;
        } else {
            System.out.println("Assignment ID cannot be empty.");
        }
    }

    public void setAssignmentName(String assignmentName) {
        if (assignmentName != null && !assignmentName.isEmpty()){
            this.assignmentName = assignmentName;
        } else {
            System.out.println("Assignment name cannot be empty.");
        }
    }

    public void setDescription(String description) {
        if (description != null && !description.isEmpty()){
            this.description = description;
        } else {
            System.out.println("Description cannot be empty.");
        }
    }

    public void setTotalMarks(int totalMarks) {
        if (totalMarks > 0){
            this.totalMarks = totalMarks;
        } else {
            System.out.println("Total marks cannot be zero or negative.");
        }
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getAssignmentID() {
        return assignmentID;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public Date getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return String.format("Assignment ID: %s%nAssignment Name: %s%nDescription: %s%nTotal Marks: %d%nDeadline: %s%n", assignmentID, assignmentName, description, totalMarks, deadline);
    }
}
