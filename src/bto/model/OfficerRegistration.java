package bto.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OfficerRegistration {
    private String registrationStatus;
    private HDBofficer officer; // Reference to the officer
    private Project project; // Reference to the project being registered for

    public OfficerRegistration(HDBofficer officer, Project project) {
        this.officer = officer;
        this.project = project;
        this.registrationStatus = "Pending"; // Default status
        
    }

    // === Core Methods ===
    private boolean checkSlots() {
        return project.getOfficers().size() < project.getOfficerSlots();
    }


    // === Status Management ===
    public void approveRegistration() {
        if (checkSlots()) {
            this.registrationStatus = "Approved";
            project.addOfficer(officer);
            officer.addRegisteredProject(project);
        }
    }

    public void rejectRegistration() {
        this.registrationStatus = "Rejected";
    }

    // === Getters and Setters ===
    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public Officer getOfficer() {
        return officer;
    }

    public Project getProject() {
        return project;
    }
}