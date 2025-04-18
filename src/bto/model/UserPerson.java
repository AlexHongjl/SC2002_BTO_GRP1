package bto.model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserPerson {
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
	
	public static void LoadUsers(ArrayList<UserPerson> target) {
	    String[] files = { "OfficerList.csv", "ManagerList.csv", "ApplicantList.csv" };
	    String[] roles = { "officer", "manager", "applicant" };

	    for (int i = 0; i < files.length; i++) {
	        List<UserPerson> userList = readUsersFromFile(files[i], roles[i]);
	        target.addAll(userList);//seems right
	    }

	    // Optional: Debug print
	    for (UserPerson user : target) {
	        System.out.println(user.getName() + " | " + user.getNRIC() + " | " + user.getUserType());
	    }
	}

	public static List<UserPerson> readUsersFromFile(String filename, String userType) {
	    List<UserPerson> users = new ArrayList<>();
	    String path = "data/" + filename;

	    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	        String line;
	        boolean isFirstLine = true;

	        while ((line = br.readLine()) != null) {
	            if (isFirstLine) {
	                isFirstLine = false;
	                continue;
	            }

	            String[] parts = line.split(",");
	            if (parts.length < 5) continue;

	            String name = parts[0].trim();
	            String NRIC = parts[1].trim();
	            int age = Integer.parseInt(parts[2].trim());
	            String maritalStatus = parts[3].trim();
	            String password = parts[4].trim();//correct

	            UserPerson user;

	            switch (userType) {
	                case "officer":
	                    user = new HDBofficer(name, NRIC, age, maritalStatus, password);//wrong constructor not same
	                    break;
	                case "manager":
	                    user = new HDBmanager(name, NRIC, age, maritalStatus, password);//sequence wrong also marital status is boolean or str...
	                    break;
	                case "applicant":
	                    user = new Applicant(name, NRIC, age, maritalStatus, password);//correct
	                    break;
	                default:
	                    continue; // skip unknown roles
	            }

	            users.add(user);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return users;
	}
	
	public static void writeBackToCSV(ArrayList<UserPerson> UserList) {//added
	    String APPLICANT_PATH = "data/ApplicantList.csv";
	    String OFFICER_PATH = "data/OfficerList.csv";
	    String MANAGER_PATH = "data/ManagerList.csv";
	    
	    String applicant = "Name,NRIC,Age,Marital Status,Password\n";
	    String officer = "Name,NRIC,Age,Marital Status,Password\n";
	    String manager = "Name,NRIC,Age,Marital Status,Password\n";
	    
	    for (UserPerson user : UserList) {
	      String newEntry = user.getName() + "," + user.getNRIC() + "," + user.getAge() + "," + user.getMaritalStatus() + "," + user.getPassword();
	      if (user instanceof HDBofficer) {
	        officer = officer + newEntry + "\n";
	      }
	      else if (user instanceof Applicant) {
	        applicant = applicant + newEntry + "\n";
	      }
	      else if (user instanceof HDBmanager) {
	        manager = manager + newEntry + "\n";
	      }
	    }
	    
	    try (FileWriter writer = new FileWriter(APPLICANT_PATH)){
	      writer.write(applicant);
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	    try (FileWriter writer = new FileWriter(OFFICER_PATH)){
	      writer.write(officer);
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	    try (FileWriter writer = new FileWriter(MANAGER_PATH)){
	      writer.write(manager);
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
	
	public void viewOwnStatus() {
	    System.out.println("User ID: " + getNRIC());
	    System.out.println("Name: " + getName());
	    System.out.println("MaritalStatus: " + getMaritalStatus());
	    // Add other base fields if any
	}


	public String getName() {
		return this.name;
	}
	
	public String getNRIC() {
		return this.NRIC;
	}
	
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
	
	public void setUserType(String userType) {
	    this.userType = userType;
	}

	
	public void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
	
}
