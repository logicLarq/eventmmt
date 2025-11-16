package CollegeEventSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainGUI extends JFrame {
    private static final String DATA_FILE = "gui_event_manager.dat";
    private EventManager manager;

    
    private DefaultTableModel eventsTableModel;
    private DefaultTableModel participantsTableModel;

    public MainGUI() {
        super("College Event Management - GUI");
        loadManager();
        initUI();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void loadManager() {
        try {
            manager = EventManager.loadFromFile(DATA_FILE);
        } catch (Exception e) {
            manager = new EventManager();
        }
    }

    private void saveManager() {
        try {
            manager.saveToFile(DATA_FILE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not save data: " + e.getMessage());
        }
    }

    private void initUI() {
        JTabbedPane tabs = new JTabbedPane();

       
        JPanel createPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        JTextField nameField = new JTextField(20);
        JTextField dateField = new JTextField(12);
        JTextField venueField = new JTextField(20);
        JTextField maxField = new JTextField(5);
        JButton createBtn = new JButton("Create Event");

        c.gridx=0; c.gridy=0; createPanel.add(new JLabel("Name:"), c);
        c.gridx=1; createPanel.add(nameField, c);
        c.gridx=0; c.gridy=1; createPanel.add(new JLabel("Date (dd-mm-yyyy):"), c);
        c.gridx=1; createPanel.add(dateField, c);
        c.gridx=0; c.gridy=2; createPanel.add(new JLabel("Venue:"), c);
        c.gridx=1; createPanel.add(venueField, c);
        c.gridx=0; c.gridy=3; createPanel.add(new JLabel("Max Participants:"), c);
        c.gridx=1; createPanel.add(maxField, c);
        c.gridx=1; c.gridy=4; createPanel.add(createBtn, c);

        createBtn.addActionListener(ae -> {
            try {
                String n = nameField.getText().trim();
                String d = dateField.getText().trim();
                String v = venueField.getText().trim();
                int max = Integer.parseInt(maxField.getText().trim());
                if (n.isEmpty()) { JOptionPane.showMessageDialog(this, "Name required"); return; }
                Event e = manager.createEvent(n,d,v,max);
                refreshEventsTable();
                saveManager();
                JOptionPane.showMessageDialog(this, "Created: " + e);
                nameField.setText(""); dateField.setText(""); venueField.setText(""); maxField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Max participants must be integer");
            }
        });

        tabs.addTab("Create Event", createPanel);

       
        JPanel listPanel = new JPanel(new BorderLayout());
        eventsTableModel = new DefaultTableModel(new Object[]{"ID","Name","Date","Venue","Registered","Max"},0);
        JTable eventsTable = new JTable(eventsTableModel);
        refreshEventsTable();
        listPanel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);

        JButton deleteSelectedBtn = new JButton("Refresh");
        deleteSelectedBtn.addActionListener(e -> refreshEventsTable());
        listPanel.add(deleteSelectedBtn, BorderLayout.SOUTH);
        tabs.addTab("Events", listPanel);

        
        JPanel regPanel = new JPanel(new GridBagLayout());
        JTextField pnameField = new JTextField(18);
        JTextField prollField = new JTextField(12);
        JTextField pdeptField = new JTextField(12);
        JTextField peidField = new JTextField(6);
        JButton regBtn = new JButton("Register");

        c.gridx=0; c.gridy=0; regPanel.add(new JLabel("Event ID:"), c);
        c.gridx=1; regPanel.add(peidField, c);
        c.gridx=0; c.gridy=1; regPanel.add(new JLabel("Name:"), c);
        c.gridx=1; regPanel.add(pnameField, c);
        c.gridx=0; c.gridy=2; regPanel.add(new JLabel("Roll No:"), c);
        c.gridx=1; regPanel.add(prollField, c);
        c.gridx=0; c.gridy=3; regPanel.add(new JLabel("Department:"), c);
        c.gridx=1; regPanel.add(pdeptField, c);
        c.gridx=1; c.gridy=4; regPanel.add(regBtn, c);

        regBtn.addActionListener(ae -> {
            try {
                int eid = Integer.parseInt(peidField.getText().trim());
                String pn = pnameField.getText().trim();
                String pr = prollField.getText().trim();
                String pd = pdeptField.getText().trim();
                if (pn.isEmpty() || pr.isEmpty()) { JOptionPane.showMessageDialog(this,"Name and roll required"); return; }
                Participant p = new Participant(pn, pr, pd);
                boolean ok = manager.registerToEvent(eid, p);
                if (ok) {
                    saveManager();
                    refreshEventsTable();
                    JOptionPane.showMessageDialog(this, "Registered successfully");
                } else JOptionPane.showMessageDialog(this, "Failed to register (full/event not found/duplicate)");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Event ID must be integer");
            }
        });

        tabs.addTab("Register", regPanel);

        
        JPanel partPanel = new JPanel(new BorderLayout());
        participantsTableModel = new DefaultTableModel(new Object[]{"EventID","EventName","Participant"},0);
        JTable partsTable = new JTable(participantsTableModel);
        refreshParticipantsTable();
        partPanel.add(new JScrollPane(partsTable), BorderLayout.CENTER);

        JButton refreshParticipantsBtn = new JButton("Refresh");
        refreshParticipantsBtn.addActionListener(e -> refreshParticipantsTable());
        partPanel.add(refreshParticipantsBtn, BorderLayout.SOUTH);

        tabs.addTab("Participants", partPanel);

        add(tabs, BorderLayout.CENTER);

        // Window close -> save
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveManager();
            }
        });
    }

    private void refreshEventsTable() {
        eventsTableModel.setRowCount(0);
        for (Event ev : manager.getEvents()) {
            eventsTableModel.addRow(new Object[]{
                ev.getId(), ev.getName(), ev.getDate(), ev.getVenue(), ev.getRegisteredCount(), ev.getMaxParticipants()
            });
        }
    }

    private void refreshParticipantsTable() {
        participantsTableModel.setRowCount(0);
        for (Event ev : manager.getEvents()) {
            for (Participant p : ev.getParticipants()) {
                participantsTableModel.addRow(new Object[]{ev.getId(), ev.getName(), p.toString()});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}
