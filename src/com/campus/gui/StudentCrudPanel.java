package com.campus.gui;

import com.campus.model.Student;
import com.campus.model.StudentUser;
import com.campus.model.User;
import com.campus.model.Course;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, nameField, emailField, ageField, programField, semesterField;
    private JPasswordField passwordField;
    private JButton addBtn;

    public StudentCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Standard setup sequence
        initializeComponents();
        setupLayout();
        attachListeners();

        // Load data on startup
        refreshTable();
    }

    /**
     * Initializes all the Swing components used for the student entry form and data
     * table.
     */
    private void initializeComponents() {
        idField = new JTextField(12);
        nameField = new JTextField(12);
        emailField = new JTextField(12);
        ageField = new JTextField(12);
        programField = new JTextField(12);
        semesterField = new JTextField(12);
        passwordField = new JPasswordField(12);
        addBtn = new JButton("Add Student");

        // Set up the table model and table for student data
        String[] columns = { "ID", "Name", "Email", "Age", "Program", "Semester", "Courses" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Arranges the components using a combination of GridBagLayout and
     * BorderLayout.
     */
    private void setupLayout() {
        // --- STUDENT FORM ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Student Record"));

        // Form Fields
        String[] labels = { "Student ID:", "Full Name:", "Email Address:", "Age:", "Program:", "Semester (1-8):", "Password:" };
        JComponent[] components = { idField, nameField, emailField, ageField, programField, semesterField, passwordField };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- SPLIT PANE ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- ACTION BUTTONS ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] actions = { "Refresh", "Edit Student", "Enroll in Course", "View Details", "View Enrolled Courses" };

        for (String act : actions) {
            JButton btn = new JButton(act);
            btn.addActionListener(e -> handleAction(act));
            actionPanel.add(btn);
        }

        // Delete button with danger styling
        JButton deleteBtn = new JButton("Delete Student");
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteStudent());
        actionPanel.add(deleteBtn);

        this.add(actionPanel, BorderLayout.SOUTH);
    }



    /**
     * Attaches main interaction listeners.
     */
    private void attachListeners() {
        addBtn.addActionListener(e -> handleAddStudent());
    }

    private void handleAction(String cmd) {
        switch (cmd) {
            case "Refresh":
                refreshTable();
                break;
            case "Edit Student":
                editStudent();
                break;
            case "Enroll in Course":
                enrollStudentInCourse();
                break;
            case "View Details":
                viewStudentDetails();
                break;
            case "View Enrolled Courses":
                viewEnrolledCourses();
                break;
        }
    }

    /**
     * Handles the creation of a new student and their associated user account.
     */
    private void handleAddStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String ageStr = ageField.getText().trim();
        String program = programField.getText().trim();
        String semStr = semesterField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || ageStr.isEmpty()
                || program.isEmpty() || semStr.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        // Duplicate ID check
        if (dataManager.getStudentRepo().findById(id) != null) {
            JOptionPane.showMessageDialog(this, "Student ID '" + id + "' already exists.");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            int semester = Integer.parseInt(semStr);

            Student s = new Student(id, name, email, age, program, semester, new ArrayList<>());
            dataManager.getStudentRepo().addItem(s);

            // Create login credentials for the student (Use email as username)
            StudentUser su = new StudentUser(email, password, name, s);
            dataManager.getUserRepo().addItem(su);

            JOptionPane.showMessageDialog(this, "Student registered successfully.");
            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for Age and Semester.");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        ageField.setText("");
        programField.setText("");
        semesterField.setText("");
        passwordField.setText("");
    }

    private void enrollStudentInCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student from the table first.");
            return;
        }

        // Build list of available courses
        if (dataManager.getCourseRepo().getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No courses available. Add a course first.");
            return;
        }

        String[] courseNames = new String[dataManager.getCourseRepo().getSize()];
        for (int i = 0; i < dataManager.getCourseRepo().getSize(); i++) {
            Course c = dataManager.getCourseRepo().get(i);
            courseNames[i] = c.getCourseID() + " - " + c.getCourseName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select a Course:",
                "Enroll in Course", JOptionPane.PLAIN_MESSAGE, null, courseNames, courseNames[0]);

        if (selected != null) {
            String courseId = selected.split(" - ")[0];
            String studentId = (String) tableModel.getValueAt(row, 0);

            Student student = dataManager.getStudentRepo().findById(studentId);
            Course course = dataManager.getCourseRepo().findById(courseId);

            if (student != null && course != null) {
                student.enrollCourse(course);
                JOptionPane.showMessageDialog(this, "Student enrolled in " + course.getCourseName());
                refreshTable();
            }
        }
    }

    private void viewEnrolledCourses() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student first.");
            return;
        }
        String studentId = (String) tableModel.getValueAt(row, 0);
        Student s = dataManager.getStudentRepo().findById(studentId);
        if (s != null) {
            if (s.getEnrolledCourses().isEmpty()) {
                JOptionPane.showMessageDialog(this, s.getName() + " is not enrolled in any courses yet.");
                return;
            }
            String res = "Enrolled Courses for " + s.getName() + ":\n\n";
            ArrayList<Course> enrolled = s.getEnrolledCourses();
            for (int j = 0; j < enrolled.size(); j++) {
                Course c = enrolled.get(j);
                res = res + "  - " + c.getCourseID() + " | " + c.getCourseName() + " | Schedule: " + c.getSchedule() + "\n";
            }
            JTextArea area = new JTextArea(res);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area),
                    "Enrolled Courses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student from the table first.");
            return;
        }
        String studentId = (String) tableModel.getValueAt(row, 0);
        String studentName = (String) tableModel.getValueAt(row, 1);
        String studentEmail = (String) tableModel.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student: " + studentName + "?\nThis will also remove their login account.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Remove student from studentRepo
            Student toDelete = dataManager.getStudentRepo().findById(studentId);
            if (toDelete != null) dataManager.getStudentRepo().removeItem(toDelete);
            
            // Remove linked StudentUser (username == email)
            User uToDelete = dataManager.getUserRepo().findById(studentEmail);
            if (uToDelete != null) dataManager.getUserRepo().removeItem(uToDelete);
            JOptionPane.showMessageDialog(this, "Student and their account deleted.");
            refreshTable();
        }
    }

    private void editStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to edit.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        Student foundS = dataManager.getStudentRepo().findById(id);
        if (foundS == null) return;
        final Student studentToEdit = foundS;

        JTextField nF = new JTextField(studentToEdit.getName());
        JTextField aF = new JTextField(String.valueOf(studentToEdit.getAge()));
        JTextField pF = new JTextField(studentToEdit.getProgram());
        JTextField sF = new JTextField(String.valueOf(studentToEdit.getSemester()));

        JPanel p = new JPanel(new GridLayout(4, 2));
        p.add(new JLabel("Name:")); p.add(nF);
        p.add(new JLabel("Age:")); p.add(aF);
        p.add(new JLabel("Program:")); p.add(pF);
        p.add(new JLabel("Semester:")); p.add(sF);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nF.getText().trim();
                int age = Integer.parseInt(aF.getText());
                int semester = Integer.parseInt(sF.getText());
                String program = pF.getText().trim();

                studentToEdit.setName(name);
                studentToEdit.setAge(age);
                studentToEdit.setProgram(program);
                studentToEdit.setSemester(semester);
                JOptionPane.showMessageDialog(this, "Student information updated successfully.",
                        "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void viewStudentDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student from the table first.");
            return;
        }
        String studentId = (String) tableModel.getValueAt(row, 0);
        Student s = dataManager.getStudentRepo().findById(studentId);
        if (s != null) {
            JTextArea area = new JTextArea(s.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Student Details",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getStudentRepo().getSize(); i++) {
            Student s = dataManager.getStudentRepo().get(i);
            tableModel.addRow(new Object[] {
                    s.getStudentID(), s.getName(), s.getEmail(),
                    s.getAge(), s.getProgram(), s.getSemester(),
                    s.getEnrolledCourses().size()
            });
        }
    }
}
