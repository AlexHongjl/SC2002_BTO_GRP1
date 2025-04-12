package bto;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Project {
    private int projectId;
    private String projectName;
    private String neighbourhood;
    private int twoRoomCount;
    private int threeRoomCount;
    private boolean projectVisibility;
    private HDBmanager managerInCharge;
    private int officerSlots;
    private LocalDate openDate;
    private LocalDate closeDate;
    private List<HDBofficer> officerList;
    private List<OfficerRegistration> officerApplicantList;
    
    private static Project[] projects = new Project[100];
    private static int count = 0;
    
    

    public Project(String projectName, String neighbourhood,
            int twoRoomCount, int threeRoomCount,
            boolean projectVisibility, LocalDate openDate,
            LocalDate closeDate, int officerSlots, HDBmanager managerInCharge) {

    	this.projectId = setCount(getCount() + 1); // Unique project ID
    	this.projectName = projectName;
    	this.neighbourhood = neighbourhood;
    	this.twoRoomCount = twoRoomCount;
    	this.threeRoomCount = threeRoomCount;
    	this.projectVisibility = projectVisibility;
    	this.openDate = openDate;
    	this.closeDate = closeDate;
    	this.officerSlots = officerSlots;
    	this.managerInCharge = managerInCharge;
    	this.officerList = new ArrayList<>();
    	this.officerApplicantList = new ArrayList<>();

    	if (getCount() <= projects.length) {
    		projects[getCount() - 1] = this; // Add to project list
    	} else {
    		System.out.println("Error: Maximum number of projects reached.");
    	}
    }


    public void displayOfficerList() {
        System.out.println("Displaying Officer List...");
        for (HDBofficer officer : officerList) {
            System.out.println("Officer: " + officer.getName() + " (NRIC: " + officer.getNRIC() + ")");
        }
    }

    public void displayApplicantList() {
        System.out.println("Displaying Officer Applicant List...");
        for (OfficerRegistration reg : officerApplicantList) {
            System.out.println("Applicant: " + reg.getOfficer().getName() + 
                               " (NRIC: " + reg.getOfficer().getNRIC() + 
                               ") - Status: " + reg.getRegistrationStatus());
        }
    }
    
    
  //------------------------------------------------------------------separate proj list functions
    

    public static boolean removeProjectById(int projectId) {
        for (int i = 0; i < getCount(); i++) {
            if (projects[i].projectId == projectId) {
                for (int j = i; j < getCount() - 1; j++) {
                    projects[j] = projects[j + 1];
                }
                projects[getCount() - 1] = null;
                setCount(getCount() - 1);
                return true;
            }
        }
        return false;
    }

    public static Project getProjectById(int projectId) {
        for (int i = 0; i < getCount(); i++) {
            if (projects[i].projectId == projectId) {
                return projects[i];
            }
        }
        return null;
    }

    public static void displayAllProjects() {
        for (int i = 0; i < getCount(); i++) {
            System.out.println("Project ID: " + projects[i].projectId + ", Name: " + projects[i].projectName);
        }
    }
    public void addOfficerRegistration(OfficerRegistration registration) {
        officerApplicantList.add(registration);
    }
    public void addOfficer(HDBofficer officer) {
        if (!officerList.contains(officer)) {
            officerList.add(officer);
        }
    }
    
    
    //------------------------------------------------------------------separate get set functions
    
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public int getTwoRoomCount() {
        return twoRoomCount;
    }

    public void setTwoRoomCount(int twoRoomCount) {
        this.twoRoomCount = twoRoomCount;
    }

    public int getThreeRoomCount() {
        return threeRoomCount;
    }

    public void setThreeRoomCount(int threeRoomCount) {
        this.threeRoomCount = threeRoomCount;
    }

    public boolean isProjectVisibility() {
        return projectVisibility;
    }

    public void setProjectVisibility(boolean projectVisibility) {
        this.projectVisibility = projectVisibility;
    }

    public HDBmanager getManagerInCharge() {
        return managerInCharge;
    }


    public List<OfficerRegistration> getOfficerRegistrations() {
        return officerApplicantList;
    }
    

    public List<HDBofficer> getOfficers() {
        return officerList;
    }
    

    public int getProjectID() {
        return projectId;
    }
    public LocalDate getOpeningDate() {
        return openDate;
    }
    
    public void setOpeningDate(LocalDate openDate) {
        this.openDate = openDate;
    }
    
    public LocalDate getClosingDate() {
        return closeDate;
    }
    
    public void setClosingDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }
    public int getOfficerSlots() {
        return officerSlots;
    }
    
    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }
    public static Project[] getProjects() {
    	return projects;
    }


	public static int getCount() {
		return count;
	}


	public static int setCount(int count) {
		Project.count = count;
		return count;
	}
}

