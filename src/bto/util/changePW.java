package bto.util;

import java.util.Scanner;
import bto.model.UserPerson;

/**
 * Allows any user of any user type to change their password
 */
public class changePW {
	/**
	   * Takes in the user that wants to change the password and the new password to be changed to from user input
	   * User will input new password and new password will be updated accordingly
	   * 
	   * @param user user to have password changed
	   * @param sc scanner input
	   */
    public static void changePassword(UserPerson user, Scanner sc) {
        System.out.print("Enter new password: ");
        String pw1 = sc.nextLine();
        System.out.print("Confirm new password: ");
        String pw2 = sc.nextLine();

        if (pw1.equals(pw2)) {
            user.changePassword(pw1);
            System.out.println("Changed Password Successfully");
        } else {
            System.out.println("Password mismatch.");
        }
    }

}