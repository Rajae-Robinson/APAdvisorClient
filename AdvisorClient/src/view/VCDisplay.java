/*
 * Author(s): Sydney Chambers
 */

package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VCDisplay {
    JFrame frame = new DefaultFrame();
    JPanel mainPanel;
    JPanel statusPanel;
    JPanel selectionPanel;
    JPanel selectionPanel2;
    JPanel videoChatPanel;
    JButton acceptCall;
    JButton declineCall;
    JButton returnBtn;
    JInternalFrame videoChatWindow;
    JRadioButton videoCallOnline;
    JRadioButton videoCallOffline;
    ButtonGroup statusButtonGroup;
    JComboBox<String> pendingRequests;
    String[] requests;

    public VCDisplay() {
        initialiseComponents();
        addActionListeners();
        addComponents();
    }

    private void initialiseComponents() {
        //Initialise the panels and define layouts
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusPanel.add(new JLabel("Video Call Availability"));
        selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectionPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        videoChatPanel = new JPanel(new BorderLayout());

        //Initialise the internal frame
        videoChatWindow = new JInternalFrame("Live Call with X", true, false, true, true);
        videoChatWindow.setPreferredSize(new Dimension(640, 480));
        videoChatWindow.setVisible(true);

        //Initialise the combo box
        requests = new String[]{"Student 1", "Student 2", "Student 3"};
        pendingRequests = new JComboBox<>(requests);
        pendingRequests.setEditable(false);

        //Initialise the buttons
        acceptCall = new JButton("Accept");
        declineCall = new JButton("Decline");
        returnBtn = new JButton("Return to Dashboard");
        videoCallOnline = new JRadioButton("Online");
        videoCallOffline = new JRadioButton("Offline");
        statusButtonGroup = new ButtonGroup();
    }

    private void addActionListeners() {
        videoCallOnline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Enable the components when videoCallOnline is selected
                pendingRequests.setEnabled(true);
                acceptCall.setEnabled(true);
                declineCall.setEnabled(true);
            }
        });

        videoCallOffline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Disable the components when videoCallOffline is selected
                pendingRequests.setEnabled(false);
                acceptCall.setEnabled(false);
                declineCall.setEnabled(false);
            }
        });

        returnBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Returning to main dashboard...");
                new AdvisorDashboard();
                frame.dispose();
            }
        });
    }

    private void addComponents() {
        statusButtonGroup.add(videoCallOnline);
        statusButtonGroup.add(videoCallOffline);
        videoCallOffline.setSelected(true);

        statusPanel.add(videoCallOnline);
        statusPanel.add(videoCallOffline);

        selectionPanel.add(pendingRequests);

        selectionPanel2.add(returnBtn);
        selectionPanel2.add(acceptCall);
        selectionPanel2.add(declineCall);

        videoChatPanel.add(videoChatWindow, BorderLayout.CENTER);
        videoChatPanel.add(selectionPanel2, BorderLayout.SOUTH);

        // Disable the components initially
        pendingRequests.setEnabled(false);
        acceptCall.setEnabled(false);
        declineCall.setEnabled(false);



        mainPanel.add(statusPanel, BorderLayout.NORTH);
        mainPanel.add(selectionPanel, BorderLayout.SOUTH);
        mainPanel.add(videoChatPanel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
