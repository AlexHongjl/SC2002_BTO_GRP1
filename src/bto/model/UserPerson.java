package bto.model;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the superclass to the Manager class and Applicant class, which Officer extends from
 * Stores information on each UserPerson object created from this class
 * Information on each person object: name, NRIC, age, marital status, password and user type
 * Has a method to read from CSV files to get Applicant/Officer/Manager information
 * Gives each user a user type based on which file they are read from
 * Contains methods to get each specific information 
 */
public class UserPerson {
	private String name;
	private String NRIC;
	private int age;
	private String maritalStatus;
	private String password;
	private String userType;
	
	/**
	 * Constructor 
	 * 
	 * @param name of user
	 * @param NRIC of user
	 * @param age of user
	 * @param maritalStatus of user
	 * @param password of user 
	 * @param userType of user
	 */
	public UserPerson(String name, String NRIC, int age, String maritalStatus, String password, String userType) {
		this.name = name;
		this.NRIC = NRIC;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.password = password;
		this.userType = userType;
	}
	
	/**
	 * Stores all user information in the target list
	 * Gives each user a user type based on the CSV they were read from
	 * 
	 * @param target list of all users
	 */
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
	
	/**
	 * Reads information of users from file
	 * Information will be stored in an array list by passing the LoadUsers method
	 * Sets user person details from read information
	 * 
	 * @param filename file to read the information from
	 * @param userType given when reading file
	 * @return user 
	 */
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
	
	/**
	 * Writes information from Applicant/Officer/Manager list back into their original list that it was read from
	 * Checks file by having the file paths 
	 * 
	 * @param UserList list of all users
	 */
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
	
	/**
	 * Allows user to view their own statuses
	 */
	public void viewOwnStatus() {
	    System.out.println("User ID: " + getNRIC());
	    System.out.println("Name: " + getName());
	    System.out.println("MaritalStatus: " + getMaritalStatus());
	    // Add other base fields if any
	}

	/**
	 * Getter function for getting name
	 * 
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Getter function for getting NRIC
	 * 
	 * @return NRIC
	 */
	public String getNRIC() {
		return this.NRIC;
	}
	
	/**
	 * Getter function for getting age
	 * 
	 * @return age
	 */
	public int getAge() {
		return this.age;
	}
	
	/**
	 * Getter function for getting marital status
	 * 
	 * @return marital status
	 */
	public String getMaritalStatus() {
		return this.maritalStatus;
	}
	
	/**
	 * Getter function for getting password
	 * 
	 * @return password of user
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Getter function for getting user type of user (Applicant/Officer/Manager)
	 * 
	 * @return String of user type assigned to each person
	 */
	public String getUserType() {
		return this.userType;
	}
	
	/**
	 * Setter function for setting user type
	 * 
	 * @param userType of user
	 */
	public void setUserType(String userType) {
	    this.userType = userType;
	}

	/**
	 * Setter function for setting updated password
	 * 
	 * @param newPassword of user
	 */
	public void changePassword(String newPassword) {
		this.password = newPassword;
	}
	
	
}
