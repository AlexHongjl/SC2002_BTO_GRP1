package test;

import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainTest {
    public static void main(String[] args) {
    	//UserPerson[] Users = new UserPerson[20]; // fixed array not suited for enhanced for loop, if use fixed need size or skip in for loop
        ArrayList<UserPerson> Users = new ArrayList<>();
        
        //Applicant a= new Applicant("John","S1234567A",35,"Single","password");
        //Users[0]=a;
        
        Users.add(new Applicant("John", "S1234567A", 35, "Single", "password"));
        
        Project p = new Project("Acacia Breeze", "Yishun", 2, 3, true, LocalDate.of(2025, 2, 15), LocalDate.of(2025, 3, 20), 2);
        //add an invalid user to try? eg usertype field is blank
        
        
        
        //login menu
        int logout=0;
        do {
            Scanner sc = new Scanner(System.in);
            UserPerson cur = login(Users, sc);

            System.out.println("\nWelcome <" + cur.getUserType() + "> " + cur.getName() + "!\n");
        
        //first interface 1 menu for each user type
        System.out.println("\nWelcome" + " <" + cur.getUserType() + "> " + cur.getName() +"!\n");
        
        /*if(curr.getUserType() == "applicant" && curr instanceof Applicant){
            Applicant cur = (Applicant) curr;
        }*/
        
        //start of v2 test
        int selection=0;
        if(cur.getUserType() == "applicant" && cur instanceof Applicant){// for applicant
            Applicant a = (Applicant) cur;
        	do {
	        	System.out.println("List of actions:");
	        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)"); 
	        	System.out.println("2. Apply for BTO project");
	        	System.out.println("3. Withdraw BTO project");
	        	System.out.println("4. View enquries");
	        	System.out.println("5. Add enquries");
	        	System.out.println("6. Edit enquries");
	        	System.out.println("7. Delete enquries");
	        	System.out.println("8. Logout");
	        	System.out.println("9. change PW");
	        	System.out.println("10. filer by area/flat type availability/px/date");
	        	System.out.println("11. End program");
	        	System.out.println("Pls input seletion: (e.g. 5)");
	        	selection = sc.nextInt();
	        	switch (selection) {
	            case 1://display the project they applied for only if they applied
	                System.out.println("Case 1 selected.");
	                if(a.getApplicationStatus()!= "None" && a.getApplicationStatus()!= "Unsuccessful") {// maybe we no need withdrawn and unsuccessful state?
	                	Project.display(a.getAppliedProjectId());//add a display method in project to display a single project
	                }else if(a.getApplicationStatus()== "None" ||a.getApplicationStatus()== "Unsuccessful") {
	                	Project.displayAllProjectsApplicant(null);// need check visibility rmb sm can change visibility add a display app method 
	                }
	                break;
	            case 2:
	            	int projectID, rtsel;
	            	String rt="";
	                System.out.println("Case 2 selected.");
	                System.out.println("enter project ID of desired project: (e.g. 1)");
	                projectID=sc.nextInt();
	                if(Project.getProjectById(projectID)==null){
	                    System.out.println("invalid ID");
	                }
	                else{
	                do {
	                System.out.println("1. 2-Room flat remaining:" + Project.getProjectById(projectID).getTwoRoomCount());// can add dynamic to check that project has what first then display menu
	                System.out.println("2. 3-Room flat remaining:" + Project.getProjectById(projectID).getThreeRoomCount());
	                System.out.println("enter disired room type: (e.g. 1)");
	                rtsel=sc.nextInt();
	                switch (rtsel) {
		                case 1:
		                    rt="2-Room";
		                    break;
		                case 2:
		                    rt="3-Room";
		                    break;
		                default:
		                	System.out.println("Invalid choice. Try again");
	                }
	                }while(rtsel!=1 && rtsel!=2);
	                
	                if(a.isEligible(Project.getProjectById(projectID), rt)){
	                	if(a.applyForProject(Project.getProjectById(projectID),rt)) {//need actually send to officer
	                	    BTOapplication app= new BTOapplication(a.getNRIC(), projectID);
	                	    app.updateStatus("Pending Approval", rt);
	                	    BTOapplication.addApplication(app);
	                		System.out.println("Successfully applied!, sent to manager for approval");
	                		app.display();
	                	}else {
	                		System.out.println("Unsuccessful!, likely having ongoing application!");
	                	}
	                }else {
	                	System.out.println("not eligible!");
	                }
	                }//if approved the room count need decrease
	                break;
	            case 3://small issue can repeatedly do this and always successful
	                System.out.println("Case 3 selected.");
	                if(a.getApplicationStatus()!= "None" && a.getApplicationStatus()!= "Unsuccessful") {// not sure need add unsuccessful
	                	a.withdrawApplication();//need to send to officer 
	                	BTOapplication.getApplicationByUserId(a.getNRIC()).updateStatus("Pending Withdrawal", BTOapplication.getApplicationByUserId(a.getNRIC()).getUnitType());
	                	    BTOapplication.getApplicationByUserId(a.getNRIC()).display();
	                	System.out.println("Successfully withdrawn!, sent to manager for approval");
	                }else if(a.getApplicationStatus()== "None" ||a.getApplicationStatus()== "Unsuccessful"){
	                	System.out.println("Unsuccessful!, no current ongoing application");
	                }
	                break;
	            case 4:
	                System.out.println("Case 4 selected.");//delete list of enquries attribute under applicant
	                Enquiry.displayAllEnquiries();
	                break;
	            case 5://cannot input likely keyboard buffer
	                System.out.println("Case 5 selected.");
	                sc.nextLine();//clear buffer
	                System.out.println("Input message:");
	                Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), cur.getName(), sc.nextLine()));// output success msg alr
	                break;
	            case 6:
	            	int enquiryID;
	                System.out.println("Case 6 selected.");
	                //optimise by creating a new array to store the user exclusive enquiries
	                if(Enquiry.displayByUser(a.getName())) {
	                	System.out.println("input enquiry ID to select enquiry to edit:");
	                	enquiryID=sc.nextInt();
	                	if(Enquiry.getByID(enquiryID)==null) {
	                		System.out.println("invalid ID");
	                	}else if(Enquiry.getByID(enquiryID).getApplicantName()!=a.getName()) {
	                		System.out.println("invalid access, not under your name");
	                	}else {//both got an enquiry and under user's name
	                		System.out.println("current enqury:");
	                		Enquiry.getByID(enquiryID).display();
	                		System.out.println("input new msg:");
	                		sc.nextLine();//clear buffer
	                		Enquiry.editEnquiry(enquiryID, sc.nextLine());//success msg included
	                	}
	                }
	                break;
	            case 7:
	            	int enquiryID1;
	                System.out.println("Case 7 selected.");
	                if(Enquiry.displayByUser(a.getName())) {
	                	System.out.println("input enquiry ID to select enquiry to delete:");
	                	enquiryID1=sc.nextInt();
	                	if(Enquiry.getByID(enquiryID1)==null) {
	                		System.out.println("invalid ID");
	                	}else if(Enquiry.getByID(enquiryID1).getApplicantName()!=a.getName()) {
	                		System.out.println("invalid access, not under your name");
	                	}else {//both got an enquiry and under user's name
	                		System.out.println("current enqury:");
	                		Enquiry.getByID(enquiryID1).display();
	                		Enquiry.deleteEnquiry(enquiryID1);//success msg included
	                	}
	                }
	                break;
	            case 8:
	                System.out.println("Case 8 selected.");
	                System.out.println("logging out");
	                break;
	            case 9:
	            	String pw;
	                System.out.println("Case 9 selected.");
	                sc.nextLine();//clear buffer
	                System.out.println("input new pw:");
	                pw=sc.nextLine();
	                System.out.println("confirm new pw:(input same pw agn)");
	                //sc.nextLine();//clear buffer
	                if(pw.equals(sc.nextLine())) {
	                	cur.changePassword(pw);
	                	System.out.println("PW successfully changed");
	                }else {
	                	System.out.println("Unsuccessful change, re-enter pw not same");
	                }
	                break;
	            case 10:
	                int ftsel;
	                String ft="";
	                do{
	                
	                System.out.println("Case 10 selected.");
	                System.out.println("1. filter by projectid");
	                System.out.println("2. filter by projectname");
	                System.out.println("3. filter by neighborhood");
	                System.out.println("4. filter by tworoomcount");
	                System.out.println("5. filter by threeroomcount");
	                System.out.println("6. filter by projectvisibility");
	                System.out.println("7. filter by openingdate");
	                System.out.println("8. filter by closingdate");
	                System.out.println("9. filter by officerslots");
	                System.out.println("10. exit to prv menu");
	                System.out.println("input selection:");
	                ftsel=sc.nextInt();
	                switch (ftsel) {
		                case 1:
		                    ft="projectid";
		                    break;
		                case 2:
		                    ft="projectname";
		                    break;
		                case 3:
		                    ft="neighborhood";
		                    break;
		                case 4:
		                    ft="tworoomcount";
		                    break;
		                case 5:
		                    ft="threeroomcount";
		                    break;
		                  case 6:
		                    ft="projectvisibility";
		                    break;
		                  case 7:
		                    ft="openingdate";
		                    break;
		                  case 8:
		                    ft="closingdate";
		                    break;
		                  case 9:
		                    ft="officerslots";
		                    break;
		                  case 10:
		                      break;
		                default:
		                	System.out.println("Invalid choice. Try again");
	                }
	                }while(ftsel>10 || ftsel<1);
	                if(ft.equals("")){
	                    
	                }else{
	                    Project.displayAllProjects(ft);
	                }
	                break;
	            case 11:
	                System.out.println("Case 11 selected.");
	                sc.close();
	                System.out.println("Ending...");
	                break;
	            default:
	                System.out.println("Invalid choice. Try again");
	        	}
        	}while(selection<12 && selection>0 && selection!=8 && selection!=11);
        	//start of v3
        	
        }
         if(selection == 8) {
        	logout=1;
        }
    }while(logout == 1);
        }
    }
    
