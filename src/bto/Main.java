package bto;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        User[] Users; // creating a array to store all users
        Applicant.LoadUsers(Users); // need a method in applicant to edit an array(as argument) and load in all user from applicant list
        HDBofficer.LoadUsers(Users);
        HDBmanager.LoadUsers(Users);
        
        int logout;
        //login menu
        do {
        Scanner sc= new Scanner(System.in);
        User cur= null; // current user, need a pointer
        do {
	        System.out.println("BTO System login page \n");
	        System.out.println("Pls input username:");
	        
	        String username = sc.nextLine();
	        System.out.println("Pls input PW:");
	        String password = sc.nextLine();
	        
	        
	        for (User user : Users) {
	        	if(username.equals(user.NRIC)) {
	        		if(password.equals(user.password)) {
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
        System.out.println("Welcome" + cur.userType + " " + cur.name +"\n");
        
        int selection;
        if(cur.userType == "manager") { //if possible change to check the obj itself
	        System.out.println("Pls input seletion:");
	        System.out.println("Pls input PW:");
	        System.out.println("Logout");
        }else if(cur.userType == "officer") {
        	System.out.println("Logout");
        }else if(cur.userType == "applicant"){// for applicant
        	do {
	        	System.out.println("List of actions:");
	        	System.out.println("1. View BTO project/or only the project you applied for(if applicable)");//not sure if this still have after they applied/approved for one, 1 for 1 
	        	System.out.println("2. Apply for BTO project");
	        	System.out.println("3. Withdraw BTO project");
	        	System.out.println("4. View enquries");
	        	System.out.println("5. Add enquries");//not sure if add here or under project
	        	System.out.println("6. Edit enquries");//should be an optional option? cause need check if have existing
	        	System.out.println("7. Delete enquries");
	        	System.out.println("8. Logout");
	        	System.out.println("9. change PW");
	        	System.out.println("Pls input seletion: (e.g. 5)");
	        	selection = sc.nextInt();
	        	switch (selection) {
	            case 1://display the project they applied for only if they applied
	                System.out.println("Case 1 selected.");
	                if(cur.applicationStatus!= "None") {// maybe we no need withdrawn and unsuccessful state?
	                	Project.display(cur.appliedProjectId);//add a display method in project to display a single project
	                }else if(cur.applicationStatus== "None") {
	                	Project.displayAppProjects();// need check visibility rmb sm can change visibility add a display app method 
	                }
	                break;
	            case 2:
	            	int projectID, rtsel;
	            	string rt;
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
	                	System.out.println("not elligible!");
	                }
	                break;
	            case 3:
	                System.out.println("Case 3 selected.");
	                if(cur.applicationStatus!= "None") {// not sure need add unsuccessful
	                	cur.withdrawApplication();//need to send to officer 
	                	System.out.println("Successful!, sent to officer for approval");
	                }else if(cur.applicationStatus== "None"){
	                	System.out.println("Unsuccessful!, no current ongoing application");
	                }
	                break;
	            case 4:
	                System.out.println("Case 4 selected.");
	                
	                break;
	            case 5:
	                System.out.println("Case 5 selected.");
	                break;
	            case 6:
	                System.out.println("Case 6 selected.");
	                break;
	            case 7:
	                System.out.println("Case 7 selected.");
	                break;
	            case 8:
	                System.out.println("Case 8 selected.");
	                System.out.println("logging out");
	                break;
	            case 9:
	                System.out.println("Case 9 selected.");
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