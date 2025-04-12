package bto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HDBofficer extends Applicant implements enquiryInterface {

    private List<Project> registeredProjects;
    private List<BTOapplication> applications;
    private List<Enquiry> enquiries;
    private List<String> appliedProjectIDs;
    private List<OfficerRegistration> officerApplications;

    public HDBofficer(String NRIC, String password, String name, boolean married, int age) {
        super(name, NRIC, age, married ? "Married" : "Single", password);
        this.registeredProjects = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.appliedProjectIDs = new ArrayList<>();
        this.officerApplications = new ArrayList<>();
    }

    public List<Project> getRegisteredProjects() {
        return registeredProjects;
    }
    public void addRegisteredProject(Project project) {
    	registeredProjects.add(project);
    }

    public List<BTOapplication> getApplications() {
        return applications;
    }
    public List<OfficerRegistration> getOfficerApplications() {
        return officerApplications;
    }

    public void addOfficerApplication(OfficerRegistration application) {
        officerApplications.add(application);
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
    private boolean checkIfApplicant(Project project) {
        Project appliedProject = this.getAppliedProject(); //reference to applicant method
        if (appliedProject != null) {
            return project.getProjectId() == appliedProject.getProjectId();
        }
        return false;
    }
    private boolean checkOverlap(Project project) {
        // Get the new project's dates
    	if (project == null) return false;
        
        LocalDate newStart = project.getOpeningDate();
        LocalDate newEnd = project.getClosingDate();
        if (newStart == null || newEnd == null) return false;
        
        // Check all existing applications (excluding denied ones)
        for (OfficerRegistration registration : officerApplications) {
            // Skip denied applications
            if (registration.getRegistrationStatus().equalsIgnoreCase("Rejected")) {
                continue;
            }
            
            Project existingProject = registration.getProject();
            LocalDate existingStart = existingProject.getOpeningDate();
            LocalDate existingEnd = existingProject.getClosingDate();
            
            // Check for date overlap
            if (!(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd))) {
                // Dates overlap
                return true;
            }
        }
        
        // No overlapping dates found
        return false;
    }
}
