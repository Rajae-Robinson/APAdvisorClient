/*
 * Author(s): Sydney Chambers
 */


package view;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;

import controller.Client;
import model.Complaint;
import model.Query;
import model.Student;

public class AdvisorDashboard {
    JFrame frame = new DefaultFrame();
    JButton replyBtn;
    JButton videoCallBtn;
    JButton complaintBtn;
    JButton queryBtn;
    JButton sendButton;
    JDialog dialog;
    JPanel mainPanel;
    JPanel cqPanel;
    JPanel tablePanel;
    JPanel buttonPanel;
    JPanel responsePanel;
    JPanel infoPanel;
    JScrollPane scrollPane;
    JTable cqTable;
    JTextArea responseArea;
    DefaultTableModel model;
    List<Complaint> complaintsList;
    List<Query> queriesList;
    int cqID;
    int studentID;
    String currentTable;
    String message;
    String name;
    String email;
    String contact;
    String category;
    String details;
    String[] columnNames;

    public AdvisorDashboard() {
        initialiseComponents();
        addActionListeners();
        addComponents();
    }

    private void initialiseComponents() {
        //Initialise the panels and define layouts
        mainPanel = new JPanel(new BorderLayout());
        cqPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cqPanel.add(new JLabel("Pending Complaints/Queries"));
        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //Initialise the buttons
        replyBtn = new JButton("Reply");
        videoCallBtn = new JButton("Join a Video Call");
        complaintBtn = new JButton("Complaints");
        queryBtn = new JButton("Queries");

        replyBtn.setPreferredSize(new Dimension(150, 50));
        replyBtn.setEnabled(false); // disable the reply button initially
        videoCallBtn.setPreferredSize(new Dimension(150, 50));
        complaintBtn.setPreferredSize(new Dimension(150, 50));
        queryBtn.setPreferredSize(new Dimension(150, 50));

        //Initialise the table
        columnNames = new String[]{"-", "-", "-", "-", "-", "-","-"};
        model = new DefaultTableModel(columnNames, 0);
        cqTable = new JTable(model);
        cqTable.getTableHeader().setReorderingAllowed(false); //Prevents rearranging columns
        cqTable.setDefaultEditor(Object.class, null); //Disables editing table cells
        cqTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Only allows one record to be selected

        //Initialise the scroll pane and set its view to the cqTable component
        scrollPane = new JScrollPane(cqTable);
        scrollPane.setPreferredSize(new Dimension(900, 500));
    }

