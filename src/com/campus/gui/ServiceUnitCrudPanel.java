package com.campus.gui;

import com.campus.model.*;
import com.campus.interfaces.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ServiceUnitCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, nameField, locationField, staffField;
    private JComboBox<String> typeBox;
    private JCheckBox statusCheck;
    private JButton addBtn;

    public ServiceUnitCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Initialize UI with modular structure
        initializeComponents();
        setupLayout();
        attachListeners();

        // Initial data sync
        refreshTable();
    }

    private void initializeComponents() {
        typeBox = new JComboBox<>(new String[] { "Transport", "Security", "Health Center" });
        idField = new JTextField(12);
        nameField = new JTextField(12);
        locationField = new JTextField(12);
        staffField = new JTextField(12);
        statusCheck = new JCheckBox("Is Operational", true);
        addBtn = new JButton("Add Service Unit");

        String[] columns = { "ID", "Type", "Name", "Location", "Staff", "Status", "Op. Cost" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    private void setupLayout() {
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Register New Service Unit"));

        String[] labels = { "Service Type:", "Unit ID:", "Unit Name:", "Primary Location:", "Number Of Staff:", "Current Status:" };
        JComponent[] components = { typeBox, idField, nameField, locationField, staffField, statusCheck };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        bottomPanel.add(createActionPanel("General Management",
                new String[] { "Refresh", "Edit Service", "View Details" }, true));
        bottomPanel.add(createActionPanel("Transport Services",
                new String[] { "Add Route", "Add Express Route", "Disable Express Routes", "Adjust Peak Hours",
                        "Transport Schedule" },
                false));
        bottomPanel.add(createActionPanel("Security & Emergency",
                new String[] { "Add Emergency Contact", "Update Security Level", "Trigger Emergency!" }, false));
        bottomPanel.add(createActionPanel("Health Center Management",
                new String[] { "Set Doctors", "Set Beds", "Set Occupied", "Set Budget", "Add Medicine" }, false));

        this.add(bottomPanel, BorderLayout.SOUTH);
    }



    private JPanel createActionPanel(String category, String[] labels, boolean isManagement) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(category + ": "));
        for (String lbl : labels) {
            JButton btn = new JButton(lbl);
            if (lbl.equals("Trigger Emergency!")) {
                btn.setBackground(new Color(200, 50, 50));
                btn.setForeground(Color.WHITE);
            }
            btn.addActionListener(e -> handleCommand(lbl));
            panel.add(btn);
        }
        if (isManagement) {
            JButton del = new JButton("Delete Service");
            del.setBackground(new Color(192, 57, 43));
            del.setForeground(Color.WHITE);
            del.addActionListener(e -> deleteServiceUnit());
            panel.add(del);
        }
        return panel;
    }

    private void attachListeners() {
        addBtn.addActionListener(e -> handleAddService());
    }

    private void handleCommand(String cmd) {
        switch (cmd) {
            case "Refresh":
                refreshTable();
                break;
            case "Edit Service":
                editService();
                break;
            case "View Details":
                viewDetails();
                break;
            case "Add Route":
                addTransportRoute();
                break;
            case "Add Express Route":
                addExpressRoute();
                break;
            case "Disable Express Routes":
                disableExpressRoutes();
                break;
            case "Adjust Peak Hours":
                adjustPeakHours();
                break;
            case "Transport Schedule":
                generateTransportSchedule();
                break;
            case "Add Emergency Contact":
                addSecurityContact();
                break;
            case "Update Security Level":
                updateSecurityLevel();
                break;
            case "Trigger Emergency!":
                triggerEmergency();
                break;
            case "Set Doctors":
                setHCField("Doctors");
                break;
            case "Set Beds":
                setHCField("Beds");
                break;
            case "Set Occupied":
                setHCField("Occupied");
                break;
            case "Set Budget":
                setHCField("Budget");
                break;
            case "Add Medicine":
                addMedicine();
                break;
        }
    }

    private void handleAddService() {
        String type = (String) typeBox.getSelectedItem();
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String staffStr = staffField.getText().trim();
        boolean isOp = statusCheck.isSelected();

        if (id.isEmpty() || name.isEmpty() || location.isEmpty() || staffStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.");
            return;
        }

        // Duplicate ID check across all service types
        boolean exists = (dataManager.getTransportRepo().findById(id) != null) ||
                         (dataManager.getSecurityRepo().findById(id) != null) ||
                         (dataManager.getHealthCenterRepo().findById(id) != null);

        if (exists) {
            JOptionPane.showMessageDialog(this, "Service ID '" + id + "' is already in use.");
            return;
        }

        try {
            int staff = Integer.parseInt(staffStr);
            if ("Transport".equals(type)) {
                ArrayList<String> routes = new ArrayList<>();
                routes.add(name + " - Standard Route");
                TransportService ts = new TransportService(id, name, location, false, staff, "06:00-22:00", 16.0, 50.0,
                        isOp, staff + 5, staff, routes, null);
                dataManager.getTransportRepo().addItem(ts);
            } else if ("Security".equals(type)) {
                String[] shifts = { "Morning", "Afternoon", "Evening", "Night" };
                String shift = (String) JOptionPane.showInputDialog(this, "Select Shift for Security Service:", "Shift",
                        JOptionPane.PLAIN_MESSAGE, null, shifts, shifts[0]);
                if (shift == null)
                    return;

                SecurityService ss = new SecurityService(id, name, location, false, staff, "24/7", 24.0, 30.0, isOp,
                        staff, shift, "Medium", new ArrayList<>());
                dataManager.getSecurityRepo().addItem(ss);
            } else if ("Health Center".equals(type)) {
                HealthCenter hc = new HealthCenter(id, name, location, false, staff, "08:00-20:00", 12.0, 100.0, isOp,
                        5, 20, 0, 5000.0, new ArrayList<>());
                dataManager.getHealthCenterRepo().addItem(hc);
            }
            clearInputs();
            JOptionPane.showMessageDialog(this, type + " registered.");
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Staff must be a number.");
        }
    }

    private void clearInputs() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        staffField.setText("");
        statusCheck.setSelected(true);
    }

    private void updateSecurityLevel() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Security service.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Security")) {
            JOptionPane.showMessageDialog(this, "Selected service is not Security.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        SecurityService security = dataManager.getSecurityRepo().findById(id);
        if (security == null)
            return;

        String[] statuses = { "Low", "Medium", "High" };
        String selected = (String) JOptionPane.showInputDialog(this, "Select New Level:", "Update Security Level",
                JOptionPane.PLAIN_MESSAGE, null, statuses, security.getSecurityLevel());

        if (selected != null) {
            final SecurityService finalSecurity = security;
            final String finalStatus = selected;
            finalSecurity.setSecurityLevel(finalStatus);
            JOptionPane.showMessageDialog(this, "Security Level updated successfully to " + finalStatus + ".",
                    "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        }
    }

    private void setHCField(String field) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Health Center.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Health Center")) {
            JOptionPane.showMessageDialog(this, "Selected service is not a Health Center.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        HealthCenter hc = dataManager.getHealthCenterRepo().findById(id);
        if (hc != null) {
            String input = JOptionPane.showInputDialog(this, "Enter new value for " + field + ":");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double val = Double.parseDouble(input.trim());
                    switch (field) {
                        case "Doctors":
                            hc.setNoOfDoctors((int) val);
                            break;
                        case "Beds":
                            hc.setNoOfBeds((int) val);
                            break;
                        case "Occupied":
                            hc.setOccupiedBeds((int) val);
                            break;
                        case "Budget":
                            hc.setMedicineBudget(val);
                            break;
                    }
                    refreshTable();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid number.");
                }
            }
        }
    }

    private void addMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Health Center.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Health Center")) {
            JOptionPane.showMessageDialog(this, "Selected service is not a Health Center.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        HealthCenter hc = dataManager.getHealthCenterRepo().findById(id);
        if (hc != null) {
            String medicine = JOptionPane.showInputDialog(this, "Enter medicine name:");
            if (medicine != null && !medicine.trim().isEmpty()) {
                final String med = medicine.trim();
                String msg = hc.addAvailableMedicine(med);
                JOptionPane.showMessageDialog(this, msg);
            }
        }
    }

    private void addSecurityContact() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Security service first.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Security")) {
            JOptionPane.showMessageDialog(this, "Selected service is not Security.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        SecurityService security = dataManager.getSecurityRepo().findById(id);
        if (security == null)
            return;

        ArrayList<Notifiable> available = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < dataManager.getHealthCenterRepo().getSize(); i++) {
            available.add(dataManager.getHealthCenterRepo().get(i));
            names.add("Health Center: " + dataManager.getHealthCenterRepo().get(i).getName());
        }
        for (int i = 0; i < dataManager.getUserRepo().getSize(); i++) {
            User u = dataManager.getUserRepo().get(i);
            if (u instanceof AdminUser) {
                available.add((AdminUser) u);
                names.add("Admin: " + u.getFullName());
            }
        }
        if (available.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No notifiable entities found.");
            return;
        }
        String selected = (String) JOptionPane.showInputDialog(this, "Select Contact:", "Add Contact",
                JOptionPane.PLAIN_MESSAGE, null, names.toArray(), names.get(0));
        if (selected != null) {
            int index = names.indexOf(selected);
            Notifiable contact = available.get(index);
            final SecurityService finalSecurity = security;
            String msg = finalSecurity.addEmergencyContact(contact);
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    private void addTransportRoute() {
        if (dataManager.getTransportRepo().getSize() == 0)
            return;
        String[] names = new String[dataManager.getTransportRepo().getSize()];
        for (int i = 0; i < names.length; i++)
            names[i] = dataManager.getTransportRepo().get(i).getName();
        String selected = (String) JOptionPane.showInputDialog(this, "Select Transport:", "Add Route",
                JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        if (selected != null) {
            String route = JOptionPane.showInputDialog(this, "Route name:");
            if (route != null && !route.trim().isEmpty()) {
                for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
                    if (dataManager.getTransportRepo().get(i).getName().equals(selected)) {
                        final TransportService ts = dataManager.getTransportRepo().get(i);
                        final String r = route.trim();
                        String msg = ts.addTransportRoute(r);
                        JOptionPane.showMessageDialog(this, msg);
                        refreshTable();
                        return;
                    }
                }
            }
        }
    }

    private void addExpressRoute() {
        if (dataManager.getTransportRepo().getSize() == 0)
            return;
        String[] names = new String[dataManager.getTransportRepo().getSize()];
        for (int i = 0; i < names.length; i++)
            names[i] = dataManager.getTransportRepo().get(i).getName();
        String selected = (String) JOptionPane.showInputDialog(this, "Select Transport:", "Add Express Route",
                JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        if (selected != null) {
            String route = JOptionPane.showInputDialog(this, "Express Route name:");
            if (route != null && !route.trim().isEmpty()) {
                for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
                    if (dataManager.getTransportRepo().get(i).getName().equals(selected)) {
                        final TransportService ts = dataManager.getTransportRepo().get(i);
                        final String r = route.trim();
                        String msg = ts.addExpressRoute(r);
                        JOptionPane.showMessageDialog(this, msg);
                        refreshTable();
                        return;
                    }
                }
            }
        }
    }

    private void adjustPeakHours() {
        String[] times = { "Morning", "Afternoon", "Evening", "Night" };
        String selected = (String) JOptionPane.showInputDialog(this, "Select time:", "Peak Hours",
                JOptionPane.PLAIN_MESSAGE, null, times, times[0]);
        if (selected != null) {
            for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
                String msg = dataManager.getTransportRepo().get(i).adjustForPeakHours(selected);
                JOptionPane.showMessageDialog(this, msg);
            }
            refreshTable();
        }
    }

    private void generateTransportSchedule() {
        StringBuilder allSchedules = new StringBuilder();
        for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
            allSchedules.append(dataManager.getTransportRepo().get(i).generateSchedule()).append("\n");
        }
        JTextArea area = new JTextArea(allSchedules.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 350));
        JOptionPane.showMessageDialog(this, scroll, "Transport Schedule", JOptionPane.INFORMATION_MESSAGE);
    }

    private void disableExpressRoutes() {
        if (dataManager.getTransportRepo().getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No transport services registered.");
            return;
        }

        String[] names = new String[dataManager.getTransportRepo().getSize()];
        for (int i = 0; i < names.length; i++) {
            names[i] = dataManager.getTransportRepo().get(i).getName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select Transport Service:",
                "Disable Express Routes", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);

        if (selected != null) {
            for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
                if (dataManager.getTransportRepo().get(i).getName().equals(selected)) {
                    final TransportService ts = dataManager.getTransportRepo().get(i);
                    ts.removeExpressRoutes();
                    ts.setExpressRoutesActive(false);
                    JOptionPane.showMessageDialog(this, "All express routes disabled.");
                    refreshTable();
                    return;
                }
            }
        }
    }

    private void triggerEmergency() {
        String loc = JOptionPane.showInputDialog(this, "Location:");
        String desc = JOptionPane.showInputDialog(this, "Description:");
        if (loc == null || desc == null)
            return;
        for (int i = 0; i < dataManager.getSecurityRepo().getSize(); i++) {
            dataManager.getSecurityRepo().get(i).handleMedicalEmergency(loc, desc);
        }
        JOptionPane.showMessageDialog(this, "Emergency notification sent to all security and medical staff.");
        for (int i = 0; i < dataManager.getHealthCenterRepo().getSize(); i++)
            dataManager.getHealthCenterRepo().get(i).sendNotification("Emergency: " + desc);
    }

    private void editService() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a service to edit.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);

        ServiceUnit service = null;
        if (type.equals("Transport")) {
            service = dataManager.getTransportRepo().findById(id);
        } else if (type.equals("Security")) {
            service = dataManager.getSecurityRepo().findById(id);
        } else if (type.equals("Health Center")) {
            service = dataManager.getHealthCenterRepo().findById(id);
        }

        if (service == null)
            return;

        JTextField nF = new JTextField(service.getName());
        JTextField lF = new JTextField(service.getLocation());
        JTextField sF = new JTextField(String.valueOf(service.getStaffCount()));
        JCheckBox oC = new JCheckBox("Is Operational", service.isOperational());

        JComboBox<String> shiftBox = null;
        if (service instanceof SecurityService) {
            shiftBox = new JComboBox<>(new String[] { "Morning", "Afternoon", "Evening", "Night" });
            shiftBox.setSelectedItem(((SecurityService) service).getShift());
        }

        int rows = shiftBox != null ? 5 : 4;
        JPanel p = new JPanel(new GridLayout(rows, 2));
        p.add(new JLabel("Name:"));
        p.add(nF);
        p.add(new JLabel("Location:"));
        p.add(lF);
        p.add(new JLabel("Number Of Staff:"));
        p.add(sF);
        if (shiftBox != null) {
            p.add(new JLabel("Shift:"));
            p.add(shiftBox);
        }
        p.add(new JLabel("Status:"));
        p.add(oC);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit Service Unit", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                service.setName(nF.getText());
                service.setLocation(lF.getText());
                service.setStaffCount(Integer.parseInt(sF.getText()));
                service.setIsOperational(oC.isSelected());
                if (service instanceof SecurityService && shiftBox != null) {
                    ((SecurityService) service).setShift((String) shiftBox.getSelectedItem());
                }
                refreshTable();
                JOptionPane.showMessageDialog(this, "Service updated.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void viewDetails() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String id = (String) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        String details = "";
        if (type.equals("Transport")) {
            TransportService ts = dataManager.getTransportRepo().findById(id);
            if (ts != null) details = ts.toString();
        } else if (type.equals("Security")) {
            SecurityService ss = dataManager.getSecurityRepo().findById(id);
            if (ss != null) details = ss.toString();
        } else if (type.equals("Health Center")) {
            HealthCenter hc = dataManager.getHealthCenterRepo().findById(id);
            if (hc != null) details = hc.toString();
        }
        if (!details.isEmpty())
            JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(details)));
    }

    private void deleteServiceUnit() {
        int row = table.getSelectedRow();
        if (row == -1)
            return;
        String id = (String) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        if (JOptionPane.showConfirmDialog(this, "Delete " + id + "?") == JOptionPane.YES_OPTION) {
            if (type.equals("Transport")) {
                TransportService ts = dataManager.getTransportRepo().findById(id);
                if (ts != null) dataManager.getTransportRepo().removeItem(ts);
            } else if (type.equals("Security")) {
                SecurityService ss = dataManager.getSecurityRepo().findById(id);
                if (ss != null) dataManager.getSecurityRepo().removeItem(ss);
            } else if (type.equals("Health Center")) {
                HealthCenter hc = dataManager.getHealthCenterRepo().findById(id);
                if (hc != null) dataManager.getHealthCenterRepo().removeItem(hc);
            }
            refreshTable();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
            TransportService ts = dataManager.getTransportRepo().get(i);
            tableModel.addRow(new Object[] { ts.getEntityID(), "Transport", ts.getName(), ts.getLocation(),
                    ts.getStaffCount(), ts.isOperational() ? "Active" : "Closed",
                    String.format("%.2f", ts.calculateOperationalCost()) });
        }
        for (int i = 0; i < dataManager.getSecurityRepo().getSize(); i++) {
            SecurityService ss = dataManager.getSecurityRepo().get(i);
            tableModel.addRow(new Object[] { ss.getEntityID(), "Security", ss.getName(), ss.getLocation(),
                    ss.getStaffCount(), ss.isOperational() ? "Active" : "Closed",
                    String.format("%.2f", ss.calculateOperationalCost()) });
        }
        for (int i = 0; i < dataManager.getHealthCenterRepo().getSize(); i++) {
            HealthCenter hc = dataManager.getHealthCenterRepo().get(i);
            tableModel.addRow(new Object[] { hc.getEntityID(), "Health Center", hc.getName(), hc.getLocation(),
                    hc.getStaffCount(), hc.isOperational() ? "Active" : "Closed",
                    String.format("%.2f", hc.calculateOperationalCost()) });
        }
    }
}
