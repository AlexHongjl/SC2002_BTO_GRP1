package bto.model;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends UserPerson {

    private int appliedProjectId = -1;
    protected String applicationStatus = "None"; // None, Pending, Successful, Booked, Unsuccessful, Withdrawn
    private String flatTypeBooked = null;
    protected boolean hasWithdrawn = false;

    private final List<Enquiry> enquiries = new ArrayList<>();

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password, "applicant");
    }

    //applicant
    @Override
    public void viewOwnStatus() {
        super.viewOwnStatus();
        System.out.println("Applied Project ID: " + appliedProjectId);
        System.out.println("Application Status: " + applicationStatus);
        System.out.println("Flat Type Booked: " + (flatTypeBooked != null ? flatTypeBooked : "None"));
        System.out.println("Has Withdrawn: " + hasWithdrawn);
    }


    public boolean isEligible(Project project, String flatType) {
        if (!project.isProjectVisibility()) return false;
        if (flatType.equals("2-Room")) {
            return (getMaritalStatus().equalsIgnoreCase("Single") && getAge() >= 35) ||
                   (getMaritalStatus().equalsIgnoreCase("Married") && getAge() >= 21);
        }
        if (flatType.equals("3-Room")) {
            return getMaritalStatus().equalsIgnoreCase("Married") && getAge() >= 21;
        }
        return false;
    }

    public boolean applyForProject(Project project, String flatType) {
        if (getAppliedProjectId() != -1 && !hasWithdrawn) return false;

        if (this instanceof HDBofficer) {
            HDBofficer officer = (HDBofficer) this;
            for (OfficerRegistration reg : officer.getOfficerApplications()) {
                if (reg.getProject().getProjectId() == project.getProjectId()) {
                    System.out.println("Error: You have already applied to this project as an officer.");
                    return false;
                }
            }
            for (Project pro : officer.getRegisteredProjects()) {
                if (pro.getProjectId() == project.getProjectId()) {
                    System.out.println("Error: You are registered to this project as an officer.");
                    return false;
                }
            }
        }

        if (!isEligible(project, flatType)) return false;

        //  Delete any old withdrawn applications
        BTOapplication.getAllApplications().removeIf(app ->
            app.getUserID().equals(this.getNRIC()) &&
            app.getStatus().equalsIgnoreCase("Withdrawn")
        );

        this.setAppliedProjectId(project.getProjectId());
        this.applicationStatus = "Pending";
        this.hasWithdrawn = false;
        return true;
    }

    public boolean withdrawApplication() {
        if (getAppliedProjectId() == -1) return false;

        for (BTOapplication app : BTOapplication.getAllApplications()) {
            if (app.getUserID().equals(getNRIC()) &&
                app.getProjectId() == getAppliedProjectId()) {

                // Store current status before requesting withdrawal
                app.setPreviousStatus(app.getStatus());
                app.updateStatus("Pending Withdrawal", app.getUnitType());

                // Update applicant object too
                this.applicationStatus = "Pending Withdrawal";
                this.hasWithdrawn = false; // still pending, not actually withdrawn yet
                return true;
            }
        }
        return false;
    }
    public void setApplicationStatus(String status) {
        this.applicationStatus = status;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void bookFlat(String flatType) {
        if (applicationStatus.equals("Successful")) {
            this.flatTypeBooked = flatType;
            this.applicationStatus = "Booked";
        }
    }

    public String getFlatTypeBooked() {
        return flatTypeBooked;
    }

    public int getAppliedProjectId() {
        return appliedProjectId;
    }

    public boolean hasApplied() {
        return getAppliedProjectId() != -1 && !hasWithdrawn;
    }

    public boolean isWithdrawn() {
        return hasWithdrawn;
    }
    
    public Project getAppliedProject() {
        if (getAppliedProjectId() == -1) return null;
        return Project.getProjectById(getAppliedProjectId());
    }

    //for enquiries

    public void addEnquiry(Enquiry e) {
        enquiries.add(e);
    }

    public void editEnquiry(int enquiryId, String newMessage) {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryID() == enquiryId) {
                e.setMessage(newMessage);
                break;
            }
        }
    }

    public void deleteEnquiry(int enquiryId) {
        enquiries.removeIf(e -> e.getEnquiryID() == enquiryId);
    }

    public void viewEnquiriesAll() {
        for (Enquiry e : enquiries) {
            System.out.println("ID: " + e.getEnquiryID() + ", Message: " + e.getMessage());
        }
    }

	public void setAppliedProjectId(int appliedProjectId) {
		this.appliedProjectId = appliedProjectId;
	}
	
	public void setWithdrawn(boolean withdrawn) {
	    this.hasWithdrawn = withdrawn;
	}

//    @Override
//    public void replyEnquiry(int enquiryId, String replyMessage) {
//        // Normally only Officer/Manager replies, but implemented to fulfill interface
//        for (Enquiry e : enquiries) {
//            if (e.getEnquiryID() == enquiryId) {
//                e.reply(replyMessage);
//                break;
//            }
//        }
//    }
//
//    public List<Enquiry> getEnquiries() {
//        return enquiries;
//    }
}