package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {
    private DataManager dataManager;
    private JTextArea textArea;

    public ReportsPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular UI setup
        initializeComponents();
        setupLayout();

        // Initial generation
        generateReport();
    }

    /**
     * Initializes the report display and controls.
     */
    private void initializeComponents() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(248, 249, 250));
    }

    /**
     * Organizes the report layout.
     */
    private void setupLayout() {
        JLabel title = new JLabel("System-Wide Analytical Reports", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        this.add(title, BorderLayout.NORTH);

        this.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Regenerate Full System Audit");
        refreshBtn.setFont(UITheme.HEADER_FONT);
        refreshBtn.addActionListener(e -> generateReport());
        this.add(refreshBtn, BorderLayout.SOUTH);
    }

    /**
     * Aggregates data from all repositories to build a comprehensive system report.
     */
    private void generateReport() {
        String report = "";

        report = report + "====================================================================\n";
        report = report + "        SMART UNIVERSITY CAMPUS MANAGEMENT SYSTEM - AUDIT REPORT\n";
        report = report + "====================================================================\n\n";

        report = report + "--- [ CORE SYSTEM METRICS ] ---\n";
        report = report + "Registered Students:   " + dataManager.getStudentRepo().getSize() + "\n";
        report = report + "Active Courses:        " + dataManager.getCourseRepo().getSize() + "\n";
        report = report + "Departments:           " + dataManager.getDepartmentRepo().getSize() + "\n";
        report = report + "Operational Labs:      " + dataManager.getLabRepo().getSize() + "\n";
        report = report + "Lecture Classrooms:    " + dataManager.getClassroomRepo().getSize() + "\n";
        report = report + "Global Facility Usage: " + Facility.getTotalFacilityUsage() + "\n";
        report = report + "System User Accounts:  " + dataManager.getUserRepo().getSize() + "\n";
        report = report + "Campus Zones:          " + dataManager.getZoneRepo().getSize() + "\n\n";

        report = report + "--- [ ACADEMIC INFRASTRUCTURE ] ---\n";
        report = report + "Departments:\n";
        for (int i = 0; i < dataManager.getDepartmentRepo().getSize(); i++) {
            Department d = dataManager.getDepartmentRepo().get(i);
            report = report + " > " + d.getName() + " (ID: " + d.getEntityID() + ")\n";
            report = report + "   Cost: $" + String.format("%.2f", d.calculateOperationalCost()) + "\n";
        }

        report = report + "\nLabs:\n";
        for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
            Lab l = dataManager.getLabRepo().get(i);
            report = report + " > " + l.getName() + " [" + l.getLabType() + "]\n";
            report = report + "   Cost: $" + String.format("%.2f", l.calculateOperationalCost()) + "\n";
        }

        report = report + "\nClassrooms:\n";
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            Classroom c = dataManager.getClassroomRepo().get(i);
            report = report + " > " + c.getName() + " (Capacity: " + c.getCapacity() + ")\n";
            report = report + "   Cost: $" + String.format("%.2f", c.calculateOperationalCost()) + "\n";
        }
        report = report + "\n";

        report = report + "--- [ CAMPUS FACILITIES ] ---\n";
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            Library lib = dataManager.getLibraryRepo().get(i);
            report = report + " > Library: " + lib.getName() + " - Cost: $" + String.format("%.2f", lib.calculateOperationalCost()) + "\n";
        }
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
            Cafeteria caf = dataManager.getCafeteriaRepo().get(i);
            report = report + " > Cafeteria: " + caf.getName() + " - Cost: $" + String.format("%.2f", caf.calculateOperationalCost()) + "\n";
        }
        for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
            Hostel hos = dataManager.getHostelRepo().get(i);
            report = report + " > Hostel: " + hos.getName() + " - Cost: $" + String.format("%.2f", hos.calculateOperationalCost()) + "\n";
        }
        report = report + "\n";

        report = report + "--- [ STUDENT SERVICES ] ---\n";
        for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
            TransportService ts = dataManager.getTransportRepo().get(i);
            report = report + " > Transport: " + ts.getName() + " - Cost: $" + String.format("%.2f", ts.calculateOperationalCost()) + "\n";
        }
        for (int i = 0; i < dataManager.getSecurityRepo().getSize(); i++) {
            SecurityService ss = dataManager.getSecurityRepo().get(i);
            report = report + " > Security: " + ss.getName() + " - Cost: $" + String.format("%.2f", ss.calculateOperationalCost()) + "\n";
        }
        for (int i = 0; i < dataManager.getHealthCenterRepo().getSize(); i++) {
            HealthCenter hc = dataManager.getHealthCenterRepo().get(i);
            report = report + " > Health: " + hc.getName() + " - Cost: $" + String.format("%.2f", hc.calculateOperationalCost()) + "\n";
        }
        report = report + "\n";

        report = report + "--- [ GEOGRAPHIC MAPPING ] ---\n";
        for (int i = 0; i < dataManager.getZoneRepo().getSize(); i++) {
            CampusZone z = dataManager.getZoneRepo().get(i);
            report = report + " > Zone: " + z.getZoneName() + " (" + z.getZoneLocation() + ")\n";
            report = report + "   Contains: " + z.getFacilities().size() + " Facilities, " + z.getServiceUnits().size() + " Services\n";
        }
        report = report + "\n";

        report = report + "====================================================================\n";
        report = report + "                      END OF AUTOMATED REPORT\n";
        report = report + "====================================================================\n";

        textArea.setText(report);
        textArea.setCaretPosition(0);
    }
}
