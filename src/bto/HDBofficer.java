package bto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Inherits from User and implements enquiryInterface to manage enquiries
public class HDBofficer extends User implements enquiryInterface {

    private List<Project> registeredProjects; // Projects officer is approved to handle
    private List<BTOapplication> applications; // applications under officer assigned project
    private List<enquiry> enquiries; // Enquiries submitted for project
    private List<String> appliedProjectIDs; // Tracks officer registration attempts (only 1 per project)

    public HDBofficer(String userID, String password, String name) {
        super(userID, password, name);
        this.registeredProjects = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.appliedProjectIDs = new ArrayList<>();
    }

    public List<Project> getRegisteredProjects() {
        return registeredProjects;
    }

    //Check if officer is approved for the project
    public boolean isRegisteredForProject(String projectID) {
        for (Project p : registeredProjects) {
            if (p.getProjectID().equals(projectID)) {
                return true;
            }
        }
        return false;
    }

    //Adds BTO application to officer's list 
    public void addApplication(BTOapplication app) {
        applications.add(app);
    }

    //Add enquiry to officer's list 
    public void addEnquiry(enquiry e) {
        enquiries.add(e);
    }

    //Apply to be officer if:
    // 1. Not already registered or pending
    // 2. Not applicant for this project
    // 3. Not officer for overlapping project
    public void registerProject(Project project) {
        String projectID = project.getProjectID();
        LocalDate start = project.getApplicationStartDate();
        LocalDate end = project.getApplicationEndDate();

        if (appliedProjectIDs.contains(projectID)) {
            System.out.println("Already registered or pending for this project.");
            return;
        }

        if (hasAppliedAsApplicant(projectID)) {
            System.out.println("Invalid: Already applied to this project as an applicant.");
            return;
        }

        if (conflictsWithExistingRegistration(start, end)) {
            System.out.println("Invalid: Handling another project within same period.");
            return;
        }

        appliedProjectIDs.add(projectID); //Tracks applied projects
        System.out.println("Officer registration request submitted for project: " + projectID);
    }

    //Check if user has applied to same project as applicant
    private boolean hasAppliedAsApplicant(String projectID) {
        for (BTOapplication app : applications) {
            if (app.getUserID().equals(this.getUserID()) &&
                app.getProjectId().equals(projectID)) {
                return true;
            }
        }
        return false;
    }

    //Check for time conflict with already approved projects
    private boolean conflictsWithExistingRegistration(LocalDate start, LocalDate end) {
        for (Project p : registeredProjects) {
            LocalDate s = p.getApplicationStartDate();
            LocalDate e = p.getApplicationEndDate();
            if (!(end.isBefore(s) || start.isAfter(e))) {
                return true; // overlap found
            }
        }
        return false;
    }

    //Display all enquiries under officer assigned project
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

    //Allow officer to reply to specific enquiry, tagged with their name and current timestamp
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

    //Book unit for applicant in the officer assigned project, only if application status is 'Successful'
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

    //Generates a receipt for booked application
    //only for officer's assigned project
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
