package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CampusZoneCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, nameField, locationField;
    private JButton addBtn;

    public CampusZoneCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular setup
        initializeComponents();
        setupLayout();
        attachListeners();

        // Load data
        refreshTable();
    }

    /**
     * Initializes all UI components for adding and managing campus zones.
     */
    private void initializeComponents() {
        idField = new JTextField(12);
        nameField = new JTextField(12);
        locationField = new JTextField(12);
        addBtn = new JButton("Register Campus Zone");

        // Set up the zone table
        String[] columns = { "ID", "Name", "Location", "Facilities", "Service Units" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Arranges components into a split pane layout with action buttons at the bottom.
     */
    private void setupLayout() {
        // --- FORM AREA ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Register New Zone"));

        String[] labels = { "Zone ID:", "Zone Name:", "Primary Location:" };
        JComponent[] components = { idField, nameField, locationField };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- SPLIT VIEW ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- LOWER ACTIONS ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] actions = {"Refresh", "View Zone Info", "Add Facility to Zone", "Add Service to Zone", "Remove Facility", "Remove Service"};
        
        for (String act : actions) {
            JButton btn = new JButton(act);
            final String fAct = act;
            btn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    handleAction(fAct);
                }
            });
            actionPanel.add(btn);
        }

        JButton deleteBtn = new JButton("Delete Zone");
        deleteBtn.setBackground(new Color(192, 57, 43));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteZone();
            }
        });
        actionPanel.add(deleteBtn);

        this.add(actionPanel, BorderLayout.SOUTH);
    }



    /**
     * Attaches core logic listeners.
     */
    private void attachListeners() {
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                handleAddZone();
            }
        });
    }

    private void handleAction(String cmd) {
        switch (cmd) {
            case "Refresh": refreshTable(); break;
            case "View Zone Info": viewZoneInfo(); break;
            case "Add Facility to Zone": addFacilityToZone(); break;
            case "Add Service to Zone": addServiceToZone(); break;
            case "Remove Facility": removeFacilityFromZone(); break;
            case "Remove Service": removeServiceFromZone(); break;
        }
    }

    /**
     * Validates input and creates a new CampusZone record.
     */
    private void handleAddZone() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields to register the zone.");
            return;
        }

        // Duplicate validation
        if (dataManager.getZoneRepo().findById(id) != null) {
            JOptionPane.showMessageDialog(this, "Zone ID '" + id + "' is already registered.");
            return;
        }

        CampusZone zone = new CampusZone(name, id, location, new ArrayList<>(), new ArrayList<>());
        dataManager.getZoneRepo().addItem(zone);

        clearFields();
        JOptionPane.showMessageDialog(this, "Campus zone registered successfully.");
        refreshTable();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
    }

    private void addFacilityToZone() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a zone first.");
            return;
        }

        String zoneId = (String) tableModel.getValueAt(row, 0);
        CampusZone zone = dataManager.getZoneRepo().findById(zoneId);
        if (zone == null) return;

        // Build a combined list of all facilities
        ArrayList<Facility> allFacilities = new ArrayList<>();
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++)
            allFacilities.add(dataManager.getLibraryRepo().get(i));
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++)
            allFacilities.add(dataManager.getCafeteriaRepo().get(i));
        for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++)
            allFacilities.add(dataManager.getHostelRepo().get(i));

        if (allFacilities.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No facilities available. Add one first.");
            return;
        }

        String[] names = new String[allFacilities.size()];
        for (int i = 0; i < allFacilities.size(); i++) {
            names[i] = allFacilities.get(i).getEntityID() + " - " + allFacilities.get(i).getName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select Facility:",
                "Add to Zone", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);

        if (selected != null) {
            String facId = selected.split(" - ")[0];
            for (Facility f : allFacilities) {
                if (f.getEntityID().equals(facId)) {
                    final Facility finalF = f;
                    String msg = zone.addFacility(finalF);
                    JOptionPane.showMessageDialog(this, msg);
                    refreshTable();
                    return;
                }
            }
        }
    }

    private void addServiceToZone() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a zone first.");
            return;
        }

        String zoneId = (String) tableModel.getValueAt(row, 0);
        CampusZone zone = dataManager.getZoneRepo().findById(zoneId);
        if (zone == null) return;

        // Build combined list of all service units
        ArrayList<ServiceUnit> allServices = new ArrayList<>();
        for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++)
            allServices.add(dataManager.getTransportRepo().get(i));
        for (int i = 0; i < dataManager.getSecurityRepo().getSize(); i++)
            allServices.add(dataManager.getSecurityRepo().get(i));
        for (int i = 0; i < dataManager.getHealthCenterRepo().getSize(); i++)
            allServices.add(dataManager.getHealthCenterRepo().get(i));

        if (allServices.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No service units available. Add one first.");
            return;
        }

        String[] names = new String[allServices.size()];
        for (int i = 0; i < allServices.size(); i++) {
            names[i] = allServices.get(i).getEntityID() + " - " + allServices.get(i).getName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select Service Unit:",
                "Add to Zone", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);

        if (selected != null) {
            String svcId = selected.split(" - ")[0];
            for (ServiceUnit s : allServices) {
                if (s.getEntityID().equals(svcId)) {
                    final ServiceUnit finalS = s;
                    String msg = zone.addServiceUnit(finalS);
                    JOptionPane.showMessageDialog(this, msg);
                    refreshTable();
                    return;
                }
            }
        }
    }

    private void removeFacilityFromZone() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a zone first.");
            return;
        }
        String zoneId = (String) tableModel.getValueAt(row, 0);
        CampusZone zone = dataManager.getZoneRepo().findById(zoneId);
        if (zone == null) return;
        ArrayList<Facility> facilities = zone.getFacilities();
        if (facilities.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No facilities in this zone.");
            return;
        }
        String[] names = new String[facilities.size()];
        for (int i = 0; i < facilities.size(); i++) {
            names[i] = facilities.get(i).getEntityID() + " - " + facilities.get(i).getName();
        }
        String selected = (String) JOptionPane.showInputDialog(this, "Select Facility to Remove:",
                "Remove from Zone", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        if (selected != null) {
            String id = selected.split(" - ")[0];
            for (Facility f : facilities) {
                if (f.getEntityID().equals(id)) {
                    final Facility finalF = f;
                    String msg = zone.removeFacility(finalF);
                    JOptionPane.showMessageDialog(this, msg);
                    refreshTable();
                    return;
                }
            }
        }
    }

    private void removeServiceFromZone() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a zone first.");
            return;
        }
        String zoneId = (String) tableModel.getValueAt(row, 0);
        CampusZone zone = dataManager.getZoneRepo().findById(zoneId);
        if (zone == null) return;
        ArrayList<ServiceUnit> services = zone.getServiceUnits();
        if (services.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No services in this zone.");
            return;
        }
        String[] names = new String[services.size()];
        for (int i = 0; i < services.size(); i++) {
            names[i] = services.get(i).getEntityID() + " - " + services.get(i).getName();
        }
        String selected = (String) JOptionPane.showInputDialog(this, "Select Service to Remove:",
                "Remove from Zone", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        if (selected != null) {
            String id = selected.split(" - ")[0];
            for (ServiceUnit s : services) {
                if (s.getEntityID().equals(id)) {
                    final ServiceUnit finalS = s;
                    String msg = zone.removeServiceUnit(finalS);
                    JOptionPane.showMessageDialog(this, msg);
                    refreshTable();
                    return;
                }
            }
        }
    }

    private void viewZoneInfo() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a zone first.");
            return;
        }
        String zoneId = (String) tableModel.getValueAt(row, 0);
        CampusZone zone = dataManager.getZoneRepo().findById(zoneId);
        if (zone == null) return;
        String report = zone.displayZoneInfo();
        JTextArea area = new JTextArea(report);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 350));
        JOptionPane.showMessageDialog(this, scroll, "Zone Detailed Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteZone() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a zone from the table first.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete Campus Zone: " + name + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            CampusZone toDelete = dataManager.getZoneRepo().findById(id);
            if (toDelete != null) {
                dataManager.getZoneRepo().removeItem(toDelete);
                JOptionPane.showMessageDialog(this, "Campus Zone deleted.");
                refreshTable();
            }
        }
    }



    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getZoneRepo().getSize(); i++) {
            CampusZone z = dataManager.getZoneRepo().get(i);
            tableModel.addRow(new Object[] {
                    z.getZoneId(), z.getZoneName(), z.getZoneLocation(),
                    z.getFacilities().size(), z.getServiceUnits().size()
            });
        }
    }
}
