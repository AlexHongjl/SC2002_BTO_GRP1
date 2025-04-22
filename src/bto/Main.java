package bto;

import java.util.ArrayList;
import java.util.Scanner;
import bto.model.*;
import bto.service.LoginService;
import bto.Menu.*;

public class Main {
    public static void main(String[] args) {
    	
        ArrayList<UserPerson> Users = new ArrayList<>();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveAll(Users);
        }));
        UserPerson.LoadUsers(Users);
        OfficerRegistration.loadRegistrationsFromCSV("data/Registrations.csv", Users);
        Project.loadProjectsFromCSV(Users);
        BTOapplication.loadApplicationsFromCSV("data/Applications.csv", Users);
        Enquiry.loadEnquiriesFromCSV("data/Enquiries.csv");
        
        int logout;
        do {
            Scanner sc = new Scanner(System.in);
            UserPerson cur = LoginService.login(Users, sc);

            System.out.println("Welcome <" + cur.getUserType() + "> " + cur.getName() + "!\n");

            logout = 0;
            switch (cur.getUserType()) {
                case "manager" -> ManagerMenu.handleManager((HDBmanager) cur, sc, Users);
                case "officer" -> OfficerMenu.handleOfficer((HDBofficer) cur, sc, Users);
                case "applicant" -> ApplicantMenu.handleApplicant((Applicant) cur, sc, Users);
                default -> System.out.println("Invalid user.");
            }


            if (cur.getUserType().equals("manager") || cur.getUserType().equals("officer") || cur.getUserType().equals("applicant")) {
                logout = 1;
            }
        } while (logout == 1);
    }
    
    public static void saveAll(ArrayList<UserPerson> Users) {
        UserPerson.writeBackToCSV(Users);
        Project.writeCSVProjects();
        BTOapplication.saveApplicationsToCSV("data/Applications.csv");
        OfficerRegistration.saveRegistrationsToCSV("data/Registrations.csv");
        Enquiry.saveEnquiriesToCSV("data/Enquiries.csv");
    }
}
