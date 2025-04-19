package bto.Menu;

import bto.model.*;
import bto.util.changePW;

import java.time.LocalDate;
import java.util.Scanner;

public class ApplicantMenu {
    public static void handleApplicant(Applicant a, Scanner sc) {
        int selection;
        String saved_filter = null;

        do {
            System.out.println("List of actions:");
            System.out.println("1. View BTO project/or only the project you applied for");
            System.out.println("2. Apply for BTO project");
            System.out.println("3. Withdraw BTO project");
            System.out.println("4. View enquiries");
            System.out.println("5. Add enquiries");
            System.out.println("6. Edit enquiries");
            System.out.println("7. Delete enquiries");
            System.out.println("8. Logout");
            System.out.println("9. Change password");
            System.out.println("10. Filter by area/flat type availability/px/date");
            System.out.println("11. End program");
            System.out.println("12. View own profile");

            selection = sc.nextInt();
            sc.nextLine();

            switch (selection) {
                case 1 -> {
                    if (!a.getApplicationStatus().equals("None") && !a.getApplicationStatus().equals("Unsuccessful"))
                        Project.display(a.getAppliedProjectId());
                    else
                        Project.displayAllProjectsApplicant(saved_filter, a);
                }
                case 2 -> {
                    System.out.print("Enter project ID: ");
                    int pid = sc.nextInt();
                    Project p = Project.getProjectById(pid);
                    System.out.println("1. 2-Room: " + p.getTwoRoomCount());
                    System.out.println("2. 3-Room: " + p.getThreeRoomCount());
                    int type = sc.nextInt();
                    String unit = (type == 1) ? "2-Room" : "3-Room";

                    if (a.isEligible(p, unit) && a.applyForProject(p, unit)) {
                        BTOapplication app = new BTOapplication(a.getNRIC(), pid, a);
                        app.updateStatus("Pending Approval", unit);
                        BTOapplication.addApplication(app);
                        app.display();
                    } else {
                        System.out.println("Ineligible or already applied.");
                    }
                }
                case 3 -> {
                    if (!a.getApplicationStatus().equals("None") && !a.getApplicationStatus().equals("Unsuccessful")) {
                        a.withdrawApplication();
                        BTOapplication app = BTOapplication.getApplicationByUserId(a.getNRIC());
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
                    Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), a.getName(), sc.nextLine()));
                }
                case 6 -> {
                    if (Enquiry.displayByUser(a.getName())) {
                        System.out.print("Enter enquiry ID to edit: ");
                        int id = sc.nextInt(); sc.nextLine();
                        if (Enquiry.getByID(id) != null && Enquiry.getByID(id).getApplicantName().equals(a.getName())) {
                            System.out.println("Current:");
                            Enquiry.getByID(id).display();
                            System.out.print("New message: ");
                            Enquiry.editEnquiry(id, sc.nextLine());
                        }
                    }
                }
                case 7 -> {
                    if (Enquiry.displayByUser(a.getName())) {
                        System.out.print("Enter enquiry ID to delete: ");
                        int id = sc.nextInt();
                        if (Enquiry.getByID(id) != null && Enquiry.getByID(id).getApplicantName().equals(a.getName())) {
                            Enquiry.getByID(id).display();
                            Enquiry.deleteEnquiry(id);
                        }
                    }
                }
                case 8 -> {
                    System.out.println("Logging out...");
                    return;
                }
                case 9 -> {
                	changePW.changePassword(a, sc);
                }
                case 10 -> {
                    System.out.println("1. projectid\n2. projectname\n3. neighborhood\n4. tworoomcount\n5. threeroomcount\n7. openingdate\n8. closingdate\n9. officerslots");
                    int ftsel = sc.nextInt(); sc.nextLine();
                    String ft = switch (ftsel) {
                        case 1 -> "projectid"; case 2 -> "projectname"; case 3 -> "neighborhood";
                        case 4 -> "tworoomcount"; case 5 -> "threeroomcount"; case 7 -> "openingdate";
                        case 8 -> "closingdate"; case 9 -> "officerslots"; default -> null;
                    };
                    if (ft != null) {
                        saved_filter = ft;
                        Project.displayAllProjects(ft);
                    }
                }
                case 11 -> {
                    System.out.println("Ending...");
                    System.exit(0);
                }
                case 12 -> a.viewOwnStatus();
                default -> System.out.println("Invalid.");
            }

        } while (selection != 8 && selection != 11);
    }
}
