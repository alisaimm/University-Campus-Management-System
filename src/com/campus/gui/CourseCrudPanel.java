package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

public class CourseCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField codeField, nameField, teacherField, creditsField;
    private JComboBox<String> scheduleBox, classroomBox;
    private JButton addBtn;

    public CourseCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular initialization
        initializeComponents();
        setupLayout();
        attachListeners();

        // Load initial table data
        refreshTable();
    }

    /**
     * Instantiates all the text fields, boxes, and buttons for the course panel.
     */
    private void initializeComponents() {
        codeField = new JTextField(12);
        nameField = new JTextField(12);
        teacherField = new JTextField(12);
        creditsField = new JTextField(12);

        scheduleBox = new JComboBox<>(new String[] {
                "Mon/Wed 8:00-9:30", "Mon/Wed 9:30-11:00", "Mon/Wed 11:00-12:30",
                "Tue/Thu 8:00-9:30", "Tue/Thu 9:30-11:00", "Tue/Thu 11:00-12:30"
        });

        classroomBox = new JComboBox<>();
        refreshClassroomBox();

        addBtn = new JButton("Add Course");

        // Table setup
        String[] columns = { "ID", "Name", "Teacher", "Schedule", "Classroom", "Credits", "Students", "Assignments" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Builds the UI hierarchy.
     */
    private void setupLayout() {
        // --- FORM AREA ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Course"));

        String[] labels = { "Course ID:", "Course Name:", "Teacher Name:", "Schedule:", "Credit Hours:", "Classroom:" };
        JComponent[] components = { codeField, nameField, teacherField, scheduleBox, creditsField, classroomBox };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- SPLIT VIEW ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- LOWER BUTTONS ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] btnLabels = { "Refresh", "Edit Course", "Add Assignment", "View Details", "View Enrolled Students",
                "Generate Schedule", "Reschedule Course" };

        for (String label : btnLabels) {
            JButton btn = new JButton(label);
            btn.addActionListener(e -> handleAction(label));
            buttonPanel.add(btn);
        }

        JButton deleteBtn = new JButton("Delete Course");
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteCourse());
        buttonPanel.add(deleteBtn);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }


    /**
     * Attaches listeners to UI components.
     */
    private void attachListeners() {
        addBtn.addActionListener(e -> handleAddCourse());
    }

    private void handleAction(String command) {
        switch (command) {
            case "Refresh":
                refreshTable();
                break;
            case "Edit Course":
                editCourse();
                break;
            case "Add Assignment":
                addAssignmentToCourse();
                break;
            case "View Details":
                viewCourseDetails();
                break;
            case "View Enrolled Students":
                viewEnrolledStudents();
                break;
            case "Generate Schedule":
                generateCourseSchedule();
                break;
            case "Reschedule Course":
                rescheduleCourse();
                break;
        }
    }

    /**
     * Validates input and adds a new course to the repository.
     */
    private void handleAddCourse() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String teacher = teacherField.getText().trim();
        String schedule = (String) scheduleBox.getSelectedItem();
        String creditsStr = creditsField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || teacher.isEmpty() || creditsStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all required fields.");
            return;
        }

        // Duplicate check
        if (dataManager.getCourseRepo().findById(code) != null) {
            JOptionPane.showMessageDialog(this, "Course ID '" + code + "' already exists.");
            return;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            int selectedIdx = classroomBox.getSelectedIndex();

            if (selectedIdx == 0) {
                JOptionPane.showMessageDialog(this, "Please select an available classroom or N/A.");
                return;
            }

            final Classroom finalCr = (selectedIdx > 1) ? dataManager.getClassroomRepo().get(selectedIdx - 2) : null;

            Course c = new Course(code, name, teacher, schedule, finalCr, credits, new ArrayList<Student>(), new ArrayList<Assignment>());
            dataManager.getCourseRepo().addItem(c);
            JOptionPane.showMessageDialog(this, "Course registered successfully.");

            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit hours must be a valid number.");
        }
    }

    private void clearFields() {
        codeField.setText("");
        nameField.setText("");
        teacherField.setText("");
        creditsField.setText("");
        classroomBox.setSelectedIndex(0);
    }

    private void addAssignmentToCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course from the table first.");
            return;
        }

        String courseId = (String) tableModel.getValueAt(row, 0);
        Course selectedCourse = dataManager.getCourseRepo().findById(courseId);
        if (selectedCourse == null)
            return;

        JTextField assignIdField = new JTextField(10);
        JTextField assignNameField = new JTextField(10);
        JTextField assignDescField = new JTextField(10);
        JTextField assignMarksField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Assignment ID:"));
        panel.add(assignIdField);
        panel.add(new JLabel("Assignment Name:"));
        panel.add(assignNameField);
        panel.add(new JLabel("Description:"));
        panel.add(assignDescField);
        panel.add(new JLabel("Total Marks:"));
        panel.add(assignMarksField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Assignment to " + selectedCourse.getCourseName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int marks = Integer.parseInt(assignMarksField.getText().trim());
                // Deadline set 7 days from now
                Date deadline = new Date(System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000));
                Assignment assignment = new Assignment(
                        assignIdField.getText().trim(),
                        assignNameField.getText().trim(),
                        assignDescField.getText().trim(),
                        marks, deadline);
                selectedCourse.addAssignment(assignment);
                JOptionPane.showMessageDialog(this, "Assignment added successfully!");
                refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Total marks must be an integer.");
            }
        }
    }

    private void editCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to edit.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        Course c = dataManager.getCourseRepo().findById(id);
        if (c == null) return;

        JTextField nF = new JTextField(c.getCourseName());
        JTextField tF = new JTextField(c.getTeacherName());
        JTextField sF = new JTextField(c.getSchedule());
        JTextField cF = new JTextField(String.valueOf(c.getCreditHours()));

        JPanel p = new JPanel(new GridLayout(4, 2));
        p.add(new JLabel("Name:")); p.add(nF);
        p.add(new JLabel("Teacher:")); p.add(tF);
        p.add(new JLabel("Schedule:")); p.add(sF);
        p.add(new JLabel("Credits:")); p.add(cF);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nF.getText().trim();
                String teacher = tF.getText().trim();
                int credits = Integer.parseInt(cF.getText());
                String schedule = sF.getText().trim();

                c.setCourseName(name);
                c.setTeacherName(teacher);
                c.setSchedule(schedule);
                c.setCreditHours(credits);
                JOptionPane.showMessageDialog(this, "Course updated successfully.");
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void viewCourseDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course from the table first.");
            return;
        }
        String courseId = (String) tableModel.getValueAt(row, 0);
        Course c = dataManager.getCourseRepo().findById(courseId);
        if (c != null) {
            JTextArea area = new JTextArea(c.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area),
                    "Course Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void generateCourseSchedule() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        String courseId = (String) tableModel.getValueAt(row, 0);
        Course c = dataManager.getCourseRepo().findById(courseId);
        if (c != null) {
            String sched = c.generateSchedule();
            JOptionPane.showMessageDialog(this, sched, "Course Schedule", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void viewEnrolledStudents() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course from the table first.");
            return;
        }
        String courseId = (String) tableModel.getValueAt(row, 0);
        Course c = dataManager.getCourseRepo().findById(courseId);
        if (c != null) {
            ArrayList<Student> students = c.getEnrolledStudents();
            if (students.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No students enrolled in " + c.getCourseName());
                return;
            }
            String res = "Enrolled Students in " + c.getCourseName() + ":\n\n";
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                res = res + " - " + s.getStudentID() + " | " + s.getName() + "\n";
            }
            JTextArea area = new JTextArea(res);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Enrolled Students",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }



    private void rescheduleCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course first.");
            return;
        }
        String courseId = (String) tableModel.getValueAt(row, 0);
        Course selectedCourse = dataManager.getCourseRepo().findById(courseId);
        if (selectedCourse == null) return;

        // Find department containing this course
        Department dept = null;
        for (int i = 0; i < dataManager.getDepartmentRepo().getSize(); i++) {
            Department d = dataManager.getDepartmentRepo().get(i);
            if (d.getCourses().contains(selectedCourse)) {
                dept = d;
                break;
            }
        }
        
        if (dept == null) {
            JOptionPane.showMessageDialog(this, "Course is not assigned to any department. Cannot reschedule.");
            return;
        }
        
        final Department finalDept = dept;
        if (finalDept.hasConflict(selectedCourse)) {
            String msg = finalDept.resolveConflict(selectedCourse);
            JOptionPane.showMessageDialog(this, msg, "Resolving Course Conflict", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No conflict in schedule.");
        }
        refreshTable();
    }

    private void deleteCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course from the table first.");
            return;
        }
        String courseId = (String) tableModel.getValueAt(row, 0);
        String courseName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete course: " + courseName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Course toDelete = dataManager.getCourseRepo().findById(courseId);
            if (toDelete != null) {
                // Release classroom before deleting so it becomes available again
                if (toDelete.getAssignedClassroom() != null) {
                    toDelete.getAssignedClassroom().setAvailable(true);
                }
                dataManager.getCourseRepo().removeItem(toDelete);
                JOptionPane.showMessageDialog(this, "Course deleted.");
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        refreshClassroomBox();
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getCourseRepo().getSize(); i++) {
            Course c = dataManager.getCourseRepo().get(i);
            tableModel.addRow(new Object[] {
                    c.getCourseID(), c.getCourseName(), c.getTeacherName(),
                    c.getSchedule(), c.getClassroomName(), c.getCreditHours(),
                    c.getEnrolledStudents().size(), c.getAssignments().size()
            });
        }
    }

    private void refreshClassroomBox() {
        if (classroomBox == null) return;
        classroomBox.removeAllItems();
        classroomBox.addItem("-- Select Classroom --");
        classroomBox.addItem("N/A");
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            Classroom cr = dataManager.getClassroomRepo().get(i);
            classroomBox.addItem(cr.getEntityID() + " - " + cr.getName()
                    + (cr.isAvailable() ? " [Available]" : " [In Use]"));
        }
    }
}