public static UserPerson login(ArrayList<UserPerson> users, Scanner sc) {
        UserPerson cur = null;
        while (cur == null) {
            System.out.println("\n--- BTO System Login ---");
            System.out.print("Enter NRIC: ");
            String username = sc.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            for (UserPerson user : users) {
                if (user.getNRIC().equals(username) && user.getPassword().equals(password)) {
                    cur = user;
                    break;
                }
            }

            if (cur == null) {
                System.out.println("Invalid username or password. Please try again.\n");
            }
        }
        return cur;
    }
    
class UserPerson {
	private String name;
	private String NRIC;
	private int age;
	private String maritalStatus;
	private String password;
	private String userType;
	
	public UserPerson(String name, String NRIC, int age, String maritalStatus, String password, String userType) {
		this.name = name;
		this.NRIC = NRIC;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.password = password;
		this.userType = userType;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNRIC() {
		return this.NRIC;
	}
	//v2 added some method below
	public int getAge() {
		return this.age;
	}
	
	public String getMaritalStatus() {
		return this.maritalStatus;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getUserType() {
		return this.userType;
	}
	
	public void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
}

class Applicant extends UserPerson {

    private int appliedProjectId = -1;
    private String applicationStatus = "None"; // None, Pending, Successful, Booked, Unsuccessful
    private String flatTypeBooked = null;
    private boolean hasWithdrawn = false;

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password, "applicant");
    }
    
