/*
 * Author(s): Rajae Robinson, Sydney Chambers
 */


package model;

import java.util.Date;
import java.io.Serializable;

public class Complaint implements Serializable {
    private static final long serialVersionUID = -8928497947145342486L;
    private int complaintID;
    private int studentID;
    private String category;
    private String details;
    private Date responseDate;
    private Integer responderID;
    private String response;

    public Complaint() {
        this.complaintID = 0;
        this.responderID = 0000000;
        this.response = " - ";
    }

    public Complaint (int complaintID, Integer responderID, String response) {
        this.complaintID = complaintID;
        this.responderID = responderID;
        this.response = response;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public Integer getResponderID() {
        return responderID;
    }

    public void setResponderID(Integer responderID) {
        this.responderID = responderID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Complaint [complaintID=" + complaintID + ", responderID=" + responderID
                + ", response=" + response + "]";
    }
}
