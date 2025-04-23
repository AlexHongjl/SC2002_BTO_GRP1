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
    
    /**
     * Constructor
     * 
     * @param enquiryID of enquiry
     * @param applicantName of applicant
     * @param message of enquiry
     */
    public Enquiry(int enquiryID, String applicantName, String message) {
        this.enquiryID = enquiryID;
        this.applicantName = applicantName;
        this.message = message;
        this.response = "";
        this.status = true;
    }
    
    /**
     * Constructor
     * 
     * @param enquiryID of enquiry
     * @param applicantName of applicant
     * @param message of enquiry
     * @param projectId of project
     */
    public Enquiry(int enquiryID, String applicantName, String message, int projectId) {
        this.enquiryID = enquiryID;
        this.applicantName = applicantName;
        this.message = message;
        this.response = "";
        this.status = true;
        this.projectId = projectId;
    }
    
    /**
     * Displays enquiry details and message
     */
    public void display() {
        System.out.println("Enquiry ID: " + enquiryID);
        System.out.println("Applicant Name: " + applicantName);
        System.out.println("Message: " + message);
        System.out.println("Response: " + (response.isEmpty() ? "No response yet." : response));
        System.out.println("Status: " + (status ? "Open" : "Closed"));
        System.out.println("Project ID: " + projectId);
        System.out.println("-------------------------------");
    }
    
    /**
     * Displays enquiry by specified user
     * Checks target applicant name against enquiry applicant name
     * Returns true if target applicant's enquiry has been found, return false otherwise
     * 
     * @param applicantName target name
     * @return true or false
     */
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
    
    /**
     * Displays enquiry by specified enquiry ID
     * Checks target applicant name against enquiry ID
     * Returns true if target enquiry ID has been found, return false otherwise
     * 
     * @param enquiryID target ID
     * @return true or false
     */
    public static Enquiry getByID(int enquiryID) {
        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getEnquiryID() == enquiryID) {
                return Enquiries[i];
            }
        }
        return null; // not found
    }

    /**
     * Displays all created enquiries
     * Returns when no enquiries create, else display all enquiries
     */
    public static void displayAllEnquiries() {
        if (count == 0) {
            System.out.println("No enquiries found.");
            return;
        }

        for (int i = 0; i < count; i++) {
            Enquiries[i].display();
        }
    }

    /**
     * Creates an enquiry by passing in the message
     * Checks if maximum enquiry limit has been reached
     * Returns when list is full and add new enquiry otherwise
     * 
     * @param e enquiry message
     */
    public static void addEnquiry(Enquiry e) {
        if (count >= Enquiries.length) {
            System.out.println("Enquiry list full.");
            return;
        }
        Enquiries[count] = e;
        count++;
        System.out.println("Enquiry added successfully.");
    }

    /**
     * Edit enquiry by passing in enquiry ID to be edited and edited message
     * Updates enquiry with new message
     * 
     * @param enquiryID enquiry ID to be edited
     * @param newMessage updated message
     */
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

    /**
     * Delete enquiry by passing in enquiry ID
     * Checks if enquiry exists by checking enquiry ID
     * Return once deleted
     * Prints message when enquiry ID not found
     * 
     * @param enquiryID target ID
     */
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
    
    /**
     * Creates response to enquiries
     * Displays reply message
     * Sets status of false to close enquiry
     * 
     * @param replyMessage response message
     */
    public void reply(String replyMessage) {
        this.response = replyMessage;
        System.out.println("Reply Sent: " + replyMessage);
        this.setStatus(false);
    }
    
    /**
     * Changes status of enquiry message
     * If true, change to false
     * If false, change to true
     * Prints message when status successfully changed
     */
    public void changeStatus() {
        this.status = !this.status;
        System.out.println("Enquiry Status Changed: " + status);
    }
    
    
    /**
     * Getter function for getting enquiry ID
     * 
     * @return enquiryID of enquiry
     */
    public int getEnquiryID() {
        return enquiryID;
    }
    
    /**
     * Setter function to set enquiry message
     * 
     * @param enquiryID of enquiry
     */
    public void setEnquiryID(int enquiryID) {
        this.enquiryID = enquiryID;
    }
    
    /**
     * Getter function for getting Applicant's name
     * 
     * @return Applicant's name
     */
    public String getApplicantName() {
        return applicantName;
    }
    
    /**
     * Getter function for getting project ID
     * 
     * @return project ID
     */
    public int getProjectId() {
        return projectId;
    }
    
    /**
     * Setter function for setting Applicant's name
     * 
     * @param applicantName Applicant's name
     */
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    /**
     * Getter function for getting enquiry message
     * 
     * @return enquiry message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Setter function for setting enquiry message
     * 
     * @param message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Setter function for setting reply message
     * 
     * @param response reply message
     */
    public void setResponse(String response) {
        this.response = response;
    }
    
    /**
     * Setter function for setting enquiry status
     * 
     * @param status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    /**
     * Getter function for enquiry count
     * 
     * @return count
     */
    public static int getCount() {
        return count;
    }
    
    /**
     * Writes back and stores created enquiries to CSV file, ensuring data persistence
     * 
     * @param filename filename to be saved into
     */
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
    
    /**
     * Reads data from CSV and writes into an array list to store the information during run time
     * 
     * @param filename to be loaded
     */
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

