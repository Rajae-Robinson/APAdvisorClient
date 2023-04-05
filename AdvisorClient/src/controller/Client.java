package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import model.Complaint;
import model.Query;
import model.Advisor;
import model.Student;

public class Client {
    private static final int PORT = 8888;
    private static final String HOST = "127.0.0.1";
    private static Socket connectionSocket;
    private ObjectOutputStream objOS;
    private ObjectInputStream objIS;
    private String action = "";

    public Client() {
        this.createConnection();
        this.configureStreams();
    }

    private void createConnection() {
        try {
            connectionSocket = new Socket(HOST, PORT);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private void configureStreams() {
        try {
            objOS = new ObjectOutputStream(connectionSocket.getOutputStream());
            objIS = new ObjectInputStream(connectionSocket.getInputStream());
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            objOS.close();
            objIS.close();
            connectionSocket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAction(String action) {
        this.action = action;
        try {
            objOS.writeObject(action);
            objOS.flush();
        } catch(IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void sendComplaintResponse(Complaint complaint) {
        try {
            objOS.writeObject(complaint);
            objOS.flush();
        } catch(IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void sendQueryResponse(Query query) {
        try {
            objOS.writeObject(query);
            objOS.flush();
        } catch(IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void sendID(int id) {
        try {
            objOS.writeObject(id);
            objOS.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendID(String id) {
        try {
            objOS.writeObject(id);
            objOS.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void authenticate(String id, String password) throws IOException {
        objOS.writeObject(id);
        objOS.writeObject(password);
        objOS.flush();
    }

    public boolean receiveAuthResp() {
        boolean authenticated = false;
        try {
            authenticated = (boolean) objIS.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return authenticated;
    }

    public Student receiveStudent() {
        Student student = new Student();
        try {
            student = (Student) objIS.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return student;
    }

    public Query receiveQuery() {
        Query query = new Query();
        try {
            query = (Query) objIS.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return query;
    }

    public Complaint receiveComplaint() {
        Complaint complaint = new Complaint();
        try {
            complaint = (Complaint) objIS.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return complaint;
    }

    public List<Complaint> receiveComplaintList() {
        List<Complaint> complaints = null;
        try {
            complaints = (List<Complaint>) objIS.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return complaints;
    }

    public List<Query> receiveQueryList() {
        List<Query> queries = null;
        try {
            queries = (List<Query>) objIS.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return queries;
    }
}
