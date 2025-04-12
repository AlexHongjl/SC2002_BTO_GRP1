package bto.model;

public class BTOapplication {
    private String userID;
    private int projectId;
    private String unitType; // "2-Room" or "3-Room"
    private String status;   // Pending, Successful, Unsuccessful, Booked
    private String timestamp; // last status update

    public BTOapplication(String userID, int projectId) {
        this.userID = userID;
        this.projectId = projectId;
        this.status = "Pending";
        this.unitType = "";
        this.timestamp = "";
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