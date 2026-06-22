package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class DepartmentCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField codeField, nameField, locationField, headField, capacityField, maxCoursesField;
    private JButton addBtn;

    public DepartmentCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Initialize and setup all UI components
        initializeComponents();
        setupLayout();
        attachListeners();

        // Load initial data
        refreshTable();
    }

    /**
     * Initializes all the input fields and buttons for the department form.
     */
    private void initializeComponents() {
        codeField = new JTextField(12);
        nameField = new JTextField(12);
        locationField = new JTextField(12);
        headField = new JTextField(12);
        capacityField = new JTextField(12);
        maxCoursesField = new JTextField(12);
        addBtn = new JButton("Add Department");

        // Initialize table for displaying department data
        String[] columns = { "Code", "Name", "Head", "Capacity", "Students", "Courses", "Op. Cost" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Sets up the visual layout using GridBagLayout for the form and BorderLayout
     * for the main panel.
     */
    private void setupLayout() {
        // --- FORM PANEL ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Department"));

        String[] labels = { "Dept Code:", "Name:", "Location:", "Head Name:", "Capacity:", "Max Courses:" };
        JComponent[] components = { codeField, nameField, locationField, headField, capacityField, maxCoursesField };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- SPLIT PANE (Form + Table) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- BOTTOM ACTION PANEL ---
        JPanel bottomPanel = new JPanel(new GridLayout(2, 0, 5, 5));
        String[] btnLabels = {
                "Refresh", "Edit Department", "View Details", "Add Student to Dept",
                "Add Course to Dept", "Generate Report",
                "Reschedule Classrooms", "Assign Classroom", "View Classrooms", "View Students",
                "Resolve Conflict"
        };

        for (String label : btnLabels) {
            JButton btn = new JButton(label);
            final String fLabel = label;
            btn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    handleActionButton(fLabel);
                }
            });
            bottomPanel.add(btn);
        }

        // Separate styling for Delete Button
        JButton deleteBtn = new JButton("Delete Department");
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteDept();
            }
        });
        bottomPanel.add(deleteBtn);

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Attaches logic to the primary interaction components.
     */
    private void attachListeners() {
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleAddDepartment();
            }
        });
    }

    /**
     * Routes button clicks to their respective handler methods.
     */
    private void handleActionButton(String command) {
        switch (command) {
            case "Refresh":
                refreshTable();
                break;
            case "Edit Department":
                editDept();
                break;
            case "View Details":
                viewDeptDetails();
                break;
            case "Add Student to Dept":
                addStudentToDept();
                break;
            case "Add Course to Dept":
                addCourseToDept();
                break;
            case "Generate Report":
                generateDeptReport();
                break;
            case "Reschedule Classrooms":
                rescheduleClassrooms();
                break;
            case "Assign Classroom":
                assignClassroomToDept();
                break;
            case "View Classrooms":
                viewDeptClassrooms();
                break;
            case "View Students":
                viewDeptStudents();
                break;
            case "Resolve Conflict":
                resolveCourseConflict();
                break;
        }
    }

    /**
     * Logic for processing the new department form submission.
     */
    private void handleAddDepartment() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String head = headField.getText().trim();
        String capStr = capacityField.getText().trim();
        String maxCStr = maxCoursesField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || location.isEmpty()
                || head.isEmpty() || capStr.isEmpty() || maxCStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        // Duplicate check
        if (dataManager.getDepartmentRepo().findById(code) != null) {
            JOptionPane.showMessageDialog(this, "Department ID '" + code + "' already exists.");
            return;
        }

        try {
            int capacity = Integer.parseInt(capStr);
            int maxCourses = Integer.parseInt(maxCStr);

            Department d = new Department(code, name, location, false, capacity, head, true,
                    code, new ArrayList<>(), new ArrayList<>(), 50, maxCourses);
            dataManager.getDepartmentRepo().addItem(d);
            JOptionPane.showMessageDialog(this, "Department registered successfully.");

            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity and Max Courses must be numbers.");
        }
    }

    private void clearFields() {
        codeField.setText("");
        nameField.setText("");
        locationField.setText("");
        headField.setText("");
        capacityField.setText("");
        maxCoursesField.setText("");
    }

    private void addStudentToDept() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        if (dataManager.getStudentRepo().getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No students available.");
            return;
        }

        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;
        String[] students = new String[dataManager.getStudentRepo().getSize()];
        for (int i = 0; i < dataManager.getStudentRepo().getSize(); i++) {
            Student s = dataManager.getStudentRepo().get(i);
            students[i] = s.getStudentID() + " - " + s.getName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select Student:",
                "Add to Department", JOptionPane.PLAIN_MESSAGE, null, students, students[0]);

        if (selected != null) {
            String studentId = selected.split(" - ")[0];
            final Student s = dataManager.getStudentRepo().findById(studentId);
            if (s != null) {
                    String msg = dept.addStudent(s);
                    JOptionPane.showMessageDialog(this, msg);
                refreshTable();
            }
        }
    }

    private void addCourseToDept() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        if (dataManager.getCourseRepo().getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No courses available.");
            return;
        }

        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;
        String[] courses = new String[dataManager.getCourseRepo().getSize()];
        for (int i = 0; i < dataManager.getCourseRepo().getSize(); i++) {
            Course c = dataManager.getCourseRepo().get(i);
            courses[i] = c.getCourseID() + " - " + c.getCourseName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select Course:",
                "Add to Department", JOptionPane.PLAIN_MESSAGE, null, courses, courses[0]);

        if (selected != null) {
            String courseId = selected.split(" - ")[0];
            final Course c = dataManager.getCourseRepo().findById(courseId);
            if (c != null) {
                    String msg = dept.addCourse(c);
                    JOptionPane.showMessageDialog(this, msg);
                refreshTable();
            }
        }
    }

    private void generateDeptReport() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;
        String report = dept.generateReport();
        JTextArea area = new JTextArea(report);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 350));
        JOptionPane.showMessageDialog(this, scroll, "Department Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void rescheduleClassrooms() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;
        String msg = dept.rescheduleClassrooms();
        JOptionPane.showMessageDialog(this, msg, "Reschedule Classrooms", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resolveCourseConflict() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String deptId = (String) tableModel.getValueAt(row, 0);
        final Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;

        ArrayList<Course> courses = dept.getCourses();
        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No courses assigned to " + dept.getName() + " yet.");
            return;
        }

        String[] courseOptions = new String[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            courseOptions[i] = c.getCourseID() + " - " + c.getCourseName();
        }

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select course to check for conflicts:",
                "Resolve Conflict", JOptionPane.PLAIN_MESSAGE,
                null, courseOptions, courseOptions[0]);

        if (selected != null) {
            String courseId = selected.split(" - ")[0];
            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).getCourseID().equals(courseId)) {
                    final Course c = courses.get(i);
                    if (dept.hasConflict(c)) {
                        String msg = dept.resolveConflict(c);
                        JOptionPane.showMessageDialog(this, msg, "Resolving Course Conflict", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No conflict in schedule.");
                    }
                    refreshTable();
                    return;
                }
            }
        }
    }

    private void assignClassroomToDept() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        if (dataManager.getClassroomRepo().getSize() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No classrooms available.\nAdd classrooms first from the Classrooms tab.");
            return;
        }

        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;

        // Build list of classrooms
        String[] roomOptions = new String[dataManager.getClassroomRepo().getSize()];
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            Classroom cr = dataManager.getClassroomRepo().get(i);
            roomOptions[i] = cr.getEntityID() + " - " + cr.getName()
                    + (cr.isAvailable() ? " [Available]" : " [In Use]");
        }

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select classroom to assign to " + dept.getName() + ":",
                "Assign Classroom", JOptionPane.PLAIN_MESSAGE,
                null, roomOptions, roomOptions[0]);

        if (selected != null) {
            String roomId = selected.split(" - ")[0];
            Classroom cr = dataManager.getClassroomRepo().findById(roomId);
            if (cr != null) {
                ArrayList<Classroom> classrooms = dept.getClassrooms();
                // check not already assigned
                for (Classroom existing : classrooms) {
                    if (existing.getEntityID().equals(cr.getEntityID())) {
                        JOptionPane.showMessageDialog(this,
                                "This classroom is already assigned to this department.");
                        return;
                    }
                }
                classrooms.add(cr);
                dept.setClassrooms(classrooms);
                JOptionPane.showMessageDialog(this,
                        "Classroom '" + cr.getName() + "' assigned to " + dept.getName() + ".");
            }
        }
    }

    private void viewDeptClassrooms() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;
        ArrayList<Classroom> classrooms = dept.getClassrooms();

        if (classrooms.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No classrooms assigned to " + dept.getName() + " yet.\n"
                            + "Use 'Assign Classroom' to link classrooms.");
            return;
        }

        String res = "Classrooms in " + dept.getName() + ":\n\n";
        for (int i = 0; i < classrooms.size(); i++) {
            Classroom cr = classrooms.get(i);
            res = res + "  ID: " + cr.getEntityID() + " | Name: " + cr.getName() + " | Type: " + cr.getRoomType() + "\n";
        }

        JTextArea area = new JTextArea(res);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 280));
        JOptionPane.showMessageDialog(this, scroll,
                "Classrooms in " + dept.getName(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void viewDeptStudents() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;
        ArrayList<Student> students = dept.getStudents();

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students enrolled in " + dept.getName());
            return;
        }

        String res = "Students in " + dept.getName() + ":\n\n";
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            res = res + " - " + s.getStudentID() + " | " + s.getName() + "\n";
        }

        JTextArea area = new JTextArea(res);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Students in " + dept.getName(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void editDept() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department to edit.");
            return;
        }
        String deptId = (String) tableModel.getValueAt(row, 0);
        final Department deptToEdit = dataManager.getDepartmentRepo().findById(deptId);
        if (deptToEdit == null) return;

        JTextField nF = new JTextField(deptToEdit.getName());
        JTextField lF = new JTextField(deptToEdit.getLocation());
        JTextField hF = new JTextField(deptToEdit.getHeadName());
        JTextField cF = new JTextField(String.valueOf(deptToEdit.getCapacity()));

        JPanel p = new JPanel(new GridLayout(4, 2));
        p.add(new JLabel("Name:")); p.add(nF);
        p.add(new JLabel("Location:")); p.add(lF);
        p.add(new JLabel("Head:")); p.add(hF);
        p.add(new JLabel("Capacity:")); p.add(cF);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit Department", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nF.getText().trim();
                String loc = lF.getText().trim();
                String head = hF.getText().trim();
                int capacity = Integer.parseInt(cF.getText());

                deptToEdit.setName(name);
                deptToEdit.setLocation(loc);
                deptToEdit.setHeadName(head);
                deptToEdit.setCapacity(capacity);
                JOptionPane.showMessageDialog(this, "Department updated successfully.");
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void viewDeptDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String deptId = (String) tableModel.getValueAt(row, 0);
        Department dept = dataManager.getDepartmentRepo().findById(deptId);
        if (dept == null) return;
        JTextArea area = new JTextArea(dept.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Department Details (toString)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteDept() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a department first.");
            return;
        }
        String code = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete department: " + name + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Department toDelete = dataManager.getDepartmentRepo().findById(code);
            if (toDelete != null) {
                dataManager.getDepartmentRepo().removeItem(toDelete);
                JOptionPane.showMessageDialog(this, "Department deleted.");
                refreshTable();
            }
        }
    }



    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getDepartmentRepo().getSize(); i++) {
            Department d = dataManager.getDepartmentRepo().get(i);
            tableModel.addRow(new Object[] {
                    d.getDeptCode(), d.getName(), d.getHeadName(),
                    d.getCapacity(), d.getStudents().size(), d.getCourses().size(),
                    String.format("%.2f", d.calculateOperationalCost())
            });
        }
    }
}
