package model;

import java.io.Serializable;

public class Advisor implements Serializable {
    private static final long serialVersionUID = 1L;
    private int advisorID;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private int supervisorID;

    public Advisor() {
        this.advisorID = 1801609;
        this.firstName = "Owen";
        this.lastName = "Lewis";
        this.email = "owenlewis01@gmail.com";
        this.contactNumber = "282-0763";
        this.supervisorID = 1991709;
    }

    public Advisor(int advisorID, String firstName, String lastName, String email, String contactNumber, int supervisorID) {
        this.advisorID = advisorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.supervisorID = supervisorID;
    }

    public int getAdvisorID() {
        return advisorID;
    }

    public void setAdvisorID(int advisorID) {
        this.advisorID = advisorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getSupervisorID() {
        return supervisorID;
    }

    public void setSupervisorID(int supervisorID) {
        this.supervisorID = supervisorID;
    }

    @Override
    public String toString() {
        return "Advisor [advisorID=" + advisorID + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
                + email + ", contactNumber=" + contactNumber + ", supervisorID=" + supervisorID + "]";
    }
}
