//package bto;
//
//import java.util.Scanner;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import bto.model.Applicant;
//import bto.model.BTOapplication;
//import bto.model.Enquiry;
//import bto.model.HDBmanager;
//import bto.model.HDBofficer;
//import bto.model.OfficerRegistration;
//import bto.model.Project;
//import bto.model.UserPerson;
//import bto.service.LoginService;
//
//public class Main {
//    public static void main(String[] args) {
//    	//UserPerson[] Users = new UserPerson[20]; // creating a array to store all users
//    	
//    	ArrayList<UserPerson> Users = new ArrayList<>();
//        
//    	UserPerson.LoadUsers(Users);
//    	OfficerRegistration.loadRegistrationsFromCSV("data/Registrations.csv", Users);
//    	Project.loadProjectsFromCSV(Users);
//    	BTOapplication.loadApplicationsFromCSV("data/Applications.csv", Users);
//    	Enquiry.loadEnquiriesFromCSV("data/Enquiries.csv");
//    	
//    	/*for (UserPerson u : Users) {
//    	    System.out.println("Found: " + u.getName() + " is instance of " + u.getClass().getSimpleName());
//    	}*/
//
//    	Project.loadProjectsFromCSV(Users);
//        
//    	//Project.display(1);
//        //load in project
//        
//        //login menu
//    	int logout = 0;
//    	do {
//    	    Scanner sc = new Scanner(System.in); // only if not declared earlier
//    	    UserPerson cur = LoginService.login(Users, sc);
//        
//        //first interface 1 menu for each user type
//        System.out.println("Welcome" + " <" + cur.getUserType() + "> " + cur.getName() +"!\n");
//        
//        
//        int selection=0;
//		String saved_filter=null;
//        if(cur.getUserType() == "manager" && cur instanceof HDBmanager) { //if possible change to check the obj itself
//        	HDBmanager m = (HDBmanager) cur;//downcast
//        	do {
//	        	System.out.println("1. View All BTO projects listing"); 
//	        	System.out.println("2. Toggle visibility");
//	        	System.out.println("3. approve BTO application/withdrawal by applicant");//both withdraw and apply
//	        	System.out.println("4. approve officer application by officer");// put view function within
//	        	System.out.println("5. Add BTO listing");
//	        	System.out.println("6. Edit BTO listing");
//	        	System.out.println("7. Delete BTO listing");
//	        	System.out.println("8. Logout");
//	        	System.out.println("9. change PW");
//	        	System.out.println("10. filter by area/flat type availability/px/date");
//	        	System.out.println("11. End program");
//	        	System.out.println("12. reply enquries");
//	        	System.out.println("13. generate report");
//	        	System.out.println("14. View own profile");
//	        	System.out.println("Pls input seletion: (e.g. 5)");
//	        	
//	        	selection = sc.nextInt();
//	        	switch (selection) {
//	        	    case 1:
//	        	        System.out.println("Case 1 selected.");
//	        	        Project.displayAllProjects(saved_filter);
//	        	        break;
//	        	    case 2:
//	        	    	int projectID2;
//	        	    	boolean a;
//	        	        System.out.println("Case 2 selected.");
//	        	        System.out.println("input project ID for the proje"
//	        	        		+ "ct you want to toggle visibility on");
//	        	        projectID2=sc.nextInt();
//	        	        System.out.println("on/off e.g. =ve int for on, 0 for off");
//	        	        a=sc.nextInt()==1?true:false;
//	        	        m.toggleProjectVisibility(projectID2, a);
//	        	        break;
//	        	    case 3:
//	        	        System.out.println("Case 3 selected.");
//	        	        int projectID1;
//	        	    	String appNRIC;
//	        	    	BTOapplication.displayStatusPendingA();
//	        	    	BTOapplication.displayStatusPendingW();
//	        	        System.out.println("input project ID:");
//	        	        projectID1=sc.nextInt();
//	        	        sc.nextLine();
//	        	        System.out.println("input applicant NRIC to approve/reject:");
//	        	        appNRIC=sc.nextLine();
//	        	        BTOapplication app = BTOapplication.getApplicationByUserId(appNRIC);
//	        	        Project targetProject = Project.getProjectById(projectID1);
//	        	        //catch exception
//	        	        if (targetProject == null) {
//	        	            System.out.println("Project ID not found. Returning to menu.");
//	        	            break;
//	        	        }
//	        	        if (app == null) {
//	        	            System.out.println("No valid application found for this applicant. Returning to menu.");
//	        	            break;
//	        	        }
//
//	        	        app.display();
//	        	        System.out.println("approve/reject " + app.getStatus() +" e.g. +ve int for approve,0 for reject");
//	        	        boolean c = (sc.nextInt()==1);
//	        	        if(app.getStatus().trim().equalsIgnoreCase("Pending Approval")) {
//	        	        	if(c) m.approveBTOApplication(projectID1, appNRIC);
//	        	        	else m.rejectBTOApplication(projectID1, appNRIC);
//	        	        } else if(app.getStatus().trim().equalsIgnoreCase("Pending Withdrawal")) {
//	        	        	if(c) m.approveWithdrawalRequest(projectID1, appNRIC);
//	        	        	else m.rejectWithdrawalRequest(projectID1, appNRIC);
//	        	        } else {
//	        	        	System.out.println("Invalid application state.");
//	        	        }
//	        	        break;
//	        	    case 4:
//	        	    	int projectID;
//	        	    	String officerNRIC;
//	        	        System.out.println("Case 4 selected.");
//	        	        OfficerRegistration.displayAll("Pending approval");
//	        	        //display list of officer reg under his list of project
//	        	        System.out.println("input project ID:");
//	        	        projectID=sc.nextInt();
//	        	        sc.nextLine();
//	        	        System.out.println("input officer NRIC to approve/reject:");
//	        	        officerNRIC=sc.nextLine();
//	        	        OfficerRegistration offreg;
//	        	        //find the offreg obj first
//						offreg=Project.getOfficerRegistrationByID(officerNRIC);
//						offreg.display();
//	        	        //sc.nextLine();
//	        	        //display the offreg obj especially cur status if approve or reject
//	        	        System.out.println("approve/reject " + offreg.getRegistrationStatus() +" e.g. 1 for approve,0 for reject");
//	        	        boolean b=sc.nextInt()==1?true:false;
//	        	        if(b) {
//	        	        	m.approveOfficerRegistration(projectID, officerNRIC);
//	        	        }else {
//	        	        	m.rejectOfficerRegistration(projectID, officerNRIC);
//	        	        }
//	        	        break;
//	        	    case 5:
//	        	    	System.out.println("Case 5 selected.");
//	        	    	// Consume line
//	        	    	sc.nextLine(); 
//	        	    	// Collect inputs from user
//	        	    	System.out.print("Enter project name: ");
//	        	    	String projectName = sc.nextLine();
//
//	        	    	System.out.print("Enter neighbourhood: ");
//	        	    	String neighbourhood = sc.nextLine();
//
//	        	    	System.out.print("Enter two-room count: ");
//	        	    	int twoRoomCount = sc.nextInt();
//
//	        	    	System.out.print("Enter selling price for two-room: ");
//	        	    	int twoRoompx = sc.nextInt();
//
//	        	    	System.out.print("Enter three-room count: ");
//	        	    	int threeRoomCount = sc.nextInt();
//
//	        	    	System.out.print("Enter selling price for three-room: ");
//	        	    	int threeRoompx = sc.nextInt();
//
//	        	    	System.out.print("Enter project visibility (1 for true, 0 for false): ");
//	        	    	boolean projectVisibility = (sc.nextInt() == 1);
//
//	        	    	sc.nextLine(); // consume the leftover newline
//	        	    	System.out.print("Enter opening date (yyyy-MM-dd): ");
//	        	    	String openDateString = sc.nextLine();
//	        	    	LocalDate openDate = LocalDate.parse(openDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//	        	    	System.out.print("Enter closing date (yyyy-MM-dd): ");
//	        	    	String closeDateString = sc.nextLine();
//	        	    	LocalDate closeDate = LocalDate.parse(closeDateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
//	        	    	System.out.print("Enter officer slots (sequence separated by space): ");
//	        	    	String officerSlotsInput = sc.nextLine();
//	        	    	String[] officerSlotsArray = officerSlotsInput.split(" ");
//	        	    	int officerSlots = officerSlotsArray.length;
//
//	        	    	// Call the updated createBTOListings method
//	        	    	m.createBTOListings(projectName, neighbourhood, 
//	        	    	                    twoRoomCount, twoRoompx, 
//	        	    	                    threeRoomCount, threeRoompx,
//	        	    	                    projectVisibility, openDate, closeDate, officerSlots);
//	        	    	break;
//	        	    case 6:
//	        	        System.out.println("Case 6 selected.");
//
//	        	        // Display formatted table of projects
//	        	        System.out.println("=========== All Projects ===========");
//	        	        System.out.println("ID | Name              | Neighbourhood     | Two-Room | Three-Room");
//	        	        System.out.println("---------------------------------------------------------------");
//
//	        	        for (Project p : Project.getProjects()) {
//	        	            if (p == null) continue;
//	        	            System.out.printf("%-2d | %-17s | %-18s | %-9d | %-11d%n",
//	        	                    p.getProjectId(),
//	        	                    p.getProjectName(),
//	        	                    p.getNeighbourhood(),
//	        	                    p.getTwoRoomCount(),
//	        	                    p.getThreeRoomCount());
//	        	        }
//	        	        System.out.println("===============================================================");
//
//	        	        // Prompt for edit input using comma format
//	        	        sc.nextLine(); // clear buffer
//	        	        System.out.println("Enter edit command in format: <ProjectID>,<Field>,<NewValue>");
//	        	        System.out.println("Valid fields: projectname, neighborhood, tworoomcount, threeroomcount, visiblity, openingdate, closingdate, officerslots");
//	        	        System.out.println("Example: 1,projectname,Taban Garden");
//
//	        	        try {
//	        	            String[] parts = sc.nextLine().split(",", 3);
//	        	            int pid = Integer.parseInt(parts[0].trim());
//	        	            String field = parts[1].trim();
//	        	            String value = parts[2].trim();
//
//	        	            Object newValue = null;
//	        	            boolean supported = true;
//
//	        	            switch (field.toLowerCase()) {
//	        	                case "projectname":
//	        	                case "neighborhood":
//	        	                    newValue = value;
//	        	                    break;
//	        	                case "tworoomcount":
//	        	                case "threeroomcount":
//	        	                case "officerslots":
//	        	                    newValue = Integer.parseInt(value);
//	        	                    break;
//	        	                case "visiblity":
//	        	                    newValue = Boolean.parseBoolean(value);
//	        	                    break;
//	        	                case "openingdate":
//	        	                case "closingdate":
//	        	                    newValue = LocalDate.parse(value);
//	        	                    break;
//	        	                default:
//	        	                    supported = false;
//	        	                    System.out.println("Unsupported field: " + field);
//	        	            }
//
//	        	            if (supported) {
//	        	                m.editBTOListings(pid, field, newValue);
//	        	            }
//	        	        } catch (Exception e) {
//	        	            System.out.println("Invalid input or format. Please use: <ID>,<field>,<newValue>");
//	        	        }
//
//	        	        break;
//	        	    case 7:
//	        	        System.out.println("Case 7 selected.");
//	        	        System.out.println("input proj ID:");
//	        	        int projectID3=sc.nextInt();
//	        	        m.deleteBTOListings(projectID3);
//	        	        break;
//	        	    case 8:
//	        	    	System.out.println("Case 8 selected.");
//		                System.out.println("logging out\n\n");
//		                break;
//	        	    case 9:
//	        	        String pw;
//		                System.out.println("Case 9 selected.");
//		                sc.nextLine();//clear buffer
//		                System.out.println("input new pw:");
//		                pw=sc.nextLine();
//		                System.out.println("confirm new pw:(input same pw agn)");
//		                if(pw.equals(sc.nextLine())) {
//		                	m.changePassword(pw);
//		                }else {
//		                	System.out.println("Unsuccessful change, re-enter pw not same");
//		                }
//	        	        break;
//	        	    case 10:
//	        	        System.out.println("Case 10 selected.");
//	        	        int ftsel;
//		                String ft="";
//		                do{
//		                System.out.println("1. filter by projectid");
//		                System.out.println("2. filter by projectname");
//		                System.out.println("3. filter by neighborhood");
//		                System.out.println("4. filter by tworoomcount");
//		                System.out.println("5. filter by threeroomcount");
//		                System.out.println("6. filter by projectvisibility");
//		                System.out.println("7. filter by openingdate");
//		                System.out.println("8. filter by closingdate");
//		                System.out.println("9. filter by officerslots");
//		                System.out.println("10. exit to prv menu");
//		                System.out.println("input selection(filter saved for current login):");
//		                ftsel=sc.nextInt();
//		                switch (ftsel) {
//			                case 1:
//			                    ft="projectid";
//			                    break;
//			                case 2:
//			                    ft="projectname";
//			                    break;
//			                case 3:
//			                    ft="neighborhood";
//			                    break;
//			                case 4:
//			                    ft="tworoomcount";
//			                    break;
//			                case 5:
//			                    ft="threeroomcount";
//			                    break;
//			                  case 6:
//			                    ft="projectvisibility";
//			                    break;
//			                  case 7:
//			                    ft="openingdate";
//			                    break;
//			                  case 8:
//			                    ft="closingdate";
//			                    break;
//			                  case 9:
//			                    ft="officerslots";
//			                    break;
//			                  case 10:
//			                      break;
//			                default:
//			                	System.out.println("Invalid choice. Try again");
//		                }
//		                }while(ftsel>10 || ftsel<1);
//		                if(ft.equals("")){
//		                    //pass
//		                }else{
//							saved_filter=ft;
//		                    Project.displayAllProjects(ft);
//		                }
//	        	        break;
//	        	    case 11:
//	        	        System.out.println("Case 11 selected.");
//		                sc.close();
//		                System.out.println("Ending...");
//	        	        break;
//	        	    case 12:
//	        	        System.out.println("Case 12 selected.");
//	        	        int enquiryID3;
//	        	        Enquiry.displayAllEnquiries();
//	        	        System.out.println("input enquiry ID to select enquiry to reply:");
//	                	enquiryID3=sc.nextInt();
//	                	if(Enquiry.getByID(enquiryID3)==null) {
//	                		System.out.println("invalid ID");
//	                	}else {
//	                		System.out.println("current enquiry:");
//	                		Enquiry.getByID(enquiryID3).display();
//	                		System.out.println("input reply:");
//	                		sc.nextLine();//clear buffer
//	                		Enquiry.getByID(enquiryID3).reply(sc.nextLine());
//	                	}
//	        	        break;
//	        	    case 13:
//						int projectID4;
//	        	        System.out.println("Case 13 selected.");
//						System.out.println("input project ID to generate report on all applicant:");
//						projectID4=sc.nextInt();
//						System.out.println("Filter by? Options:(married, single, 2-room, 3-room, all");
//						String fil = sc.nextLine();
//	        	        m.generateReport(projectID4, fil);
//	        	        break;
//	        	    case 14:
//	        	    	m.viewOwnStatus();
//	        	    	break;
//	        	    default:
//	        	        System.out.println("Invalid choice. Try again");
//	        	        break;
//	        	}
//        	}while(selection<15 && selection>0 && selection!=8 && selection!=11);
//        	
//        }else if(cur.getUserType() == "officer" && cur instanceof HDBofficer) {// no need the enquiries list attribute and applied projID attribute cause applicant class alr have
//        	HDBofficer o = (HDBofficer) cur;//downcast
//        	do {
//	        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)"); 
//	        	System.out.println("2. Apply for BTO project");//if not officer in charge, need change a bit, check he is using officer mode by attribute or project he is officer
//	        	System.out.println("3. Withdraw BTO project");
//	        	System.out.println("4. View enquries");//
//	        	System.out.println("5. Add enquries");
//	        	System.out.println("6. Edit enquries");
//	        	System.out.println("7. Delete enquries");
//	        	System.out.println("8. Logout");
//	        	System.out.println("9. change PW");
//	        	System.out.println("10. filter by area/flat type availability/px/date");
//	        	System.out.println("11. End program");
//	        	System.out.println("--------officer functions----------");
//	        	System.out.println("12. reply enquries");//doc mention about enquiry for a project
//	        	System.out.println("13. apply for officer of a project");
//	        	System.out.println("14. book slot for applicant on approved applications & generate receipt");//after approved then visible to officer?
//	        	System.out.println("15. View own profile");
//	        	System.out.println("Pls input seletion: (e.g. 5)");
//	        	
//	        	selection = sc.nextInt();
//	        	switch (selection) {
//		        	case 1:
//		                System.out.println("Case 1 selected.");
//		                if(o.getApplicationStatus()!= "None" && o.getApplicationStatus()!= "Unsuccessful") {
//		                	Project.display(o.getAppliedProjectId());
//		                }else if(o.getApplicationStatus()== "None" ||o.getApplicationStatus()== "Unsuccessful") {
//		                	Project.displayAllProjectsApplicant(saved_filter,o);//sld be same same applicant but display an extra one if he officer
//		                }
//		                break;
//		            case 2:
//		            	int projectID, rtsel;
//		            	String rt="";
//		                System.out.println("Case 2 selected.");
//		                System.out.println("enter project ID of desired project: (e.g. 1)");
//		                projectID=sc.nextInt();
//		                
//		                do {
//		                System.out.println("1. 2-Room flat remaining:" + Project.getProjectById(projectID).getTwoRoomCount());// can add dynamic to check that project has what first then display menu
//		                System.out.println("2. 3-Room flat remaining:" + Project.getProjectById(projectID).getThreeRoomCount());
//		                System.out.println("enter disired room type: (e.g. 1)");
//		                rtsel=sc.nextInt();
//		                switch (rtsel) {
//			                case 1:
//			                    rt="2-Room";
//			                    break;
//			                case 2:
//			                    rt="3-Room";
//			                    break;
//			                default:
//			                	System.out.println("Invalid choice. Try again");
//		                }
//		                }while(rtsel!=1 && rtsel!=2);
//		                
//		                if(o.isEligible(Project.getProjectById(projectID),rt)){
//		                	if(o.applyForProject(Project.getProjectById(projectID),rt)) {
//		                		BTOapplication app= new BTOapplication(o.getNRIC(), projectID,o);//upcast?
//		                	    app.updateStatus("Pending Approval", rt);
//		                	    BTOapplication.addApplication(app);
//		                		System.out.println("Successfully applied!, sent to manager for approval");
//		                		app.display();
//		                	}else {
//		                		System.out.println("Unsuccessful!, likely having ongoing application!");
//		                	}
//		                }else {
//		                	System.out.println("not eligible!");
//		                }
//		                break;
//		            case 3:
//		                System.out.println("Case 3 selected.");
//		                if(o.getApplicationStatus()!= "None" && o.getApplicationStatus()!= "Unsuccessful") {
//		                	o.withdrawApplication();
//		                	BTOapplication.getApplicationByUserId(o.getNRIC()).updateStatus("Pending Withdrawal", BTOapplication.getApplicationByUserId(o.getNRIC()).getUnitType());
//		                	BTOapplication.getApplicationByUserId(o.getNRIC()).display();
//		                	System.out.println("Successfully withdrawn!, sent to manager for approval");
//		                }else if(o.getApplicationStatus()== "None" ||o.getApplicationStatus()== "Unsuccessful"){
//		                	System.out.println("Unsuccessful!, no current ongoing application");
//		                }
//		                break;
//		            case 4:
//		                System.out.println("Case 4 selected.");
//		                Enquiry.displayAllEnquiries();
//		                break;
//		            case 5:
//		                System.out.println("Case 5 selected.");
//		                sc.nextLine();
//		                System.out.println("Input message:");
//		                Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), o.getName(), sc.nextLine()));
//		                break;
//		            case 6:
//		            	int enquiryID;
//		                System.out.println("Case 6 selected.");
//		                if(Enquiry.displayByUser(o.getName())) {
//		                	System.out.println("input enquiry ID to select enquiry to edit:");
//		                	enquiryID=sc.nextInt();
//		                	if(Enquiry.getByID(enquiryID)==null) {
//		                		System.out.println("invalid ID");
//		                	}else if(Enquiry.getByID(enquiryID).getApplicantName()!=o.getName()) {
//		                		System.out.println("invalid access, not under your name");
//		                	}else {
//		                		System.out.println("current enquiry:");
//		                		Enquiry.getByID(enquiryID).display();
//		                		System.out.println("input new msg:");
//		                		sc.nextLine();//clear buffer
//		                		Enquiry.editEnquiry(enquiryID, sc.nextLine());
//		                	}
//		                }
//		                break;
//		            case 7:
//		            	int enquiryID1;
//		                System.out.println("Case 7 selected.");
//		                if(Enquiry.displayByUser(o.getName())) {
//		                	System.out.println("input enquiry ID to select enquiry to delete:");
//		                	enquiryID1=sc.nextInt();
//		                	if(Enquiry.getByID(enquiryID1)==null) {
//		                		System.out.println("invalid ID");
//		                	}else if(Enquiry.getByID(enquiryID1).getApplicantName()!=o.getName()) {
//		                		System.out.println("invalid access, not under your name");
//		                	}else {
//		                		System.out.println("current enquiry:");
//		                		Enquiry.getByID(enquiryID1).display();
//		                		Enquiry.deleteEnquiry(enquiryID1);//success msg included
//		                	}
//		                }
//		                break;
//		            case 8:
//		            	System.out.println("Case 8 selected.");
//		                System.out.println("logging out\n\n");
//		                break;
//		            case 9:
//		            	String pw;
//		                System.out.println("Case 9 selected.");
//		                sc.nextLine();//clear buffer
//		                System.out.println("input new pw:");
//		                pw=sc.nextLine();
//		                System.out.println("confirm new pw:(input same pw agn)");
//		                if(pw.equals(sc.nextLine())) {
//		                	o.changePassword(pw);
//		                }else {
//		                	System.out.println("Unsuccessful change, re-enter pw not same");
//		                }
//		                break;//add filter by px
//		            case 10:
//						System.out.println("Case 10 selected.");
//		                int ftsel;
//		                String ft="";
//		                do{
//		                System.out.println("1. filter by projectid");
//		                System.out.println("2. filter by projectname");
//		                System.out.println("3. filter by neighborhood");
//		                System.out.println("4. filter by tworoomcount");
//		                System.out.println("5. filter by threeroomcount");
//		                System.out.println("7. filter by openingdate");
//		                System.out.println("8. filter by closingdate");
//		                System.out.println("9. filter by officerslots");
//		                System.out.println("10. exit to prv menu");
//		                System.out.println("input selection:");
//		                ftsel=sc.nextInt();
//		                switch (ftsel) {
//			                case 1:
//			                    ft="projectid";
//			                    break;
//			                case 2:
//			                    ft="projectname";
//			                    break;
//			                case 3:
//			                    ft="neighborhood";
//			                    break;
//			                case 4:
//			                    ft="tworoomcount";
//			                    break;
//			                case 5:
//			                    ft="threeroomcount";
//			                    break;
//			                  case 6:
//			                    break;
//			                  case 7:
//			                    ft="openingdate";
//			                    break;
//			                  case 8:
//			                    ft="closingdate";
//			                    break;
//			                  case 9:
//			                    ft="officerslots";
//			                    break;
//			                  case 10:
//			                      break;
//			                default:
//			                	System.out.println("Invalid choice. Try again");
//		                }
//		                }while(ftsel>10 || ftsel<1);
//		                if(ft.equals("")){
//		                    //pass
//		                }else{
//		                    Project.displayAllProjects(ft);
//		                }
//		                break;
//		            case 11:
//		                System.out.println("Case 11 selected.");
//		                sc.close();
//		                System.out.println("Ending...");
//		                break;
//	        	    case 12:
//	        	        System.out.println("Case 12 selected.");
//	        	        int enquiryID2;
//	        	        Enquiry.displayAllEnquiries();
//	        	        System.out.println("input enquiry ID to select enquiry to reply:");
//	                	enquiryID2=sc.nextInt();
//	                	if(Enquiry.getByID(enquiryID2)==null) {
//	                		System.out.println("invalid ID");
//	                	}else {
//	                		System.out.println("current enquiry:");
//	                		Enquiry.getByID(enquiryID2).display();
//	                		System.out.println("input reply:");
//	                		sc.nextLine();//clear buffer
//	                		Enquiry.getByID(enquiryID2).reply(sc.nextLine());
//	                	}
//	        	        break;
//	        	    case 13:
//						int projectID5;
//	        	        System.out.println("Case 13 selected.");
//						Project.displayAllProjects(saved_filter);//if not alr regged
//						System.out.println("enter project ID:");
//						projectID5=sc.nextInt();
//						OfficerRegistration reg = o.applyOffReg(Project.getProjectById(projectID5));
//						if (reg != null && reg.getRegistrationStatus().equals("Pending approval")) {
//						    System.out.println("Application submitted successfully. Status: Pending approval.");
//						}
//	        	        break;
//	        	    case 14:
//	        	        System.out.println("Case 14 selected.");
//
//	        	        // Display all successful applications for officer's projects
//	        	        ArrayList<BTOapplication> matchingApps = new ArrayList<>();
//						System.out.println("AVAILABLE BTO APPLICATIONS TO BOOK");
//	        	        for (BTOapplication a : BTOapplication.getAllApplications()) {
//	        	            if (a.getStatus().equalsIgnoreCase("successful")) {
//	        	                Project proj = Project.getProjectById(a.getProjectId());
//	        	                if (proj != null && proj.getOfficers().contains(o)) {
//	        	                    a.display();
//	        	                    matchingApps.add(a);
//	        	                }
//	        	            }
//	        	        }
//
//	        	        if (matchingApps.isEmpty()) {
//	        	            System.out.println("No successful applications found for your projects.");
//	        	            break;
//	        	        }
//
//	        	        System.out.print("Input applicant user ID to select BTO application to book: ");
//	        	        sc.nextLine(); // Clear buffer
//	        	        String appId = sc.nextLine();
//	        	        BTOapplication selected = BTOapplication.getApplicationByUserId(appId);
//
//	        	        if (selected == null || !selected.getStatus().equalsIgnoreCase("successful") ||
//	        	            !Project.getProjectById(selected.getProjectId()).getOfficers().contains(o)) {
//	        	            System.out.println("Invalid or unauthorized booking request.");
//	        	            break;
//	        	        }
//
//	        	        selected.display();
//	        	        System.out.print("Book flat for this applicant? (Y/N): ");
//	        	        String confirm = sc.nextLine().trim().toUpperCase();
//
//	        	        if (confirm.equals("Y")) {
//	        	            o.bookUnitForApplicant(appId, selected.getUnitType(), Project.getProjectById(selected.getProjectId()));
//	        	            o.generateReceipt(appId);
//	        	        } else {
//	        	            System.out.println("Cancelled.");
//	        	        }
//
//	        	   
//	        	        break;
//	        	    case 15:
//	        	    	o.viewOwnStatus();
//	        	    	break;
//	        	    default:
//	        	        System.out.println("Invalid choice. Try again");
//	        	        break;
//	        	}
//
//        	}while(selection<16 && selection>0 && selection!=8 && selection!=11);
//        	
//        }else if(cur.getUserType() == "applicant" && cur instanceof Applicant){// for applicant
//        	Applicant a = (Applicant) cur;//downcast
//        	do {
//	        	System.out.println("List of actions:");
//	        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)");//1 for 1, 1 applicant only can have 1 at a time
//	        	System.out.println("2. Apply for BTO project");
//	        	System.out.println("3. Withdraw BTO project");
//	        	System.out.println("4. View enquries");
//	        	System.out.println("5. Add enquries");//add here as 1 bulk for whole program
//	        	System.out.println("6. Edit enquries");
//	        	System.out.println("7. Delete enquries");
//	        	System.out.println("8. Logout");
//	        	System.out.println("9. change PW");
//	        	System.out.println("10. filter by area/flat type availability/px/date");
//	        	System.out.println("11. End program");
//	        	System.out.println("12. View own profile");
//	        	System.out.println("Pls input seletion: (e.g. 5)");
//	        	selection = sc.nextInt();
//	        	switch (selection) {
//	            case 1:
//	                System.out.println("Case 1 selected.");
//	                if(a.getApplicationStatus()!= "None" && a.getApplicationStatus()!= "Unsuccessful") {
//	                	Project.display(a.getAppliedProjectId());
//	                }else if(a.getApplicationStatus()== "None" ||a.getApplicationStatus()== "Unsuccessful") {
//	                	Project.displayAllProjectsApplicant(saved_filter,a);
//	                }
//	                break;
//	            case 2:
//	            	int projectID, rtsel;
//	            	String rt="";
//	                System.out.println("Case 2 selected.");
//	                System.out.println("enter project ID of desired project: (e.g. 1)");
//	                projectID=sc.nextInt();
//	                
//	                do {
//	                System.out.println("1. 2-Room flat remaining:" + Project.getProjectById(projectID).getTwoRoomCount());// can add dynamic to check that project has what first then display menu
//	                System.out.println("2. 3-Room flat remaining:" + Project.getProjectById(projectID).getThreeRoomCount());
//	                System.out.println("enter disired room type: (e.g. 1)");
//	                rtsel=sc.nextInt();
//	                switch (rtsel) {
//		                case 1:
//		                    rt="2-Room";
//		                    break;
//		                case 2:
//		                    rt="3-Room";
//		                    break;
//		                default:
//		                	System.out.println("Invalid choice. Try again");
//	                }
//	                }while(rtsel!=1 && rtsel!=2);
//	                
//	                if(a.isEligible(Project.getProjectById(projectID),rt)){
//	                	if(a.applyForProject(Project.getProjectById(projectID),rt)) {//need actually send to manager, i assume is all a program wide array, and every manager can approve
//	                		BTOapplication app= new BTOapplication(a.getNRIC(), projectID,a);
//	                	    app.updateStatus("Pending Approval", rt);
//	                	    BTOapplication.addApplication(app);
//	                		System.out.println("Successfully applied!, sent to manager for approval");
//	                		app.display();
//	                	}else {
//	                		System.out.println("Unsuccessful!, likely having ongoing application!");
//	                	}
//	                }else {
//	                	System.out.println("not eligible!");
//	                }
//	                break;
//	            case 3:
//	                System.out.println("Case 3 selected.");
//	                if(a.getApplicationStatus()!= "None" && a.getApplicationStatus()!= "Unsuccessful") {//need to send to manager, also need delete repeated attributes under applicant and bto app?
//	                	a.withdrawApplication();
//	                	BTOapplication.getApplicationByUserId(a.getNRIC()).updateStatus("Pending Withdrawal", BTOapplication.getApplicationByUserId(a.getNRIC()).getUnitType());
//	                	BTOapplication.getApplicationByUserId(a.getNRIC()).display();
//	                	System.out.println("Successfully withdrawn!, sent to manager for approval");
//	                }else if(a.getApplicationStatus()== "None" ||a.getApplicationStatus()== "Unsuccessful"){
//	                	System.out.println("Unsuccessful!, no current ongoing application");
//	                }
//	                break;
//	            case 4:
//	                System.out.println("Case 4 selected.");//delete list of enquries attribute under applicant
//	                Enquiry.displayAllEnquiries();
//	                break;
//	            case 5:
//	                System.out.println("Case 5 selected.");
//	                sc.nextLine();//clear buffer
//	                System.out.println("Input message:");
//	                Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), a.getName(), sc.nextLine()));// output success msg alr
//	                break;
//	            case 6:
//	            	int enquiryID;
//	                System.out.println("Case 6 selected.");
//	                //optimise by creating a new array to store the user exclusive enquiries
//	                if(Enquiry.displayByUser(a.getName())) {
//	                	System.out.println("input enquiry ID to select enquiry to edit:");
//	                	enquiryID=sc.nextInt();
//	                	if(Enquiry.getByID(enquiryID)==null) {
//	                		System.out.println("invalid ID");
//	                	}else if(Enquiry.getByID(enquiryID).getApplicantName()!=a.getName()) {
//	                		System.out.println("invalid access, not under your name");
//	                	}else {//both got an enquiry and under user's name
//	                		System.out.println("current enquiry:");
//	                		Enquiry.getByID(enquiryID).display();
//	                		System.out.println("input new msg:");
//	                		sc.nextLine();//clear buffer
//	                		Enquiry.editEnquiry(enquiryID, sc.nextLine());//success msg included
//	                	}
//	                }
//	                break;
//	            case 7:
//	            	int enquiryID1;
//	                System.out.println("Case 7 selected.");
//	                if(Enquiry.displayByUser(a.getName())) {
//	                	System.out.println("input enquiry ID to select enquiry to delete:");
//	                	enquiryID1=sc.nextInt();
//	                	if(Enquiry.getByID(enquiryID1)==null) {
//	                		System.out.println("invalid ID");
//	                	}else if(Enquiry.getByID(enquiryID1).getApplicantName()!=a.getName()) {
//	                		System.out.println("invalid access, not under your name");
//	                	}else {//both got an enquiry and under user's name
//	                		System.out.println("current enquiry:");
//	                		Enquiry.getByID(enquiryID1).display();
//	                		Enquiry.deleteEnquiry(enquiryID1);//success msg included
//	                	}
//	                }
//	                break;
//	            case 8:
//	            	System.out.println("Case 8 selected.");
//	                System.out.println("logging out\n\n");
//	                break;
//	            case 9:
//	            	String pw;
//	                System.out.println("Case 9 selected.");
//	                sc.nextLine();//clear buffer
//	                System.out.println("input new pw:");
//	                pw=sc.nextLine();
//	                System.out.println("confirm new pw:(input same pw agn)");
//	                if(pw.equals(sc.nextLine())) {
//	                	a.changePassword(pw);
//	                }else {
//	                	System.out.println("Unsuccessful change, re-enter pw not same");
//	                }
//	                break;//add filter by px
//	            case 10:
//					System.out.println("Case 10 selected.");
//	                int ftsel;
//	                String ft="";
//	                do{
//	                System.out.println("1. filter by projectid");
//	                System.out.println("2. filter by projectname");
//	                System.out.println("3. filter by neighborhood");
//	                System.out.println("4. filter by tworoomcount");
//	                System.out.println("5. filter by threeroomcount");
//	                System.out.println("7. filter by openingdate");
//	                System.out.println("8. filter by closingdate");
//	                System.out.println("9. filter by officerslots");
//	                System.out.println("10. exit to prv menu");
//	                System.out.println("input selection:");
//	                ftsel=sc.nextInt();
//	                switch (ftsel) {
//		                case 1:
//		                    ft="projectid";
//		                    break;
//		                case 2:
//		                    ft="projectname";
//		                    break;
//		                case 3:
//		                    ft="neighborhood";
//		                    break;
//		                case 4:
//		                    ft="tworoomcount";
//		                    break;
//		                case 5:
//		                    ft="threeroomcount";
//		                    break;
//		                  case 6:
//		                    break;
//		                  case 7:
//		                    ft="openingdate";
//		                    break;
//		                  case 8:
//		                    ft="closingdate";
//		                    break;
//		                  case 9:
//		                    ft="officerslots";
//		                    break;
//		                  case 10:
//		                      break;
//		                default:
//		                	System.out.println("Invalid choice. Try again");
//	                }
//	                }while(ftsel>10 || ftsel<1);
//	                if(ft.equals("")){
//	                    
//	                }else{
//	                	saved_filter=ft;
//	                    Project.displayAllProjects(ft);
//	                }
//	                break;
//	            case 11:
//	                System.out.println("Case 11 selected.");
//	                sc.close();
//	                System.out.println("Ending...");
//	                break;
//	            case 12:
//	            	a.viewOwnStatus();
//	            	break;
//	            default:
//	                System.out.println("Invalid choice. Try again");
//	        	}
//        	}while(selection<13 && selection>0 && selection!=8 && selection!=11);
//        	
//        	
//        }else {
//        	System.out.println("invalid user");
//        }
//        
//        UserPerson.writeBackToCSV(Users);
//        Project.writeCSVProjects();
//        BTOapplication.saveApplicationsToCSV("data/Applications.csv");
//        OfficerRegistration.saveRegistrationsToCSV("data/Registrations.csv");
//        Enquiry.saveEnquiriesToCSV("data/Enquiries.csv");
//        
//        if(selection == 8) {
//        	logout=1;
//        }else {
//        	logout=0;
//        }
//    }while(logout == 1);
//    }
//}

