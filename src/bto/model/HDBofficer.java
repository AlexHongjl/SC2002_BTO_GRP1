package bto.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import bto.util.enquiryInterface;

/**
 * HDBofficer extends from Applicants, which also extends from UserPerson, and implements enquiry interface
 * Inherits all attributes and methods from Applicant class, also inherits attributes from UserPerson class
 * Able to view/reply to enquiries under projects that they handle
 * Can register to handle a project, provided that Officer is not an current Applicant that is applied to that same project
 */
public class HDBofficer extends Applicant implements enquiryInterface {

    private List<Project> registeredProjects;//as officer
    private List<Enquiry> enquiries;
    private List<Integer> appliedProjectIDs; // as applicant
    private List<OfficerRegistration> officerApplications;
    
    /**
     * Constructor
     * 
     * @param name of officer
     * @param NRIC of officer
     * @param age of officer
     * @param married of officer
     * @param password of officer
     */
    public HDBofficer(String name, String NRIC, int age, String married, String password) {
        super(name, NRIC, age, married, password);
        setUserType("officer");
        this.registeredProjects = new ArrayList<>();
        this.enquiries = new ArrayList<>();
        this.appliedProjectIDs = new ArrayList<>();
        this.officerApplications = new ArrayList<>();
    }

    public boolean hasPendingReg() {
        for (OfficerRegistration offReg : officerApplications) {
            String status = offReg.getApplicationStatus();
            if (status.equalsIgnoreCase("Pending Approval") || status.equalsIgnoreCase("Successful")) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Checks if registered projects exists
     * 
     * @return true or false
     */
    public boolean isOfficerInCharge() {
        return registeredProjects != null && !registeredProjects.isEmpty();
    }
    
    /**
     * Getter function for registered projects
     * 
     * @return list of registered projects
     */
    public List<Project> getRegProj() {
        return registeredProjects;
    }

    /**
     * Allows Officers to view their own status 
     * Displays Officer's details including the details as an Applicant
     * Shows basic informations, as well as projects registered/applied for
     */
    @Override
    public void viewOwnStatus() {
        super.viewOwnStatus(); // Calls Applicant's viewOwnStatus

        // Convert Integer list to String list for display
        String ids = appliedProjectIDs.stream()
                                      .map(String::valueOf)
                                      .collect(Collectors.joining(", "));
        System.out.println("Applied Project IDs: " + ids);

        System.out.println("Registered Projects: ");
        for (Project p : registeredProjects) {
            System.out.println(" - " + p.getProjectName());
        }

        System.out.println("Officer Applications:");
        for (OfficerRegistration reg : officerApplications) {
            System.out.println(" - " + reg.getProject().getProjectName() + " (Status: " + reg.getRegistrationStatus() + ")");
        }
    }

    /**
     * Checks if Officer is handling a specific project
     * If Officer is handling that project, return true, return false otherwise
     * 
     * @param projectID ID of project
     * @return true or false
     */
    public boolean isOfficerForProject(int projectID) {
        if (registeredProjects == null) return false;

        for (Project p : registeredProjects) {
            if (p != null && p.getProjectId() == projectID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter function for getting Officer's registered projects
     * 
     * @return registered projects
     */
    public List<Project> getRegisteredProjects() {
        return registeredProjects;
    }
    
    /**
     * Adds project to list of projects the Officer is handling
     * 
     * @param project to be added
     */
    public void addRegisteredProject(Project project) {
        registeredProjects.add(project);
    }
    
    /**
     * Getter method for getting Officer's Application as an Applicant
     * 
     * @return officer applications
     */
    public List<OfficerRegistration> getOfficerApplications() {
        return officerApplications;
    }

    /**
     * Adds Officer's application as an Applicant to list of their applications
     * 
     * @param application created as an Applicant
     */
    public void addOfficerApplication(OfficerRegistration application) {
        officerApplications.add(application);
    }
    
    /**
     * Getter function for getting project ID thats Officers had applied for
     * 
     * @return list of projects applied for
     */
    public List<Integer> getAppliedProjectIDs() {
        return appliedProjectIDs;
    }

    /**
     * Checks if Officer is registered for current project
     * Returns true if they are handling the project, return false otherwise
     * 
     * @param projectId ID of project
     * @return true or false
     */
    public boolean isRegisteredForProject(int projectId) {
        for (Project p : registeredProjects) {
            if (p.getProjectId() == projectId) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds created enquiry to the list of all enquiries
     */
    public void addEnquiry(Enquiry e) {
        enquiries.add(e);
    }
    
    /**
     * Displays all enquiries for the project Officer is handling
     */
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
    
    /**
     * Able to reply to enquiries that have been made under the project Officer is handling
     * Updates with the response by the officer
     */
    @Override
    public void replyEnquiry(int enquiryId, String replyMessage) {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryID() == enquiryId && isRegisteredForProject(e.getProjectId())) {
                String fullReply = "[" + this.getName() + " | " + LocalDateTime.now() + "] " + replyMessage;
                e.reply(fullReply);
                e.setStatus(false);
                System.out.println("Reply sent.");
                return;
            }
        }
        System.out.println("Enquiry ID not found or not under your assigned projects.");
    }
    
    /**
     * Upon successful approval by the Manager in charge, Officer can book flat
     * Officer can book a flat for the Applicant based on the flat type stated in the BTO application of the Applicant
     * Checks if room type is available for booking
     * Updates application if booking is successful or unsuccessful
     * Displays error message if no available flat left
     * 
     * @param userID ID of applicant
     * @param unitType flat type applicant wants to book
     * @param project project name
     */
    public void bookUnitForApplicant(String userID, String unitType, Project project) {
        if (!isRegisteredForProject(project.getProjectId())) {
            System.out.println("Access denied: you are not handling this project.");
            return;
        }

        BTOapplication app = null;
        for (BTOapplication a : BTOapplication.getAllApplications()) {
            if (a.getUserID().equals(userID) &&
                a.getProjectId() == project.getProjectId() &&
                a.getStatus().equalsIgnoreCase("Successful")) {
                app = a;
                break;
            }
        }
        
        if (app != null && app.isBookable()) {
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
    
                // Update flatTypeBooked directly from the user reference in BTOapplication
                Applicant applicant = app.getUser();
                if (applicant != null) {
                    applicant.bookFlat(unitType);;
                }
            } else {
                System.out.println("No available units for type: " + unitType);
                app.updateStatus("Unsuccessful", unitType);
            }
            return;
        }
        System.out.println("No eligible application found.");
    }
    
    /**
     * Officer can generate the receipt of the booking once it is done
     * 
     * @param userID user ID of Applicant
     */
    public void generateReceipt(String userID) {
        for (Project project : registeredProjects) {
            BTOapplication app = project.getApplicationByUserId(userID);
            if (app != null && app.isBooked()) {
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
    
    /**
     * Officer can create a registration to handle a project
     * Checks if Officer is currently an Applicant for the project
     * Checks if Officer is currently already an Officer for the project
     * Reject with error message if either of the 2 conditions are true
     * Create Officer registration if both conditions are not met
     * 
     * @param project project to apply for
     * @return Officer's registration
     */
    public OfficerRegistration applyOffReg(Project project) {

        if (project == null) {
            System.out.println("Error: Project cannot be null.");
        }
        else if (this.checkOverlap(project)) {
        	System.out.println("Error: Already an officer during this period");
        }
        else if (this.getAppliedProjectId() == project.getProjectId()) {
            System.out.println("Error: You have already applied to this project as an applicant.");
        }

        // Create the registration with default status "pending"
        OfficerRegistration reg = new OfficerRegistration(this, project);

        return reg;
    }
    
    private boolean checkIfApplicant(Project project) {
        Project appliedProject = this.getAppliedProject();
        if (appliedProject != null) {
            return project.getProjectId() == appliedProject.getProjectId();
        }
        return false;
    }
    
    /**
     * Officer applying for a project as an Applicant
     * Checks if Officer is currently already an Applicant for the project
     * Checks if Officer is currently an Officer for the project
     * Reject with error message if either of the 2 conditions are true
     * Create Officer application if both conditions are not met
     */
    public boolean applyForProject(Project project, String flatType) {
        if (getAppliedProjectId() != -1 && !isWithdrawn()) return false; // already applied

        // Check if this applicant is also an officer who has applied for the same project
            for (OfficerRegistration reg : this.getOfficerApplications()) {
                if (reg.getProject().getProjectId() == project.getProjectId()) {
                    System.out.println("Error: You have already applied to this project as an officer.");
                    return false;
                }
            }
            for (Project pro : this.getRegisteredProjects()) {
                if (pro.getProjectId() == project.getProjectId()) {
                    System.out.println("Error: You are registered to this project as an officer.");
                    return false;
                }
            }

        if (!isEligible(project, flatType)) return false;

        this.setAppliedProjectId(project.getProjectId());
        this.applicationStatus = "Pending";
        this.hasWithdrawn = false;
        return true;
    }

    private boolean checkOverlap(Project project) {
        if (project == null) return false;

        LocalDate newStart = project.getOpeningDate();
        LocalDate newEnd = project.getClosingDate();
        if (newStart == null || newEnd == null) return false;

        for (OfficerRegistration registration : officerApplications) {
            if (registration.getRegistrationStatus().equalsIgnoreCase("Unsuccessful")) {
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