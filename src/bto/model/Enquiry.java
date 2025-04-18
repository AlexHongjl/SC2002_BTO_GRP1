package bto.model;

import bto.model.Enquiry;

public class Enquiry {
    private int enquiryID;
    private String applicantName;
    private String message;
    private String response;// sld be a list?
    private boolean status;//true means open for reply?
    private int projectId;//still need? since enquiry not by project anymore
    
    private static Enquiry[] Enquiries = new Enquiry[100];
    private static int count = 0;

    public Enquiry(int enquiryID, String applicantName, String message) {
        this.enquiryID = enquiryID;
        this.applicantName = applicantName;
        this.message = message;
        this.response = "";
        this.status = true;
    }

    public Enquiry(int enquiryID, String applicantName, String message, int projectId) {
        this.enquiryID = enquiryID;
        this.applicantName = applicantName;
        this.message = message;
        this.response = "";
        this.status = true;
        this.projectId = projectId;
    }
    
    public void display() {
        System.out.println("Enquiry ID: " + enquiryID);
        System.out.println("Applicant Name: " + applicantName);
        System.out.println("Message: " + message);
        System.out.println("Response: " + (response.isEmpty() ? "No response yet." : response));
        System.out.println("Status: " + (status ? "Open" : "Closed"));
        System.out.println("Project ID: " + projectId);
        System.out.println("-------------------------------");
    }
    
    public static boolean displayByUser(String applicantName) {
        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getApplicantName().equals(applicantName)) {
                Enquiries[i].display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No enquiries found for user: " + applicantName);
        }

        return found;
    }
    
    public static Enquiry getByID(int enquiryID) {
        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getEnquiryID() == enquiryID) {
                return Enquiries[i];
            }
        }
        return null; // not found
    }

    // Display all enquiries
    public static void displayAllEnquiries() {
        if (count == 0) {
            System.out.println("No enquiries found.");
            return;
        }

        for (int i = 0; i < count; i++) {
            Enquiries[i].display();
        }
    }

    // Add enquiry
    public static void addEnquiry(Enquiry e) {
        if (count >= Enquiries.length) {
            System.out.println("Enquiry list full.");
            return;
        }
        Enquiries[count] = e;
        count++;
        System.out.println("Enquiry added successfully.");
    }

    // Edit enquiry
    public static void editEnquiry(int enquiryID, String newMessage) {
        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getEnquiryID() == enquiryID) {
                Enquiries[i].setMessage(newMessage);
                System.out.println("Enquiry " + enquiryID + " updated.");
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }

    // Delete enquiry
    public static void deleteEnquiry(int enquiryID) {
        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getEnquiryID() == enquiryID) {
                for (int j = i; j < count - 1; j++) {
                    Enquiries[j] = Enquiries[j + 1];
                }
                Enquiries[count - 1] = null;
                count--;
                System.out.println("Enquiry " + enquiryID + " deleted.");
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }
    
    
    public void reply(String replyMessage) {
        this.response = replyMessage;
        System.out.println("Reply Sent: " + replyMessage);
        this.setStatus(false);
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
    
    public int getProjectId() {
        return projectId;
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
    
    public static int getCount() {
        return count;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
}

