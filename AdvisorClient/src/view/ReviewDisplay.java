/*
 * Author(s): Sydney Chambers
 */

package view;

import controller.Client;
import model.Complaint;
import model.Query;
import model.Student;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class ReviewDisplay {
    JFrame frame = new DefaultFrame();
    JScrollPane cqPane;
    JTable cqTable;
    JButton pastCBtn;
    JButton pastQBtn;
    JButton editBtn;
    JButton reviewBtn;
    JButton returnBtn;
    JDialog dialog;
    JPanel mainPanel;
    JPanel cqPanel;
    JPanel tablePanel;
    JPanel buttonPanel;
    DefaultTableModel model;
    String currentTable;
    String message;
    String prevResponse;
    String[] columnNames;
    List<Complaint> pastComplaintsList;
    List<Query> pastQueriesList;
    int cqID;
    public ReviewDisplay() {
        initialiseComponents();
        addActionListeners();
        addComponents();
    }

    private void initialiseComponents() {
        //Initialise the panels and define layouts
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cqPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cqPanel.add(new JLabel("Previously Addressed Complaints/Queries"));
        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Initialise the buttons
        pastCBtn = new JButton("Get Past Complaints");
        pastQBtn = new JButton("Get Past Queries");
        reviewBtn = new JButton("View Details");
        editBtn = new JButton("Edit Response");
        returnBtn = new JButton("Return to Dashboard");


        pastCBtn.setPreferredSize(new Dimension(150, 50));
        pastQBtn.setPreferredSize(new Dimension(150, 50));
        pastCBtn.setPreferredSize(new Dimension(150, 50));
        reviewBtn.setPreferredSize(new Dimension(150, 50));
        reviewBtn.setEnabled(false); // disable the reply button initially
        editBtn.setPreferredSize(new Dimension(150, 50));
        editBtn.setEnabled(false); // disable the reply button initially
        pastQBtn.setPreferredSize(new Dimension(150, 50));
        returnBtn.setPreferredSize(new Dimension(200, 50));

        //Initialise the table
        columnNames = new String[]{"-", "-", "-", "-"};
        model = new DefaultTableModel(columnNames, 0);
        cqTable = new JTable(model);
        cqTable.getTableHeader().setReorderingAllowed(false); //Prevents rearranging columns
        cqTable.setDefaultEditor(Object.class, null); //Disables editing table cells
        cqTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Only allows one record to be selected

        //Initialise the scroll pane and set its view to the cqTable component
        cqPane = new JScrollPane(cqTable);
        cqPane.setPreferredSize(new Dimension(900, 500));
    }

    private void addActionListeners() {
        //Enables/Disables the reply button depending on whether a record is selected
        ListSelectionListener tableSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                //Check if a row is selected
                if (cqTable.getSelectedRow() != -1) {
                    reviewBtn.setEnabled(true); //Enable view details button
                    editBtn.setEnabled(true); //Enable edit response button
                } else {
                    reviewBtn.setEnabled(false); //Disable view details button
                    editBtn.setEnabled(false); //Disable edit response button
                }
            }
        };

        cqTable.getSelectionModel().addListSelectionListener(tableSelectionListener);

        //Renames the relevant columns, and populates the table with past complaint data once the relevant button is clicked
        pastCBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JButton clickedButton = (JButton) e.getSource();
                    if (clickedButton == pastCBtn) {
                        System.out.println("Past complaints button pressed...");
                        Client clientC = new Client();
                        clientC.sendAction("getComplaintsForAdvisor");
                        clientC.sendID(Integer.parseInt(LoginScreen.loginID));
                        pastComplaintsList = clientC.receiveComplaintList();
                        model.setRowCount(0);

                        currentTable = "Complaint";
                        String[] columnNames = {"Complaint ID", "Complaint Category", "Details of Issue", "Response"};
                        model.setColumnIdentifiers(columnNames);

                        for (Complaint complaint : pastComplaintsList) {
                            Client clientID = new Client();
                            clientID.sendAction("findStudent");
                            clientID.sendID(String.valueOf(complaint.getStudentID()));
                            Student student = clientID.receiveStudent();
                            complaint.setFirstName(student.getFirstName());
                            complaint.setLastName(student.getLastName());
                            complaint.setEmail(student.getEmail());
                            complaint.setContactNumber(student.getContactNumber());

                            if (complaint.getResponse() != null) {
                                Object[] data = {complaint.getComplaintID(), complaint.getCategory(), 
                                        complaint.getDetails(), complaint.getResponse()};

                                model.addRow(data);
                            }
                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });

        //Renames the relevant columns, and populates the table with past query data once the relevant button is clicked
        pastQBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JButton clickedButton = (JButton) e.getSource();
                    if (clickedButton == pastQBtn) {
                        System.out.println("Past queries button pressed...");
                        Client clientQ = new Client();
                        clientQ.sendAction("getQueriesForAdvisor");
                        clientQ.sendID(Integer.parseInt(LoginScreen.loginID));
                        pastQueriesList = clientQ.receiveQueryList();

                        model.setRowCount(0);

                        currentTable = "Query";
                        String[] columnNames = {"Query ID", "Query Category", "Details of Issue", "Response"};
                        model.setColumnIdentifiers(columnNames);

                        for (Query query : pastQueriesList) {
                            Client clientID = new Client();
                            clientID.sendAction("findStudent");
                            clientID.sendID(String.valueOf(query.getStudentID()));
                            Student student = clientID.receiveStudent();
                            query.setFirstName(student.getFirstName());
                            query.setLastName(student.getLastName());
                            query.setEmail(student.getEmail());
                            query.setContactNumber(student.getContactNumber());

                            if (query.getResponse() != null) {
                                Object[] data = {query.getQueryID(), query.getCategory(), 
                                        query.getDetails(), query.getResponse()};

                                model.addRow(data);
                            }
                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });

        ListSelectionModel selectionModel = cqTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {  // Ignore extra events
                    int selectedRow = cqTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get the complaint/query details from the selected row
                        cqID = (int) cqTable.getValueAt(selectedRow, 0);
                        prevResponse = (String) cqTable.getValueAt(selectedRow, 3);

                        if (Objects.equals(currentTable, "Complaint")) {
                            System.out.println("Complaint #" + cqID + " is currently selected");
                        }

                        if (Objects.equals(currentTable, "Query")) {
                            System.out.println("Query #" + cqID + " is currently selected");
                        }
                    }
                }
            }
        });

        reviewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("View details button pressed...");

                // Show information message dialog with field values
                if (Objects.equals(currentTable, "Complaint")) {
                    for (Complaint complaint : pastComplaintsList) {
                        if (complaint.getComplaintID() == cqID) {
                                String id = "Complaint ID: ";
                            String message = id + complaint.getComplaintID()
                                    + "\nStudent's ID: " + complaint.getStudentID()
                                    + "\nStudent's Name: " + complaint.getFirstName() + " " + complaint.getLastName()
                                    + "\nStudent's Contact Details: [Email: " + complaint.getEmail() + "] [Phone: " + complaint.getContactNumber() + "] "
                                    + "\nComplaint Category: " + complaint.getCategory()
                                    + "\nComplaint Details: " + complaint.getDetails()
                                    + "\nYour response: " + complaint.getResponse();

                            JOptionPane.showMessageDialog(frame, message, "Review", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }

                if (Objects.equals(currentTable, "Query")) {
                    for (Query query : pastQueriesList) {
                        if (query.getQueryID() == cqID) {
                            String id = "Query ID: ";
                            String message = id + query.getQueryID()
                                    + "\nStudent's ID: " + query.getStudentID()
                                    + "\nStudent's Name: " + query.getFirstName() + " " + query.getLastName()
                                    + "\nStudent's Contact Details: [Email: " + query.getEmail() + "] [Phone: " + query.getContactNumber() + "] "
                                    + "\nQuery Category: " + query.getCategory()
                                    + "\nQuery Details: " + query.getDetails()
                                    + "\nYour response: " + query.getResponse();

                            JOptionPane.showMessageDialog(frame, message, "Review", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Returning to main dashboard...");
                new AdvisorDashboard();
                frame.dispose();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Edit button pressed...");
                responseWindow();
            }
        });
    }

    private void addComponents() {
        tablePanel.add(cqPane);

        buttonPanel.add(pastCBtn);
        buttonPanel.add(pastQBtn);
        buttonPanel.add(reviewBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(returnBtn);

        mainPanel.add(cqPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void responseWindow() {
        //Initialise the dialog
        dialog = new JDialog(null, "Response Window", Dialog.ModalityType.APPLICATION_MODAL);

        //Initialise the panel
        JPanel responsePanel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));

        //Initialise the labels
        JLabel prevResponseLbl = new JLabel("Your previous response: " + prevResponse);
        JLabel emptyLbl = new JLabel();
        JLabel responseLbl = new JLabel("Type your new response here:");
        responseLbl.setHorizontalAlignment(SwingConstants.CENTER);

        //Initialise the text area
        JTextArea responseArea = new JTextArea();
        responseArea.setPreferredSize(new Dimension(400, 200));
        responseArea.setLineWrap(true);
        responseArea.setWrapStyleWord(true);
        responseArea.setText(prevResponse);
        JScrollPane scrollPane = new JScrollPane(responseArea);
        responsePanel.add(scrollPane, BorderLayout.CENTER);
        responsePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Initialise the button
        JButton sendButton = new JButton("Send");
        sendButton.setEnabled(false);

        //Setup the dialog
        dialog.setSize(800, 600);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(frame);

        //Sends complaint.query response to server
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Prepare response info
                int responderID = Integer.parseInt(LoginScreen.loginID);
                String response = responseArea.getText();

                //Sets complaint-specific or query-specific message to be displayed on confirmation dialog
                if (Objects.equals(currentTable, "Complaint")) {
                    message = "Are you sure you want to send the following updated response for complaint ticket #" + cqID + "?\n\n" +
                            "Response: '" + response + "'";
                }

                if (Objects.equals(currentTable, "Query")) {
                    message = "Are you sure you want to send the following updated response for query ticket #" + cqID + "?\n\n" +
                            "Response: '" + response + "'";
                }

                //Code to send message to server
                int result = JOptionPane.showConfirmDialog(frame, message, "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    if (Objects.equals(currentTable, "Complaint")) {
                        Complaint complaint = new Complaint(cqID, responderID, response);
                        System.out.println("\nSending updated response to complaint #" + cqID + "...");
                        System.out.println("complaintID: " + cqID + "\nresponderID: " + responderID
                                + "\nresponse: " + response);

                        Client clientCR = new Client();
                        clientCR.sendAction("respondComplaint");
                        clientCR.sendComplaintResponse(complaint);
                        System.out.println("Response sent!\n");
                    }

                    if (Objects.equals(currentTable, "Query")) {
                        Query query = new Query(cqID, responderID, response);
                        System.out.println("\nSending updated response to query #" + cqID + "...");
                        System.out.println("queryID: " + cqID + "\nresponderID: " + responderID
                                + "\nresponse: " + response);

                        Client clientQR = new Client();
                        clientQR.sendAction("respondQuery");
                        clientQR.sendQueryResponse(query);
                        System.out.println("Response sent!\n");
                    }

                    JOptionPane.showMessageDialog(frame, "Response sent!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    Window root = (Window) SwingUtilities.getRoot((JComponent) e.getSource());  //Gets the root frame of the 'source' which is
                                                                                                //the object on which the event occurred
                    root.dispose(); // Disposes of the root frame (the ResponseWindow)
                }
            }
        });

        //Uses a DocumentListener to enable or disable the send button depending on whether
        //the text area is empty or not. This is to avoid sending empty responses.
        responseArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (responseArea.getText().trim().isEmpty()) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (responseArea.getText().trim().isEmpty()) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (responseArea.getText().trim().isEmpty()) {
                    sendButton.setEnabled(false);
                } else {
                    sendButton.setEnabled(true);
                }
            }
        });

        //Add components
        infoPanel.add(prevResponseLbl);
        infoPanel.add(emptyLbl);
        infoPanel.add(responseLbl);

        responsePanel.add(infoPanel, BorderLayout.NORTH);
        responsePanel.add(sendButton, BorderLayout.SOUTH);
        dialog.add(responsePanel);
        dialog.setVisible(true);
    }
}