    //v2 added all method below
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
    
    public Project getAppliedProject() {
        if (appliedProjectId == -1) return null;
        return Project.getProjectById(appliedProjectId);
    }
}

//newly added whole Project class
class Project {
    private int projectId;
    private String projectName;
    private String neighbourhood;
    private int twoRoomCount;
    private int threeRoomCount;
    private boolean projectVisibility;
    //private HDBmanager managerInCharge;
    private int officerSlots;
    private LocalDate openDate;
    private LocalDate closeDate;
    //private List<HDBofficer> officerList;
    //private List<OfficerRegistration> officerApplicantList;
    
    private static Project[] projects = new Project[100];
    private static int count = 0;
    
    

    public Project(String projectName, String neighbourhood,
            int twoRoomCount, int threeRoomCount,
            boolean projectVisibility, LocalDate openDate,
            LocalDate closeDate, int officerSlots) {//deleted parameter

    	this.projectId = setCount(getCount() + 1); // Unique project ID
    	this.projectName = projectName;
    	this.neighbourhood = neighbourhood;
    	this.twoRoomCount = twoRoomCount;
    	this.threeRoomCount = threeRoomCount;
    	this.projectVisibility = projectVisibility;
    	this.openDate = openDate;
    	this.closeDate = closeDate;
    	this.officerSlots = officerSlots;
    	//this.managerInCharge = managerInCharge;
    	//this.officerList = new ArrayList<>();
    	//this.officerApplicantList = new ArrayList<>();

    	if (getCount() <= projects.length) {
    		projects[getCount() - 1] = this; // Add to project list
    	} else {
    		System.out.println("Error: Maximum number of projects reached.");
    	}
    }


