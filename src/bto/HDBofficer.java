package bto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//  Inherits from User and implements enquiryInterface to manage enquiries
public class HDBofficer extends User implements enquiryInterface {
    private String officerProjectId; // ID of project officer is assigned
    private List<BTOapplication> applications; // applications under officer incharge project
    private List<enquiry> enquiries; // Enquiries submitted for project

    public HDBofficer(String userID, String password, String name, String officerProjectId) {
        super(userID, password, name);
        this.officerProjectId = officerProjectId;
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    public String getOfficerProjectId() {
        return officerProjectId;
    }

    //Adds BTO application to officer's list if it matches their assigned project's id
    public void addApplication(BTOapplication app) {
        if (app.getProjectId().equals(officerProjectId)) {
            applications.add(app);
        }
    }

  //Add enquiry to officer's list if it matches their assigned project's id
    public void addEnquiry(enquiry e) {
        if (e.getProjectId().equals(officerProjectId)) {
            enquiries.add(e);
        }
    }

    //Display all enquiries under officer assigned project
    @Override
    public void viewEnquiriesAll() {
        System.out.println("---- Enquiries for project: " + officerProjectId + " ----");
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries available.");
            return;
        }
        for (enquiry e : enquiries) {
            System.out.println(e.toString());
        }
    }

    //Allow officer to reply to specific enquiry, tagged with their name and current timestamp
    @Override
    public void replyEnquiry(int enquiryId, String replyMessage) {
        for (enquiry e : enquiries) {
            if (e.getEnquiryID() == enquiryId) {
                String fullReply = "[" + this.getName() + " | " + LocalDateTime.now() + "] " + replyMessage;
                e.reply(fullReply);
                System.out.println("Reply sent.");
                return;
            }
        }
        System.out.println("Enquiry ID not found.");
    }
    
    //Book unit for applicant in the officer assigned project, only if application status is 'Successful'
    public void bookUnitForApplicant(String userID, String unitType, Project project) {
        for (BTOapplication app : applications) {
            if (app.getUserID().equals(userID) && app.isBookable()) {
                boolean booked = project.allocateUnit(unitType);
                if (booked) {
                    app.updateStatus("Booked", unitType);
                    System.out.println("Booking successful for " + userID);
                } else {
                    System.out.println("No available units for type: " + unitType);
                }
                return;
            }
        }
        System.out.println("No eligible application found.");
    }
    
    //Generates a receipt for booked application
    //only for officer's assigned project
    public void generateReceipt(String userID) {
        for (BTOapplication app : applications) {
            if (app.getUserID().equals(userID) && app.isBooked()) {
                System.out.println("----- BTO Booking Receipt -----");
                System.out.println("Officer: " + this.getName());
                System.out.println("Applicant NRIC: " + userID);
                System.out.println("Project ID: " + officerProjectId);
                System.out.println("Unit Type: " + app.getUnitType());
                System.out.println("Booking Time: " + app.getTimestamp());
                System.out.println("Status: " + app.getStatus());
                return;
            }
        }
        System.out.println("No valid booking found for receipt.");
    }
}
