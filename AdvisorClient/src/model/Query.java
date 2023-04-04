/*
 * Author(s): Rajae Robinson, Sydney Chambers
 */


package model;

import java.io.Serializable;
import java.util.Date;

public class Query implements Serializable {
    private static final long serialVersionUID = 8485189943949795110L;

    private int queryID;
    private int studentID;
    private String category;
    private String details;
    private Date responseDate;
    private Integer responderID;
    private String response;


    public Query() {
        this.queryID = 0;
        this.responderID = 0000000;
        this.response = " - ";
    }

    public Query(int queryID, Integer responderID, String response) {
        this.queryID = queryID;
        this.responderID = responderID;
        this.response = response;
    }

    public int getQueryID() {
        return queryID;
    }

    public void setQueryID(int queryID) {
        this.queryID = queryID;
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
        return "Query [queryID=" + queryID + ", responderID=" + responderID + ", response=" + response
                + "]";
    }
}
