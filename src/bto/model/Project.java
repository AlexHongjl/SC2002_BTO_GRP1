package bto.model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * This is class for projects to be created
 * Each project will have many different attributes tied to the project object created
 * Every project object will be stored in a list of projects
 * Managers who create the project will automatically be set as the Manager in charge of that projects
 * Officers can apply to handle the projects based on the availability of officer slots for each project
 * Allows filter of projects to be displayed to officers and applicant for viewing
 * Applicants can create BTO applications for project should they wish to apply for a specific project
 * Projects have opening and closing dates set by the manager
 * Manager can edit the project details but officers and applicants cannot
 * Includes helpful methods to ensure project details are set correctly and displayed accordingly to the needs of the user
 */
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
    
    
    /**
     * Constructor 
     * 
     * @param projectName name
     * @param neighbourhood of project
     * @param twoRoomCount 2 room count
     * @param threeRoomCount 3 room count
     * @param projectVisibility of project
     * @param openDate of project
     * @param closeDate of project
     * @param officerSlots of project
     * @param managerInCharge of project
     * @param twoRoompx in project
     * @param threeRoompx in project
     */
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
        LocalDate today = LocalDate.now();
        if (closeDate.isBefore(today) || openDate.isAfter(today)) {
            this.projectVisibility = false;
        } else {
            this.projectVisibility = projectVisibility; // keep original value if date is valid
        }


    	if (getCount() <= projects.length) {
    		projects[getCount() - 1] = this; // Add to project list
    	} else {
    		System.out.println("Error: Maximum number of projects reached.");
    	}
    }
    
    /**
     * Writes the list of projects back into the original CSV file for data persistence
     * Includes header to differentiate the categories of information for the project
     * Throws a IOException to handle error and prints the stack trace for debugging purposes
     */
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
    
    /**
     * Filter projects based on the filter keyword and filter values
     * Uses stream to check and filter from the list of projects
     * Displays projects filtered by the filter field
     * 
     * @param filterField to be filtered by
     * @param filterValue to be filtered by
     */
    public static void filterProjects(String filterField, String filterValue) {
        // Create a stream from the projects array, skipping null elements
        List<Project> filteredProjects = Arrays.stream(projects)
            .limit(count)
            .filter(Objects::nonNull)
            .filter(project -> {
                // Convert filterValue to lowercase for case-insensitive string comparison
                String filterValueLower = filterValue.toLowerCase();
                
                switch (filterField.toLowerCase()) {
                    case "projectid":
                        try {
                            int idValue = Integer.parseInt(filterValue);
                            return project.getProjectId() == idValue;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for projectId filter.");
                            return false;
                        }
                    case "projectname":
                        return project.getProjectName().toLowerCase().startsWith(filterValueLower);
                    case "neighborhood":
                    case "neighbourhood":
                        return project.getNeighbourhood().toLowerCase().startsWith(filterValueLower);
                    case "tworoomcount":
                        try {
                            int roomCount = Integer.parseInt(filterValue);
                            return project.getTwoRoomCount() == roomCount;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for twoRoomCount filter.");
                            return false;
                        }
                    case "threeroomcount":
                        try {
                            int roomCount = Integer.parseInt(filterValue);
                            return project.getThreeRoomCount() == roomCount;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for threeRoomCount filter.");
                            return false;
                        }
                    case "tworoompx":
                    case "tworoomprice":
                        try {
                            int price = Integer.parseInt(filterValue);
                            return project.getTwoRoompx() == price;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for twoRoompx filter.");
                            return false;
                        }
                    case "threeroompx":
                    case "threeroomprice":
                        try {
                            int price = Integer.parseInt(filterValue);
                            return project.getThreeRoompx() == price;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for threeRoompx filter.");
                            return false;
                        }
                    case "visibility":
                    case "projectvisibility":
                        boolean visibilityValue = Boolean.parseBoolean(filterValue);
                        return project.isProjectVisibility() == visibilityValue;
                    case "manager":
                    case "managerincharge":
                        return project.getManagerInCharge() != null && 
                               project.getManagerInCharge().getName().toLowerCase().startsWith(filterValueLower);
                    case "officerslots":
                        try {
                            int slots = Integer.parseInt(filterValue);
                            return project.getOfficerSlots() == slots;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for officerSlots filter.");
                            return false;
                        }
                    default:
                        System.out.println("Unknown filter field: " + filterField);
                        return false;
                }
            })
            .collect(Collectors.toList());
        
        // Display filtered projects
        System.out.println("=============== Filtered Projects ===============");
        System.out.println("Filter: " + filterField + " = " + filterValue);
        
        if (filteredProjects.isEmpty()) {
            System.out.println("No projects match the filter criteria.");
        } else {
            filteredProjects.forEach(project -> {
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
                    project.getOfficers().forEach(officer -> System.out.println(" - " + officer.getName()));
                } else {
                    System.out.println("Assigned Officers: None");
                }
                System.out.println("Applications: " + project.getApplicationList().size());
            });
        }
        System.out.println("=================================================");
    }

    /**
     * Overloaded method to handle integer filter values directly
     * 
     * @param filterField to be filtered by
     * @param filterValue to be filtered by 
     */
    public static void filterProjects(String filterField, int filterValue) {
        filterProjects(filterField, String.valueOf(filterValue));
    }
    
    /**
     * static method to check if projects should be visible
     */
    public static void refreshProjectVisibilities() {
        LocalDate today = LocalDate.now();

        for (int i = 0; i < count; i++) {
            Project p = projects[i];
            if (p == null) continue;

            if (p.openDate.isAfter(today) || p.closeDate.isBefore(today)) {
                p.projectVisibility = false;
            } else {
            	if(p.openDate == today)
            		p.projectVisibility = true;
            }
        }
    }

    /**
     * Overloaded method to handle boolean filter values directly
     * 
     * @param filterField to be filtered by 
     * @param filterValue to be filtered by 
     */
    public static void filterProjects(String filterField, boolean filterValue) {
        filterProjects(filterField, String.valueOf(filterValue));
    }
    
    /**
     * Filters project applicants based on filter keywords and filter values
     * Uses streams to filter by eligibility of applicant
     * Displays the projects available to them based on their eligibility
     * 
     * @param filterField to be filtered by 
     * @param filterValue to be filtered by 
     * @param applicant to check eligibility
     */
    public static void filterProjectsApplicant(String filterField, String filterValue, Applicant applicant) {
        // Create a stream of eligible projects for the applicant
        List<Project> filteredProjects = Arrays.stream(projects)
            .limit(count)
            .filter(Objects::nonNull)
            // Filter for project visibility - only show visible projects to applicants
            .filter(project -> project.isProjectVisibility())
            // Filter for applicant eligibility
            .filter(project -> {
                // Singles aged 35+ can only apply for 2-Room
                if (applicant.getMaritalStatus().equalsIgnoreCase("Single") && applicant.getAge() >= 35) {
                    return project.getTwoRoomCount() > 0;
                } 
                // Married aged 21+ can apply for any flat types
                else if (applicant.getMaritalStatus().equalsIgnoreCase("Married") && applicant.getAge() >= 21) {
                    return project.getTwoRoomCount() > 0 || project.getThreeRoomCount() > 0;
                }
                return false; // Not eligible otherwise
            })
            // Apply the specific filter criteria
            .filter(project -> {
                String filterValueLower = filterValue.toLowerCase();
                
                switch (filterField.toLowerCase()) {
                    case "projectid":
                        try {
                            int idValue = Integer.parseInt(filterValue);
                            return project.getProjectId() == idValue;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for projectId filter.");
                            return false;
                        }
                    case "projectname":
                        return project.getProjectName().toLowerCase().startsWith(filterValueLower);
                    case "neighborhood":
                    case "neighbourhood":
                        return project.getNeighbourhood().toLowerCase().startsWith(filterValueLower);
                    case "tworoomcount":
                        try {
                            int roomCount = Integer.parseInt(filterValue);
                            return project.getTwoRoomCount() == roomCount;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for twoRoomCount filter.");
                            return false;
                        }
                    case "tworoompx":
                    case "tworoomprice":
                        try {
                            int price = Integer.parseInt(filterValue);
                            return project.getTwoRoompx() == price;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for twoRoompx filter.");
                            return false;
                        }
                    case "threeroomcount":
                        // Singles can't filter by 3-room count since they're not eligible
                        if (applicant.getMaritalStatus().equalsIgnoreCase("Single")) {
                            System.out.println("Singles are not eligible for 3-Room flats.");
                            return false;
                        }
                        try {
                            int roomCount = Integer.parseInt(filterValue);
                            return project.getThreeRoomCount() == roomCount;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for threeRoomCount filter.");
                            return false;
                        }
                    case "threeroompx":
                    case "threeroomprice":
                        // Singles can't filter by 3-room price since they're not eligible
                        if (applicant.getMaritalStatus().equalsIgnoreCase("Single")) {
                            System.out.println("Singles are not eligible for 3-Room flats.");
                            return false;
                        }
                        try {
                            int price = Integer.parseInt(filterValue);
                            return project.getThreeRoompx() == price;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numeric value for threeRoompx filter.");
                            return false;
                        }
                    case "openingdate":
                        try {
                            LocalDate date = LocalDate.parse(filterValue, DateTimeFormatter.ofPattern("d/M/yyyy"));
                            return project.getOpeningDate().equals(date);
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
                            return false;
                        }
                    case "closingdate":
                        try {
                            LocalDate date = LocalDate.parse(filterValue, DateTimeFormatter.ofPattern("d/M/yyyy"));
                            return project.getClosingDate().equals(date);
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Please use dd/MM/yyyy.");
                            return false;
                        }
                    default:
                        System.out.println("Unknown filter field: " + filterField);
                        return false;
                }
            })
            .collect(Collectors.toList());

        // Sort the list based on the field
        if (filterField == null || filterField.isEmpty()) {
            // Default sort by project name
            filteredProjects.sort(Comparator.comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER));
        } else {
            // Sort based on the specified field
            filteredProjects.sort((p1, p2) -> {
                switch (filterField.toLowerCase()) {
                    case "projectid":
                        return Integer.compare(p1.getProjectId(), p2.getProjectId());
                    case "projectname":
                        return p1.getProjectName().compareToIgnoreCase(p2.getProjectName());
                    case "neighborhood":
                    case "neighbourhood":
                        return p1.getNeighbourhood().compareToIgnoreCase(p2.getNeighbourhood());
                    case "tworoomcount":
                        return Integer.compare(p1.getTwoRoomCount(), p2.getTwoRoomCount());
                    case "threeroomcount":
                        return Integer.compare(p1.getThreeRoomCount(), p2.getThreeRoomCount());
                    case "tworoompx":
                    case "tworoomprice":
                        return Integer.compare(p1.getTwoRoompx(), p2.getTwoRoompx());
                    case "threeroompx":
                    case "threeroomprice":
                        return Integer.compare(p1.getThreeRoompx(), p2.getThreeRoompx());
                    case "openingdate":
                        return p1.getOpeningDate().compareTo(p2.getOpeningDate());
                    case "closingdate":
                        return p1.getClosingDate().compareTo(p2.getClosingDate());
                    default:
                        return 0;
                }
            });
        }

        // Print header
        System.out.println("========================== Filtered Eligible Projects ==========================");
        System.out.println("Filter: " + filterField + " = " + filterValue);
        System.out.println("ID | Name | Neighbourhood | Two-Room | Price | Three-Room | Price");
        System.out.println("-------------------------------------------------------------------------");
        
        // Display each project with appropriate eligibility information
        for (Project project : filteredProjects) {
            // Show available flat types for this applicant
            String twoRoomInfo = project.getTwoRoomCount() > 0 ? String.valueOf(project.getTwoRoomCount()) : "-";
            String twoRoomPxs = project.getTwoRoomCount() > 0 ? String.valueOf(project.getTwoRoompx()) : "-";
            String threeRoomInfo;
            String threeRoomPxs;
            
            // If single, not eligible for 3-Room
            if (applicant.getMaritalStatus().equalsIgnoreCase("Single")) {
                threeRoomInfo = "Not Eligible";
                threeRoomPxs = "Not Eligible";
                
            } else {
                threeRoomInfo = project.getThreeRoomCount() > 0 ? String.valueOf(project.getThreeRoomCount()) : "-";
                threeRoomPxs = project.getThreeRoomCount() > 0 ? String.valueOf(project.getThreeRoompx()) : "-";
            }
            
            System.out.printf("%d | %s | %s | %s | %s%n", 
                project.getProjectId(), 
                project.getProjectName(), 
                project.getNeighbourhood(),
                twoRoomInfo,
                twoRoomPxs,
                threeRoomInfo,
                threeRoomPxs);
        }
        
        if (filteredProjects.isEmpty()) {
            System.out.println("No projects match the filter criteria or you are not eligible.");
        }
        
        System.out.println("==================================================================");
    }

    /**
     *  Overloaded method to handle integer filter values directly
     *  
     * @param filterField to be filtered by 
     * @param filterValue to be filtered by 
     * @param applicant to be filtered by 
     */
    public static void filterProjectsApplicant(String filterField, int filterValue, Applicant applicant) {
        filterProjectsApplicant(filterField, String.valueOf(filterValue), applicant);
    }
    
    /**
     * Loads all projects from the CSV file into an array list for easier access during the runtime of the program
     * 
     * @param Users list of users
     */
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


      /**
       * When reading CSV files
       * Splits each line by ','
       * Clears buffer after
       * 
       * @param line to be read in CSV file
       * @return array results
       */
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
      
      /**
       * Displays summary of all projects
       */
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
      
    /**
     * Getter method for getting list of officers
     * 
     * @return list of officers
     */
    public List<HDBofficer> getOfficerList() {
        return officerList;
    }
    
    /**
     * Getter method for getting officers application list
     * 
     * @return list of officer applicants
     */
    public List<OfficerRegistration> getOfficerApplicantList() {
        return this.officerApplicantList;
    }
    
    /**
     * Getter method for getting application list
     * 
     * @return application list
     */
    public List<BTOapplication> getApplicationList() {
        return this.applicationList;
    }
    
    /**
     * Adds application created to the list of all applications
     * 
     * @param application created by applicants
     */
    public void addApplication(BTOapplication application) {
        this.applicationList.add(application);
    }
    
    /**
     * Getter method for getting Officer registrations by the officer ID
     * 
     * @param officerID to check against
     * @return registration by the officer
     */
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
    
    /**
     * Getter method for getting application by user ID within this project
     * 
     * @param userId to be checked against
     * @return application created by user
     */
    public BTOapplication getApplicationByUserId(String userId) {
        for (BTOapplication app : applicationList) {
            if (app.getUserID().equals(userId)) {
                return app;
            }
        }
        return null;
    }
    
    /** 
     * Static method to find an application by user ID across all projects
     * 
     * @param userId to be checked against
     * @return application created by user
     */
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
    
    /**
     * Displays the list of officers for each project
     */
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
    
    /**
     * Displays the list of officer applications for each project
     */
    public void displayApplicantList() {
        System.out.println("Displaying Officer Applicant List...");
        for (OfficerRegistration reg : officerApplicantList) {
            System.out.println("Applicant: " + reg.getOfficer().getName() + 
                               " (NRIC: " + reg.getOfficer().getNRIC() + 
                               ") - Status: " + reg.getRegistrationStatus());
        }
    }
    
    /**
     * Displays the list of applications for each project
     */
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
    
    /**
     * Displays all projects
     * Sorts the list of projects based on the field
     * Prints all details of the different projects
     * 
     * @param field to be checked against
     */
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

    /**
     * Displays all applicants in the projects
     * Checks applicant's marital status and display accordingly
     * Sorts list of applicants based on the field provided
     * 
     * @param field to be checked against
     * @param applicant applicant user
     */
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
    
    /**
     * Display all project details
     * Displays officers assigned to handle the project, shows error message if no officers handling the project
     * Displays number of Officer applications
     * Displays number of Applicant applications
     * 
     * @param projectIdToFind project ID to look for
     */
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

    /**
     * Method to display all BTO applications with specific status
     * Checks if status match, and displays applications that match
     * Displays error message if no such applications exist
     * 
     * @param status of application
     */
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
    
    /**
     * Getter method to get list of all applications created
     * 
     * @return list of all applications made
     */
    public static List<BTOapplication> getAllApplications() {
        List<BTOapplication> allApps = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (projects[i] != null) {
                allApps.addAll(projects[i].getApplicationList());
            }
        }
        return allApps;
    }
        
    /**
     * Removes project by passing in project ID
     * Checks project ID to list of projects
     * Update the count of projects available
     * 
     * @param projectId of project
     * @return true or false
     */
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
    
    /**
     * Getter method for getting project from list of projects
     * Checks if project ID matches from project list
     * 
     * @param projectId of project
     * @return project or null
     */
    public static Project getProjectById(int projectId) {
        for (int i = 0; i < getCount(); i++) {
            if (projects[i].projectId == projectId) {
                return projects[i];
            }
        }
        return null;
    }
    
    /**
     * Adds officer registration to list of registrations
     * 
     * @param registration of officer
     */
    public void addOfficerRegistration(OfficerRegistration registration) {
        officerApplicantList.add(registration);
    }
    
    /**
     * Adds officer to list of officers handling the project
     * Provided Officer is not already in the list
     * 
     * @param officer to be added
     */
    public void addOfficer(HDBofficer officer) {
        if (!officerList.contains(officer)) {
            officerList.add(officer);
        }
    }
    
    /**
     * Getter method for getting project ID
     * 
     * @return project ID
     */
    public int getProjectId() {
        return projectId;
    }
    
    /**
     * Setter method for setting project ID
     * 
     * @param projectId of project
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    
    /**
     * Getter method for getting project name
     * 
     * @return project name
     */
    public String getProjectName() {
        return projectName;
    }
    
    /**
     * Setter method for setting project name
     * 
     * @param projectName or project
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    /**
     * Getter method of getting neighborhood of the project
     * 
     * @return neighborhood
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }
    
    /**
     * Setter method for setting neighborhood of project
     * 
     * @param neighbourhood of project
     */
    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }
    
    /**
     * Getter method for getting two room count
     * 
     * @return two room count
     */
    public int getTwoRoomCount() {
        return twoRoomCount;
    }
    
    /**
     * Setter method for setting count for two room flats
     * 
     * @param twoRoomCount of two rooms
     */
    public void setTwoRoomCount(int twoRoomCount) {
        this.twoRoomCount = twoRoomCount;
    }
    
    /**
     * Getter method for getting three room count
     * 
     * @return three room count
     */
    public int getThreeRoomCount() {
        return threeRoomCount;
    }
    
    /**
     * Setter method for setting count for three room flats
     * 
     * @param threeRoomCount of three rooms
     */
    public void setThreeRoomCount(int threeRoomCount) {
        this.threeRoomCount = threeRoomCount;
    }
    
    /**
     * Getter method for getting project visibility
     * 
     * @return current project's visibility
     */
    public boolean isProjectVisibility() {
        return projectVisibility;
    }
    
    /**
     * Setter method to set visibility of the project
     * 
     * @param projectVisibility to be set to
     */
    public void setProjectVisibility(boolean projectVisibility) {
        this.projectVisibility = projectVisibility;
    }
    
    /**
     * Getter method for getting manager in charge of the project
     * 
     * @return manager in charge of the project
     */
    public HDBmanager getManagerInCharge() {
        return managerInCharge;
    }

    /**
     * Getter method for getting list of officer registrations
     * 
     * @return list of officer registrations
     */
    public List<OfficerRegistration> getOfficerRegistrations() {
        return officerApplicantList;
    }
    
    /**
     * Getter method for getting officer list of project
     * 
     * @return list of officers
     */
    public List<HDBofficer> getOfficers() {
        return officerList;
    }
    
    /**
     * Getter method for getting opening date of project
     * 
     * @return opening date
     */
    public LocalDate getOpeningDate() {
        return openDate;
    }
    
    /**
     * Setter method for setting opening date of project
     * 
     * @param openDate of project
     */
    public void setOpeningDate(LocalDate openDate) {
        this.openDate = openDate;
    }
    
    /**
     * Getter method for getting closing date of project
     * 
     * @return closing date
     */
    public LocalDate getClosingDate() {
        return closeDate;
    }
    
    /**
     * Setter method for setting closing data of project
     * 
     * @param closeDate of project
     */
    public void setClosingDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }
    
    /**
     * Getter method for getting number of officer slots
     * 
     * @return number of officer slots
     */
    public int getOfficerSlots() {
        return officerSlots;
    }
    	
    /**
     * Setter method for setting amount of officer slots
     * 
     * @param officerSlots available slots
     */
    public void setOfficerSlots(int officerSlots) {
        this.officerSlots = officerSlots;
    }
    
    /**
     * Getter method for getting projects
     * 
     * @return projects of project
     */
    public static Project[] getProjects() {
    	return projects;
    }

    /**
     * Getter method for getting project count 
     * 
     * @return count of project
     */
	public static int getCount() {
		return count;
	}

	/**
	 * Setter method for setting project count
	 * 
	 * @param count project count
	 * @return count
	 */
	public static int setCount(int count) {
		Project.count = count;
		return count;
	}
	
	/**
	 * Getter method for getting two room px
	 * 
	 * @return two room px
	 */
	public int getTwoRoompx() {
	    return twoRoompx;
	}
	
	/**
	 * Setter method for setting two room px
	 * 
	 * @param twoRoompx of project
	 */
	public void setTwoRoompx(int twoRoompx) {
	    this.twoRoompx = twoRoompx;
	}
	
	/**
	 * Getter method for getting three room px
	 * 
	 * @return three room px
	 */
	public int getThreeRoompx() {
	    return threeRoompx;
	}
	
	/**
	 * Setter method for setting three room px
	 * 
	 * @param threeRoompx of project
	 */
	public void setThreeRoompx(int threeRoompx) {
	    this.threeRoompx = threeRoompx;
	}

}