    /*public void displayOfficerList() {
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
            //System.out.println("Manager In Charge: " + (project.getManagerInCharge() != null ? project.getManagerInCharge().getName() : "None"));
            //System.out.println("Number of Officers Assigned: " + project.getOfficers().size());
            //System.out.println("Number of Officer Applications: " + project.getOfficerRegistrations().size());
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
                //System.out.println("Manager In Charge: " + project.managerInCharge);
                System.out.println("Officer Slots: " + project.officerSlots);
                System.out.println("Open Date: " + (project.openDate != null ? project.openDate : "Not set"));
                System.out.println("Close Date: " + (project.closeDate != null ? project.closeDate : "Not set"));
                //System.out.println("Number of Officers: " + project.officerList.size());
                //System.out.println("Number of Officer Applications: " + project.officerApplicantList.size());
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
    

    public static boolean removeProjectById(int projectId) {// not used in v2
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

    public static void displayAllProjects() {//have not use yet
        for (int i = 0; i < getCount(); i++) {
            System.out.println("Project ID: " + projects[i].projectId + ", Name: " + projects[i].projectName);
        }
    }
    
    //deleted officer related func
    
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
    
    //deleted some func

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

//new added for v2
class Enquiry {
    private int enquiryID;
    private String applicantName;
    private String message;
    private String response;
    private boolean status;
    
    
    private static Enquiry[] Enquiries = new Enquiry[100];
    private static int count = 0;

    public Enquiry(int enquiryID, String applicantName, String message) {
        this.enquiryID = enquiryID;
        this.applicantName = applicantName;
        this.message = message;
        this.response = "";
        this.status = true;
        //this.projectId = projectId;
    }
    
    public void display() {
        System.out.println("Enquiry ID: " + enquiryID);
        System.out.println("Applicant Name: " + applicantName);
        System.out.println("Message: " + message);
        System.out.println("Response: " + (response.isEmpty() ? "No response yet." : response));
        System.out.println("Status: " + (status ? "Open" : "Closed"));
        //System.out.println("Project ID: " + projectId);
        System.out.println("-------------------------------");
    }
    
    public static boolean displayByUser(String applicantName) {
        boolean found = false;

        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getApplicantName().equals(applicantName)) {
                Enquiries[i].display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No enquiries found for user: " + applicantName);
        }

        return found;
    }
    
    public static Enquiry getByID(int enquiryID) {
        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getEnquiryID() == enquiryID) {
                return Enquiries[i];
            }
        }
        return null; // not found
    }

    // Display all enquiries
    public static void displayAllEnquiries() {
        if (count == 0) {
            System.out.println("No enquiries found.");
            return;
        }

        for (int i = 0; i < count; i++) {
            Enquiries[i].display();
        }
    }

    // Add enquiry
    public static void addEnquiry(Enquiry e) {
        if (count >= Enquiries.length) {
            System.out.println("Enquiry list full.");
            return;
        }
        Enquiries[count] = e;
        count++;
        System.out.println("Enquiry added successfully.");
    }

    // Edit enquiry
    public static void editEnquiry(int enquiryID, String newMessage) {
        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getEnquiryID() == enquiryID) {
                Enquiries[i].setMessage(newMessage);
                System.out.println("Enquiry " + enquiryID + " updated.");
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }

    // Delete enquiry
    public static void deleteEnquiry(int enquiryID) {
        for (int i = 0; i < count; i++) {
            if (Enquiries[i].getEnquiryID() == enquiryID) {
                for (int j = i; j < count - 1; j++) {
                    Enquiries[j] = Enquiries[j + 1];
                }
                Enquiries[count - 1] = null;
                count--;
                System.out.println("Enquiry " + enquiryID + " deleted.");
                return;
            }
        }
        System.out.println("Enquiry not found.");
    }
    
    
    public void reply(String replyMessage) {//nvr use yet at v2
        this.response = replyMessage;
        System.out.println("Reply Sent: " + replyMessage);
    }

    public void changeStatus() {//nvr use yet at v2
        this.status = !this.status;
        System.out.println("Enquiry Status Changed: " + status);
    }
    
    
    //------------------------------------------------------------------separate get set functions
    public int getEnquiryID() {
        return enquiryID;
    }

    public void setEnquiryID(int enquiryID) {
        this.enquiryID = enquiryID;
    }
    
    public String getApplicantName() {
        return applicantName;
    }
    
    /*public int getProjectId() {
        return projectId;
    }*/

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static int getCount() {
        return count;
    }
}

