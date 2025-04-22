package bto.Menu;


import bto.Main;
import bto.model.*;
import bto.util.changePW;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OfficerMenu {
    public static void handleOfficer(HDBofficer o, Scanner sc,  ArrayList<UserPerson> Users) {
        int selection;
        String saved_filter = null;

        do {
        	System.out.println(); 
            System.out.println("1. View BTO project/or only the project you applied for");
            System.out.println("2. Apply for BTO project");
            System.out.println("3. Withdraw BTO project");
            System.out.println("4. View enquiries");
            System.out.println("5. Add enquiries");
            System.out.println("6. Edit enquiries");
            System.out.println("7. Delete enquiries");
            System.out.println("8. Logout");
            System.out.println("9. Change password");
            System.out.println("10. Filter by area/flat type availability/price/date");
            System.out.println("11. End program");
            System.out.println("-------- Officer Functions --------");
            System.out.println("12. Reply enquiries");
            System.out.println("13. Apply for officer of a project");
            System.out.println("14. Book slot for applicant & generate receipt");
            System.out.println("15. View own profile");

            selection = sc.nextInt();
            sc.nextLine(); // clear

            switch (selection) {
                case 1 -> {
                    if (!o.getApplicationStatus().equals("None") && !o.getApplicationStatus().equals("Unsuccessful"))
                        Project.display(o.getAppliedProjectId());
                    else if(o.isOfficerInCharge()) {
                    	Project.display(o.getRegProj().get(0).getProjectId());
                    }
                    else
                        Project.displayAllProjectsApplicant(saved_filter, o);
                }
                case 2 -> {
                    try {
                        System.out.print("Enter project ID: ");
                        int pid = sc.nextInt();
                        Project p = Project.getProjectById(pid);

                        System.out.println("1. 2-Room available: " + p.getTwoRoomCount());
                        System.out.println("2. 3-Room available: " + p.getThreeRoomCount());
                        int type = sc.nextInt(); sc.nextLine();
                        String unit = (type == 1) ? "2-Room" : "3-Room";

                        if (o.isEligible(p, unit) && o.applyForProject(p, unit)) {
                            BTOapplication app = new BTOapplication(o.getNRIC(), pid, o);
                            app.updateStatus("Pending Approval", unit);
                            BTOapplication.addApplication(app);
                            app.display();
                        } else {
                            System.out.println("Ineligible or already applied.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter numbers only.");
                        sc.nextLine();
                    }
                }

                case 3 -> {
                    if (!o.getApplicationStatus().equals("None") && !o.getApplicationStatus().equals("Unsuccessful")) {
                        o.withdrawApplication();
                        BTOapplication app = BTOapplication.getApplicationByUserId(o.getNRIC());
                        app.updateStatus("Pending Withdrawal", app.getUnitType());
                        app.display();
                        System.out.println("Withdrawal request sent.");
                    } else {
                        System.out.println("No application to withdraw.");
                    }
                }
                case 4 -> Enquiry.displayAllEnquiries();
                case 5 -> {
                    System.out.print("Enter enquiry message: ");
                    Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), o.getName(), sc.nextLine()));
                }
                case 6 -> {
                    if (Enquiry.displayByUser(o.getName())) {
                        try {
                            System.out.print("Enter enquiry ID to edit: ");
                            int id = sc.nextInt(); sc.nextLine();
                            if (Enquiry.getByID(id) != null && Enquiry.getByID(id).getApplicantName().equals(o.getName())) {
                                System.out.println("Current:");
                                Enquiry.getByID(id).display();
                                System.out.print("New message: ");
                                Enquiry.editEnquiry(id, sc.nextLine());
                            } else {
                                System.out.println("Enquiry not found or not owned.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            sc.nextLine();
                        }
                    }
                }
                case 7 -> {
                    if (Enquiry.displayByUser(o.getName())) {
                        try {
                            System.out.print("Enter enquiry ID to delete: ");
                            int id = sc.nextInt(); sc.nextLine();
                            if (Enquiry.getByID(id) != null && Enquiry.getByID(id).getApplicantName().equals(o.getName())) {
                                Enquiry.getByID(id).display();
                                Enquiry.deleteEnquiry(id);
                            } else {
                                System.out.println("Enquiry not found or not owned.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a number.");
                            sc.nextLine();
                        }
                    }
                }
                case 8 -> {
                    System.out.println("Logging out...");
                    Main.saveAll(Users);
                    return;
                }
                case 9 -> {
                	changePW.changePassword(o, sc);
                }
                case 10 -> {
                    try {
                        System.out.println("1. projectid\n2. projectname\n3. neighborhood\n4. tworoomcount\n5. threeroomcount\n7. openingdate\n8. closingdate\n9. officerslots");
                        int ftsel = sc.nextInt(); sc.nextLine();
                        String ft = switch (ftsel) {
                            case 1 -> "projectid"; case 2 -> "projectname"; case 3 -> "neighborhood";
                            case 4 -> "tworoomcount"; case 5 -> "threeroomcount"; case 7 -> "openingdate";
                            case 8 -> "closingdate"; case 9 -> "officerslots"; default -> null;
                        };
                        if (ft != null) Project.displayAllProjects(ft);
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid selection. Please enter a number.");
                        sc.nextLine();
                    }
                }
                case 11 -> {
                    System.out.println("Ending...");
                    System.exit(0);
                }
                case 12 -> {
                    Enquiry.displayAllEnquiries();
                    System.out.print("Enquiry ID to reply: ");
                    int eid = sc.nextInt(); sc.nextLine();
                    
                    try {
                        String input = sc.nextLine();
                        eid = Integer.parseInt(input);
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid input. Please enter a numeric enquiry ID.");
                        break;
                    }
                    
                    Enquiry e = Enquiry.getByID(eid);
                    if (e != null) {
                        e.display();
                        System.out.print("Reply: ");
                        e.reply(sc.nextLine());
                    }else {
                    	System.out.println("No enquiry found with ID " + eid + ".");
                    }
                }
                case 13 -> {
                    try {
                        Project.displayAllProjects(saved_filter);
                        System.out.print("Enter project ID: ");
                        int pid = sc.nextInt(); sc.nextLine();
                        OfficerRegistration reg = o.applyOffReg(Project.getProjectById(pid));
                        if (reg != null && reg.getRegistrationStatus().equals("Pending approval")) {
                            System.out.println("Application submitted.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        sc.nextLine();
                    }
                }
                case 14 -> {
                    try {
                        ArrayList<BTOapplication> apps = new ArrayList<>();
                        System.out.println("AVAILABLE BTO APPLICATIONS TO BOOK");
                        for (BTOapplication a : BTOapplication.getAllApplications()) {
                            if (a.getStatus().equalsIgnoreCase("Successful") &&
                                Project.getProjectById(a.getProjectId()).getOfficers().contains(o)) {
                                a.display();
                                apps.add(a);
                            }
                        }
                        if (apps.isEmpty()) {
                            System.out.println("None.");
                            break;
                        }

                        System.out.print("Enter applicant ID: ");
                        String aid = sc.nextLine();
                        BTOapplication selected = BTOapplication.getApplicationByUserId(aid);
                        if (selected == null || !selected.getStatus().equals("Successful")) {
                            System.out.println("Invalid or not bookable.");
                            break;
                        }
                        selected.display();
                        System.out.print("Book unit? (Y/N): ");
                        if (sc.nextLine().trim().equalsIgnoreCase("Y")) {
                            o.bookUnitForApplicant(aid, selected.getUnitType(), Project.getProjectById(selected.getProjectId()));
                            o.generateReceipt(aid);
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input.");
                        sc.nextLine();
                    }
                }
                case 15 -> o.viewOwnStatus();
                default -> System.out.println("Invalid.");
            }
        } while (selection != 8 && selection != 11);
    }
}
