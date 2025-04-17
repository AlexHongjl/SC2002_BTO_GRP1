package bto.model;

import java.time.LocalDate;

public class OfficerRegistration {
    private String registrationStatus;
    private HDBofficer officer; // Reference to the officer
    private Project project; // Reference to the project being registered for
    private LocalDate registrationDate;

    public OfficerRegistration(HDBofficer officer, Project project) {
        this.officer = officer;
        this.project = project;
        this.registrationStatus = "Pending"; // Default status
        this.registrationDate = LocalDate.now();
        
        // Validate eligibility before creating registration
        if (!isEligible()) {
            this.registrationStatus = "Rejected";
            System.out.println("Registration automatically rejected due to eligibility issues.");
        } else {
            // Add this registration to both the project and officer
            project.addOfficerRegistration(this);
            officer.addOfficerApplication(this);
        }
    }

    public void display() {
        System.out.println("Officer Registration Details:");
        System.out.println("Officer Name: " + officer.getName());
        System.out.println("Officer ID: " + officer.getNRIC());
        System.out.println("Project: " + project.getProjectName());
        System.out.println("Project ID: " + project.getProjectId());
        System.out.println("Status: " + registrationStatus); // e.g. "pending", "approved", etc.
    }
    

    // === Eligibility Checks ===
    private boolean isEligible() {
        return !isApplicantForSameProject() && !hasOverlappingProjects() && hasAvailableSlots();
    }
    
    private boolean isApplicantForSameProject() {
        // Check if officer is also an applicant for this project
        if (officer.getAppliedProjectId() == project.getProjectId()) {
            System.out.println("Error: Officer has already applied for this project as an applicant.");
            return true;
        }
        return false;
    }
    
    private boolean hasOverlappingProjects() {
        // Check if officer is handling another project during the same period
        LocalDate newStart = project.getOpeningDate();
        LocalDate newEnd = project.getClosingDate();
        
        for (Project registeredProject : officer.getRegisteredProjects()) {
            LocalDate existingStart = registeredProject.getOpeningDate();
            LocalDate existingEnd = registeredProject.getClosingDate();
            
            if (datesOverlap(existingStart, existingEnd, newStart, newEnd)) {
                System.out.println("Error: Officer is already handling a project during this period.");
                return true;
            }
        }
        
        // Also check pending applications
        for (OfficerRegistration reg : officer.getOfficerApplications()) {
            // Skip if it's the same registration or already rejected
            if (reg == this || reg.getRegistrationStatus().equals("Rejected")) {
                continue;
            }
            
            Project otherProject = reg.getProject();
            if (datesOverlap(otherProject.getOpeningDate(), otherProject.getClosingDate(),
                             newStart, newEnd)) {
                System.out.println("Error: Officer has a pending registration for another project during this period.");
                return true;
            }
        }
        
        return false;
    }
    
    private boolean hasAvailableSlots() {
        // Check if project has available slots
        if (project.getOfficers().size() >= project.getOfficerSlots()) {
            System.out.println("Error: No available slots in project: " + project.getProjectName());
            return false;
        }
        return true;
    }
    
    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    // === Status Management ===
    public void approveRegistration() {
        if (registrationStatus.equals("Pending")) {
            if (hasAvailableSlots()) {
                registrationStatus = "Approved";
                project.addOfficer(officer);
                officer.addRegisteredProject(project);
                System.out.println("Officer registration approved for project: " + project.getProjectName());
            } else {
                System.out.println("Error: No available slots in project: " + project.getProjectName());
            }
        } else {
            System.out.println("Error: Can only approve pending registrations. Current status: " + registrationStatus);
        }
    }

    public void rejectRegistration() {
        if (registrationStatus.equals("Pending")) {
            registrationStatus = "Rejected";
            System.out.println("Officer registration rejected for project: " + project.getProjectName());
        } else {
            System.out.println("Error: Can only reject pending registrations. Current status: " + registrationStatus);
        }
    }

    // === Getters and Setters ===
    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public HDBofficer getOfficer() {
        return officer;
    }

    public Project getProject() {
        return project;
    }
    
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
}