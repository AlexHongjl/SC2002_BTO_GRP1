package bto.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BTO application that holds all the information and methods for an Applicant's application
 * Able to display application details: User ID, Project ID, Unit type, Application Status, Last Update Status
 * Creates a BTO application for Applicant
 */
public class BTOapplication {
    private String userID;
    private int projectId;
    private String unitType; // "2-Room" or "3-Room"
    private String status;   // Pending approval,Pending withdrawal, Successful, Unsuccessful, Booked
    private String timestamp; // last status update
    private Applicant user;
    private String previousStatus;//"",Pending approval,Pending withdrawal, Successful, Unsuccessful
    
    private static ArrayList<BTOapplication> allApplications = new ArrayList<>();
    
    /**
     * Constructor
     * 
     * @param userID of Applicant
     * @param projectId of project
     * @param u user
     */
    public BTOapplication(String userID, int projectId, Applicant u) {
        this.userID = userID;
        this.projectId = projectId;
        this.status = "Pending Approval";
        this.unitType = "";
        this.timestamp = "";
        this.previousStatus="";
        user = u;
        Project project = Project.getProjectById(projectId);
        if (project != null) {
            project.addApplication(this);
        }
    }
    
    /**
     * Getter function for previous status
     * 
     * @return previous status
     */
    public String getPreviousStatus(){
        return this.previousStatus;
    }
    
    /**
     * Adds a BTO application into the array list of all applications
     * 
     * @param app created application
     */
    public static void addApplication(BTOapplication app) {
        allApplications.add(app);
    }
    
    /**
     * Displays all created BTO applications
     */
    public static void displayAll() {
        System.out.println("=== All BTO Applications ===");
        for (BTOapplication app : allApplications) {
            System.out.println(app);
        }
    }
    
    /**
     * Displays BTO information for specific applications
     */
    public void display() {
        System.out.println("=== BTO Application ===");
        System.out.println("User ID     : " + userID);
        System.out.println("Project ID  : " + projectId);
        System.out.println("Unit Type   : " + (unitType.isEmpty() ? "N/A" : unitType));
        System.out.println("Status      : " + status);
        System.out.println("Last Updated: " + (timestamp.isEmpty() ? "N/A" : timestamp));
        System.out.println("========================\n");
    }
    
    /**
     * Displays pending withdrawal applications
     * Returns true when an application with the pending withdrawal status has been found and false otherwise
     * 
     * @return true or false
     */
    public static boolean displayStatusPendingW() {
        boolean found = false;
        for (BTOapplication app : allApplications) {
            if (app.getStatus().trim().equalsIgnoreCase("Pending Withdrawal")) {
                if (!found) {
                    System.out.println("=== Pending Withdrawals ===");
                    found = true;
                }
                System.out.println(app);
            }
        }
        return found;
    }
    
