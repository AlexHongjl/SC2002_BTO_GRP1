package bto.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BTOapplication {
    private String userID;
    private int projectId;
    private String unitType; // "2-Room" or "3-Room"
    private String status;   // Pending approval,Pending withdrawal, Successful, Unsuccessful, Booked
    private String timestamp; // last status update
    private Applicant user;
    private String previousStatus;//"",Pending approval,Pending withdrawal, Successful, Unsuccessful
    
    private static ArrayList<BTOapplication> allApplications = new ArrayList<>();

    public BTOapplication(String userID, int projectId, Applicant u) {
        this.userID = userID;
        this.projectId = projectId;
        this.status = "Pending approval";
        this.unitType = "";
        this.timestamp = "";
        this.previousStatus="";
        user = u;
        Project project = Project.getProjectById(projectId);
        if (project != null) {
            project.addApplication(this);
        }
    }

    public String getPreviousStatus(){
        return this.previousStatus;
    }
    
    public static void addApplication(BTOapplication app) {
        allApplications.add(app);
    }
    
    public static void displayAll() {
        System.out.println("=== All BTO Applications ===");
        for (BTOapplication app : allApplications) {
            System.out.println(app);
        }
    }
    
    public void display() {
        System.out.println("=== BTO Application ===");
        System.out.println("User ID     : " + userID);
        System.out.println("Project ID  : " + projectId);
        System.out.println("Unit Type   : " + (unitType.isEmpty() ? "N/A" : unitType));
        System.out.println("Status      : " + status);
        System.out.println("Last Updated: " + (timestamp.isEmpty() ? "N/A" : timestamp));
        System.out.println("========================\n");
    }

    public static void displayStatusPendingW() {
        System.out.println("=== Pending Applications ===");
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Pending Withdrawn")) {
                System.out.println(app);
            }
        }
    }
    
    public static void displayStatusPendingA() {
        System.out.println("=== Pending Applications ===");
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Pending Approval")) {
                System.out.println(app);
            }
        }
    }

    
    public static void displayStatusSuccessful() {
        System.out.println("=== Successful Applications ===");
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Successful")) {
                System.out.println(app);
            }
        }
    }

    
    public String toString() {
        return "UserID: " + userID + ", ProjectID: " + projectId + ", UnitType: " + unitType +
               ", Status: " + status + ", Last Updated: " + timestamp;
    }

    public static ArrayList<BTOapplication> getAllApplications() {
        return allApplications;
    }
    
    public static BTOapplication getApplicationByUserId(String userId) {
    for (BTOapplication app : allApplications) {
        if (app.getUserID().equals(userId)) {
            return app;
        }
    }
    return null; 
    }

    public void updateStatus(String status, String unitType) {
        this.status = status;
        this.unitType = unitType;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public String getStatus() {
        return status;
    }

    public String getUserID() {
        return userID;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getUnitType() {
        return unitType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isBookable() {
        return status.equals("Successful");
    }

    public boolean isBooked() {
        return status.equals("Booked");
    }
    
    public boolean approveApplication() {
    	return status.equals("Approved");
    }
    
    public boolean deniedApplication() {
    	return status.equals("Unsuccessful");
    }
    public Applicant getUser() {
    	return user;
    }
    
    //Data Persistence
    public static void saveApplicationsToCSV(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("UserID,ProjectID,UnitType,Status,Timestamp\n");
            for (BTOapplication app : allApplications) {
                writer.write(app.userID + "," + app.projectId + "," + app.unitType + "," + app.status + "," + app.timestamp + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadApplicationsFromCSV(String filename, List<UserPerson> allUsers) {
        allApplications.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                String userId = parts[0];
                int projectId = Integer.parseInt(parts[1]);
                String unitType = parts[2];
                String status = parts[3];
                String timestamp = parts[4];

                Applicant appUser = null;
                for (UserPerson u : allUsers) {
                    if (u instanceof Applicant && u.getNRIC().equals(userId)) {
                        appUser = (Applicant) u;
                        break;
                    }
                }

                if (appUser != null) {
                    BTOapplication app = new BTOapplication(userId, projectId, appUser);
                    app.updateStatus(status, unitType);
                    app.timestamp = timestamp;
                    allApplications.add(app);
                    Project p = Project.getProjectById(projectId);
                    if (p != null) p.addApplication(app);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}