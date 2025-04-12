package bto.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public static void displayAllProjects(String field) {
        // Create a list from the projects array, skipping null elements
        List<Project> projectList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (projects[i] != null) {
                projectList.add(projects[i]);
            }
        }

        // Sort the list based on the field
        if (field == null || field.isEmpty()) {
            Collections.sort(projectList, Comparator.comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER));
        } else {
            Collections.sort(projectList, (p1, p2) -> {
                switch (field.toLowerCase()) {
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
                        System.out.println("Unknown sorting field: " + field);
                        return 0;
                }
            });
        }

        // Print all details for each project
        System.out.println("=============== All Projects ===============");
        for (Project project : projectList) {
            System.out.println("--------------------------------------------");
            System.out.println("Project ID: " + project.getProjectId());
            System.out.println("Name: " + project.getProjectName());
            System.out.println("Neighbourhood: " + project.getNeighbourhood());
            System.out.println("Two Room Count: " + project.getTwoRoomCount());
            System.out.println("Three Room Count: " + project.getThreeRoomCount());
            System.out.println("Project Visibility: " + (project.isProjectVisibility() ? "Visible" : "Hidden"));
            System.out.println("Opening Date: " + (project.getOpeningDate() != null ? project.getOpeningDate() : "Not set"));
            System.out.println("Closing Date: " + (project.getClosingDate() != null ? project.getClosingDate() : "Not set"));
            System.out.println("Officer Slots: " + project.getOfficerSlots());
            System.out.println("Manager In Charge: " + (project.getManagerInCharge() != null ? project.getManagerInCharge().getName() : "None"));
            System.out.println("Number of Officers Assigned: " + project.getOfficers().size());
            System.out.println("Number of Officer Applications: " + project.getOfficerRegistrations().size());
        }
        System.out.println("============================================");
    }


    public static void displayAllProjectsApplicant(String field) {
        // Create a list from the projects array, including only visible projects
        List<Project> visibleProjects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (projects[i] != null && projects[i].isProjectVisibility()) {
                visibleProjects.add(projects[i]);
            }
        }
        
        // Sort the list based on the field
        if (field == null || field.isEmpty()) {
            // Default sort by project name
            Collections.sort(visibleProjects, Comparator.comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER));
        } else {
            // Sort based on the specified field
            Collections.sort(visibleProjects, (p1, p2) -> {
                switch (field.toLowerCase()) {
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
                    case "openingdate":
                        return p1.getOpeningDate().compareTo(p2.getOpeningDate());
                    case "closingdate":
                        return p1.getClosingDate().compareTo(p2.getClosingDate());
                    case "officerslots":
                        return Integer.compare(p1.getOfficerSlots(), p2.getOfficerSlots());
                    default:
                        System.out.println("Unknown sorting field: " + field);
                        return 0;
                }
            });
        }
        
        // Print header
        System.out.println("============ Available Projects ============");
        System.out.println("ID | Name | Neighbourhood | Two-Room | Three-Room");
        System.out.println("-------------------------------------------");
        
        // Display each project
        for (Project project : visibleProjects) {
            System.out.printf("%d | %s | %s | %d | %d%n", 
                project.getProjectId(), 
                project.getProjectName(), 
                project.getNeighbourhood(),
                project.getTwoRoomCount(),
                project.getThreeRoomCount());
        }
        System.out.println("===========================================");
    }
    public static void display(int projectIdToFind) {
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (projects[i] != null && projects[i].projectId == projectIdToFind) {
                Project project = projects[i];
                System.out.println("===== Project Details =====");
                System.out.println("Project ID: " + project.projectId);
                System.out.println("Project Name: " + project.projectName);
                System.out.println("Neighbourhood: " + project.neighbourhood);
                System.out.println("Two Room Count: " + project.twoRoomCount);
                System.out.println("Three Room Count: " + project.threeRoomCount);
                System.out.println("Project Visibility: " + (project.projectVisibility ? "Visible" : "Hidden"));
                System.out.println("Manager In Charge: " + project.managerInCharge);
                System.out.println("Officer Slots: " + project.officerSlots);
                System.out.println("Open Date: " + (project.openDate != null ? project.openDate : "Not set"));
                System.out.println("Close Date: " + (project.closeDate != null ? project.closeDate : "Not set"));
                System.out.println("Number of Officers: " + project.officerList.size());
                System.out.println("Number of Officer Applications: " + project.officerApplicantList.size());
                System.out.println("==========================");
                found = true;
                break;
            }
        }
            
        if (!found) {
            System.out.println("No project found with ID: " + projectIdToFind);
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