    /**
     * Displays pending approval applications
     * Returns true when an application with the pending approval status has been found and false otherwise
     * 
     * @return true or false
     */
    public static boolean displayStatusPendingA() {
        boolean found = false;
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Pending Approval")) {
                if (!found) {
                    System.out.println("=== Pending Approval ===");
                    found = true;
                }
                System.out.println(app);
            }
        }
        return found;
    }

    /**
     * Displays successful applications
     * Returns true when an application with the successful status has been found and false otherwise
     */
    public static void displayStatusSuccessful() {
        System.out.println("=== Successful Applications ===");
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Successful")) {
                System.out.println(app);
            }
        }
    }

    /**
     * Converts non-String variables to Strings
     */
    public String toString() {
        return "UserID: " + userID + ", ProjectID: " + projectId + ", UnitType: " + unitType +
               ", Status: " + status + ", Last Updated: " + timestamp;
    }
    
    /**
     * Getter function for array list of all applications created
     * 
     * @return all applications created
     */
    public static ArrayList<BTOapplication> getAllApplications() {
        return allApplications;
    }
    
    /**
     * Getter function for getting specific application by user ID
     * Returns specific user's application if it exist and null if it doesn't exist
     * 
     * @param userId target user
     * @return specific user's application
     */
    public static BTOapplication getApplicationByUserId(String userId) {
    for (BTOapplication app : allApplications) {
        if (app.getUserID().equals(userId)) {
            return app;
        }
    }
    return null; 
    }
    
    /**
     * Update statuses and unit type
     * Updates previous status with current status
     * Updates current status with new status
     * Update unit type details
     * Update time stamp of update
     * 
     * @param status new status to be updated
     * @param unitType type of flat to have status updated
     */
    public void updateStatus(String status, String unitType) {
        this.previousStatus = this.status; 
        this.status = status;
        this.unitType = unitType;
        this.timestamp = java.time.LocalDateTime.now().toString();

        if (user != null) {
            user.setApplicationStatus(status);
            user.setAppliedProjectId(projectId);
            user.setWithdrawn(status.equals("Withdrawn"));
        }
    }
    
    /**
     * Getter function for status
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Getter function for user ID
     * 
     * @return user ID
     */
    public String getUserID() {
        return userID;
    }
    
    /**
     * Getter function for project ID
     * 
     * @return project ID
     */
    public int getProjectId() {
        return projectId;
    }
    
    /**
     * Getter function for unit type
     * 
     * @return unit type
     */
    public String getUnitType() {
        return unitType;
    }

    /**
     * Getter function for time stamp
     * 
     * @return time stamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Checks if Applicant has a successful application and can move on to booking
     * 
     * @return true or false
     */
    public boolean isBookable() {
        return status.equals("Successful");
    }

    /**
     * Checks if Applicant has a current booking
     * 
     * @return true or false
     */
    public boolean isBooked() {
        return status.equals("Booked");
    }
    
    /**
     * Setter function to set Applicant for application
     * 
     * @param user Applicant
     */
    public void setUser(Applicant user) {
        this.user = user;
    }
    
    /**
     * Checks if Applicant has an approved application
     * 
     * @return true or false
     */
    public boolean approveApplication() {
    	return status.equals("Approved");
    }
    
    /**
     * Checks if Applicant has a unsuccessful application
     * 
     * @return true or false
     */
    public boolean deniedApplication() {
    	return status.equals("Unsuccessful");
    }
    
    /**
     * Getter function to get user
     * 
     * @return user
     */
    public Applicant getUser() {
    	return user;
    }
    
    /**
     * Setter function to set the previous status
     * 
     * @param prevStatus status before change
     */
    public void setPreviousStatus(String prevStatus) {
        this.previousStatus = prevStatus;
    }
    
    /**
     * Removes applications that had been withdrawn
     * 
     * @param userID of applicant
     * @param projectId of project
     */
    public static void removeOldWithdrawn(String userID, int projectId) {
        allApplications.removeIf(app ->
            app.getUserID().equals(userID) &&
            app.getProjectId() == projectId &&
            app.getStatus().equalsIgnoreCase("Withdrawn")
        );
    }
    
    /**
     * Writes back and stores created applications to CSV file, ensuring data persistence
     * 
     * @param filename filename to be saved into
     */
    public static void saveApplicationsToCSV(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("NRIC,ProjectID,UnitType,Status,Timestamp\n");
            for (BTOapplication app : allApplications) {
                writer.write(app.userID + "," + app.projectId + "," + app.unitType + "," + app.status + "," + app.timestamp + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads data from CSV and writes into an array list to store the information during run time
     * 
     * @param filename name of CSV file
     * @param users list of users
     */
    public static void loadApplicationsFromCSV(String filename, List<UserPerson> users) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String userID = parts[0];
                int projectId = Integer.parseInt(parts[1]);
                String unitType = parts[2];
                String status = parts[3];
                String timestamp = parts[4];

            
                Applicant applicant = null;
                for (UserPerson u : users) {
                    if (u instanceof Applicant && u.getNRIC().equals(userID)) {
                        applicant = (Applicant) u;
                        break;
                    }
                }

                if (applicant == null) {
                    System.out.println("⚠ Applicant with NRIC " + userID + " not found. Skipping.");
                    continue;
                }

            
                BTOapplication app = new BTOapplication(userID, projectId, applicant);
                app.unitType = unitType;
                app.status = status;
                app.timestamp = timestamp;

       
                applicant.setAppliedProjectId(projectId);
                applicant.setApplicationStatus(status);
                applicant.setWithdrawn(status.equalsIgnoreCase("Withdrawn"));

                Project p = Project.getProjectById(projectId);
                if (p != null) {
                    p.addApplication(app);
                }

                allApplications.add(app);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}