package bto;

import java.util.ArrayList;
import java.util.Scanner;
import bto.model.*;
import bto.service.LoginService;
import bto.Menu.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<UserPerson> Users = new ArrayList<>();

        UserPerson.LoadUsers(Users);
        OfficerRegistration.loadRegistrationsFromCSV("data/Registrations.csv", Users);
        Project.loadProjectsFromCSV(Users);
        BTOapplication.loadApplicationsFromCSV("data/Applications.csv", Users);
        Enquiry.loadEnquiriesFromCSV("data/Enquiries.csv");

        int logout;
        do {
            Scanner sc = new Scanner(System.in);
            UserPerson cur = LoginService.login(Users, sc);

            System.out.println("Welcome <" + cur.getUserType() + "> " + cur.getName() + "!\n");

            logout = 0;
            switch (cur.getUserType()) {
                case "manager" -> ManagerMenu.handleManager((HDBmanager) cur, sc);
                case "officer" -> OfficerMenu.handleOfficer((HDBofficer) cur, sc);
                case "applicant" -> ApplicantMenu.handleApplicant((Applicant) cur, sc);
                default -> System.out.println("Invalid user.");
            }

            UserPerson.writeBackToCSV(Users);
            Project.writeCSVProjects();
            BTOapplication.saveApplicationsToCSV("data/Applications.csv");
            OfficerRegistration.saveRegistrationsToCSV("data/Registrations.csv");
            Enquiry.saveEnquiriesToCSV("data/Enquiries.csv");

            if (cur.getUserType().equals("manager") || cur.getUserType().equals("officer") || cur.getUserType().equals("applicant")) {
                logout = 1;
            }
        } while (logout == 1);
    }
}
