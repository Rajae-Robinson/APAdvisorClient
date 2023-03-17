package view;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;

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

    public AdvisorDashboard() {
        initialiseComponents();
        addActionListeners();
        addComponents();

    }

    private void initialiseComponents() {
        //Affix names to each column and add test data for display purposes
        String[] columnNames = {"Student ID", "Name", "Email Address", "Contact Number", "Type of Issue", "Details of Issue"};
        String[][] data = {{"1001899", "John Doe", "johndoe@email.com", "+1 (123) 456-7890", "Complaint", "My tutor has barred me from classes."}};

        //Initialise the panels and define layouts
        mainPanel = new JPanel(new BorderLayout());
        cqPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cqPanel.add(new JLabel("Pending Complaints/Queries"));
        tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        //Initialise the buttons
        replyBtn = new JButton("Reply");
        videoCallBtn = new JButton("Join a Video Call");
        replyBtn.setPreferredSize(new Dimension(150, 50));
        replyBtn.setEnabled(false); // disable the reply button initially
        videoCallBtn.setPreferredSize(new Dimension(150, 50));

        //Initialise the table and set column widths individually
        cqTable = new JTable(data, columnNames);
        cqTable.getTableHeader().setReorderingAllowed(false); //Prevents rearranging of columns
        cqTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        cqTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        cqTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        cqTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        cqTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        cqTable.getColumnModel().getColumn(5).setPreferredWidth(400);

        //Loops through all columns in the table and prohibits resizing of them
        TableColumnModel columnModel = cqTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setResizable(false);
        }

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
                new ResponseWindow();
            }
        });
    }

    private void addComponents() {
        buttonPanel.add(replyBtn);
        buttonPanel.add(videoCallBtn);

        mainPanel.add(cqPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AdvisorDashboard();
    }
}