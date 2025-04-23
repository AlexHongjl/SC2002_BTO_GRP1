package bto.model;
import java.util.stream.*;

import bto.util.enquiryInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * HDBmanagers is a class that extends UserPerson and subsequently implements the EnquiryInterface
 * Inherits the methods and attributes of the UserPerson class for each Manager person
 * Implements concrete methods of the methods in the enquiry interface
 * Has functions to create/edit/delete BTO listings
 * Approves/Rejects BTO applications and officers registering to handle the project
 */
public class HDBmanager extends UserPerson implements enquiryInterface {
    private List<Project> managedProjects; // Only tracks projects this manager owns
    
    /**
     * Constructor
     * 
     * @param name of manager
     * @param NRIC of manager
     * @param age of manager
     * @param maritalStatus of manager
     * @param password of manager
     */
    public HDBmanager(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password, "manager");
        this.managedProjects = new ArrayList<>();
    }
    
    /**
     * Overrides the method in the UserPerson's class
     * Method implemented uses the superclass's method in addition to adding more
     * Fits the manager class more as it displays more details that only the managers have
     */
    @Override
    public void viewOwnStatus() {
        super.viewOwnStatus(); // Calls UserPerson's viewOwnStatus
        System.out.println("Managed Projects:");
        for (Project p : managedProjects) {
            System.out.println(" - " + p.getProjectName() + " (Project ID: " + p.getProjectId() + ")");
        }
    }

    /**
     * Creates project listings with all the details of the listing accounted for
     * Manager who creates the project listing will automatically become the Manager in charge
     * Checks whether the manager already has a project under his name during the same period
     * Returns null if manager is already managing another project
     * Returns newly created Project should creation be successful
     * 
     * @param projectName name of project
     * @param neighborhood area which project would be built at
     * @param twoRoomCount number of two rooms to be in the project
     * @param twoRoompx two room px
     * @param threeRoomCount number of three rooms to be in the project
     * @param threeRoompx three room px
     * @param projectVisibility visibility of project depending on whether manager wants to change turn it on or off
     * @param openingDate application period where applicants can apply for the project
     * @param closingDate deadline for application period
     * @param officerSlots number of officers allowed to handle the project
     * @return project
     */
    public Project createBTOListings(String projectName, String neighborhood,
            int twoRoomCount, int twoRoompx,
            int threeRoomCount, int threeRoompx,
            boolean projectVisibility, 
            LocalDate openingDate, LocalDate closingDate,
            int officerSlots) {
				if (isManagingProjectDuringPeriod(openingDate, closingDate)) {
				System.out.println("Error: You are already managing a project during this period.");
				return null;
				}
				
				Project newProject = new Project(projectName, neighborhood,
				                twoRoomCount, threeRoomCount,
				                projectVisibility, 
				                openingDate, closingDate,
				                officerSlots, this, twoRoompx,
				                threeRoompx);
				managedProjects.add(newProject);
				return newProject;
				}

    /**
     * Manager able to change the details of the project by updating it with new information
     * Checks against the project ID to see if project exists
     * Prints error message and return if no project matches the project ID
     * Throws an error when invalid value type is input and catches the error to print out error message
     * 
     * @param projectID project ID to check if exists
     * @param field information to be changed in the project
     * @param newValue new details of the project to be updated
     */
    public void editBTOListings(int projectID, String field, Object newValue) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission to edit it.");
            return;
        }

        try {
            switch (field.toLowerCase()) {
                case "projectname":
                    project.setProjectName((String) newValue);
                    break;
                case "neighborhood":
                    project.setNeighbourhood((String) newValue);
                    break;
                case "tworoomcount":
                    int twoRoomCount = (Integer) newValue;
                    if (twoRoomCount >= 0) {
                        project.setTwoRoomCount(twoRoomCount);
                    } else {
                        System.out.println("Error: Room count cannot be negative.");
                    }
                    break;
                case "threeroomcount":
                    int threeRoomCount = (Integer) newValue;
                    if (threeRoomCount >= 0) {
                        project.setThreeRoomCount(threeRoomCount);
                    } else {
                        System.out.println("Error: Room count cannot be negative.");
                    }
                    break;
                case "visiblity":
                    project.setProjectVisibility((Boolean) newValue);
                    break;
                case "openingdate":
                    LocalDate newOpening = (LocalDate) newValue;
                    if (newOpening.isAfter(project.getClosingDate())) {
                        System.out.println("Error: Opening date must be before closing date.");
                    } else if (isManagingProjectDuringPeriod(newOpening, project.getClosingDate())) {
                        System.out.println("Error: You are already managing another project during this period.");
                    } else {
                        project.setOpeningDate(newOpening);
                    }
                    break;
                case "closingdate":
                    LocalDate newClosing = (LocalDate) newValue;
                    if (newClosing.isBefore(project.getOpeningDate())) {
                        System.out.println("Error: Closing date must be after opening date.");
                    } else if (isManagingProjectDuringPeriod(project.getOpeningDate(), newClosing)) {
                        System.out.println("Error: You are already managing another project during this period.");
                    } else {
                        project.setClosingDate(newClosing);
                    }
                    break;
                case "officerslots":
                    int slots = (Integer) newValue;
                    if (slots >= project.getOfficers().size()) {
                        project.setOfficerSlots(slots);
                    } else {
                        System.out.println("Error: Cannot set slots below current number of registered officers.");
                    }
                    break;
                default:
                    System.out.println("Error: Invalid field specified.");
            }
        } catch (ClassCastException e) {
            System.out.println("Error: Invalid value type for field " + field);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
    }
    
    /**
     * Manager choosing to delete BTO listing that he has created
     * Checks if project exists using project ID
     * Checks if project has active applications, don't allow deletion
     * checks if project has active registrations, don't allow deletion
     * Checks if project has no applications or registrations, allow deletion
     * 
     * @param projectID ID of project to be deleted
     */
    public void deleteBTOListings(int projectID) {
        // Find project in both system and manager's list
        Project project = Project.getProjectById(projectID);

        if (project == null) {
            System.out.println("Error: Project not found.");
            return;
        }

        if (!managedProjects.contains(project)) {
            System.out.println("Error: You don't have permission to delete this project.");
            return;
        }

        // Check BTO applications for "Booked", "Pending", or "Successful"
        List<BTOapplication> apps = project.getApplicationList();
        if (apps != null) {
            for (BTOapplication app : apps) {
                String status = app.getStatus();
                if ("Booked".equalsIgnoreCase(status) || 
                    "Pending approval".equalsIgnoreCase(status) || 
                    "Successful".equalsIgnoreCase(status)) {
                    System.out.println("Error: Cannot delete project. It has active applications (Booked, Pending, or Successful).");
                    return;
                }
            }
        }

        // Check officer list is empty
        if (project.getOfficerList() != null && !project.getOfficerList().isEmpty()) {
            System.out.println("Error: Cannot delete project. Officer list is not empty.");
            return;
        }

        // Check officer registration list for "Pending" or "Successful"
        List<OfficerRegistration> regList = project.getOfficerApplicantList();
        if (regList != null) {
            for (OfficerRegistration reg : regList) {
                String status = reg.getRegistrationStatus();
                if ("Pending approval".equalsIgnoreCase(status) || "Successful".equalsIgnoreCase(status)) {
                    System.out.println("Error: Cannot delete project. It has pending or accepted officer applications.");
                    return;
                }
            }
        }

        // Proceed with deletion
        boolean removedFromSystem = Project.removeProjectById(projectID);
        boolean removedFromManaged = managedProjects.remove(project);

        if (removedFromSystem && removedFromManaged) {
            System.out.println("Project deleted successfully from all records.");
        } else {
            System.out.println("Error: Project could not be fully deleted.");
        }
    }


    /**
     * Changes visibility setting of the target project
     * Display error message if project not found or person dont have existing permissions to modify project
     * 
     * @param projectID project ID to be changed
     * @param visible visibility setting to be changed to
     */
    public void toggleProjectVisibility(int projectID, boolean visible) {
        Project project = Project.getProjectById(projectID);
        if (project != null && managedProjects.contains(project)) {
            project.setProjectVisibility(visible);
            System.out.println("Project visibility set to: " + visible);
        } else {
            System.out.println("Error: Project not found or you don't have permission to modify it.");
        }
    }

    /**
     * Displays officer registration details that was created under current project
     * Only allows manager to view registrations under their project
     * 
     * @param projectID project ID of registration
     */
    public void viewOfficerRegistrations(int projectID) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission to view it.");
            return;
        }
        
        System.out.println("Officer Registrations for Project: " + project.getProjectName());
        for (OfficerRegistration reg : project.getOfficerRegistrations()) {
            HDBofficer officer = reg.getOfficer();
            System.out.println("Officer: " + officer.getName() + 
                             " (NRIC: " + officer.getNRIC() + 
                             ") - Status: " + reg.getRegistrationStatus());
        }
    }
    
    /**
     * Manager can approve Officers who wish to register to handle the project
     * Checks if there is any registrations "Pending Approval"
     * 
     * @param projectID ID of project
     * @param officerNRIC NRIC of officer who registered
     */
    public void approveOfficerRegistration(int projectID, String officerNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }
        
        for (OfficerRegistration reg : project.getOfficerRegistrations()) {
            if (reg.getOfficer().getNRIC().equals(officerNRIC) && reg.getRegistrationStatus().equals("Pending approval")) {
                reg.approveRegistration();
                return;
            }
        }
        
        System.out.println("No pending registration found for officer with NRIC: " + officerNRIC);
    }
    
    /**
     * Able to reject officer registration
     * Checks if officer registration is pending approval
     * 
     * @param projectID ID of project
     * @param officerNRIC NRIC of officer who registered
     */
    public void rejectOfficerRegistration(int projectID, String officerNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }
        
        for (OfficerRegistration reg : project.getOfficerRegistrations()) {
            if (reg.getOfficer().getNRIC().equals(officerNRIC) && reg.getRegistrationStatus().equals("Pending approval")) {
                reg.rejectRegistration();
                System.out.println("Officer registration rejected.");
                return;
            }
        }
        
        System.out.println("No pending registration found for officer with NRIC: " + officerNRIC);
    }

    /**
     * Manager able to approve BTO applications made by applicants
     * Checks if project exists
     * Checks if there are available room flats that match the BTO applications
     * Displays error message if no available room flats left
     * 
     * @param projectID ID of project
     * @param applicantNRIC NRIC of applicant who made application
     */
    public void approveBTOApplication(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app : BTOapplication.getAllApplications()) {
            if (app.getProjectId() == projectID &&
                app.getUserID().equals(applicantNRIC) &&
                app.getStatus().trim().equalsIgnoreCase("Pending Approval")) {
                
                if (app.getUnitType().equals("2-Room") && project.getTwoRoomCount() > 0) {
                    //project.setTwoRoomCount(project.getTwoRoomCount() - 1);
                    app.updateStatus("Successful", "2-Room");
                    System.out.println("Application for 2 room flat approved");
                } else if (app.getUnitType().equals("3-Room") && project.getThreeRoomCount() > 0) {
                    //project.setThreeRoomCount(project.getThreeRoomCount() - 1);
                    app.updateStatus("Successful", "3-Room");
                    System.out.println("Application for 3 room flat approved");
                } else {
                    System.out.println("No available units.");
                }

                BTOapplication.saveApplicationsToCSV("data/Applications.csv");
                return;
            }
        }
        System.out.println("No pending application found for applicant with NRIC: " + applicantNRIC);
    }

    /**
     * Manager able to reject BTO application
     * Checks if application status is "Pending Approval"
     * Updates status of application to unsuccessful
     * Gives error message saying application being rejected
     * 
     * @param projectID of project
     * @param applicationNRIC of applicant
     */
    public void rejectBTOApplication(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app : BTOapplication.getAllApplications()) {
            if (app.getUser() != null &&
                app.getUser().getNRIC().equals(applicantNRIC) &&
                app.getStatus().equals("Pending Approval")) {

                app.updateStatus("Unsuccessful", app.getUnitType());
                System.out.println("Application rejected.");
                return;
            }
        }
        System.out.println("No pending application found for applicant with NRIC: " + applicantNRIC);
    }
    
    /**
     * Manager able to approve withdrawal request by applicants who applied for the project
     * If successfully allowed to be withdrawn, update project details accordingly
     * Updates application status to "Withdrawn" and displays message approving withdrawal request
     * 
     * @param projectID ID of project
     * @param applicantNRIC NRIC of Applicant requesting to withdraw from project
     */
    public void approveWithdrawalRequest(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app :BTOapplication.getAllApplications()) {
            if (app.getUserID().equals(applicantNRIC) &&
                app.getStatus().trim().equalsIgnoreCase("Pending Withdrawal")) {

                // Return flat if needed
                if (app.getPreviousStatus().equalsIgnoreCase("Successful") ||
                    app.getPreviousStatus().equalsIgnoreCase("Booked")) {

                    if (app.getUnitType().equals("2-Room")) {
                        //project.setTwoRoomCount(project.getTwoRoomCount() + 1);
                    } else if (app.getUnitType().equals("3-Room")) {
                        //project.setThreeRoomCount(project.getThreeRoomCount() + 1);
                    }
                }

                // Update application and applicant
                app.updateStatus("Withdrawn", app.getUnitType());

                if (app.getUser() != null) {
                    app.getUser().setApplicationStatus("Withdrawn");
                    app.getUser().setAppliedProjectId(-1);
                    app.getUser().setWithdrawn(true);
                }

                BTOapplication.saveApplicationsToCSV("data/Applications.csv");
                System.out.println("Withdrawal request approved.");
                return;
            }
        }

        System.out.println("No withdrawal request found for applicant with NRIC: " + applicantNRIC);
    }
    
    /**
     * Manager able to reject withdrawal request by Applicants
     * Upon rejection of withdrawal requests, application status will be changed back to successful
     * Displays message to show withdrawal rejection
     * 
     * @param projectID ID of project
     * @param applicantNRIC NRIC of applicant
     */
    public void rejectWithdrawalRequest(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app : BTOapplication.getAllApplications()) {
            if (app.getProjectId() == projectID &&
                app.getUserID().equals(applicantNRIC) &&
                app.getStatus().trim().equalsIgnoreCase("Pending Withdrawal")) {

            	String prevStatus = app.getPreviousStatus();
            	if (prevStatus == null || prevStatus.isBlank()) {
            	    prevStatus = "Successful"; // fallback default if missing
            	}
            	app.updateStatus(prevStatus, app.getUnitType());

                Applicant applicant = app.getUser();
                if (applicant != null) {
                    applicant.setApplicationStatus(prevStatus);
                    applicant.setWithdrawn(false);
                    applicant.setAppliedProjectId(projectID);
                }

                System.out.println("Withdrawal request rejected.");
                BTOapplication.saveApplicationsToCSV("data/Applications.csv");
                return;
            }
        }

        System.out.println("No withdrawal request found for applicant with NRIC: " + applicantNRIC);
    }
    
    /**
     * Views all enquiries created
     */
    @Override
    public void viewEnquiriesAll() {
        System.out.println("All Enquiries:");
        Enquiry.displayAllEnquiries();
    }
    
    /**
     * View enquiries specific to project
     * Display error message if project not found
     * 
     * @param projectID ID of project
     */
    public void viewProjectEnquiries(int projectID) {
        Project project = Project.getProjectById(projectID);
        if (project == null) {
            System.out.println("Error: Project not found.");
            return;
        }
        
        System.out.println("Enquiries for Project: " + project.getProjectName());
    }
    
    /**
     * Reply to enquiries
     * Updates enquiry with reply message
     * Updates status of enquiry to close after reply
     */
    @Override
    public void replyEnquiry(int enquiryID, String reply) {
        Enquiry enquiry = Enquiry.getByID(enquiryID);
        if (enquiry == null) {
            System.out.println("Error: Enquiry not found.");
            return;
        }
        
        enquiry.reply(reply);
        enquiry.setStatus(false);
    }

    /**
     * View projects created by the Manager
     * Can be filtered by keywords
     * Displays projects created based on the filter keywords
     * 
     * @param filter keyword to filter the project details by
     */
    public void viewMyProjects(String filter) {
        System.out.println("My Managed Projects:");
        
        if (managedProjects.isEmpty()) {
            System.out.println("You are not currently managing any projects.");
            return;
        }
        
        // Create a copy of managed projects
        List<Project> projectList = new ArrayList<>(managedProjects);
        
        // Sort based on the specified field
        if (filter != null && !filter.isEmpty()) {
            Collections.sort(projectList, (p1, p2) -> {
                switch (filter.toLowerCase()) {
                    case "projectid":
                        return Integer.compare(p1.getProjectId(), p2.getProjectId());
                    case "projectname":
                        return p1.getProjectName().compareToIgnoreCase(p2.getProjectName());
                    case "neighborhood":
                        return p1.getNeighbourhood().compareToIgnoreCase(p2.getNeighbourhood());
                    case "tworoomcount":
                        return Integer.compare(p1.getTwoRoomCount(), p2.getTwoRoomCount());
                    case "threeroomcount":
                        return Integer.compare(p1.getThreeRoomCount(), p2.getThreeRoomCount());
                    case "projectvisibility":
                        return Boolean.compare(p1.isProjectVisibility(), p2.isProjectVisibility());
                    case "openingdate":
                        return p1.getOpeningDate().compareTo(p2.getOpeningDate());
                    case "closingdate":
                        return p1.getClosingDate().compareTo(p2.getClosingDate());
                    case "officerslots":
                        return Integer.compare(p1.getOfficerSlots(), p2.getOfficerSlots());
                    default:
                        System.out.println("Unknown sorting field: " + filter + ". Using default sort by project name.");
                        return p1.getProjectName().compareToIgnoreCase(p2.getProjectName());
                }
            });
        } else {
            // Default sort by project name
            Collections.sort(projectList, Comparator.comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER));
        }
        
        // Display the sorted projects
        System.out.println(String.format("%-5s %-20s %-15s %-12s %-12s %-8s %-8s %-5s", 
            "ID", "Project Name", "Neighborhood", "Opening", "Closing", "2-Room", "3-Room", "Visible"));
        System.out.println("--------------------------------------------------------------------------------------");
        
        for (Project project : projectList) {
            System.out.println(String.format("%-5d %-20s %-15s %-12s %-12s %-8d %-8d %-5s",
                project.getProjectId(),
                project.getProjectName(),
                project.getNeighbourhood(),
                project.getOpeningDate(),
                project.getClosingDate(),
                project.getTwoRoomCount(),
                project.getThreeRoomCount(),
                project.isProjectVisibility() ? "Yes" : "No"));
        }
    }
    
    /**
     * Generates report of the project based on the filter keyword
     * Displays the report header with details of the project at the top
     * Displays applications under that specific project
     * 
     * @param projectID ID of project
     * @param filter keyword to be filtered by
     */
    public void generateReport(int projectID, String filter) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }
        
        List<BTOapplication> filteredApplications = BTOapplication.getAllApplications().stream()
            .filter(app -> app.getProjectId() == projectID)
            .filter(app -> app.getStatus().equals("Booked"))
            .filter(app -> {
                switch (filter.toLowerCase()) {
                    case "married":
                    	return app.getUser().getMaritalStatus().equalsIgnoreCase("Married");
                    case "single":
                        return app.getUser().getMaritalStatus().equalsIgnoreCase("Single");
                    case "2-room":
                        return app.getUnitType().equals("2-Room");
                    case "3-room":
                        return app.getUnitType().equals("3-Room");
                    case "all":
                        return true;
                    default:
                        return true;
                }
            })
            .collect(Collectors.toList());
        
        // Print the report header
        System.out.println("=========== Project Booking Report ===========");
        System.out.println("Project ID: " + projectID);
        System.out.println("Project Name: " + project.getProjectName());
        System.out.println("Filter: " + filter);
        System.out.println("Number of Bookings: " + filteredApplications.size());
        System.out.println("============================================");
        
        if (filteredApplications.isEmpty()) {
            System.out.println("No bookings found matching the filter criteria.");
        } else {
            // Print column headers
            System.out.printf("%-15s %-15s %-10s %-20s%n", "User ID", "Unit Type", "Status", "Booking Time");
            System.out.println("------------------------------------------------------------");
            
            // Print each application
            for (BTOapplication app : filteredApplications) {
                System.out.printf("%-15s %-15s %-10s %-20s%n", 
                    app.getUserID(),
                    app.getUnitType(),
                    app.getStatus(),
                    app.getTimestamp());
            }
        }
        
        System.out.println("============================================");
    }
    
    /**
     * Allows Manager to view all project available regardless of visibility setting 
     * 
     * @param field field
     */
    public void viewAllProjects(String field) {
        Project.displayAllProjects(field);
    }

    private boolean isManagingProjectDuringPeriod(LocalDate newOpening, LocalDate newClosing) {
        for (Project project : managedProjects) {
            if (datesOverlap(project.getOpeningDate(), project.getClosingDate(), newOpening, newClosing)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Add created project to list of all projects managed by the Manager
     * 
     * @param project to be added
     */
    public void addProject(Project project) {
    	managedProjects.add(project);
    }

    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}