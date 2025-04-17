package bto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import bto.util.enquiryInterface;

public class HDBofficer extends Applicant implements enquiryInterface {

    private List<Project> registeredProjects;
    private List<BTOapplication> applications;
    private List<Enquiry> enquiries;
    private List<String> appliedProjectIDs;//sld be a list of int?
    private List<OfficerRegistration> officerApplications;//sld it be a list, unless want to store history

    public HDBofficer(String name, String NRIC, int age, String married, String password) {
        super(name, NRIC, age, married, password);
        setUserType("officer");
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

    public boolean isRegisteredForProject(int projectId) {
        for (Project p : registeredProjects) {
            if (p.getProjectId() == projectId) {
                return true;
            }
        }
        return false;
    }

    public void addApplication(BTOapplication app) {
        applications.add(app);
    }

    public void addEnquiry(Enquiry e) {
        enquiries.add(e);
    }

    @Override
    public void viewEnquiriesAll() {
        System.out.println("---- Enquiries for your assigned projects ----");
        boolean found = false;
        for (Enquiry e : enquiries) {
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
        for (Enquiry e : enquiries) {
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
        if (!isRegisteredForProject(project.getProjectId())) {
            System.out.println("Access denied: you are not handling this project.");
            return;
        }

        for (BTOapplication app : applications) {
            if (app.getUserID().equals(userID) &&
                app.getProjectId() == project.getProjectId() &&
                app.isBookable()) {

                boolean booked = false;
                if (unitType.equals("2-Room") && project.getTwoRoomCount() > 0) {
                    project.setTwoRoomCount(project.getTwoRoomCount() - 1);
                    booked = true;
                } else if (unitType.equals("3-Room") && project.getThreeRoomCount() > 0) {
                    project.setThreeRoomCount(project.getThreeRoomCount() - 1);
                    booked = true;
                }

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
        // Method for officer to apply for a project (no slot checking here)
        public OfficerRegistration applyOffReg(Project project) {
            if (project == null) {
                System.out.println("Error: Project cannot be null.");
                return null;
            }
    
            // Create the registration with default status "pending"
            OfficerRegistration reg = new OfficerRegistration(this, project);
    
            // Add to the project's officer applicant list
    
            System.out.println(this.getName() + " has applied to project '" + project.getProjectName() + "'. Status: pending");
    
            return reg;
        }
    
    private boolean checkIfApplicant(Project project) {
        Project appliedProject = this.getAppliedProject();
        if (appliedProject != null) {
            return project.getProjectId() == appliedProject.getProjectId();
        }
        return false;
    }

    private boolean checkOverlap(Project project) {
        if (project == null) return false;

        LocalDate newStart = project.getOpeningDate();
        LocalDate newEnd = project.getClosingDate();
        if (newStart == null || newEnd == null) return false;

        for (OfficerRegistration registration : officerApplications) {
            if (registration.getRegistrationStatus().equalsIgnoreCase("Rejected")) {
                continue;
            }

            Project existingProject = registration.getProject();
            LocalDate existingStart = existingProject.getOpeningDate();
            LocalDate existingEnd = existingProject.getClosingDate();

            if (!(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd))) {
                return true;
            }
        }

        return false;
    }
}
