package com.campus.gui;

import com.campus.model.*;
import com.campus.repository.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class FacilityCrudPanel extends JPanel {
    private DataManager dataManager;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField idField, nameField, locationField;
    private JComboBox<String> typeBox;
    private JCheckBox statusCheck;
    private JButton addBtn;

    public FacilityCrudPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.setLayout(new BorderLayout());

        // Modular UI initialization
        initializeComponents();
        setupLayout();
        attachListeners();

        // Load initial data
        refreshTable();
    }

    /**
     * Initializes all UI components for adding and managing facilities.
     */
    private void initializeComponents() {
        typeBox = new JComboBox<>(new String[] { "Library", "Cafeteria", "Hostel" });
        idField = new JTextField(12);
        nameField = new JTextField(12);
        locationField = new JTextField(12);
        statusCheck = new JCheckBox("Is Open", true);
        addBtn = new JButton("Add Facility");

        // Set up main data table
        String[] columns = { "ID", "Type", "Name", "Location", "Status", "Op. Cost" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
    }

    /**
     * Organizes components into a categorized layout for better user experience.
     */
    private void setupLayout() {
        // --- FORM AREA ---
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Facility"));

        String[] labels = { "Type:", "Entity ID:", "Name:", "Location:", "Status:" };
        JComponent[] components = { typeBox, idField, nameField, locationField, statusCheck };
        FormUtility.buildForm(formPanel, labels, components, addBtn);

        // --- CENTER VIEW (Table) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, new JScrollPane(table));
        splitPane.setDividerLocation(300);
        this.add(splitPane, BorderLayout.CENTER);

        // --- CATEGORIZED ACTION PANEL ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Group 1: General
        bottomPanel.add(createActionGroup("Management", new String[]{"Refresh", "Edit Facility", "View Details"}, true));
        // Group 2: Library
        bottomPanel.add(createActionGroup("Library", new String[]{"Add Book", "Issue Book", "Return Book", "Library Report"}, false));
        // Group 3: Cafeteria
        bottomPanel.add(createActionGroup("Cafeteria", new String[]{"Add Menu Item", "Remove Menu Item", "View Menu Items"}, false));
        // Group 4: Hostel
        bottomPanel.add(createActionGroup("Hostel", new String[]{"Assign Room", "Unassign Room"}, false));

        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createActionGroup(String title, String[] actions, boolean includeDelete) {
        JPanel group = new JPanel(new FlowLayout(FlowLayout.LEFT));
        group.add(new JLabel(title + ": "));
        for (String act : actions) {
            JButton btn = new JButton(act);
            btn.addActionListener(e -> handleAction(act));
            group.add(btn);
        }
        if (includeDelete) {
            JButton delBtn = new JButton("Delete Facility");
            delBtn.setBackground(new Color(192, 57, 43));
            delBtn.setForeground(Color.WHITE);
            delBtn.addActionListener(e -> deleteFacility());
            group.add(delBtn);
        }
        return group;
    }

    /**
     * Attaches main interaction listeners.
     */
    private void attachListeners() {
        addBtn.addActionListener(e -> handleAddFacility());
    }

    private void handleAction(String cmd) {
        switch (cmd) {
            case "Refresh": refreshTable(); break;
            case "Edit Facility": editFacility(); break;
            case "View Details": viewFacilityDetails(); break;
            case "Add Book": addBookToLibrary(); break;
            case "Issue Book": issueBookFromLibrary(); break;
            case "Return Book": returnBookToLibrary(); break;
            case "Library Report": generateLibraryReport(); break;
            case "Add Menu Item": addMenuItemToCafeteria(); break;
            case "Remove Menu Item": removeMenuItemFromCafeteria(); break;
            case "View Menu Items": viewCafeteriaMenu(); break;
            case "Assign Room": assignHostelRoom(); break;
            case "Unassign Room": unassignHostelRoom(); break;
        }
    }

    /**
     * Handles the creation of various facility types.
     */
    private void handleAddFacility() {
        String type = (String) typeBox.getSelectedItem();
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        boolean isOpen = statusCheck.isSelected();

        if (id.isEmpty() || name.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all identity fields.");
            return;
        }

        // Duplicate ID check across all facility types
        boolean exists = false;
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            if (dataManager.getLibraryRepo().get(i).getEntityID().equalsIgnoreCase(id)) exists = true;
        }
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
            if (dataManager.getCafeteriaRepo().get(i).getEntityID().equalsIgnoreCase(id)) exists = true;
        }
        for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
            if (dataManager.getHostelRepo().get(i).getEntityID().equalsIgnoreCase(id)) exists = true;
        }

        if (exists) {
            JOptionPane.showMessageDialog(this, "Facility ID '" + id + "' is already in use.");
            return;
        }

        // Inheritance check and instantiation
        if ("Library".equals(type)) {
            Library lib = new Library(id, name, location, false, 1000.0, 50, "08:00-20:00", isOpen, new ArrayList<>(), 10, 500.0);
            dataManager.getLibraryRepo().addItem(lib);
        } else if ("Cafeteria".equals(type)) {
            Cafeteria caf = new Cafeteria(id, name, location, false, 2000.0, 100, "07:00-22:00", isOpen, new ArrayList<>(), 50, 10, 500);
            dataManager.getCafeteriaRepo().addItem(caf);
        } else if ("Hostel".equals(type)) {
            Hostel hos = new Hostel(id, name, location, false, 5000.0, 300, "24/7", isOpen, 100, 0, "Boys", 1000.0, new ArrayList<>());
            dataManager.getHostelRepo().addItem(hos);
        }

        clearInputFields();
        JOptionPane.showMessageDialog(this, type + " added successfully.");
        refreshTable();
    }

    private void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        locationField.setText("");
        statusCheck.setSelected(true);
    }

    private void addBookToLibrary() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Library from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Library")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Library.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        Library lib = null;
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            if (dataManager.getLibraryRepo().get(i).getEntityID().equals(id)) {
                lib = dataManager.getLibraryRepo().get(i);
                break;
            }
        }
        if (lib == null)
            return;

        JTextField titleF = new JTextField(10);
        JTextField isbnF = new JTextField(10);
        JTextField authorF = new JTextField(10);
        JTextField genreF = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Title:"));
        panel.add(titleF);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnF);
        panel.add(new JLabel("Author:"));
        panel.add(authorF);
        panel.add(new JLabel("Genre:"));
        panel.add(genreF);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Book book = new Book(titleF.getText().trim(), isbnF.getText().trim(),
                    authorF.getText().trim(), genreF.getText().trim());
            final Library finalLib = lib;
            String msg = finalLib.addBook(book);
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    private void generateLibraryReport() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Library from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Library")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Library.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            if (dataManager.getLibraryRepo().get(i).getEntityID().equals(id)) {
                Library lib = dataManager.getLibraryRepo().get(i);
                String report = lib.generateReport();
                JTextArea area = new JTextArea(report);
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scroll = new JScrollPane(area);
                scroll.setPreferredSize(new Dimension(500, 350));
                JOptionPane.showMessageDialog(this, scroll, "Library Report", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    private void addMenuItemToCafeteria() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Cafeteria from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Cafeteria")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Cafeteria.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
            if (dataManager.getCafeteriaRepo().get(i).getEntityID().equals(id)) {
                Cafeteria caf = dataManager.getCafeteriaRepo().get(i);
                String item = JOptionPane.showInputDialog(this, "Enter menu item name:");
                if (item != null && !item.trim().isEmpty()) {
                    final String finalItem = item.trim();
                    String msg = caf.addMenuItem(finalItem);
                    JOptionPane.showMessageDialog(this, msg);
                }
                return;
            }
        }
    }

    private void removeMenuItemFromCafeteria() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Cafeteria from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Cafeteria")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Cafeteria.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
            if (dataManager.getCafeteriaRepo().get(i).getEntityID().equals(id)) {
                Cafeteria caf = dataManager.getCafeteriaRepo().get(i);
                if (caf.getMenuItems().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Menu is empty for this cafeteria.");
                    return;
                }
                String[] menuArray = caf.getMenuItems().toArray(new String[0]);
                String selected = (String) JOptionPane.showInputDialog(this,
                        "Select item to remove:",
                        "Remove Menu Item", JOptionPane.PLAIN_MESSAGE, null, menuArray, menuArray[0]);
                if (selected != null) {
                    String msg = caf.removeMenuItem(selected);
                    JOptionPane.showMessageDialog(this, msg);
                }
                return;
            }
        }
    }

    private void viewCafeteriaMenu() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Cafeteria from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Cafeteria")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Cafeteria.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
            if (dataManager.getCafeteriaRepo().get(i).getEntityID().equals(id)) {
                Cafeteria caf = dataManager.getCafeteriaRepo().get(i);
                ArrayList<String> items = caf.getMenuItems();

                if (items.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No menu items added to " + caf.getName() + " yet.");
                    return;
                }

                String res = "Menu Items for " + caf.getName() + ":\n\n";
                for (int j = 0; j < items.size(); j++) {
                    res = res + "  " + (j + 1) + ". " + items.get(j) + "\n";
                }

                JTextArea area = new JTextArea(res);
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane scroll = new JScrollPane(area);
                scroll.setPreferredSize(new Dimension(350, 250));
                JOptionPane.showMessageDialog(this, scroll,
                        "Cafeteria Menu - " + caf.getName(), JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    private void assignHostelRoom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Hostel from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Hostel")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Hostel.");
            return;
        }

        if (dataManager.getStudentRepo().getSize() == 0) {
            JOptionPane.showMessageDialog(this, "No students available.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        Hostel hostel = null;
        for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
            if (dataManager.getHostelRepo().get(i).getEntityID().equals(id)) {
                hostel = dataManager.getHostelRepo().get(i);
                break;
            }
        }
        if (hostel == null)
            return;

        String[] studentNames = new String[dataManager.getStudentRepo().getSize()];
        for (int i = 0; i < dataManager.getStudentRepo().getSize(); i++) {
            Student s = dataManager.getStudentRepo().get(i);
            studentNames[i] = s.getStudentID() + " - " + s.getName();
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select Student:",
                "Assign Hostel Room", JOptionPane.PLAIN_MESSAGE, null, studentNames, studentNames[0]);

        if (selected != null) {
            String studentId = selected.split(" - ")[0];
            final Hostel finalHostel = hostel;
            for (int i = 0; i < dataManager.getStudentRepo().getSize(); i++) {
                if (dataManager.getStudentRepo().get(i).getStudentID().equals(studentId)) {
                    final Student s = dataManager.getStudentRepo().get(i);
                    String msg = finalHostel.assignRoom(s);
                    JOptionPane.showMessageDialog(this, msg);
                    return;
                }
            }
        }
    }

    private void unassignHostelRoom() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Hostel from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Hostel")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Hostel.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        Hostel hostel = null;
        for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
            if (dataManager.getHostelRepo().get(i).getEntityID().equals(id)) {
                hostel = dataManager.getHostelRepo().get(i);
                break;
            }
        }
        if (hostel == null) return;

        ArrayList<Student> residents = hostel.getStudents();
        if (residents.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students currently in this hostel.");
            return;
        }

        String[] studentNames = new String[residents.size()];
        for (int i = 0; i < residents.size(); i++) {
            studentNames[i] = residents.get(i).getStudentID() + " - " + residents.get(i).getName();
        }

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select student to unassign room:",
                "Unassign Room", JOptionPane.PLAIN_MESSAGE, null, studentNames, studentNames[0]);

        if (selected != null) {
            String studentId = selected.split(" - ")[0];
            final Hostel finalHostel = hostel;
            for (Student s : residents) {
                if (s.getStudentID().equals(studentId)) {
                    final Student toUnassign = s;
                    String msg = finalHostel.unassignRoom(toUnassign);
                    JOptionPane.showMessageDialog(this, msg);
                    return;
                }
            }
        }
    }

    private void issueBookFromLibrary() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Library from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Library")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Library.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        Library lib = null;
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            if (dataManager.getLibraryRepo().get(i).getEntityID().equals(id)) {
                lib = dataManager.getLibraryRepo().get(i);
                break;
            }
        }
        if (lib == null) return;

        ArrayList<Book> books = lib.getBooks();
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books available in this library.");
            return;
        }

        String[] bookOptions = new String[books.size()];
        for (int i = 0; i < books.size(); i++) {
            bookOptions[i] = books.get(i).getISBN() + " - " + books.get(i).getTitle();
        }

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select book to issue:",
                "Issue Book", JOptionPane.PLAIN_MESSAGE, null, bookOptions, bookOptions[0]);

        if (selected != null) {
            String isbn = selected.split(" - ")[0];
            final Library finalLib = lib;
            for (Book b : books) {
                if (b.getISBN().equals(isbn)) {
                    final Book toIssue = b;
                    String msg = finalLib.issueBook(toIssue);
                    JOptionPane.showMessageDialog(this, msg);
                    return;
                }
            }
        }
    }

    private void returnBookToLibrary() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a Library from the table.");
            return;
        }
        if (!tableModel.getValueAt(row, 1).equals("Library")) {
            JOptionPane.showMessageDialog(this, "Selected item is not a Library.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        Library lib = null;
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            if (dataManager.getLibraryRepo().get(i).getEntityID().equals(id)) {
                lib = dataManager.getLibraryRepo().get(i);
                break;
            }
        }
        if (lib == null) return;

        // In a real system, we'd know which book the student has.
        // Here we just ask for details of the book being returned.
        JTextField titleF = new JTextField(10);
        JTextField isbnF = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Book Title:"));
        panel.add(titleF);
        panel.add(new JLabel("Book ISBN:"));
        panel.add(isbnF);

        int result = JOptionPane.showConfirmDialog(this, panel, "Return Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            final Book toReturn = new Book(titleF.getText().trim(), isbnF.getText().trim(), "Unknown", "Unknown");
            final Library finalLib = lib;
            String msg = finalLib.returnBook(toReturn);
            JOptionPane.showMessageDialog(this, msg);
        }
    }

    private void editFacility() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a facility to edit.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        
        Facility facility = null;
        if (type.equals("Library")) {
            for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
                if (dataManager.getLibraryRepo().get(i).getEntityID().equals(id)) {
                    facility = dataManager.getLibraryRepo().get(i);
                    break;
                }
            }
        } else if (type.equals("Cafeteria")) {
            for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
                if (dataManager.getCafeteriaRepo().get(i).getEntityID().equals(id)) {
                    facility = dataManager.getCafeteriaRepo().get(i);
                    break;
                }
            }
        } else if (type.equals("Hostel")) {
            for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
                if (dataManager.getHostelRepo().get(i).getEntityID().equals(id)) {
                    facility = dataManager.getHostelRepo().get(i);
                    break;
                }
            }
        }

        if (facility == null) return;

        JTextField nF = new JTextField(facility.getName());
        JTextField lF = new JTextField(facility.getLocation());
        JCheckBox oC = new JCheckBox("Is Operational", facility.isOpen());

        JPanel p = new JPanel(new GridLayout(3, 2));
        p.add(new JLabel("Name:")); p.add(nF);
        p.add(new JLabel("Location:")); p.add(lF);
        p.add(new JLabel("Status:")); p.add(oC);

        int result = JOptionPane.showConfirmDialog(this, p, "Edit Facility", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                facility.setName(nF.getText());
                facility.setLocation(lF.getText());
                facility.setIsOpen(oC.isSelected());
                refreshTable();
                JOptionPane.showMessageDialog(this, "Facility updated.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteFacility() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a facility from the table first.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        String name = (String) tableModel.getValueAt(row, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete " + type + ": " + name + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (type.equals("Library")) {
                for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
                    if (dataManager.getLibraryRepo().get(i).getEntityID().equals(id)) {
                        dataManager.getLibraryRepo().removeItem(dataManager.getLibraryRepo().get(i));
                        break;
                    }
                }
            } else if (type.equals("Cafeteria")) {
                for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
                    if (dataManager.getCafeteriaRepo().get(i).getEntityID().equals(id)) {
                        dataManager.getCafeteriaRepo().removeItem(dataManager.getCafeteriaRepo().get(i));
                        break;
                    }
                }
            } else if (type.equals("Hostel")) {
                for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
                    if (dataManager.getHostelRepo().get(i).getEntityID().equals(id)) {
                        dataManager.getHostelRepo().removeItem(dataManager.getHostelRepo().get(i));
                        break;
                    }
                }
            }
            JOptionPane.showMessageDialog(this, type + " deleted.");
            refreshTable();
        }
    }

    private void viewFacilityDetails() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a facility from the table.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        String type = (String) tableModel.getValueAt(row, 1);
        String details = "";

        if (type.equals("Library")) {
            for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
                if (dataManager.getLibraryRepo().get(i).getEntityID().equals(id)) {
                    details = dataManager.getLibraryRepo().get(i).toString();
                    break;
                }
            }
        } else if (type.equals("Cafeteria")) {
            for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
                if (dataManager.getCafeteriaRepo().get(i).getEntityID().equals(id)) {
                    details = dataManager.getCafeteriaRepo().get(i).toString();
                    break;
                }
            }
        } else if (type.equals("Hostel")) {
            for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
                if (dataManager.getHostelRepo().get(i).getEntityID().equals(id)) {
                    details = dataManager.getHostelRepo().get(i).toString();
                    break;
                }
            }
        }

        if (!details.isEmpty()) {
            JTextArea area = new JTextArea(details);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area),
                    "Facility Details (toString)", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < dataManager.getLibraryRepo().getSize(); i++) {
            Library lib = dataManager.getLibraryRepo().get(i);
            tableModel.addRow(new Object[] { lib.getEntityID(), "Library", lib.getName(),
                    lib.getLocation(), lib.isOpen() ? "Open" : "Closed",
                    String.format("%.2f", lib.calculateOperationalCost()) });
        }
        for (int i = 0; i < dataManager.getCafeteriaRepo().getSize(); i++) {
            Cafeteria caf = dataManager.getCafeteriaRepo().get(i);
            tableModel.addRow(new Object[] { caf.getEntityID(), "Cafeteria", caf.getName(),
                    caf.getLocation(), caf.isOpen() ? "Open" : "Closed",
                    String.format("%.2f", caf.calculateOperationalCost()) });
        }
        for (int i = 0; i < dataManager.getHostelRepo().getSize(); i++) {
            Hostel hos = dataManager.getHostelRepo().get(i);
            tableModel.addRow(new Object[] { hos.getEntityID(), "Hostel", hos.getName(),
                    hos.getLocation(), hos.isOpen() ? "Open" : "Closed",
                    String.format("%.2f", hos.calculateOperationalCost()) });
        }
    }
}
