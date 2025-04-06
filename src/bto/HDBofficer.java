package bto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HDBofficer extends Applicant implements enquiryInterface {

    private List<Project> registeredProjects;
    private List<BTOapplication> applications;
    private List<enquiry> enquiries;
    private List<String> appliedProjectIDs;

    public HDBofficer(String userID, String password, String name, boolean married, int age) {
        super(userID, password, name, married, age);
        this.registeredProjects = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.appliedProjectIDs = new ArrayList<>();
    }

    public List<Project> getRegisteredProjects() {
        return registeredProjects;
    }

    public List<BTOapplication> getApplications() {
        return applications;
    }

    public List<String> getAppliedProjectIDs() {
        return appliedProjectIDs;
    }

    public boolean isRegisteredForProject(String projectID) {
        for (Project p : registeredProjects) {
            if (p.getProjectID().equals(projectID)) {
                return true;
            }
        }
        return false;
    }

    public void addApplication(BTOapplication app) {
        applications.add(app);
    }

    public void addEnquiry(enquiry e) {
        enquiries.add(e);
    }

    @Override
    public void viewEnquiriesAll() {
        System.out.println("---- Enquiries for your assigned projects ----");
        boolean found = false;
        for (enquiry e : enquiries) {
            if (isRegisteredForProject(e.getProjectId())) {
                System.out.println(e.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No enquiries available.");
        }
    }

    @Override
    public void replyEnquiry(int enquiryId, String replyMessage) {
        for (enquiry e : enquiries) {
            if (e.getEnquiryID() == enquiryId && isRegisteredForProject(e.getProjectId())) {
                String fullReply = "[" + this.getName() + " | " + LocalDateTime.now() + "] " + replyMessage;
                e.reply(fullReply);
                System.out.println("Reply sent.");
                return;
            }
        }
        System.out.println("Enquiry ID not found or not under your assigned projects.");
    }

    public void bookUnitForApplicant(String userID, String unitType, Project project) {
        if (!isRegisteredForProject(project.getProjectID())) {
            System.out.println("Access denied: you are not handling this project.");
            return;
        }

        for (BTOapplication app : applications) {
            if (app.getUserID().equals(userID) &&
                app.getProjectId().equals(project.getProjectID()) &&
                app.isBookable()) {

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

    public void generateReceipt(String userID) {
        for (BTOapplication app : applications) {
            if (app.getUserID().equals(userID) && app.isBooked() && isRegisteredForProject(app.getProjectId())) {
                System.out.println("----- BTO Booking Receipt -----");
                System.out.println("Officer: " + this.getName());
                System.out.println("Applicant NRIC: " + userID);
                System.out.println("Project ID: " + app.getProjectId());
                System.out.println("Unit Type: " + app.getUnitType());
                System.out.println("Booking Time: " + app.getTimestamp());
                System.out.println("Status: " + app.getStatus());
                return;
            }
        }
        System.out.println("No valid booking found for receipt.");
    }
}
