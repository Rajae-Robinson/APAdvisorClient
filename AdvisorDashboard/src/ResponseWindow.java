import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class ResponseWindow {

    private JFrame frame;
    private JPanel responsePanel;
    private JTextArea responseArea;
    private JLabel responseLabel;
    private JButton sendButton;
    JScrollPane scrollPane;

    public ResponseWindow() {
        initialiseComponents();
        addActionListeners();
        addComponents();
    }

    private void initialiseComponents() {
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
    }

    private void addActionListeners() {
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // Code to send message to server...

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
                updateSendButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSendButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSendButtonState();
            }
        });
    }

    private void addComponents() {
        responsePanel.add(responseLabel, BorderLayout.NORTH);
        responsePanel.add(sendButton, BorderLayout.SOUTH);
        frame.add(responsePanel);
        frame.setVisible(true);
    }

    private void updateSendButtonState() {
        if (responseArea.getText().trim().isEmpty()) {
            sendButton.setEnabled(false);
        } else {
            sendButton.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        new ResponseWindow();
    }
}