    private void addActionListeners() {
        videoCallBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VCDisplay();
                frame.dispose();
            }
        });

        //Enables/Disables the reply button depending on whether a record is selected
        ListSelectionListener tableSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
            //Check if a row is selected
                if (cqTable.getSelectedRow() != -1) {
                    replyBtn.setEnabled(true); //Enable reply button
                } else {
                    replyBtn.setEnabled(false); //Disable reply button
                }
            }
        };

        cqTable.getSelectionModel().addListSelectionListener(tableSelectionListener);

        //Creates an instance of the response window once the reply button is clicked
        replyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //prevLocation = frame.getLocation();
                //centerX = prevLocation.x + frame.getWidth() / 2;
                //centerY = prevLocation.y + frame.getHeight() / 2;
                responseWindow(studentID, name, email, contact, category, details);
            }
        });


        //Renames the relevant columns, and populates the table with complaint data once the complaints button is clicked
        complaintBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JButton clickedButton = (JButton) e.getSource();
                    if (clickedButton == complaintBtn) {
                        System.out.println("Complaints button pressed...");
                        Client clientC = new Client();
                        clientC.sendAction("getComplaintsForAdvisor");
                        clientC.sendID(Integer.parseInt(LoginScreen.loginID));
                        complaintsList = clientC.receiveComplaintList();
                        model.setRowCount(0);

                        currentTable = "Complaint";
                        String[] columnNames = {"Complaint ID", "Student ID", "Name", "Email", "Contact #", "Complaint Category",
                                "Details of Issue"};
                        model.setColumnIdentifiers(columnNames);

                        for (Complaint complaint : complaintsList) {
                            Client clientID = new Client();
                            clientID.sendAction("findStudent");
                            clientID.sendID(String.valueOf(complaint.getStudentID()));
                            Student student = clientID.receiveStudent();
                            complaint.setFirstName(student.getFirstName());
                            complaint.setLastName(student.getLastName());
                            complaint.setEmail(student.getEmail());
                            complaint.setContactNumber(student.getContactNumber());

                            Object[] data = {complaint.getComplaintID(), complaint.getStudentID(),
                                    complaint.getFirstName() + " " + complaint.getLastName(),complaint.getEmail(),
                                     complaint.getContactNumber(), complaint.getCategory(), complaint.getDetails()};

                            model.addRow(data);
                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });

        //Renames the relevant columns, and populates the table with query data once the queries button is clicked
        queryBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JButton clickedButton = (JButton) e.getSource();
                    if (clickedButton == queryBtn) {
                        System.out.println("Queries button pressed...");
                        Client clientQ = new Client();
                        clientQ.sendAction("getQueriesForAdvisor");
                        clientQ.sendID(Integer.parseInt(LoginScreen.loginID));
                        queriesList = clientQ.receiveQueryList();

                        model.setRowCount(0);

                        currentTable = "Query";
                        String[] columnNames = {"Query ID", "Student ID", "Name", "Email", "Contact #", "Query Category",
                                "Details of Issue"};
                        model.setColumnIdentifiers(columnNames);

                        for (Query query : queriesList) {
                            Client clientID = new Client();
                            clientID.sendAction("findStudent");
                            clientID.sendID(String.valueOf(query.getStudentID()));
                            Student student = clientID.receiveStudent();
                            query.setFirstName(student.getFirstName());
                            query.setLastName(student.getLastName());
                            query.setEmail(student.getEmail());
                            query.setContactNumber(student.getContactNumber());

                            Object[] data = {query.getQueryID(), query.getStudentID(),
                                    query.getFirstName() + " " + query.getLastName(),query.getEmail(),
                                    query.getContactNumber(), query.getCategory(), query.getDetails()};

                            model.addRow(data);
                        }
                    }
                } catch (NullPointerException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });

        //Keeps track of the ID attached to the currently selected record
        ListSelectionModel selectionModel = cqTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {  // Ignore extra events
                    int selectedRow = cqTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get the complaint/query deatils from the selected row
                        cqID = (int) cqTable.getValueAt(selectedRow, 0);
                        studentID = (int) cqTable.getValueAt(selectedRow, 1);
                        name = (String) cqTable.getValueAt(selectedRow, 2);
                        email = (String) cqTable.getValueAt(selectedRow, 3);
                        contact = (String) cqTable.getValueAt(selectedRow, 4);
                        category = (String) cqTable.getValueAt(selectedRow, 5);
                        details = (String) cqTable.getValueAt(selectedRow, 6);

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
    }

    private void addComponents() {
        tablePanel.add(scrollPane);

        buttonPanel.add(complaintBtn);
        buttonPanel.add(queryBtn);
        buttonPanel.add(replyBtn);
        buttonPanel.add(videoCallBtn);

        mainPanel.add(cqPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void responseWindow(int studentID, String name, String email, String contact, String category, String details) {
        //Initialise the dialog
        dialog = new JDialog(null, "Response Window", Dialog.ModalityType.APPLICATION_MODAL);

        //Initialise the panel
        responsePanel = new JPanel(new BorderLayout());
        infoPanel = new JPanel(new GridLayout(9, 1));

        //Initialise the labels
        JLabel cqLbl = new JLabel();

        if (Objects.equals(currentTable, "Complaint")) {
            cqLbl.setText("Complaint ID: " + cqID);
        }

        if (Objects.equals(currentTable, "Query")) {
            cqLbl.setText("Query ID: " + cqID);
        }

        JLabel stuIDLbl = new JLabel("Student's ID#: " + studentID);
        JLabel stuNameLbl = new JLabel("Student's Name: " + name);
        JLabel stuEmlLbl = new JLabel("Student's Email: " + email);
        JLabel stuConLbl = new JLabel("Student's Contact#: " + contact);
        JLabel catLbl = new JLabel("Category: " + category);
        JLabel dtLbl = new JLabel("Details: " + details);
        JLabel emptyLbl = new JLabel();
        JLabel responseLbl = new JLabel("Type your response here:");
        responseLbl.setHorizontalAlignment(SwingConstants.CENTER);

        //Initialise the text area
        responseArea = new JTextArea();
        responseArea.setPreferredSize(new Dimension(400, 200));
        responseArea.setLineWrap(true);
        responseArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(responseArea);
        responsePanel.add(scrollPane, BorderLayout.CENTER);
        responsePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Initialise the button
        sendButton = new JButton("Send");
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
                    message = "Are you sure you want to send the following response for complaint #" + cqID + "?\n\n" +
                            "complaintID: " + cqID + "\nresponderID: " + responderID + "\nresponse: " + response;
                }

                if (Objects.equals(currentTable, "Query")) {
                    message = "Are you sure you want to send the following response for query #" + cqID + "?\n\n" +
                            "queryID: " + cqID + "\nresponderID: " + responderID + "\nresponse: " + response;
                }

                //Code to send message to server
                int result = JOptionPane.showConfirmDialog(frame, message, "Confirmation", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    if (Objects.equals(currentTable, "Complaint")) {
                        Complaint complaint = new Complaint(cqID, responderID, response);
                        System.out.println("\nSending response to complaint #" + cqID + "...");
                        System.out.println("complaintID: " + cqID + "\nresponderID: " + responderID
                                + "\nresponse: " + response);

                        Client clientCR = new Client();
                        clientCR.sendAction("respondComplaint");
                        clientCR.sendComplaintResponse(complaint);
                        System.out.println("Response sent!\n");
                    }

                    if (Objects.equals(currentTable, "Query")) {
                        Query query = new Query(cqID, responderID, response);
                        System.out.println("\nSending response to query #" + cqID + "...");
                        System.out.println("queryID: " + cqID + "\nresponderID: " + responderID
                                + "\nresponse: " + response);

                        Client clientQR = new Client();
                        clientQR.sendAction("respondQuery");
                        clientQR.sendQueryResponse(query);
                        System.out.println("Response sent!\n");
                    }

                    JOptionPane.showMessageDialog(frame, "Message sent!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
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
        infoPanel.add(cqLbl);
        infoPanel.add(stuIDLbl);
        infoPanel.add(stuNameLbl);
        infoPanel.add(stuEmlLbl);
        infoPanel.add(stuConLbl);
        infoPanel.add(catLbl);
        infoPanel.add(dtLbl);
        infoPanel.add(emptyLbl);
        infoPanel.add(responseLbl);

        responsePanel.add(infoPanel, BorderLayout.NORTH);
        responsePanel.add(sendButton, BorderLayout.SOUTH);
        dialog.add(responsePanel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new AdvisorDashboard();
    }
}