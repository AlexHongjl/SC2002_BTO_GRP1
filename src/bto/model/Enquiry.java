package bto.model;

public class Enquiry {
    private int enquiryID;
    private String applicantName;
    private String message;
    private String response;
    private boolean status;

    public Enquiry(int enquiryID, String applicantName, String message) {
        this.enquiryID = enquiryID;
        this.applicantName = applicantName;
        this.message = message;
        this.response = "";
        this.status = false;
    }

    public void reply(String replyMessage) {
        this.response = replyMessage;
        System.out.println("Reply Sent: " + replyMessage);
    }

    public void changeStatus() {
        this.status = !this.status;
        System.out.println("Enquiry Status Changed: " + status);
    }
    
    
    //------------------------------------------------------------------separate get set functions
    public int getEnquiryID() {
        return enquiryID;
    }

    public void setEnquiryID(int enquiryID) {
        this.enquiryID = enquiryID;
    }
    
    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

