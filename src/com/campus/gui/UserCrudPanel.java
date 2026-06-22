package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class UserCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField usernameField, nameField;
    private JPasswordField passwordField;
    private JComboBox<String> typeComboBox;
    private JButton addBtn;

    public UserCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular setup sequence
        initializeComponents();
        setupLayout();
        attachListeners();

        // Sync initial state
        refreshTable();
    }

    /**
     * Initializes the user input components and data table.
     */
    private void initializeComponents() {
        typeComboBox = new JComboBox<>(new String[] { "Admin", "Teacher", "Student" });
        usernameField = new JTextField(12);
        passwordField = new JPasswordField(12);
        nameField = new JTextField(12);
        addBtn = new JButton("Add User Account");

        // Table definition
        String[] columns = { "Username", "Full Name", "Assigned Role" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Lays out the form and table using a split pane configuration.
     */
    private void setupLayout() {
        // --- FORM SIDE ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Create User Account"));

        String[] labels = { "Account Type:", "Username:", "Account Password:", "User Full Name:" };
        JComponent[] components = { typeComboBox, usernameField, passwordField, nameField };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- SPLIT VIEW ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- BOTTOM ACTIONS ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshBtn = new JButton("Refresh Table");
        refreshBtn.addActionListener(e -> refreshTable());
        actionPanel.add(refreshBtn);

        JButton editBtn = new JButton("Edit User");
        editBtn.addActionListener(e -> editUser());
        actionPanel.add(editBtn);

        JButton deleteBtn = new JButton("Delete User Account");
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteUser());
        actionPanel.add(deleteBtn);

        this.add(actionPanel, BorderLayout.SOUTH);
    }



    /**
     * Attaches core logic listeners.
     */
    private void attachListeners() {
        addBtn.addActionListener(e -> handleAddUser());
    }

    /**
     * Handles account creation logic based on user role.
     */
    private void handleAddUser() {
        String type = (String) typeComboBox.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String name = nameField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All identification fields are mandatory.");
            return;
        }

        // Duplicate username validation
        if (dataManager.getUserRepo().findById(username) != null) {
            JOptionPane.showMessageDialog(this, "The username '" + username + "' is already in use.");
            return;
        }

        User user = null;
        if (type.equals("Admin")) {
            user = new AdminUser(username, password, name);
        } else if (type.equals("Teacher")) {
            user = new TeacherUser(username, password, name, new ArrayList<>());
        } else if (type.equals("Student")) {
            // Check if student record already exists
            Student st = dataManager.getStudentRepo().findById(username);
            
            if (st == null) {
                // Create new synchronized Student record
                st = new Student(username, name, username + "@campus.edu", 20, "General", 1, new ArrayList<>());
                dataManager.getStudentRepo().addItem(st);
            }
            
            user = new StudentUser(username, password, name, st);
        }

        if (user != null) {
            dataManager.getUserRepo().addItem(user);
            JOptionPane.showMessageDialog(this, "User account created successfully.");
            refreshTable();
            clearFields();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        nameField.setText("");
        typeComboBox.setSelectedIndex(0);
    }

    private void editUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to edit.");
            return;
        }
        String username = (String) tableModel.getValueAt(row, 0);
        User foundUser = dataManager.getUserRepo().findById(username);
        if (foundUser == null) return;
        final User userToEdit = foundUser;

        JTextField nF = new JTextField(userToEdit.getFullName());
        JTextField pF = new JTextField(userToEdit.getPassword());

        JPanel p = new JPanel(new GridLayout(2, 2));
        p.add(new JLabel("Full Name:")); p.add(nF);
        p.add(new JLabel("Password:")); p.add(pF);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nF.getText().trim();
                String pass = pF.getText().trim();

                userToEdit.setFullName(name);
                userToEdit.setPassword(pass);
                JOptionPane.showMessageDialog(this, "User account updated successfully.");
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user from the table first.");
            return;
        }
        String username = (String) tableModel.getValueAt(row, 0);
        String role = (String) tableModel.getValueAt(row, 2);

        // Protect the default admin account
        if (username.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Cannot delete the default admin account.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user: " + username + " (" + role + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            User toDelete = dataManager.getUserRepo().findById(username);
            if (toDelete != null) {
                dataManager.getUserRepo().removeItem(toDelete);
                JOptionPane.showMessageDialog(this, "User deleted.");
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getUserRepo().getSize(); i++) {
            User u = dataManager.getUserRepo().get(i);
            tableModel.addRow(new Object[] { u.getUsername(), u.getFullName(), u.getRole() });
        }
    }
}
