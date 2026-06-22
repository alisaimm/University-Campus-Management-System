package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.CampusRepository;
import com.campus.repository.DataManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CampusMapPanel extends JPanel {
    private DataManager dataManager;
    private JPanel mapContainer;

    private JButton refreshBtn;

    public CampusMapPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular setup sequence
        initializeComponents();
        setupLayout();
        attachListeners();

        // Initial map load
        populateMap();
    }

    /**
     * Initializes the map container and refresh controls.
     */
    private void initializeComponents() {
        mapContainer = new JPanel();
        mapContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        mapContainer.setBackground(new Color(240, 242, 245));
        mapContainer.setBackground(new Color(240, 242, 245));

        refreshBtn = new JButton("Synchronize Map Status");
        refreshBtn.setFont(UITheme.HEADER_FONT);
    }

    /**
     * Organizes the header title, scrollable map area, and footer controls.
     */
    private void setupLayout() {
        JLabel title = new JLabel("Interactive Campus Directory", SwingConstants.CENTER);
        title.setFont(UITheme.TITLE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        this.add(title, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(mapContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane, BorderLayout.CENTER);

        this.add(refreshBtn, BorderLayout.SOUTH);
    }

    /**
     * Attaches interaction listeners.
     */
    private void attachListeners() {
        refreshBtn.addActionListener(e -> populateMap());
    }

    /**
     * Rebuilds the map by iterating through all managed campus entities.
     */
    private void populateMap() {
        mapContainer.removeAll();

        // Categorized map population
        addAcademicBuildings();
        addCampusFacilities();
        addServiceNodes();

        if (mapContainer.getComponentCount() == 0) {
            showEmptyMapMessage();
        }

        mapContainer.revalidate();
        mapContainer.repaint();
    }

    private void addAcademicBuildings() {
        for (int i = 0; i < dataManager.getDepartmentRepo().getSize(); i++) {
            Department d = dataManager.getDepartmentRepo().get(i);
            createCard(d, "Academic Dept", d.isActive(), () -> d.setActive(!d.isActive()));
        }
        for (int i = 0; i < dataManager.getLabRepo().getSize(); i++) {
            Lab l = dataManager.getLabRepo().get(i);
            createCard(l, "Lab Unit", l.isActive(), () -> l.setActive(!l.isActive()));
        }
        for (int i = 0; i < dataManager.getClassroomRepo().getSize(); i++) {
            Classroom c = dataManager.getClassroomRepo().get(i);
            createCard(c, "Classroom", c.isAvailable(), () -> c.setAvailable(!c.isAvailable()));
        }
    }

    private void addCampusFacilities() {
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            Library lib = dataManager.getLibraryRepo().get(i);
            createCard(lib, "Library", lib.isOpen(), () -> lib.setIsOpen(!lib.isOpen()));
        }
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
            Cafeteria caf = dataManager.getCafeteriaRepo().get(i);
            createCard(caf, "Cafeteria", caf.isOpen(), () -> caf.setIsOpen(!caf.isOpen()));
        }
        for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
            Hostel hos = dataManager.getHostelRepo().get(i);
            createCard(hos, "Hostel", hos.isOpen(), () -> hos.setIsOpen(!hos.isOpen()));
        }
    }

    private void addServiceNodes() {
        for (int i = 0; i < dataManager.getTransportRepo().getSize(); i++) {
            TransportService ts = dataManager.getTransportRepo().get(i);
            createCard(ts, "Transport", ts.isOperational(), () -> ts.setIsOperational(!ts.isOperational()));
        }
        for (int i = 0; i < dataManager.getSecurityRepo().getSize(); i++) {
            SecurityService ss = dataManager.getSecurityRepo().get(i);
            createCard(ss, "Security", ss.isOperational(), () -> ss.setIsOperational(!ss.isOperational()));
        }
        for (int i = 0; i < dataManager.getHealthCenterRepo().getSize(); i++) {
            HealthCenter hc = dataManager.getHealthCenterRepo().get(i);
            createCard(hc, "Health Center", hc.isOperational(), () -> hc.setIsOperational(!hc.isOperational()));
        }
    }

    /**
     * Creates an interactive building card with three-state status cycling.
     * Each click cycles the state: OPERATIONAL → BUSY → CLOSED → OPERATIONAL.
     * The BUSY state is tracked in the GUI-only busyEntities set without modifying the backend model.
     */
    private void createCard(CampusEntity entity, String category, boolean isActive, Runnable toggleAction) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 110));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Determine three-state status from backend flag + GUI busy set
        boolean isBusy = entity.getIsBusy();
        String statusText;
        Color statusColor;
        if (!isActive) {
            statusText = "CLOSED";
            statusColor = new Color(231, 76, 60);
        } else if (isBusy) {
            statusText = "BUSY";
            statusColor = new Color(230, 126, 34);
        } else {
            statusText = "OPERATIONAL";
            statusColor = new Color(46, 204, 113);
        }

        // Click cycles: OPERATIONAL → BUSY → CLOSED → OPERATIONAL
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!isActive) {
                    // CLOSED → OPERATIONAL: re-activate, clear any stale busy flag
                    toggleAction.run();
                    entity.setIsBusy(false);
                } else if (entity.getIsBusy()) {
                    // BUSY → CLOSED: deactivate via backend, clear busy flag
                    entity.setIsBusy(false);
                    toggleAction.run();
                } else {
                    // OPERATIONAL → BUSY: mark busy in GUI set only (stays active in backend)
                    entity.setIsBusy(true);
                }
                populateMap();
            }
        });

        // Content label
        JLabel info = new JLabel("<html><center><small>" + category.toUpperCase() + "</small><br/><b>"
                + entity.getName() + "</b><br/><font color='gray'><small>Click to Cycle Status</small></font></center></html>",
                SwingConstants.CENTER);
        info.setFont(UITheme.BODY_FONT);
        card.add(info, BorderLayout.CENTER);

        // Color-coded status bar
        JLabel status = new JLabel(statusText, SwingConstants.CENTER);
        status.setOpaque(true);
        status.setFont(new Font("Segoe UI", Font.BOLD, 10));
        status.setForeground(Color.WHITE);
        status.setBackground(statusColor);
        card.add(status, BorderLayout.SOUTH);

        mapContainer.add(card);
    }

    private void showEmptyMapMessage() {
        JLabel msg = new JLabel("No campus infrastructure registered. Add units in management tabs.");
        msg.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        msg.setForeground(Color.GRAY);
        mapContainer.add(msg);
    }
}
