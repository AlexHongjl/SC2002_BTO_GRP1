package bto;

import java.io.*;
import java.util.*;

public class User {
	
	public static List<UserPerson> allUsers = new ArrayList<>();

	public static void  LoadUsers() {
		String[] files = { "OfficerList.csv", "ManagerList.csv", "ApplicantList.csv" };
		String[] roles = { "officer", "manager", "applicant" };

		for (int i = 0; i < files.length; i++) {
			List<UserPerson> userList = readUsersFromFile(files[i], roles[i]);
			allUsers.addAll(userList);
		}

		// Test output
		for (UserPerson user : allUsers) {
			System.out.println(user.getName() + " | " + user.getNRIC() + " | " + user.getUserType());
		}
	}

	public static List<UserPerson> readUsersFromFile(String filename, String userType) {
		List<UserPerson> users = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			boolean isFirstLine = true;

			while ((line = br.readLine()) != null) {
				if (isFirstLine) {
					isFirstLine = false;
					continue;
				}
				String[] parts = line.split(",");

				if (parts.length < 5) continue; // skip malformed lines

				String name = parts[0].trim(); // trim removes leading and trailing white spaces
				String NRIC = parts[1].trim();
				int age = Integer.parseInt(parts[2].trim());
				String maritalStatus = parts[3].trim();
				String password = parts[4].trim();

				UserPerson user = new UserPerson(name, NRIC, age, maritalStatus, password, userType);
				users.add(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return users;
	}
	
	public boolean login(String userNRIC, String password) {
		boolean grantAccess = false;
		for (UserPerson user : allUsers) {
			if (user.getNRIC().equals(userNRIC) && user.getPassword().equals(password)) {
				grantAccess = true;
				return grantAccess;
			}
		}
		System.out.println("Wrong username or password");
		grantAccess = false;
		return grantAccess;
	}
	
	public void changePassword(String userNRIC, String passwordToChange) {
		for (UserPerson user : allUsers) {
			if (user.getNRIC().equals(userNRIC)) {
				user.changePassword(passwordToChange);
				System.out.println("Password changed!");
			}
		}
	}
}



//	private String userID;
//	private String Password;
//	public String Usertype;
//	public String Filter;
//	
//	public User(String userID, String Password) {
//        this.userID = userID;
//        this.Password = Password;
//	}
//	
//	
//	
//	
//	boolean login() {
//		while(false) {
//			if(userID.equals() && Password.equals("password")) {
//				return true;
//			}else {
//				//prompt failure then again
//			}
//		}
//	}
//	
	
}
