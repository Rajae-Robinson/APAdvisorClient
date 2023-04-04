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

import controller.Client;
import model.Complaint;
import model.Query;

public class AdvisorDashboard {
    JFrame frame = new DefaultFrame();
    JPanel mainPanel;
    JPanel cqPanel;
    JPanel tablePanel;
    JTable cqTable;
    JButton replyBtn;
    JButton videoCallBtn;
    JScrollPane scrollPane;
    JPanel buttonPanel;
    JButton complaintBtn;
    JButton queryBtn;
    List<Complaint> complaintsList;
    List<Query> queriesList;
    int cqID;
    String currentTable;
    private boolean recordSelected = false;

    public AdvisorDashboard() {
        initialiseComponents();
        addActionListeners();
        addComponents();
    }

    private void initialiseComponents() {
        //Request complaints and queries from server
//        clientC.sendAction("getComplaints");
//        complaintsList = clientC.receiveComplaintList();
//        clientQ.sendAction("getQueries");
//        queriesList = clientQ.receiveQueryList();


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

        String[] columnNames = {"-", "-", "-", "-"};

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        cqTable = new JTable(model);
        cqTable.getTableHeader().setReorderingAllowed(false); //Prevents rearranging of columns
        cqTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        cqTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        cqTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        cqTable.getColumnModel().getColumn(3).setPreferredWidth(400);

        //Prohibits resizing of columns in the table
        TableColumnModel columnModel = cqTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setResizable(false);
        }

        //Renames the relevant columns, and populates the table with complaint data once the complaints button is clicked
        complaintBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JButton clickedButton = (JButton) e.getSource();
                    if (clickedButton == complaintBtn) {
                        replyBtn.setEnabled(false);

                        System.out.println("Complaints button pressed...");
                        Client clientC = new Client();
                        clientC.sendAction("getComplaints");
                        complaintsList = clientC.receiveComplaintList();
                        model.setRowCount(0);

                        currentTable = "Complaint";
                        String[] columnNames = {"Complaint ID", "Student ID", "Category", "Details of Issue"};
                        model.setColumnIdentifiers(columnNames);

                        for (Complaint complaint : complaintsList) {
                            Object[] data = {complaint.getComplaintID(), complaint.getStudentID(), complaint.getCategory(), complaint.getDetails()};
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
                        replyBtn.setEnabled(false);

                        System.out.println("Queries button pressed...");
                        Client clientQ = new Client();
                        clientQ.sendAction("getQueries");
                        queriesList = clientQ.receiveQueryList();

                        model.setRowCount(0);

                        currentTable = "Query";
                        String[] columnNames = {"Query ID", "Student ID", "Category", "Details of Issue"};
                        model.setColumnIdentifiers(columnNames);

                        for (Query query : queriesList) {
                            Object[] data = {query.getQueryID(), query.getStudentID(), query.getCategory(), query.getDetails()};
                            model.addRow(data);
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
                    if (selectedRow != -1) {  // If a row is selected
                        // Get the data from the selected row
                        cqID = (int) cqTable.getValueAt(selectedRow, 0);
                        System.out.println("Record selected is #" + cqID);
                    }
                }
            }
        });

        //Initialise the scroll pane and set its view to the cqTable component
        scrollPane = new JScrollPane(cqTable);
        scrollPane.setPreferredSize(new Dimension(900, 500));
        tablePanel.add(scrollPane);
    }

    private void addActionListeners() {
        videoCallBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VCDisplay();
                frame.dispose();
            }
        });


        //Enables the reply button when a record is selected
        cqTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    replyBtn.setEnabled(true);
                }
            }
        });

        //Creates an instance of the response window once the reply button is clicked
        replyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                responseWindow();
            }
        });
    }

    private void addComponents() {
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

    private void responseWindow() {
        JFrame frame;
        JPanel responsePanel;
        JTextArea responseArea;
        JLabel responseLabel;
        JButton sendButton;
        JScrollPane scrollPane;

        //Initialise the frame
        frame = new JFrame();

        //Initialise the panel
        responsePanel = new JPanel(new BorderLayout());

        //Initialise the text area
        responseArea = new JTextArea();
        responseArea.setPreferredSize(new Dimension(400, 200));
        responseArea.setLineWrap(true);
        responseArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(responseArea);
        responsePanel.add(scrollPane, BorderLayout.CENTER);

        //Initialise the label
        responseLabel = new JLabel("Type your response here:");
        responseLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Initialise the button
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);

        //Setup the frame
        frame.setTitle("Response Window");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Prepare response info
                int responderID = Integer.parseInt(LoginScreen.loginID);
                String response = responseArea.getText();

                // Code to send message to server
                if (currentTable == "Complaint") {
                    Complaint complaint = new Complaint(cqID, responderID, response);
                    System.out.println("Sending response to complaint #" + cqID + "...");
                    System.out.println("complaintID: " + cqID + "\nresponderID: " + responderID
                    + "\nresponse: " + response);

                    Client clientCR = new Client();
                    clientCR.sendAction("respondComplaint");
                    clientCR.sendComplaintResponse(complaint);
                    System.out.println("Response sent!");
                }

                if (currentTable == "Query") {
                    Query query = new Query(cqID, responderID, response);
                    System.out.println("Sending response to query #" + cqID + "...");
                    System.out.println("queryID: " + cqID + "\nresponderID: " + responderID
                            + "\nresponse: " + response);

                    Client clientQR = new Client();
                    clientQR.sendAction("respondQuery");
                    clientQR.sendQueryResponse(query);
                    System.out.println("Response sent!");
                }

                JOptionPane.showMessageDialog(frame, "Message sent!", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                Window root = (Window) SwingUtilities.getRoot((JComponent) e.getSource());  //Gets the root frame of the 'source' which is
                                                                                            //the object on which the event occurred
                root.dispose(); // Disposes of the root frame (the ResponseWindow)
            }
        });

        //Uses a DocumentListener to detect changes in the text area and enable or disable the button
        //depending on whether the text area is empty or not. This is to avoid sending empty responses.
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

        responsePanel.add(responseLabel, BorderLayout.NORTH);
        responsePanel.add(sendButton, BorderLayout.SOUTH);
        frame.add(responsePanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AdvisorDashboard();
    }
}