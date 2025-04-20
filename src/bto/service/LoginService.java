package bto.service;

import java.util.ArrayList;
import java.util.Scanner;
import bto.model.UserPerson;

public class LoginService {

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
