package bto;

import java.util.Scanner;
import java.util.ArrayList;

import bto.model.Applicant;
import bto.model.BTOapplication;
import bto.model.Enquiry;
import bto.model.HDBmanager;
import bto.model.HDBofficer;
import bto.model.OfficerRegistration;
import bto.model.Project;
import bto.model.UserPerson;
import bto.service.LoginService;

public class Main {
    public static void main(String[] args) {
    	//UserPerson[] Users = new UserPerson[20]; // creating a array to store all users
    	
    	ArrayList<UserPerson> Users = new ArrayList<>();
        
    	UserPerson.LoadUsers(Users);
    	
    	Project.loadProjectsFromCSV(Users);
        
        //load in project
        
        //login menu
    	int logout = 0;
    	do {
    	    Scanner sc = new Scanner(System.in); // only if not declared earlier
    	    UserPerson cur = LoginService.login(Users, sc);
        
        //first interface 1 menu for each user type
        System.out.println("Welcome" + " <" + cur.getUserType() + "> " + cur.getName() +"!\n");
        
        
        int selection=0;
		String saved_filter=null;
        if(cur.getUserType() == "manager" && cur instanceof HDBmanager) { //if possible change to check the obj itself
        	HDBmanager m = (HDBmanager) cur;//downcast
        	do {
	        	System.out.println("1. View All BTO projects listing"); 
	        	System.out.println("2. Toggle visibility");
	        	System.out.println("3. approve BTO application by applicant");//both withdraw and apply
	        	System.out.println("4. approve officer application by officer");// put view function within
	        	System.out.println("5. Add BTO listing");
	        	System.out.println("6. Edit BTO listing");
	        	System.out.println("7. Delete BTO listing");
	        	System.out.println("8. Logout");
	        	System.out.println("9. change PW");
	        	System.out.println("10. filer by area/flat type availability/px/date");
	        	System.out.println("11. End program");
	        	System.out.println("12. reply enquries");
	        	System.out.println("13. generate report");
	        	System.out.println("Pls input seletion: (e.g. 5)");
	        	
	        	selection = sc.nextInt();
	        	switch (selection) {
	        	    case 1:
	        	        System.out.println("Case 1 selected.");
	        	        Project.displayAllProjects(saved_filter);
	        	        break;
	        	    case 2:
	        	    	int projectID2;
	        	    	boolean a;
	        	        System.out.println("Case 2 selected.");
	        	        System.out.println("input project ID for the project you want to toggle visibility on");
	        	        projectID2=sc.nextInt();
	        	        System.out.println("on/off e.g. =ve int for on, 0 for off");
	        	        a=sc.nextInt()==1?true:false;
	        	        m.toggleProjectVisibility(projectID2, a);
	        	        break;
	        	    case 3:
	        	        System.out.println("Case 3 selected.");
	        	        int projectID1;
	        	    	String appNRIC;
	        	    	BTOapplication.displayStatusPendingA();
	        	    	BTOapplication.displayStatusPendingW();
	        	        System.out.println("input project ID:");
	        	        projectID1=sc.nextInt();
	        	        sc.nextLine();
	        	        System.out.println("input applicant NRIC to approve/reject:");
	        	        appNRIC=sc.nextLine();
	        	        BTOapplication app;
	        	        app= BTOapplication.getApplicationByUserId(appNRIC);
	        	        app.display();
	        	        System.out.println("approve/reject " + app.getStatus() +" e.g. +ve int for approve,0 for reject");
	        	        boolean c=sc.nextBoolean();
	        	        if(c) {
	        	        	m.approveBTOApplication(projectID1, appNRIC);
	        	        }else {
	        	        	m.rejectBTOApplication(projectID1, appNRIC);
	        	        }
	        	        break;
	        	    case 4:
	        	    	int projectID;
	        	    	String officerNRIC;
	        	        System.out.println("Case 4 selected.");
	        	        //display list of officer reg under his list of project
	        	        System.out.println("input project ID:");
	        	        projectID=sc.nextInt();
	        	        sc.nextLine();
	        	        System.out.println("input officer NRIC to approve/reject:");
	        	        officerNRIC=sc.nextLine();
	        	        OfficerRegistration offreg;
	        	        //find the offreg obj first
	        	        //display the offreg obj especially cur status if approve or reject
	        	        //System.out.println("approve/reject " + offreg.getRegistrationStatus() +" e.g. +ve int for approve,0 for reject");
	        	        boolean b=sc.nextBoolean();
	        	        if(b) {
	        	        	m.approveOfficerRegistration(projectID, officerNRIC);
	        	        }else {
	        	        	m.rejectOfficerRegistration(projectID, officerNRIC);
	        	        }
	        	        break;
	        	    case 5:
	        	        System.out.println("Case 5 selected.");
	        	        //display how many list under him first then he want to add to which list or create new list
	        	        System.out.println("input projectName, String neighborhood,\r\n"
	        	        		+ "                                    int twoRoomCount,\r\n"
	        	        		+ "                                    int threeRoomCount, boolean projectVisibility, \r\n"
	        	        		+ "                                   LocalDate openingDate, LocalDate closingDate,\r\n"
	        	        		+ "                                   int officerSlots in sequence separated by space, e.g. 1 0 1 0 1 0 1 0");
	        	        m.createBTOListings(null, null, 0, 0, true, null, null, 0);//sld be a list?
	        	        break;
	        	    case 6:
	        	        System.out.println("Case 6 selected.");// edit field by field can be quite slow
	        	        System.out.println("input proj ID, field to edit(e.g. projectname), new value in sequence e.g. 1 projectname Taban Garden:");
	        	        //m.editBTOListings(projectID, officerNRIC, offreg);
	        	        break;
	        	    case 7:
	        	        System.out.println("Case 7 selected.");
	        	        System.out.println("input proj ID:");
	        	        int projectID3=sc.nextInt();
	        	        m.deleteBTOListings(projectID3);
	        	        break;
	        	    case 8:
	        	    	System.out.println("Case 8 selected.");
		                System.out.println("logging out\n\n");
		                break;
	        	    case 9:
	        	        System.out.println("Case 9 selected.");
	        	        String pw;
		                System.out.println("Case 9 selected.");
		                sc.nextLine();//clear buffer
		                System.out.println("input new pw:");
		                pw=sc.nextLine();
		                System.out.println("confirm new pw:(input same pw agn)");
		                if(pw.equals(sc.nextLine())) {
		                	m.changePassword(pw);
		                }else {
		                	System.out.println("Unsuccessful change, re-enter pw not same");
		                }
	        	        break;
	        	    case 10:
	        	        System.out.println("Case 10 selected.");
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
		                System.out.println("input selection(filter saved for current login):");
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
		                    //pass
		                }else{
							saved_filter=ft;
		                    Project.displayAllProjects(ft);
		                }
	        	        break;
	        	    case 11:
	        	        System.out.println("Case 11 selected.");
		                sc.close();
		                System.out.println("Ending...");
	        	        break;
	        	    case 12:
	        	        System.out.println("Case 12 selected.");
	        	        int enquiryID3;
	        	        Enquiry.displayAllEnquiries();
	        	        System.out.println("input enquiry ID to select enquiry to reply:");
	                	enquiryID3=sc.nextInt();
	                	if(Enquiry.getByID(enquiryID3)==null) {
	                		System.out.println("invalid ID");
	                	}else {
	                		System.out.println("current enquiry:");
	                		Enquiry.getByID(enquiryID3).display();
	                		System.out.println("input reply:");
	                		sc.nextLine();//clear buffer
	                		Enquiry.getByID(enquiryID3).reply(sc.nextLine());
	                	}
	        	        break;
	        	    case 13:
						int projectID4;
	        	        System.out.println("Case 13 selected.");
						System.out.println("input project ID to generate report on all applicant:");
						projectID4=sc.nextInt();
	        	        m.generateReport(projectID4, null);
	        	        break;
	        	    default:
	        	        System.out.println("Invalid choice. Try again");
	        	        break;
	        	}
        	}while(selection<14 && selection>0 && selection!=8 && selection!=11);
        	
        }else if(cur.getUserType() == "officer" && cur instanceof HDBofficer) {// no need the enquiries list attribute and applied projID attribute cause applicant class alr have
        	HDBofficer o = (HDBofficer) cur;//downcast
        	do {
	        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)"); 
	        	System.out.println("2. Apply for BTO project");//if not officer in charge, need change a bit, check he is using officer mode by attribute or project he is officer
	        	System.out.println("3. Withdraw BTO project");
	        	System.out.println("4. View enquries");//
	        	System.out.println("5. Add enquries");
	        	System.out.println("6. Edit enquries");
	        	System.out.println("7. Delete enquries");
	        	System.out.println("8. Logout");
	        	System.out.println("9. change PW");
	        	System.out.println("10. filer by area/flat type availability/px/date");
	        	System.out.println("11. End program");
	        	System.out.println("--------officer functions----------");
	        	System.out.println("12. reply enquries");//doc mention about enquiry for a project
	        	System.out.println("13. apply for officer of a project");
	        	System.out.println("14. book slot for applicant on approved applications");//after approved then visible to officer?
	        	System.out.println("15. generate receipt");
	        	System.out.println("Pls input seletion: (e.g. 5)");
	        	
	        	selection = sc.nextInt();
	        	switch (selection) {
		        	case 1:
		                System.out.println("Case 1 selected.");
		                if(o.getApplicationStatus()!= "None" && o.getApplicationStatus()!= "Unsuccessful") {
		                	Project.display(o.getAppliedProjectId());
		                }else if(o.getApplicationStatus()== "None" ||o.getApplicationStatus()== "Unsuccessful") {
		                	Project.displayAllProjectsApplicant(null);//sld be same same applicant but display an extra one if he officer
		                }
		                break;
		            case 2:
		            	int projectID, rtsel;
		            	String rt="";
		                System.out.println("Case 2 selected.");
		                System.out.println("enter project ID of desired project: (e.g. 1)");
		                projectID=sc.nextInt();
		                
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
		                
		                if(o.isEligible(Project.getProjectById(projectID),rt)){
		                	if(o.applyForProject(Project.getProjectById(projectID),rt)) {
		                		BTOapplication app= new BTOapplication(o.getNRIC(), projectID,o);//upcast?
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
		                break;
		            case 3:
		                System.out.println("Case 3 selected.");
		                if(o.getApplicationStatus()!= "None" && o.getApplicationStatus()!= "Unsuccessful") {
		                	o.withdrawApplication();
		                	BTOapplication.getApplicationByUserId(o.getNRIC()).updateStatus("Pending Withdrawal", BTOapplication.getApplicationByUserId(o.getNRIC()).getUnitType());
		                	BTOapplication.getApplicationByUserId(o.getNRIC()).display();
		                	System.out.println("Successfully withdrawn!, sent to manager for approval");
		                }else if(o.getApplicationStatus()== "None" ||o.getApplicationStatus()== "Unsuccessful"){
		                	System.out.println("Unsuccessful!, no current ongoing application");
		                }
		                break;
		            case 4:
		                System.out.println("Case 4 selected.");
		                Enquiry.displayAllEnquiries();
		                break;
		            case 5:
		                System.out.println("Case 5 selected.");
		                sc.nextLine();
		                System.out.println("Input message:");
		                Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), o.getName(), sc.nextLine()));
		                break;
		            case 6:
		            	int enquiryID;
		                System.out.println("Case 6 selected.");
		                if(Enquiry.displayByUser(o.getName())) {
		                	System.out.println("input enquiry ID to select enquiry to edit:");
		                	enquiryID=sc.nextInt();
		                	if(Enquiry.getByID(enquiryID)==null) {
		                		System.out.println("invalid ID");
		                	}else if(Enquiry.getByID(enquiryID).getApplicantName()!=o.getName()) {
		                		System.out.println("invalid access, not under your name");
		                	}else {
		                		System.out.println("current enquiry:");
		                		Enquiry.getByID(enquiryID).display();
		                		System.out.println("input new msg:");
		                		sc.nextLine();//clear buffer
		                		Enquiry.editEnquiry(enquiryID, sc.nextLine());
		                	}
		                }
		                break;
		            case 7:
		            	int enquiryID1;
		                System.out.println("Case 7 selected.");
		                if(Enquiry.displayByUser(o.getName())) {
		                	System.out.println("input enquiry ID to select enquiry to delete:");
		                	enquiryID1=sc.nextInt();
		                	if(Enquiry.getByID(enquiryID1)==null) {
		                		System.out.println("invalid ID");
		                	}else if(Enquiry.getByID(enquiryID1).getApplicantName()!=o.getName()) {
		                		System.out.println("invalid access, not under your name");
		                	}else {
		                		System.out.println("current enquiry:");
		                		Enquiry.getByID(enquiryID1).display();
		                		Enquiry.deleteEnquiry(enquiryID1);//success msg included
		                	}
		                }
		                break;
		            case 8:
		            	System.out.println("Case 8 selected.");
		                System.out.println("logging out\n\n");
		                break;
		            case 9:
		            	String pw;
		                System.out.println("Case 9 selected.");
		                sc.nextLine();//clear buffer
		                System.out.println("input new pw:");
		                pw=sc.nextLine();
		                System.out.println("confirm new pw:(input same pw agn)");
		                if(pw.equals(sc.nextLine())) {
		                	o.changePassword(pw);
		                }else {
		                	System.out.println("Unsuccessful change, re-enter pw not same");
		                }
		                break;//add filter by px
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
		                    //pass
		                }else{
		                    Project.displayAllProjects(ft);
		                }
		                break;
		            case 11:
		                System.out.println("Case 11 selected.");
		                sc.close();
		                System.out.println("Ending...");
		                break;
	        	    case 12:
	        	        System.out.println("Case 12 selected.");
	        	        int enquiryID2;
	        	        Enquiry.displayAllEnquiries();
	        	        System.out.println("input enquiry ID to select enquiry to reply:");
	                	enquiryID2=sc.nextInt();
	                	if(Enquiry.getByID(enquiryID2)==null) {
	                		System.out.println("invalid ID");
	                	}else {
	                		System.out.println("current enquiry:");
	                		Enquiry.getByID(enquiryID2).display();
	                		System.out.println("input reply:");
	                		sc.nextLine();//clear buffer
	                		Enquiry.getByID(enquiryID2).reply(sc.nextLine());
	                	}
	        	        break;
	        	    case 13:
	        	        System.out.println("Case 13 selected.");
	        	        break;
	        	    case 14://only 
	        	    	String app;
	        	    	BTOapplication c=null;
	        	        System.out.println("Case 14 selected.");
	        	        //display "successful" bto app also filter by his officer project so he can book his own project only
	        	        for(BTOapplication a :BTOapplication.getAllApplications()) {
	        	        	for(HDBofficer ok :(Project.getProjectById(a.getProjectId())).getOfficers()) {
	        	        	if(a.getStatus().equals("successful") && ok==o){
	        	        		c =a;
	        	        		a.display();
	        	        	}
	        	        	}
	        	        }
	        	        
	        	        System.out.println("input applicant user ID to select BTO application to book:");
	        	        app=sc.nextLine();
	        	        if(BTOapplication.getApplicationByUserId(app)==null){//check if is in range or accessing other or prvs app
	        	        	System.out.println("invalid userID");
	        	        
	        	        }else {
	        	        	boolean book;
	        	        	BTOapplication.getApplicationByUserId(app).display();
	        	        	System.out.println("book flats? Y/N e.g. +ve int for Y, 0 for N");
	        	        	book=sc.hasNextBoolean();
	        	        	if(book==true) {
	        	        		o.bookUnitForApplicant(app,c.getUnitType(), Project.getProjectById(c.getProjectId()));
	        	        		}
        	        		else {
        	        		System.out.println("you entered No");
	        	        	}
	        	        }
	        	        
	        	        
	        	        break;
	        	    case 15:
	        	        System.out.println("Case 15 selected.");
	        	        break;
	        	    default:
	        	        System.out.println("Invalid choice. Try again");
	        	        break;
	        	}

        	}while(selection<16 && selection>0 && selection!=8 && selection!=11);
        	
        }else if(cur.getUserType() == "applicant" && cur instanceof Applicant){// for applicant
        	Applicant a = (Applicant) cur;//downcast
        	do {
	        	System.out.println("List of actions:");
	        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)");//1 for 1, 1 applicant only can have 1 at a time
	        	System.out.println("2. Apply for BTO project");
	        	System.out.println("3. Withdraw BTO project");
	        	System.out.println("4. View enquries");
	        	System.out.println("5. Add enquries");//add here as 1 bulk for whole program
	        	System.out.println("6. Edit enquries");
	        	System.out.println("7. Delete enquries");
	        	System.out.println("8. Logout");
	        	System.out.println("9. change PW");
	        	System.out.println("10. filer by area/flat type availability/px/date");
	        	System.out.println("11. End program");
	        	System.out.println("Pls input seletion: (e.g. 5)");
	        	selection = sc.nextInt();
	        	switch (selection) {
	            case 1:
	                System.out.println("Case 1 selected.");
	                if(a.getApplicationStatus()!= "None" && a.getApplicationStatus()!= "Unsuccessful") {
	                	Project.display(a.getAppliedProjectId());
	                }else if(a.getApplicationStatus()== "None" ||a.getApplicationStatus()== "Unsuccessful") {
	                	Project.displayAllProjectsApplicant(null);
	                }
	                break;
	            case 2:
	            	int projectID, rtsel;
	            	String rt="";
	                System.out.println("Case 2 selected.");
	                System.out.println("enter project ID of desired project: (e.g. 1)");
	                projectID=sc.nextInt();
	                
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
	                
	                if(a.isEligible(Project.getProjectById(projectID),rt)){
	                	if(a.applyForProject(Project.getProjectById(projectID),rt)) {//need actually send to manager, i assume is all a program wide array, and every manager can approve
	                		BTOapplication app= new BTOapplication(a.getNRIC(), projectID,a);
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
	                break;
	            case 3:
	                System.out.println("Case 3 selected.");
	                if(a.getApplicationStatus()!= "None" && a.getApplicationStatus()!= "Unsuccessful") {//need to send to manager, also need delete repeated attributes under applicant and bto app?
	                	a.withdrawApplication();
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
	            case 5:
	                System.out.println("Case 5 selected.");
	                sc.nextLine();//clear buffer
	                System.out.println("Input message:");
	                Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), a.getName(), sc.nextLine()));// output success msg alr
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
	                		System.out.println("current enquiry:");
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
	                		System.out.println("current enquiry:");
	                		Enquiry.getByID(enquiryID1).display();
	                		Enquiry.deleteEnquiry(enquiryID1);//success msg included
	                	}
	                }
	                break;
	            case 8:
	            	System.out.println("Case 8 selected.");
	                System.out.println("logging out\n\n");
	                break;
	            case 9:
	            	String pw;
	                System.out.println("Case 9 selected.");
	                sc.nextLine();//clear buffer
	                System.out.println("input new pw:");
	                pw=sc.nextLine();
	                System.out.println("confirm new pw:(input same pw agn)");
	                if(pw.equals(sc.nextLine())) {
	                	a.changePassword(pw);
	                }else {
	                	System.out.println("Unsuccessful change, re-enter pw not same");
	                }
	                break;//add filter by px
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
        	}while(selection<11 && selection>0 && selection!=8 && selection!=11);
        	
        	
        }else {
        	System.out.println("invalid user");
        }
        
        if(selection == 8) {
        	logout=1;
        }else {
        	logout=0;
        }
    }while(logout == 1);
    }
}