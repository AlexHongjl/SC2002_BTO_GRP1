package bto.model;

import java.util.ArrayList;

public class BTOapplication {
    private String userID;
    private int projectId;
    private String unitType; // "2-Room" or "3-Room"
    private String status;   // Pending, Successful, Unsuccessful, Booked
    private String timestamp; // last status update
    
    private static ArrayList<BTOapplication> allApplications = new ArrayList<>();

    public BTOapplication(String userID, int projectId) {
        this.userID = userID;
        this.projectId = projectId;
        this.status = "Pending approval";
        this.unitType = "";
        this.timestamp = "";
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
}