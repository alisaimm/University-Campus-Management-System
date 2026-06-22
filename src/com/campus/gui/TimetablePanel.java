package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TimetablePanel extends JPanel {
    private DataManager dataManager;
    private JTabbedPane innerTabs;

    private DefaultTableModel courseModel, transportModel, assignModel;
    private JButton refreshBtn;

    public TimetablePanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular setup
        initializeComponents();
        setupLayout();
        attachListeners();

        // Initial data sync
        refreshAllData();
    }

    /**
     * Initializes the table models and tabbed interface.
     */
    private void initializeComponents() {
        innerTabs = new JTabbedPane();

        // Course Model
        String[] courseColumns = {"Course ID", "Course Name", "Teacher", "Schedule", "Classroom", "Credits", "Students"};
        courseModel = new DefaultTableModel(courseColumns, 0);

        // Transport Model
        String[] transportColumns = {"Service Name", "Business Hours", "Vehicles", "Drivers", "Routes", "Express Active"};
        transportModel = new DefaultTableModel(transportColumns, 0);

        // Assignment Model
        String[] assignColumns = {"Course", "Assignment ID", "Name", "Marks", "Deadline"};
        assignModel = new DefaultTableModel(assignColumns, 0);

        refreshBtn = new JButton("Synchronize All Schedules");
    }

    /**
     * Organizes the tabbed panels and layout.
     */
    private void setupLayout() {
        JLabel title = new JLabel("University Timetables & Operational Schedules", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        this.add(title, BorderLayout.NORTH);

        // Academic Tab
        innerTabs.addTab("Academic Courses", new JScrollPane(new JTable(courseModel)));
        
        // Transport Tab
        innerTabs.addTab("Campus Transport", new JScrollPane(new JTable(transportModel)));
        
        // Assignments Tab
        innerTabs.addTab("Due Assignments", new JScrollPane(new JTable(assignModel)));

        this.add(innerTabs, BorderLayout.CENTER);
        this.add(refreshBtn, BorderLayout.SOUTH);
    }

    /**
     * Attaches refresh logic.
     */
    private void attachListeners() {
        refreshBtn.addActionListener(e -> refreshAllData());
    }

    private void refreshAllData() {
        populateCourses(courseModel);
        populateTransport(transportModel);
        populateAssignments(assignModel);
    }

    private void populateCourses(DefaultTableModel model) {
        model.setRowCount(0);
        for (int i = 0; i < dataManager.getCourseRepo().getSize(); i++) {
            Course c = dataManager.getCourseRepo().get(i);
            model.addRow(new Object[]{
                    c.getCourseID(), c.getCourseName(), c.getTeacherName(),
                    c.getSchedule(), c.getClassroomName(), c.getCreditHours(),
                    c.getEnrolledStudents().size()
            });
        }
    }

    private void populateTransport(DefaultTableModel model) {
        model.setRowCount(0);
        for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
            TransportService ts = dataManager.getTransportRepo().get(i);
            model.addRow(new Object[]{
                    ts.getName(), ts.getBusinessHours(), ts.getNumberOfVehicles(),
                    ts.getDriverCount(), ts.getTransportRoutes().size(),
                    ts.isExpressRoutesActive() ? "Yes" : "No"
            });
        }
    }

    private void populateAssignments(DefaultTableModel model) {
        model.setRowCount(0);
        for (int i = 0; i < dataManager.getCourseRepo().getSize(); i++) {
            Course c = dataManager.getCourseRepo().get(i);
            for (int j = 0; j < c.getAssignments().size(); j++) {
                Assignment a = c.getAssignments().get(j);
                model.addRow(new Object[]{
                        c.getCourseName(), a.getAssignmentID(), a.getAssignmentName(),
                        a.getTotalMarks(), a.getDeadline().toString()
                });
            }
        }
    }

}
