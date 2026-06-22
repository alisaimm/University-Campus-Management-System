package com.campus.gui;

import com.campus.repository.DataManager;
import com.campus.service.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Dashboard with a sidebar navigation instead of a JTabbedPane.
 * This gives a distinctly different look from the default Swing tabs.
 */
public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private DataManager dataManager;
    private AuthenticationService authService;

    private JPanel contentPanel;
    private CardLayout contentCards;
    private ArrayList<JButton> navButtons;
    private JButton currentlySelected;

    private JPanel sidebar, headerBar;

    public DashboardPanel(MainFrame mainFrame, DataManager dataManager, AuthenticationService authService) {
        this.mainFrame = mainFrame;
        this.dataManager = dataManager;
        this.authService = authService;
        this.navButtons = new ArrayList<>();

        this.setLayout(new BorderLayout());

        // Modular UI build sequence
        initializeContentArea();
        setupSidebar();
        setupHeader();
        buildNavigation();

        // Finalize assembly
        this.add(sidebar, BorderLayout.WEST);
        this.add(headerBar, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);

        // Default selection
        if (!navButtons.isEmpty()) {
            selectNavButton(navButtons.get(0));
        }
    }

    /**
     * Initializes the central content area that switches between different management views.
     */
    private void initializeContentArea() {
        contentCards = new CardLayout();
        contentPanel = new JPanel(contentCards);
        contentPanel.setBackground(UITheme.CONTENT_BG);
    }

    /**
     * Constructs the persistent sidebar containing branding and navigation controls.
     */
    private void setupSidebar() {
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(UITheme.SIDEBAR_BG);

        // --- SIDEBAR BRANDING ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 49, 63));
        header.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));

        JLabel title = new JLabel("<html><b>UMS</b><br/><small>Campus Manager</small></html>");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        title.setForeground(UITheme.TEXT_LIGHT);
        header.add(title, BorderLayout.CENTER);
        sidebar.add(header, BorderLayout.NORTH);

        // --- SIDEBAR FOOTER (Logout) ---
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(34, 49, 63));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(UITheme.SIDEBAR_FONT);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(UITheme.DANGER);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.logout());
        
        footer.add(logoutBtn, BorderLayout.CENTER);
        sidebar.add(footer, BorderLayout.SOUTH);
    }

    /**
     * Configures the top header bar showing current user and role info.
     */
    private void setupHeader() {
        String username = authService.getCurrentUser().getFullName();
        String role = authService.getCurrentRole();

        headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(UITheme.HEADER_BG);
        headerBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setFont(UITheme.HEADER_FONT);
        welcome.setForeground(UITheme.TEXT_DARK);
        headerBar.add(welcome, BorderLayout.WEST);

        JLabel badge = new JLabel(role.toUpperCase());
        badge.setFont(UITheme.BODY_FONT);
        badge.setForeground(UITheme.ACCENT);
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.ACCENT, 1),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
        ));
        headerBar.add(badge, BorderLayout.EAST);
    }

    /**
     * Dynamically builds the navigation menu based on user permissions.
     */
    private void buildNavigation() {
        String role = authService.getCurrentRole();
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(UITheme.SIDEBAR_BG);
        navPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // Public Views
        addNavItem(navPanel, "Campus Map", "MAP", new CampusMapPanel(dataManager));

        // Role-Based Views
        if (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("TEACHER")) {
            addNavItem(navPanel, "Students", "STUDENTS", new StudentCrudPanel(dataManager));
            addNavItem(navPanel, "Courses", "COURSES", new CourseCrudPanel(dataManager));
        }

        if (role.equalsIgnoreCase("ADMIN")) {
            addNavItem(navPanel, "Facilities", "FACILITIES", new FacilityCrudPanel(dataManager));
            addNavItem(navPanel, "Service Units", "SERVICES", new ServiceUnitCrudPanel(dataManager));
            addNavItem(navPanel, "Departments", "DEPARTMENTS", new DepartmentCrudPanel(dataManager));
            addNavItem(navPanel, "Labs", "LABS", new LabCrudPanel(dataManager));
            addNavItem(navPanel, "Classrooms", "CLASSROOMS", new ClassroomCrudPanel(dataManager));
            addNavItem(navPanel, "Campus Zones", "ZONES", new CampusZoneCrudPanel(dataManager));
            addNavItem(navPanel, "Users", "USERS", new UserCrudPanel(dataManager));
        }

        // Shared Utility Views
        addNavItem(navPanel, "Timetable", "TIMETABLE", new TimetablePanel(dataManager));
        addNavItem(navPanel, "Reports", "REPORTS", new ReportsPanel(dataManager));

        // Scrollable container for the nav list
        JScrollPane scroll = new JScrollPane(navPanel);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(UITheme.SIDEBAR_BG);
        
        sidebar.add(scroll, BorderLayout.CENTER);
    }

    /**
     * Adds a navigation button to the sidebar and its corresponding panel to the card layout.
     */
    private void addNavItem(JPanel navPanel, String label, String cardName, JPanel panel) {
        JButton btn = UITheme.createSidebarButton(label);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        btn.addActionListener(e -> selectNavButton(btn));
        btn.putClientProperty("cardName", cardName);

        navPanel.add(btn);
        contentPanel.add(panel, cardName);
        navButtons.add(btn);
    }

    /**
     * Highlights the clicked nav button and shows the corresponding content panel.
     */
    private void selectNavButton(JButton btn) {
        // Reset all buttons to default look
        for (JButton b : navButtons) {
            b.setBackground(UITheme.SIDEBAR_BG);
            b.setFont(UITheme.SIDEBAR_FONT);
        }

        // Highlight selected button
        btn.setBackground(UITheme.SIDEBAR_SELECTED);
        btn.setFont(UITheme.SIDEBAR_FONT_SELECTED);
        currentlySelected = btn;

        // Show corresponding card
        String cardName = (String) btn.getClientProperty("cardName");
        contentCards.show(contentPanel, cardName);
    }
}
