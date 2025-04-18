package bto.model;
import java.util.stream.*;

import bto.service.User;
import bto.util.enquiryInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//implement hdb manager functionality
public class HDBmanager extends UserPerson implements enquiryInterface {
    private List<Project> managedProjects; // Only tracks projects this manager owns

    public HDBmanager(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password, "manager");
        this.managedProjects = new ArrayList<>();
    }
    
    @Override
    public void viewOwnStatus() {
        super.viewOwnStatus(); // Calls UserPerson's viewOwnStatus
        System.out.println("Managed Projects:");
        for (Project p : managedProjects) {
            System.out.println(" - " + p.getProjectName() + " (Project ID: " + p.getProjectId() + ")");
        }
    }


    // === Project Management ===
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
				                twoRoomCount, threeRoompx,
				                projectVisibility, 
				                openingDate, closingDate,
				                officerSlots, this, twoRoompx,
				                threeRoomCount);
				managedProjects.add(newProject);
				return newProject;
				}


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
        
        // Remove from both collections
        boolean removedFromSystem = Project.removeProjectById(projectID);
        boolean removedFromManaged = managedProjects.remove(project);
        
        if (removedFromSystem && removedFromManaged) {
            System.out.println("Project deleted successfully from all records.");
        } else {
            System.out.println("Error: Project could not be fully deleted.");
        }
    }

    public void toggleProjectVisibility(int projectID, boolean visible) {
        Project project = Project.getProjectById(projectID);
        if (project != null && managedProjects.contains(project)) {
            project.setProjectVisibility(visible);
            System.out.println("Project visibility set to: " + visible);
        } else {
            System.out.println("Error: Project not found or you don't have permission to modify it.");
        }
    }

    // === Officer Registration Management ===
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

    // === Application Management ===
    public void approveBTOApplication(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        // Search in the project's application list instead of global list
        for (BTOapplication app : project.getApplicationList()) {
            if (app.getUserID().equals(applicantNRIC) && app.getStatus().equals("Pending Approval")) {
                if (app.getUnitType().equals("2-Room") && project.getTwoRoomCount() > 0) {
                    project.setTwoRoomCount(project.getTwoRoomCount() - 1);
                    app.updateStatus("Successful", "2-Room");
                    System.out.println("Application approved for 2-Room flat.");
                } else if (app.getUnitType().equals("3-Room") && project.getThreeRoomCount() > 0) {
                    project.setThreeRoomCount(project.getThreeRoomCount() - 1);
                    app.updateStatus("Successful", "3-Room");
                    System.out.println("Application approved for 3-Room flat.");
                } else {
                    System.out.println("Error: No available units of the requested type.");
                }
                return;
            }
        }
        System.out.println("No pending application found for applicant with NRIC: " + applicantNRIC);
    }

    public void rejectBTOApplication(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app : project.getApplicationList()) {
            if (app.getUserID().equals(applicantNRIC) && app.getStatus().equals("Pending Approval")) {
                app.updateStatus("Unsuccessful", app.getUnitType());
                System.out.println("Application rejected.");
                return;
            }
        }
        System.out.println("No pending application found for applicant with NRIC: " + applicantNRIC);
    }
    
    public void approveWithdrawalRequest(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app : project.getApplicationList()) {
            if (app.getUserID().equals(applicantNRIC) && app.getStatus().equals("Withdrawal Requested")) {
                String previousStatus = app.getStatus();
                app.updateStatus("Withdrawn", app.getUnitType());
                
                // If the application was approved or booked, return the unit to the inventory
                if (previousStatus.equals("Successful") || previousStatus.equals("Booked")) {
                    if (app.getUnitType().equals("2-Room")) {
                        project.setTwoRoomCount(project.getTwoRoomCount() + 1);
                    } else if (app.getUnitType().equals("3-Room")) {
                        project.setThreeRoomCount(project.getThreeRoomCount() + 1);
                    }
                }
                
                System.out.println("Withdrawal request approved.");
                return;
            }
        }
        System.out.println("No withdrawal request found for applicant with NRIC: " + applicantNRIC);
    }

    public void rejectWithdrawalRequest(int projectID, String applicantNRIC) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app : project.getApplicationList()) {
            if (app.getUserID().equals(applicantNRIC) && app.getStatus().equals("Withdrawal Requested")) {
                // Set the status back to what it was before the withdrawal request
                app.updateStatus(app.getPreviousStatus(), app.getUnitType());
                System.out.println("Withdrawal request rejected.");
                return;
            }
        }
        System.out.println("No withdrawal request found for applicant with NRIC: " + applicantNRIC);
    }
    // === Enquiry Management ===
    @Override
    public void viewEnquiriesAll() {
        System.out.println("All Enquiries:");
        Enquiry.displayAllEnquiries();
    }
    
    public void viewProjectEnquiries(int projectID) {
        Project project = Project.getProjectById(projectID);
        if (project == null) {
            System.out.println("Error: Project not found.");
            return;
        }
        
        System.out.println("Enquiries for Project: " + project.getProjectName());
        // Assuming Enquiry class has method to filter by projectId
        // This would need to be implemented
        // Enquiry.displayByProject(projectID);
    }
    
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

    // === Reports & Views ===
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
    
    public void viewAllProjects(String field) {
        Project.displayAllProjects(field);
    }

    // === Helper Methods ===
    private boolean isManagingProjectDuringPeriod(LocalDate newOpening, LocalDate newClosing) {
        for (Project project : managedProjects) {
            if (datesOverlap(project.getOpeningDate(), project.getClosingDate(), newOpening, newClosing)) {
                return true;
            }
        }
        return false;
    }
    public void addProject(Project project) {
    	managedProjects.add(project);
    }

    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}