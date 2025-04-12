package bto;

import java.util.Scanner;
import java.util.ArrayList;

import bto.model.Enquiry;
import bto.model.UserPerson;

public class Main {
    public static void main(String[] args) {
    	//UserPerson[] Users = new UserPerson[20]; // creating a array to store all users
    	
    	ArrayList<UserPerson> Users = new ArrayList<>();
    	
        Applicant.LoadUsers(Users); // need a method in applicant to edit an array(as argument) and load in all user from applicant list
        HDBofficer.LoadUsers(Users);
        HDBmanager.LoadUsers(Users);
        
        //login menu
        int logout;
        do {
        Scanner sc= new Scanner(System.in);
        UserPerson cur= null; // current user, need a pointer
        do {
	        System.out.println("BTO System login page \n");
	        System.out.println("Pls input username:");
	        
	        String username = sc.nextLine();
	        System.out.println("Pls input PW:");
	        String password = sc.nextLine();
	        
	        
	        for (UserPerson user : Users) {
	        	//if (user == null) continue;
	        	
	        	if(username.equals(user.getNRIC())) {
	        		if(password.equals(user.getPassword())) {
	        			cur= user;
	        			break;
	        		}
	        	}
	        }
	        
	        if (cur == null) {
                System.out.println("Invalid credentials. Try again.\n");
            }
	        
        }while(cur== null);
        
        //first interface 1 menu for each user type
        System.out.println("Welcome" + " <" + cur.getUserType() + "> " + cur.getName() +"!\n");
        
        //if cur pointer cannot just init 3 cur for diff obj
        
        int selection;
        if(cur.getUserType() == "manager") { //if possible change to check the obj itself
        	System.out.println("1. View All BTO projects"); 
        	System.out.println("2. Toggle visibility");//if not officer in charge
        	System.out.println("3. approve BTO application by applicant");//both withdraw and apply
        	System.out.println("4. approve officer application by officer");// put view function within
        	System.out.println("5. Add BTO listing");
        	System.out.println("6. Edit BTO listing");
        	System.out.println("7. Delete BTO listing");
        	System.out.println("8. Logout");
        	System.out.println("9. change PW");
        	System.out.println("10. filer by area/flat type availability/px/date");
        	System.out.println("11. reply enquries");
        	System.out.println("12. generate report");
        	System.out.println("Pls input seletion: (e.g. 5)");
        }else if(cur.getUserType() == "officer") {// no need the enquiries list attribute and applied projID attribute cause applicant class alr have
        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)"); 
        	System.out.println("2. Apply for BTO project");//if not officer in charge
        	System.out.println("3. Withdraw BTO project");
        	System.out.println("4. View enquries");
        	System.out.println("5. Add enquries");
        	System.out.println("6. Edit enquries");
        	System.out.println("7. Delete enquries");
        	System.out.println("8. Logout");
        	System.out.println("9. change PW");
        	System.out.println("10. filer by area/flat type availability/px/date");
        	System.out.println("--------officer functions----------");
        	System.out.println("11. reply enquries");
        	System.out.println("12. apply for officer of a project");
        	//System.out.println("12. approve BTO application by applicant"); wait approve by manage is it?
        	System.out.println("13. book slot for applicant on approved applications");
        	System.out.println("14. generate receipt");
        	System.out.println("Pls input seletion: (e.g. 5)");
        	
        }else if(cur.getUserType() == "applicant"){// for applicant
        	do {
	        	System.out.println("List of actions:");
	        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)");//not sure if this still have after they applied/approved for one, 1 for 1 
	        	System.out.println("2. Apply for BTO project");
	        	System.out.println("3. Withdraw BTO project");
	        	System.out.println("4. View enquries");
	        	System.out.println("5. Add enquries");//not sure if add here or under project, add here as 1 bulk for whole program
	        	System.out.println("6. Edit enquries");//should be an optional option? cause need check if have existing
	        	System.out.println("7. Delete enquries");
	        	System.out.println("8. Logout");
	        	System.out.println("9. change PW");
	        	System.out.println("10. filer by area/flat type availability/px/date");
	        	System.out.println("Pls input seletion: (e.g. 5)");
	        	selection = sc.nextInt();
	        	switch (selection) {
	            case 1://display the project they applied for only if they applied
	                System.out.println("Case 1 selected.");
	                if(cur.getApplicationStatus()!= "None") {// maybe we no need withdrawn and unsuccessful state?
	                	Project.display(cur.getAppliedProjectId());//add a display method in project to display a single project
	                }else if(cur.getApplicationStatus()== "None") {
	                	Project.displayAppProjects();// need check visibility rmb sm can change visibility add a display app method 
	                }
	                break;
	            case 2:
	            	int projectID, rtsel;
	            	String rt;
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
	                }while(rtsel!=1 || rtsel!=2);
	                
	                if(cur.isEligible(Project.getProjectById(projectID),rt)){
	                	if(cur.applyForProject(Project.getProjectById(projectID),rt)) {//need actually send to officer
	                		System.out.println("Successful!, sent to officer for approval");
	                	}else {
	                		System.out.println("Unsuccessful!, likely having ongoing application!");
	                	}
	                }else {
	                	System.out.println("not eligible!");
	                }
	                break;
	            case 3:
	                System.out.println("Case 3 selected.");
	                if(cur.getApplicationStatus()!= "None") {// not sure need add unsuccessful
	                	cur.withdrawApplication();//need to send to officer 
	                	System.out.println("Successful!, sent to officer for approval");
	                }else if(cur.getApplicationStatus()== "None"){
	                	System.out.println("Unsuccessful!, no current ongoing application");
	                }
	                break;
	            case 4:
	                System.out.println("Case 4 selected.");//delete list of enquries attribute under applicant
	                Enquiry.displayAllEnquiries();
	                break;
	            case 5:
	                System.out.println("Case 5 selected.");
	                System.out.println("Input message:");
	                Enquiry.addEnquiry(Enquiry(Enquiry.getCount(), cur.getName(), sc.nextLine()));// output success msg alr
	                break;
	            case 6:
	            	int enquiryID;
	                System.out.println("Case 6 selected.");
	                //optimise by creating a new array to store the user exclusive enquiries
	                if(Enquiry.displayByUser(cur.getName())) {
	                	System.out.println("input enquiry ID to select enquiry to edit:");
	                	enquiryID=sc.nextInt();
	                	if(Enquiry.getByID(enquiryID)==null) {
	                		System.out.println("invalid ID");
	                	}else if(Enquiry.getByID(enquiryID).getApplicantName()!=cur.getName()) {
	                		System.out.println("invalid access, not under your name");
	                	}else {//both got an enquiry and under user's name
	                		System.out.println("current enqury:");
	                		Enquiry.getByID(enquiryID).display();
	                		System.out.println("input new msg:");
	                		Enquiry.editEnquiry(enquiryID, sc.nextLine());//success msg included
	                	}
	                }
	                break;
	            case 7:
	            	int enquiryID1;
	                System.out.println("Case 7 selected.");
	                if(Enquiry.displayByUser(cur.getName())) {
	                	System.out.println("input enquiry ID to select enquiry to delete:");
	                	enquiryID1=sc.nextInt();
	                	if(Enquiry.getByID(enquiryID1)==null) {
	                		System.out.println("invalid ID");
	                	}else if(Enquiry.getByID(enquiryID1).getApplicantName()!=cur.getName()) {
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
	                System.out.println("input new pw:");
	                pw=sc.nextLine();
	                System.out.println("confirm new pw:(input same pw agn)");
	                if(pw==sc.nextLine()) {
	                	cur.changePassword(pw);
	                }else {
	                	System.out.println("Unsuccessful change, re-enter pw not same");
	                }
	                break;
	            default:
	                System.out.println("Invalid choice. Try again");
	        	}
        	}while(selection<10 && selection>0 && selection!=8);
        	
        	
        }else {
        	System.out.println("invalid user");
        }
        
        if(selection == 8) {
        	logout=1;
        }
    }while(logout == 1);
    }
}