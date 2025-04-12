package bto;

import java.util.ArrayList;
import java.util.List;

public class Applicant extends UserPerson {

    private int appliedProjectId = -1;
    private String applicationStatus = "None"; // None, Pending, Successful, Booked, Unsuccessful, Withdrawn
    private String flatTypeBooked = null;
    private boolean hasWithdrawn = false;

    private final List<Enquiry> enquiries = new ArrayList<>();

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password, "applicant");
    }

    //applicant

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
        if (appliedProjectId != -1 && !hasWithdrawn) return false; // already applied

        if (!isEligible(project, flatType)) return false;

        this.appliedProjectId = project.getProjectId();
        this.applicationStatus = "Pending";
        this.hasWithdrawn = false;
        return true;
    }

    public boolean withdrawApplication() {
        if (appliedProjectId == -1) return false;

        this.hasWithdrawn = true;
        this.applicationStatus = "Withdrawn";
        return true;
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
        return appliedProjectId != -1 && !hasWithdrawn;
    }

    public boolean isWithdrawn() {
        return hasWithdrawn;
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