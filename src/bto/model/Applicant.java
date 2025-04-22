package bto.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Applicant class that inherits from UserPerson class
 * Applicant is a subclass of UserPerson, inheriting attributes and methods from the superclass
 * Attributes inherited: name, NRIC, age, marital status, password and user type
 */
public class Applicant extends UserPerson {

    private int appliedProjectId = -1;
    protected String applicationStatus = "None"; // None, Pending, Successful, Booked, Unsuccessful, Withdrawn
    private String flatTypeBooked = null;
    protected boolean hasWithdrawn = false;

    private final List<Enquiry> enquiries = new ArrayList<>();

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password, "applicant");
    }

    /**
     * Prints project details of applicant, project ID, status, booked flat type and withdrawal status
     * Application Status set to "None" by default, once an application is made, it will become "Pending"
     * Upon Manager's approval it will show "Successful" and upon Officer booking flat, it will turn to "Booked"
     * Manager's refusal will result in "Unsuccessful"
     * Applicant wishing to withdraw from the project will show "Pending Withdrawal" and having the Manager's approval will show "Withdrawn"
     */
    @Override
    public void viewOwnStatus() {
        super.viewOwnStatus();
        System.out.println("Applied Project ID: " + appliedProjectId);
        System.out.println("Application Status: " + applicationStatus);
        System.out.println("Flat Type Booked: " + (flatTypeBooked != null ? flatTypeBooked : "None"));
        System.out.println("Has Withdrawn: " + hasWithdrawn);
    }

    /**
     * Checks eligibility of the person for the flat type, depending on their marital status
     * Returns true if person is eligible and false otherwise
     * 
     * @param project BTO project
     * @param flatType flat type to be checked
     * @return returns true or false
     */
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
    
    /**
     * Applicant applying for project that will be done by the Officer
     * Checks whether Applicant has an existing application, returns false if exists
     * Checks whether Applicant is a current Officer for the project, returns false is they are an Officer
     * Checks eligibility of Applicant, if not eligible, return false
     * Successful creation of application will return true and change application status to "Pending"
     * 
     * @param project BTO project
     * @param flatType flat type to apply for
     * @return returns true or false
     */
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
    
    /**
     * Applicant wishes to withdraw from the project
     * Gets BTO application of Applicant by checking against NRIC
     * Updates status of application to "Pending Withdrawal"
     * returns true when withdrawal has been requested
     * 
     * @return returns true or false
     */
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
    
    /**
     * Setter function to set application status
     */
    public void setApplicationStatus(String status) {
        this.applicationStatus = status;
    }
    
    /**
     * Getter function to get application status
     * 
     * @return returns current application status
     */
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    /**
     * Books flat type depending on the type of flat
     * Done by the Officer
     * Application status will be set to "Booked" upon successful booking and return
     * 
     * @param flatType flat type to be booked
     */
    public void bookFlat(String flatType) {
        if (applicationStatus.equals("Successful")) {
            this.flatTypeBooked = flatType;
            this.applicationStatus = "Booked";
        }
    }
    
    /**
     * Getter function to get booked flat type
     * 
     * @return returns type of flat booked
     */
    public String getFlatTypeBooked() {
        return flatTypeBooked;
    }
    
    /**
     * Getter function to get project ID of project Applicant applied for
     * 
     * @return returns project ID
     */
    public int getAppliedProjectId() {
        return appliedProjectId;
    }
    
    /**
     * Checks whether application has applied to and not withdrawn from project
     * 
     * @return returns true if applied
     */
    public boolean hasApplied() {
        return getAppliedProjectId() != -1 && !hasWithdrawn;
    }
    
    /**
     * Checks if Applicant is withdrawn
     * returns true if Applicant withdrew and false otherwise
     * 
     * @return returns true or false
     */
    public boolean isWithdrawn() {
        return hasWithdrawn;
    }
    
    /**
     * Getter function to get project details
     * 
     * @return returns Project if project exists
     */
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

}