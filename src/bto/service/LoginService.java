package bto.service;

import java.util.ArrayList;
import java.util.Scanner;
import bto.model.UserPerson;

/**
 * Login service to allow users to login to access the program functions
 * Generates a login interface that prompts user to enter their username and password
 * username would be their NRIC and default password is password
 */
public class LoginService {
	
	/**
	   * Prompts users to enter NRIC and password
	   * Users who log in will have their username and password checked against stored previous usernames and password
	   * If username or password does not match existing users, display error message for invalid credentials and prompt again
	   * 
	   * @param users list of existing users
	   * @param sc scanner input
	   * @return UserPerson object if it exists
	   */
    public static UserPerson login(ArrayList<UserPerson> users, Scanner sc) {
        UserPerson currentUser = null;

        do {
            System.out.println("\n===== BTO System Login Page =====\n");
            System.out.print("Enter Username (NRIC): ");
            String username = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            for (UserPerson user : users) {
                if (username.equals(user.getNRIC()) && password.equals(user.getPassword())) {
                    currentUser = user;
                    break;
                }
            }

            if (currentUser == null) {
                System.out.println("Invalid credentials. Please try again.\n");
                System.out.println("Username should be in this format. 'S/T', '7 digit, 'last letter'., 'E.g, S1234567A'\n");
            }
        } while (currentUser == null);

        return currentUser;
    }
}
