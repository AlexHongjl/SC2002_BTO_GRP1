package bto.model;

import java.io.BufferedReader;
import java.io.FileWriter;
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
    private int twoRoompx;
    private int threeRoomCount;
    private int threeRoompx;
    private boolean projectVisibility;
    private HDBmanager managerInCharge;
    private int officerSlots;
    private LocalDate openDate;
    private LocalDate closeDate;
    private List<HDBofficer> officerList;
    private List<OfficerRegistration> officerApplicantList;
    private List<BTOapplication> applicationList; // Added application list
    
    private static Project[] projects = new Project[100];
    private static int count = 0;
    
    

    public Project(String projectName, String neighbourhood,
            int twoRoomCount, int threeRoomCount,
            boolean projectVisibility, LocalDate openDate,
            LocalDate closeDate, int officerSlots, HDBmanager managerInCharge,int twoRoompx, int threeRoompx) {

    	this.projectId = setCount(getCount() + 1); // Unique project ID
    	this.projectName = projectName;
    	this.neighbourhood = neighbourhood;
    	this.twoRoomCount = twoRoomCount;
    	this.twoRoompx=twoRoompx;
    	this.threeRoomCount = threeRoomCount;
    	this.threeRoompx=threeRoompx;
    	this.projectVisibility = projectVisibility;
    	this.openDate = openDate;
    	this.closeDate = closeDate;
    	this.officerSlots = officerSlots;
    	this.managerInCharge = managerInCharge;
    	this.officerList = new ArrayList<>();
    	this.officerApplicantList = new ArrayList<>();
        this.applicationList = new ArrayList<>(); // Initialize application list

    	if (getCount() <= projects.length) {
    		projects[getCount() - 1] = this; // Add to project list
    	} else {
    		System.out.println("Error: Maximum number of projects reached.");
    	}
    }
    
    

    public static void writeCSVProjects() {
        String project = "Project Name,Neighborhood,Type 1,Number of units for Type 1,Selling price for Type 1,Type 2,Number of units for Type 2,Selling price for Type 2,Application opening date,Application closing date,Manager,Officer Slot,Officer\n";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        for (Project proj : projects) {
            if (proj != null) {
                String openDate = (proj.getOpeningDate() != null) ? proj.getOpeningDate().format(formatter) : "Not set";
                String closeDate = (proj.getClosingDate() != null) ? proj.getClosingDate().format(formatter) : "Not set";

                project += proj.getProjectName()
                    + "," + proj.getNeighbourhood()
                    + "," + "2-Room"
                    + "," + proj.getTwoRoomCount()
                    + "," + proj.getTwoRoompx()
                    + "," + "3-Room"
                    + "," + proj.getThreeRoomCount()
                    + "," + proj.getThreeRoompx()
                    + "," + openDate
                    + "," + closeDate
                    + "," + proj.getManagerInCharge().getName()
                    + "," + proj.getOfficerSlots()
                    + ",\"" + String.join(",", proj.getOfficers().stream().map(x -> x.getName()).toList())
                    + "\"\n";
            }
        }

        try (FileWriter writer = new FileWriter("data/ProjectList.csv")) {
            writer.write(project);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadProjectsFromCSV(List<UserPerson> Users) {
        String path = "data/ProjectList.csv";
        count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] tokens = smartSplitCSVLine(line);
                if (tokens.length < 13) continue;

                String projectName = tokens[0].trim();
                String neighbourhood = tokens[1].trim();
                String type1 = tokens[2].trim();
                int units1 = Integer.parseInt(tokens[3].trim());
                int price1 = Integer.parseInt(tokens[4].trim());
                String type2 = tokens[5].trim();
                int units2 = Integer.parseInt(tokens[6].trim());
                int price2 = Integer.parseInt(tokens[7].trim());
                LocalDate openDate = LocalDate.parse(tokens[8].trim(), DateTimeFormatter.ofPattern("d/M/yyyy"));
                LocalDate closeDate = LocalDate.parse(tokens[9].trim(), DateTimeFormatter.ofPattern("d/M/yyyy"));
                String managerName = tokens[10].trim();
                int officerSlots = Integer.parseInt(tokens[11].trim());
                String[] officerNames = tokens[12].replace("\"", "").split(",");

                int twoRoomCount = 0;
                int threeRoomCount = 0;

                if (type1.equals("2-Room")) twoRoomCount = units1;
                else if (type1.equals("3-Room")) threeRoomCount = units1;

                if (type2.equals("2-Room")) twoRoomCount = units2;
                else if (type2.equals("3-Room")) threeRoomCount = units2;

                // Find manager
                HDBmanager manager = null;
                for (UserPerson m : Users) {
                    if (m instanceof HDBmanager && m.getName().equalsIgnoreCase(managerName)) {
                        manager = (HDBmanager) m;
                        break;
                    }
                }

                if (manager == null) {
                    System.out.println("⚠ Manager \"" + managerName + "\" not found. Skipping project: " + projectName);
                    continue;
                }

                // Create project
                Project p = new Project(
                    projectName,
                    neighbourhood,
                    twoRoomCount,
                    threeRoomCount,
                    true,
                    openDate,
                    closeDate,
                    officerSlots,
                    manager,
                    price1,
                    price2
                );
                manager.addProject(p);

                // Reattach officers from CSV column directly (skip approval check)
                for (String officerName : officerNames) {
                    for (UserPerson u : Users) {
                        if (u instanceof HDBofficer && u.getName().equalsIgnoreCase(officerName.trim())) {
                            HDBofficer o = (HDBofficer) u;

                            if (!p.getOfficerList().contains(o)) {
                                p.getOfficerList().add(o);
                            }

                            if (!o.getRegisteredProjects().contains(p)) {
                                o.getRegisteredProjects().add(p);
                            }

                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


      
      public static String[] smartSplitCSVLine(String line) {
    	    ArrayList<String> result = new ArrayList<>();
    	    StringBuilder sb = new StringBuilder();
    	    boolean inQuotes = false;

    	    for (int i = 0; i < line.length(); i++) {
    	        char c = line.charAt(i);

    	        if (c == '"') {
    	            inQuotes = !inQuotes; // toggle state
    	        } else if (c == ',' && !inQuotes) {
    	            result.add(sb.toString().trim());
    	            sb.setLength(0); // clear buffer
    	        } else {
    	            sb.append(c);
    	        }
    	    }
    	    result.add(sb.toString().trim()); // add last value
    	    return result.toArray(new String[0]);
    	}

      public static void displayAllSummary() {
    	    System.out.println("=========== All Projects ===========");
    	    System.out.println("ID | Name              | Neighbourhood     | Two-Room | Three-Room");
    	    System.out.println("---------------------------------------------------------------");

    	    for (Project p : getProjects()) {
    	        if (p == null) continue;
    	        System.out.printf("%-2d | %-17s | %-18s | %-9d | %-11d%n",
    	                p.getProjectId(),
    	                p.getProjectName(),
    	                p.getNeighbourhood(),
    	                p.getTwoRoomCount(),
    	                p.getThreeRoomCount());
    	    }

    	    System.out.println("===============================================================");
    	}
      
    
    public List<HDBofficer> getOfficerList() {
        return officerList;
    }
    
    public List<OfficerRegistration> getOfficerApplicantList() {
        return this.officerApplicantList;
    }

    public List<BTOapplication> getApplicationList() {
        return this.applicationList;
    }

    public void addApplication(BTOapplication application) {
        this.applicationList.add(application);
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
    
    // Method to get application by user ID within this project
    public BTOapplication getApplicationByUserId(String userId) {
        for (BTOapplication app : applicationList) {
            if (app.getUserID().equals(userId)) {
                return app;
            }
        }
        return null;
    }
    
    // Static method to find an application by user ID across all projects
    public static BTOapplication findApplicationByUserId(String userId) {
        for (int i = 0; i < count; i++) {
            Project p = projects[i];
            if (p == null) continue;
            
            BTOapplication app = p.getApplicationByUserId(userId);
            if (app != null) {
                return app;
            }
        }
        return null;
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
    
    public void displayApplicationList() {
        System.out.println("Displaying BTO Applications for Project " + projectName + ":");
        if (applicationList == null || applicationList.isEmpty()) {
            System.out.println("No applications for this project.");
            return;
        }
        for (BTOapplication app : applicationList) {
            System.out.println("User ID: " + app.getUserID() + 
                               ", Unit Type: " + app.getUnitType() + 
                               ", Status: " + app.getStatus());
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
                    case "tworoompx":
                        return Integer.compare(p1.getTwoRoompx(), p2.getTwoRoompx());
                    case "threeroompx":
                        return Integer.compare(p1.getThreeRoompx(), p2.getThreeRoompx());
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
            System.out.println("Two Room Price: " + project.getTwoRoompx());
            System.out.println("Three Room Price: " + project.getThreeRoompx());
            System.out.println("Project Visibility: " + (project.isProjectVisibility() ? "Visible" : "Hidden"));
            System.out.println("Opening Date: " + (project.getOpeningDate() != null ? project.getOpeningDate() : "Not set"));
            System.out.println("Closing Date: " + (project.getClosingDate() != null ? project.getClosingDate() : "Not set"));
            System.out.println("Officer Slots: " + project.getOfficerSlots());
            System.out.println("Manager In Charge: " + (project.getManagerInCharge() != null ? project.getManagerInCharge().getName() : "None"));
            System.out.println("Number of Officers Assigned: " + project.getOfficers().size());
            if (!project.getOfficers().isEmpty()) {
                System.out.println("Assigned Officers:");
                for (HDBofficer officer : project.getOfficers()) {
                    System.out.println(" - " + officer.getName() );//+ " (NRIC: " + officer.getNRIC() + ")"
                }
            } else {
                System.out.println("Assigned Officers: None");
            }
            System.out.println("Applications: " + project.getApplicationList().size());
        }
        System.out.println("============================================");
    }


    public static void displayAllProjectsApplicant(String field, Applicant applicant) {
        // Create a list from the projects array, including only visible projects that the applicant is eligible for
        List<Project> eligibleProjects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (projects[i] != null && projects[i].isProjectVisibility()) {
                // Check if the applicant is eligible for this project
                boolean isEligible = false;
                
                // Singles aged 35+ can only apply for 2-Room
                if (applicant.getMaritalStatus().equalsIgnoreCase("Single") && applicant.getAge() >= 35) {
                    if (projects[i].getTwoRoomCount() > 0) {
                        isEligible = true;
                    }
                } 
                // Married aged 21+ can apply for any flat types
                else if (applicant.getMaritalStatus().equalsIgnoreCase("Married") && applicant.getAge() >= 21) {
                    if (projects[i].getTwoRoomCount() > 0 || projects[i].getThreeRoomCount() > 0) {
                        isEligible = true;
                    }
                }
                
                if (isEligible) {
                    eligibleProjects.add(projects[i]);
                }
            }
        }
        
        // Sort the list based on the field
        if (field == null || field.isEmpty()) {
            // Default sort by project name
            Collections.sort(eligibleProjects, Comparator.comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER));
        } else {
            // Sort based on the specified field
            Collections.sort(eligibleProjects, (p1, p2) -> {
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
                    case "tworoompx":
                        return Integer.compare(p1.getTwoRoompx(), p2.getTwoRoompx());
                    case "threeroompx":
                        return Integer.compare(p1.getThreeRoompx(), p2.getThreeRoompx());
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
        System.out.println("=========== Available Eligible Projects ===========");
        System.out.println("ID | Name | Neighbourhood | Two-Room | Three-Room");
        System.out.println("---------------------------------------------------");
        
        // Display each project
        for (Project project : eligibleProjects) {
            // Show available flat types for this applicant
            String twoRoomInfo = project.getTwoRoomCount() > 0 ? String.valueOf(project.getTwoRoomCount()) : "-";
            String threeRoomInfo = project.getThreeRoomCount() > 0 ? String.valueOf(project.getThreeRoomCount()) : "-";
            
            // If single, not eligible for 3-Room
            if (applicant.getMaritalStatus().equalsIgnoreCase("Single")) {
                threeRoomInfo = "Not Eligible";
            }
            
            System.out.printf("%d | %s | %s | %s | %s%n", 
                project.getProjectId(), 
                project.getProjectName(), 
                project.getNeighbourhood(),
                twoRoomInfo,
                threeRoomInfo);
        }
        
        if (eligibleProjects.isEmpty()) {
            System.out.println("No eligible projects available for you.");
        }
        
        System.out.println("===================================================");
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
                System.out.println("Two Room Price: " + project.getTwoRoompx());
                System.out.println("Three Room Price: " + project.getThreeRoompx());
                System.out.println("Project Visibility: " + (project.projectVisibility ? "Visible" : "Hidden"));
                System.out.println("Manager In Charge: " + project.managerInCharge.getName());
                System.out.println("Officer Slots: " + project.officerSlots);
                System.out.println("Open Date: " + (project.openDate != null ? project.openDate : "Not set"));
                System.out.println("Close Date: " + (project.closeDate != null ? project.closeDate : "Not set"));
                System.out.println("Number of Officers Assigned: " + project.officerList.size());
                if (!project.officerList.isEmpty()) {
                    for (HDBofficer officer : project.officerList) {
                        System.out.println("- Officer: " + officer.getName() );//+ " (NRIC: " + officer.getNRIC() + ")"
                    }
                } else {
                    System.out.println("No officers assigned.");
                }

                System.out.println("Number of Officer Applications: " + project.officerApplicantList.size());
                System.out.println("Number of BTO Applications: " + project.applicationList.size());
                System.out.println("==========================");
                found = true;
                break;
            }
        }
            
        if (!found) {
            System.out.println("No project found with ID: " + projectIdToFind);
        }
    }

    // Method to display all BTO applications with specific status
    public void displayApplicationsByStatus(String status) {
        System.out.println("=== " + status + " Applications for Project " + projectName + " ===");
        boolean found = false;
        for (BTOapplication app : applicationList) {
            if (app.getStatus().equals(status)) {
                System.out.println(app);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No applications with status: " + status);
        }
    }
    
    // Get all applications across all projects
    public static List<BTOapplication> getAllApplications() {
        List<BTOapplication> allApps = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (projects[i] != null) {
                allApps.addAll(projects[i].getApplicationList());
            }
        }
        return allApps;
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
	
	public int getTwoRoompx() {
	    return twoRoompx;
	}

	public void setTwoRoompx(int twoRoompx) {
	    this.twoRoompx = twoRoompx;
	}

	public int getThreeRoompx() {
	    return threeRoompx;
	}

	public void setThreeRoompx(int threeRoompx) {
	    this.threeRoompx = threeRoompx;
	}

}

