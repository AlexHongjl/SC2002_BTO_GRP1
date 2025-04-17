package bto.service;

import java.io.Console;
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

            String password = readPasswordWithAsterisks("Enter Password: ");

            for (UserPerson user : users) {
                if (username.equals(user.getNRIC()) && password.equals(user.getPassword())) {
                    currentUser = user;
                    break;
                }
            }

            if (currentUser == null) {
                System.out.println("Invalid credentials. Please try again.\n");
                System.out.println("Username should be in this format. 'S/T', '7 digit, 'last letter'., 'E.g, S1234567A'\n");
                sc.nextLine();
            }
        } while (currentUser == null);

        return currentUser;
    }

    // Reads password with asterisk feedback in console
    public static String readPasswordWithAsterisks(String prompt) {
        System.out.print(prompt);
        StringBuilder password = new StringBuilder();
        try {
            while (true) {
                int ch = System.in.read();
                if (ch == '\n' || ch == '\r') {
                    System.out.println();
                    break;
                } else if (ch == 8 || ch == 127) { // backspace
                    if (password.length() > 0) {
                        password.deleteCharAt(password.length() - 1);
                        System.out.print("\b \b");
                    }
                } else {
                    password.append((char) ch);
                    System.out.print("*");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password.toString();
    }
}

