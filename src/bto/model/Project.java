package bto.model;

import java.time.LocalDate;
import java.util.List;

public class Project {
    private int projectId;
    private String projectName;
    private String neighbourhood;
    private int twoRoomCount;
    private int threeRoomCount;
    private boolean projectVisibility;
    private int managerInCharge;
    private int officerSlots;
    private LocalDate openDate;
    private LocalDate closeDate;
    private List<Integer> officerlist;
    private List<Integer> officerApplicantList;
    
    private static Project[] projects = new Project[100];
    private static int count = 0;
    
    

    public Project(int projectId, String projectName, String manager) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.managerInCharge = manager;
        this.officerApplicantList = new ArrayList<>();
    }

    public void displayOfficerList() {
        System.out.println("Displaying Officer List...");
    }

    public void displayApplicantList() {
        System.out.println("Displaying Applicant List...);
    }
    
    public int getTwoRoomAvailability() {
    	return twoRoomCount-officerApplicantList.size();
    }
    
  //------------------------------------------------------------------separate proj list functions
    
    public static void addProject(Project project) {
        if (count < projects.length) {
            projects[count++] = project;
        } else {
            System.out.println("Project array is full.");
        }
    }

    public static boolean removeProjectById(int projectId) {
        for (int i = 0; i < count; i++) {
            if (projects[i].projectId == projectId) {
                for (int j = i; j < count - 1; j++) {
                    projects[j] = projects[j + 1];
                }
                projects[count - 1] = null;
                count--;
                return true;
            }
        }
        return false;
    }

    public static Project getProjectById(int projectId) {
        for (int i = 0; i < count; i++) {
            if (projects[i].projectId == projectId) {
                return projects[i];
            }
        }
        return null;
    }

    public static void displayAllProjects() {
        for (int i = 0; i < count; i++) {
            System.out.println("Project ID: " + projects[i].projectId + ", Name: " + projects[i].projectName);
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

    public int getManagerInCharge() {
        return managerInCharge;
    }

    public void setManagerInCharge(int managerInCharge) {
        this.managerInCharge = managerInCharge;
    }
}

