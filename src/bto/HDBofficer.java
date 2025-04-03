package bto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//  Inherits from User and implements enquiryInterface to manage enquiries
public class HDBofficer extends User implements enquiryInterface {

    private List<Project> registeredProjects; // Projects officer is approved to handle
    private List<OfficerRegistration> officerRegistrations; // Officer's registration records
    private List<BTOapplication> applications; // applications under officer incharge project
    private List<enquiry> enquiries; // Enquiries submitted for project

    public HDBofficer(String userID, String password, String name) {
        super(userID, password, name);
        this.registeredProjects = new ArrayList<>();
        this.officerRegistrations = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    public List<Project> getRegisteredProjects() {
        return registeredProjects;
    }

    public List<OfficerRegistration> getOfficerRegistrations() {
        return officerRegistrations;
    }

    public void addRegisteredProject(Project project) {
        registeredProjects.add(project);
    }

    public boolean isRegisteredForProject(String projectID) {
        for (Project p : registeredProjects) {
            if (p.getProjectID().equals(projectID)) {
                return true;
            }
        }
        return false;
    }

    //Apply to register as HDB officer for a specific project
    public void registerProject(Project project) {
        if (hasAppliedToProject(project.getProjectID())) {
            System.out.println("Already registered or pending approval for this project.");
            return;
        }

        OfficerRegistration reg = new OfficerRegistration(this.getUserID(), project.getProjectID());
        officerRegistrations.add(reg);
        System.out.println("Registration request submitted for project: " + project.getProjectID());
    }

    //Check if officer has applied or registered for a project
    public boolean hasAppliedToProject(String projectID) {
        for (OfficerRegistration reg : officerRegistrations) {
            if (reg.getProjectID().equals(projectID)) {
                return true;
            }
        }
        return false;
    }

    //View the registration status for a given project
    public String registrationStatus(String projectID) {
        for (OfficerRegistration reg : officerRegistrations) {
            if (reg.getProjectID().equals(projectID)) {
                return reg.getStatus();
            }
        }
        return "Not Registered";
    }

    //Adds BTO application to officer's list 
    public void addApplication(BTOapplication app) {
        applications.add(app);
    }

    //Add enquiry to officer's list 
    public void addEnquiry(enquiry e) {
        enquiries.add(e);
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
