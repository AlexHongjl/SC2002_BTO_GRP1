package bto.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    //add project specific enquiry list
    
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

      public static void loadProjectsFromCSV(List<UserPerson> Users) {
        String path = "data/ProjectList.csv";
    
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true;
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
    
                String[] parts = line.split(",");
    
                if (parts.length < 13) continue;
    
                String projectName = parts[0].trim();
                String neighbourhood = parts[1].trim();
                String type1 = parts[2].trim();
                int countType1 = Integer.parseInt(parts[3].trim());
                String type2 = parts[5].trim();
                int countType2 = Integer.parseInt(parts[6].trim());
                LocalDate openDate = LocalDate.parse(parts[8].trim(), DateTimeFormatter.ofPattern("d/M/yyyy"));
                LocalDate closeDate = LocalDate.parse(parts[9].trim(), DateTimeFormatter.ofPattern("d/M/yyyy"));
                String managerName = parts[10].trim();
                int officerSlots = Integer.parseInt(parts[11].trim());
                String[] officerNames = parts[12].split(";|,");
    
                int twoRoomCount = 0;
                int threeRoomCount = 0;
    
                if (type1.equals("2-Room")) twoRoomCount = countType1;
                else if (type1.equals("3-Room")) threeRoomCount = countType1;
    
                if (type2.equals("2-Room")) twoRoomCount = countType2;
                else if (type2.equals("3-Room")) threeRoomCount = countType2;
    
                // Find manager from Users list
                HDBmanager manager = null;
                for (UserPerson m : Users) {
                    if (m instanceof HDBmanager && m.getName().equalsIgnoreCase(managerName)) {
                        manager = (HDBmanager) m;
                        break;
                    }
                }
    
                if (manager == null) {
                    System.out.println("Warning: Manager \"" + managerName + "\" not found. Skipping project: " + projectName);
                    continue;
                }
    
                // Create project using constructor
                Project p = new Project(
                    projectName,
                    neighbourhood,
                    twoRoomCount,
                    threeRoomCount,
                    true,
                    openDate,
                    closeDate,
                    officerSlots,
                    manager
                );

                if(manager!=null) {
                	manager.addProject(p);
                }
    
                // Find and assign officers
                for (String officerName : officerNames) {
                    officerName = officerName.trim();
                    for (UserPerson u : Users) {
                        if (u instanceof HDBofficer && u.getName().equalsIgnoreCase(officerName)) {
                            p.getOfficerList().add((HDBofficer) u);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<HDBofficer> getOfficerList() {
        return officerList;
    }
    
    public List<OfficerRegistration> getOfficerApplicantList() {
        return this.officerApplicantList;
    }

    public static OfficerRegistration getOfficerRegistrationByID(String officerID) {
        for (int i = 0; i < count; i++) {
            Project p = projects[i];
            if (p == null) continue;
    
            for (OfficerRegistration reg : p.officerApplicantList) {
                if (reg.getOfficer().getNRIC().equalsIgnoreCase(officerID)) {
                    return reg;
                }
            }
        }
    
        return null; // not found
    }
    
    

    public void displayOfficerList() {
        System.out.println("Displaying Officer List...");
        if (officerList == null || officerList.isEmpty()) {
            System.out.println("No officers assigned to this project.");
            return;
        }
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
            if (!project.getOfficers().isEmpty()) {
                System.out.println("Assigned Officers:");
                for (HDBofficer officer : project.getOfficers()) {
                    System.out.println(" - " + officer.getName() + " (NRIC: " + officer.getNRIC() + ")");
                }
            } else {
                System.out.println("Assigned Officers: None");
            }
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
        	System.out.printf("%d |  %s  |   %s   |  %d  |   %d%n", 
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
                System.out.println("Number of Officers Assigned: " + project.officerList.size());
                if (!project.officerList.isEmpty()) {
                    for (HDBofficer officer : project.officerList) {
                        System.out.println("- Officer: " + officer.getName() + " (NRIC: " + officer.getNRIC() + ")");
                    }
                } else {
                    System.out.println("No officers assigned.");
                }

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