class BTOapplication {
    private static ArrayList<BTOapplication> allApplications = new ArrayList<>();
    
    private String userID;
    private int projectId;
    private String unitType; // "2-Room" or "3-Room"
    private String status;   // Pending approval, pending withdrawal, Successful, Unsuccessful, Booked
    private String timestamp; // last status update

    public BTOapplication(String userID, int projectId) {
        this.userID = userID;
        this.projectId = projectId;
        this.status = "Pending approval";
        this.unitType = "";
        this.timestamp = "";
    }
    
    public static void addApplication(BTOapplication app) {
        allApplications.add(app);
    }
    
        public static void displayAll() {
        System.out.println("=== All BTO Applications ===");
        for (BTOapplication app : allApplications) {
            System.out.println(app);
        }
    }

    // Display applications with status "Pending"
    public static void displayStatusPendingW() {
        System.out.println("=== Pending Applications ===");
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Pending Withdrawn")) {
                System.out.println(app);
            }
        }
    }
    
    public static void displayStatusPendingA() {
        System.out.println("=== Pending Applications ===");
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Pending Approval")) {
                System.out.println(app);
            }
        }
    }

    // Display applications with status "Successful"
    public static void displayStatusSuccessful() {
        System.out.println("=== Successful Applications ===");
        for (BTOapplication app : allApplications) {
            if (app.status.equals("Successful")) {
                System.out.println(app);
            }
        }
    }

    // To nicely print the application
    public String toString() {
        return "UserID: " + userID + ", ProjectID: " + projectId + ", UnitType: " + unitType +
               ", Status: " + status + ", Last Updated: " + timestamp;
    }
    
    public static BTOapplication getApplicationByUserId(String userId) {
    for (BTOapplication app : allApplications) {
        if (app.getUserID().equals(userId)) {
            return app;
        }
    }
    return null; // If not found
    }

    public void updateStatus(String status, String unitType) {
        this.status = status;
        this.unitType = unitType;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
    
    public void display() {
    System.out.println(this);
    }

    public String getStatus() {
        return status;
    }

    public String getUserID() {
        return userID;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getUnitType() {
        return unitType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isBookable() {
        return status.equals("Successful");
    }

    public boolean isBooked() {
        return status.equals("Booked");
    }
    
    public boolean approveApplication() {
    	return status.equals("Approved");
    }
    
    public boolean deniedApplication() {
    	return status.equals("Unsuccessful");
    }
}//v3 to implement logout/relogin, filter, read in file, create and send bto app, add end program
//btoapp: change bto app to have a whole list in attribute and list management functions