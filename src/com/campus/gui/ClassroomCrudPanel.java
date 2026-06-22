package com.campus.gui;

import com.campus.model.Classroom;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClassroomCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, nameField, locationField, capacityField, headField, roomTypeField;
    private JCheckBox projectorCheck, availableCheck, activeCheck;
    private JButton addBtn;

    public ClassroomCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular initialization sequence
        initializeComponents();
        setupLayout();
        attachListeners();

        // Sync initial data
        refreshTable();
    }

    /**
     * Instantiates all UI components for the classroom management form and table.
     */
    private void initializeComponents() {
        idField = new JTextField(12);
        nameField = new JTextField(12);
        locationField = new JTextField(12);
        capacityField = new JTextField(12);
        headField = new JTextField(12);

        roomTypeField = new JTextField(12);

        projectorCheck = new JCheckBox("Has Projector", true);
        availableCheck = new JCheckBox("Available for Use", true);
        activeCheck = new JCheckBox("Is Operational", true);

        addBtn = new JButton("Add Classroom");

        // Define the data table for classrooms
        String[] columns = { "ID", "Name", "Location", "Capacity", "Type", "Projector", "Available", "Status",
                "Op. Cost" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Arranges the UI components into a split pane layout.
     */
    private void setupLayout() {
        // --- DATA ENTRY FORM ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Register New Classroom"));

        String[] labels = { "Room ID:", "Display Name:", "Building/Floor:", "Seating Capacity:", "In-Charge/Head:", "Facility Type:", "", "", "" };
        JComponent[] components = { idField, nameField, locationField, capacityField, headField, roomTypeField, projectorCheck, availableCheck, activeCheck };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- SPLIT VIEW ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- LOWER ACTIONS ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] actions = { "Refresh", "Edit Classroom", "View Details", "Toggle Available" };

        for (String act : actions) {
            JButton btn = new JButton(act);
            btn.addActionListener(e -> handleCommand(act));
            actionPanel.add(btn);
        }

        JButton deleteBtn = new JButton("Delete Classroom");
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteClassroom());
        actionPanel.add(deleteBtn);

        this.add(actionPanel, BorderLayout.SOUTH);
    }



    /**
     * Attaches interaction logic to components.
     */
    private void attachListeners() {
        addBtn.addActionListener(e -> handleAddClassroom());
    }

    private void handleCommand(String cmd) {
        switch (cmd) {
            case "Refresh":
                refreshTable();
                break;
            case "Edit Classroom":
                editClassroom();
                break;
            case "View Details":
                viewDetails();
                break;
            case "Toggle Available":
                toggleAvailability();
                break;
        }
    }

    /**
     * Validates input and adds a new Classroom to the repository.
     */
    private void handleAddClassroom() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String capStr = capacityField.getText().trim();
        String head = headField.getText().trim();
        String roomType = roomTypeField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || location.isEmpty() || capStr.isEmpty() || head.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all identity and capacity fields.");
            return;
        }

        // Duplicate check
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            if (dataManager.getClassroomRepo().get(i).getEntityID().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "Room ID '" + id + "' is already registered.");
                return;
            }
        }

        try {
            int capacity = Integer.parseInt(capStr);
            Classroom room = new Classroom(id, name, location, false, capacity, head,
                    activeCheck.isSelected(), roomType, projectorCheck.isSelected(), availableCheck.isSelected());
            dataManager.getClassroomRepo().addItem(room);
            JOptionPane.showMessageDialog(this, "Classroom registered successfully.");
            resetInputs();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seating capacity must be a valid numerical value.");
        }
    }

    private void editClassroom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a classroom to edit.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        Classroom foundRoom = null;
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            if (dataManager.getClassroomRepo().get(i).getEntityID().equals(id)) {
                foundRoom = dataManager.getClassroomRepo().get(i);
                break;
            }
        }
        if (foundRoom == null)
            return;
        final Classroom roomToEdit = foundRoom;

        JTextField nF = new JTextField(roomToEdit.getName());
        JTextField lF = new JTextField(roomToEdit.getLocation());
        JTextField cF = new JTextField(String.valueOf(roomToEdit.getCapacity()));
        JTextField hF = new JTextField(roomToEdit.getHeadName());
        JCheckBox projF = new JCheckBox("Has Projector", roomToEdit.hasProjector());
        JCheckBox availF = new JCheckBox("Available for Use", roomToEdit.isAvailable());
        JCheckBox aC = new JCheckBox("Is Operational", roomToEdit.isActive());

        JPanel p = new JPanel(new GridLayout(7, 2));
        p.add(new JLabel("Name:"));
        p.add(nF);
        p.add(new JLabel("Location:"));
        p.add(lF);
        p.add(new JLabel("Capacity:"));
        p.add(cF);
        p.add(new JLabel("Head:"));
        p.add(hF);
        p.add(new JLabel("Has Projector:"));
        p.add(projF);
        p.add(new JLabel("Available for Use:"));
        p.add(availF);
        p.add(new JLabel("Status:"));
        p.add(aC);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit Classroom", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nF.getText().trim();
                String loc = lF.getText().trim();
                int capacity = Integer.parseInt(cF.getText());
                String head = hF.getText().trim();
                boolean hasProjector = projF.isSelected();
                boolean available = availF.isSelected();
                boolean active = aC.isSelected();

                roomToEdit.setName(name);
                roomToEdit.setLocation(loc);
                roomToEdit.setCapacity(capacity);
                roomToEdit.setHeadName(head);
                roomToEdit.setHasProjector(hasProjector);
                roomToEdit.setAvailable(available);
                roomToEdit.setActive(active);
                JOptionPane.showMessageDialog(this, "Classroom updated successfully.");
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void resetInputs() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        capacityField.setText("");
        headField.setText("");
        roomTypeField.setText("");
        projectorCheck.setSelected(true);
        availableCheck.setSelected(true);
        activeCheck.setSelected(true);
    }

    private void viewDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a classroom from the table first.");
            return;
        }
        String roomId = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            Classroom room = dataManager.getClassroomRepo().get(i);
            if (room.getEntityID().equals(roomId)) {
                JTextArea area = new JTextArea(room.toString());
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scroll = new JScrollPane(area);
                scroll.setPreferredSize(new Dimension(420, 260));
                JOptionPane.showMessageDialog(this, scroll,
                        "Classroom Details (toString)", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    private void toggleAvailability() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a classroom from the table first.");
            return;
        }
        String roomId = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            Classroom room = dataManager.getClassroomRepo().get(i);
            if (room.getEntityID().equals(roomId)) {
                room.setAvailable(!room.isAvailable());
                JOptionPane.showMessageDialog(this,
                        room.getName() + " is now: " + (room.isAvailable() ? "Available" : "Unavailable"));
                refreshTable();
                return;
            }
        }
    }

    private void deleteClassroom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a classroom from the table first.");
            return;
        }
        String roomId = (String) tableModel.getValueAt(row, 0);
        String roomName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete classroom: " + roomName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
                if (dataManager.getClassroomRepo().get(i).getEntityID().equals(roomId)) {
                    Classroom toDelete = dataManager.getClassroomRepo().get(i);
                    dataManager.getClassroomRepo().removeItem(toDelete);
                    JOptionPane.showMessageDialog(this, "Classroom deleted.");
                    refreshTable();
                    return;
                }
            }
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            Classroom room = dataManager.getClassroomRepo().get(i);
            tableModel.addRow(new Object[] {
                    room.getEntityID(),
                    room.getName(),
                    room.getLocation(),
                    room.getCapacity(),
                    room.getRoomType(),
                    room.hasProjector() ? "Yes" : "No",
                    room.isAvailable() ? "Available" : "In Use",
                    room.isActive() ? "Active" : "Inactive",
                    String.format("%.2f", room.calculateOperationalCost())
            });
        }
    }
}
