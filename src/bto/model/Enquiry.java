package bto.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import bto.model.Enquiry;

/**
 * The enquiry class stores the enquries created in a list Enquiries
 * Allows responses to the created enquiries
 * Able to display the project ID, enquiry ID, Applicant who created the enquiry, enquiry message and response, and status of the enquiry
 */
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
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public static int getCount() {
        return count;
    }
    
    //Data Persistence
    public static void saveEnquiriesToCSV(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("ID,Name,Message,Response,Status,ProjectID\n");
            for (int i = 0; i < count; i++) {
                Enquiry e = Enquiries[i];
                writer.write(e.enquiryID + "," + e.applicantName + "," +
                             e.message.replace(",", ";") + "," +
                             e.response.replace(",", ";") + "," +
                             e.status + "," + e.projectId + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadEnquiriesFromCSV(String filename) {
        count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null && count < Enquiries.length) {
                String[] parts = line.split(",");
                if (parts.length < 6) continue;
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String msg = parts[2].replace(";", ",");
                String resp = parts[3].replace(";", ",");
                boolean status = Boolean.parseBoolean(parts[4]);
                int projId = Integer.parseInt(parts[5]);

                Enquiry e = new Enquiry(id, name, msg, projId);
                e.setResponse(resp);
                e.setStatus(status);
                Enquiries[count++] = e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

