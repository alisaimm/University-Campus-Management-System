package com.campus.gui;

import com.campus.model.Lab;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LabCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, nameField, locationField, capacityField, headField, technicianField, equipmentField,
            labTypeField;
    private JCheckBox activeCheck;
    private JButton addBtn;

    public LabCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Standard modular setup
        initializeComponents();
        setupLayout();
        attachListeners();

        // Sync data
        refreshTable();
    }

    /**
     * Initializes all UI components for the lab management form and table.
     */
    private void initializeComponents() {
        idField = new JTextField(12);
        nameField = new JTextField(12);
        locationField = new JTextField(12);
        capacityField = new JTextField(12);
        headField = new JTextField(12);
        technicianField = new JTextField(12);
        equipmentField = new JTextField(12);

        labTypeField = new JTextField(12);

        activeCheck = new JCheckBox("Is Lab Operational", true);
        addBtn = new JButton("Add Laboratory");

        // Define the data table for labs
        String[] columns = { "ID", "Name", "Location", "Capacity", "Lab Type", "Technician", "Equipment", "Status",
                "Op. Cost" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Organizes components into a split pane layout.
     */
    private void setupLayout() {
        // --- DATA ENTRY FORM ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Register New Lab"));

        String[] labels = { "Lab ID:", "Display Name:", "Building/Floor:", "Seating Capacity:", "In-Charge/Head:", "Lab Specialty:", "Technician:", "Equipment Count:", "" };
        JComponent[] components = { idField, nameField, locationField, capacityField, headField, labTypeField, technicianField, equipmentField, activeCheck };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- SPLIT VIEW ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- LOWER ACTIONS ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] actions = { "Refresh", "Edit Lab", "View Details", "Add Equipment", "Remove Equipment" };

        for (String act : actions) {
            JButton btn = new JButton(act);
            btn.addActionListener(e -> handleCommand(act));
            actionPanel.add(btn);
        }

        JButton deleteBtn = new JButton("Delete Lab");
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(e -> deleteLab());
        actionPanel.add(deleteBtn);

        this.add(actionPanel, BorderLayout.SOUTH);
    }



    /**
     * Attaches core logic listeners.
     */
    private void attachListeners() {
        addBtn.addActionListener(e -> handleAddLab());
    }

    private void handleCommand(String cmd) {
        switch (cmd) {
            case "Refresh":
                refreshTable();
                break;
            case "Edit Lab":
                editLab();
                break;
            case "View Details":
                viewLabDetails();
                break;
            case "Add Equipment":
                adjustEquipment(true);
                break;
            case "Remove Equipment":
                adjustEquipment(false);
                break;
        }
    }

    /**
     * Orchestrates lab registration and validation.
     */
    private void handleAddLab() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String capStr = capacityField.getText().trim();
        String head = headField.getText().trim();
        String labType = labTypeField.getText().trim();
        String technician = technicianField.getText().trim();
        String equipStr = equipmentField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || location.isEmpty() || capStr.isEmpty() || head.isEmpty()
                || technician.isEmpty() || equipStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all identity, technical, and capacity fields.");
            return;
        }

        // Duplicate check
        for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
            if (dataManager.getLabRepo().get(i).getEntityID().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "Lab ID '" + id + "' is already registered.");
                return;
            }
        }

        try {
            int capacity = Integer.parseInt(capStr);
            int equipment = Integer.parseInt(equipStr);

            Lab lab = new Lab(id, name, location, false, capacity, head, activeCheck.isSelected(),
                    labType, technicianField.getText().trim(), equipment);
            dataManager.getLabRepo().addItem(lab);
            JOptionPane.showMessageDialog(this, "Lab registered successfully.");

            resetInputs();
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacity and equipment counts must be valid numerical values.");
        }
    }

    private void resetInputs() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        capacityField.setText("");
        headField.setText("");
        technicianField.setText("");
        equipmentField.setText("");
        labTypeField.setText("");
        activeCheck.setSelected(true);
    }

    private void adjustEquipment(boolean increment) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a lab from the table first.");
            return;
        }
        String labId = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
            Lab lab = dataManager.getLabRepo().get(i);
            if (lab.getEntityID().equals(labId)) {
                if (increment) {
                    lab.setNumberOfEquipment(lab.getNumberOfEquipment() + 1);
                } else {
                    if (lab.getNumberOfEquipment() > 0) {
                        lab.setNumberOfEquipment(lab.getNumberOfEquipment() - 1);
                    } else {
                        JOptionPane.showMessageDialog(this, "No equipment left to remove.");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Equipment count updated: " + lab.getNumberOfEquipment());
                refreshTable();
                return;
            }
        }
    }

    private void editLab() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a lab to edit.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        Lab foundLab = null;
        for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
            if (dataManager.getLabRepo().get(i).getEntityID().equals(id)) {
                foundLab = dataManager.getLabRepo().get(i);
                break;
            }
        }
        if (foundLab == null)
            return;
        final Lab labToEdit = foundLab;

        JTextField nF = new JTextField(labToEdit.getName());
        JTextField lF = new JTextField(labToEdit.getLocation());
        JTextField cF = new JTextField(String.valueOf(labToEdit.getCapacity()));
        JTextField hF = new JTextField(labToEdit.getHeadName());
        JTextField tF = new JTextField(labToEdit.getTechnicianName());
        JTextField ltF = new JTextField(labToEdit.getLabType());
        JTextField eF = new JTextField(String.valueOf(labToEdit.getNumberOfEquipment()));
        JCheckBox aC = new JCheckBox("Is Active", labToEdit.isActive());

        JPanel p = new JPanel(new GridLayout(8, 2));
        p.add(new JLabel("Name:"));
        p.add(nF);
        p.add(new JLabel("Location:"));
        p.add(lF);
        p.add(new JLabel("Capacity:"));
        p.add(cF);
        p.add(new JLabel("Head:"));
        p.add(hF);
        p.add(new JLabel("Lab Type:"));
        p.add(ltF);
        p.add(new JLabel("Technician:"));
        p.add(tF);
        p.add(new JLabel("Equipment:"));
        p.add(eF);
        p.add(new JLabel("Status:"));
        p.add(aC);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit Lab", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nF.getText().trim();
                String loc = lF.getText().trim();
                int capacity = Integer.parseInt(cF.getText());
                String head = hF.getText().trim();
                String lType = ltF.getText().trim();
                String tech = tF.getText().trim();
                int equip = Integer.parseInt(eF.getText());
                boolean active = aC.isSelected();

                labToEdit.setName(name);
                labToEdit.setLocation(loc);
                labToEdit.setCapacity(capacity);
                labToEdit.setHeadName(head);
                labToEdit.setLabType(lType);
                labToEdit.setTechnicianName(tech);
                labToEdit.setNumberOfEquipment(equip);
                labToEdit.setActive(active);
                JOptionPane.showMessageDialog(this, "Lab updated successfully.");
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void viewLabDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a lab from the table first.");
            return;
        }
        String labId = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
            Lab lab = dataManager.getLabRepo().get(i);
            if (lab.getEntityID().equals(labId)) {
                JTextArea area = new JTextArea(lab.toString());
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scroll = new JScrollPane(area);
                scroll.setPreferredSize(new Dimension(450, 300));
                JOptionPane.showMessageDialog(this, scroll,
                        "Lab Details (toString)", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    private void deleteLab() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a lab from the table first.");
            return;
        }
        String labId = (String) tableModel.getValueAt(row, 0);
        String labName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete lab: " + labName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
                if (dataManager.getLabRepo().get(i).getEntityID().equals(labId)) {
                    Lab labToDelete = dataManager.getLabRepo().get(i);
                    dataManager.getLabRepo().removeItem(labToDelete);
                    JOptionPane.showMessageDialog(this, "Lab deleted successfully.");
                    refreshTable();
                    return;
                }
            }
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
            Lab lab = dataManager.getLabRepo().get(i);
            tableModel.addRow(new Object[] {
                    lab.getEntityID(),
                    lab.getName(),
                    lab.getLocation(),
                    lab.getCapacity(),
                    lab.getLabType(),
                    lab.getTechnicianName(),
                    lab.getNumberOfEquipment(),
                    lab.isActive() ? "Active" : "Inactive",
                    String.format("%.2f", lab.calculateOperationalCost())
            });
        }
    }
}
