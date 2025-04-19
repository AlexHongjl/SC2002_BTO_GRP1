package bto.util;

import java.util.Scanner;

import bto.model.HDBmanager;
import bto.model.UserPerson;

public class changePW {
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