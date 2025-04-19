package bto.Menu;

import bto.model.*;
import bto.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ManagerMenu {
    public static void handleManager(HDBmanager m, Scanner sc) {
        int selection = 0;
        String saved_filter = null;

        do {
            System.out.println("1. View All BTO projects listing");
            System.out.println("2. Toggle visibility");
            System.out.println("3. Approve BTO application/withdrawal by applicant");
            System.out.println("4. Approve officer application by officer");
            System.out.println("5. Add BTO listing");
            System.out.println("6. Edit BTO listing");
            System.out.println("7. Delete BTO listing");
            System.out.println("8. Logout");
            System.out.println("9. Change password");
            System.out.println("10. Filter by area/flat type availability/price/date");
            System.out.println("11. End program");
            System.out.println("12. Reply enquiries");
            System.out.println("13. Generate report");
            System.out.println("14. View own profile");

            selection = sc.nextInt();
            sc.nextLine(); // consume newline
            switch (selection) {
                case 1 -> Project.displayAllProjects(saved_filter);
                case 2 -> {
                    System.out.println("Input project ID:");
                    int id = sc.nextInt();
                    System.out.println("Set visibility (1 = visible, 0 = hidden):");
                    boolean vis = sc.nextInt() == 1;
                    m.toggleProjectVisibility(id, vis);
                }
                case 3 -> {
                    BTOapplication.displayStatusPendingA();
                    BTOapplication.displayStatusPendingW();
                    System.out.println("Input project ID:");
                    int pid = sc.nextInt(); sc.nextLine();
                    System.out.println("Input applicant NRIC:");
                    String nric = sc.nextLine();
                    BTOapplication app = BTOapplication.getApplicationByUserId(nric);
                    if (app == null || Project.getProjectById(pid) == null) {
                        System.out.println("Invalid application or project.");
                        break;
                    }
                    app.display();
                    System.out.println("Approve (1) or Reject (0):");
                    boolean approve = sc.nextInt() == 1;
                    if (app.getStatus().equalsIgnoreCase("Pending Approval")) {
                        if (approve) m.approveBTOApplication(pid, nric);
                        else m.rejectBTOApplication(pid, nric);
                    } else if (app.getStatus().equalsIgnoreCase("Pending Withdrawal")) {
                        if (approve) m.approveWithdrawalRequest(pid, nric);
                        else m.rejectWithdrawalRequest(pid, nric);
                    }
                }
                case 4 -> {
                    OfficerRegistration.displayAll("Pending approval");
                    System.out.println("Input project ID:");
                    int pid = sc.nextInt(); sc.nextLine();
                    System.out.println("Input officer NRIC:");
                    String onric = sc.nextLine();
                    OfficerRegistration reg = Project.getOfficerRegistrationByID(onric);
                    reg.display();
                    System.out.println("Approve (1) or Reject (0):");
                    boolean b = sc.nextInt() == 1;
                    if (b) m.approveOfficerRegistration(pid, onric);
                    else m.rejectOfficerRegistration(pid, onric);
                }
                case 5 -> {
                    System.out.print("Enter project name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter neighbourhood: ");
                    String hood = sc.nextLine();
                    System.out.print("Enter two-room count: ");
                    int t2c = sc.nextInt();
                    System.out.print("Enter selling price for two-room: ");
                    int t2px = sc.nextInt();
                    System.out.print("Enter three-room count: ");
                    int t3c = sc.nextInt();
                    System.out.print("Enter selling price for three-room: ");
                    int t3px = sc.nextInt();
                    System.out.print("Enter visibility (1 = true, 0 = false): ");
                    boolean vis = sc.nextInt() == 1;
                    sc.nextLine();
                    System.out.print("Enter opening date (yyyy-MM-dd): ");
                    LocalDate od = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    System.out.print("Enter closing date (yyyy-MM-dd): ");
                    LocalDate cd = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    System.out.print("Enter officer slots (space separated): ");
                    int slots = sc.nextLine().split(" ").length;

                    m.createBTOListings(name, hood, t2c, t2px, t3c, t3px, vis, od, cd, slots);
                }
                case 6 -> {
                    Project.displayAllSummary();
                    System.out.println("Enter edit command in format: <ProjectID>,<Field>,<NewValue>");
                    try {
                        String[] parts = sc.nextLine().split(",", 3);
                        int pid = Integer.parseInt(parts[0].trim());
                        String field = parts[1].trim().toLowerCase();
                        String value = parts[2].trim();

                        Object newValue = switch (field) {
                            case "projectname", "neighborhood" -> value;
                            case "tworoomcount", "threeroomcount", "officerslots" -> Integer.parseInt(value);
                            case "visiblity" -> Boolean.parseBoolean(value);
                            case "openingdate", "closingdate" -> LocalDate.parse(value);
                            default -> {
                                System.out.println("Unsupported field."); yield null;
                            }
                        };
                        if (newValue != null) m.editBTOListings(pid, field, newValue);
                    } catch (Exception e) {
                        System.out.println("Invalid format.");
                    }
                }
                case 7 -> {
                    System.out.println("Input project ID to delete:");
                    m.deleteBTOListings(sc.nextInt());
                }
                case 8 -> {
                    System.out.println("Logging out...");
                    return;
                }
                case 9 -> {
                    changePW.changePassword(m, sc);
                }
                case 10 -> {
                    System.out.println("1. projectid\n2. projectname\n3. neighborhood\n4. tworoomcount\n5. threeroomcount\n6. projectvisibility\n7. openingdate\n8. closingdate\n9. officerslots");
                    int ftsel = sc.nextInt(); sc.nextLine();
                    String ft = switch (ftsel) {
                        case 1 -> "projectid"; case 2 -> "projectname"; case 3 -> "neighborhood";
                        case 4 -> "tworoomcount"; case 5 -> "threeroomcount"; case 6 -> "projectvisibility";
                        case 7 -> "openingdate"; case 8 -> "closingdate"; case 9 -> "officerslots";
                        default -> null;
                    };
                    if (ft != null) Project.displayAllProjects(ft);
                }
                case 11 -> {
                    System.out.println("Ending program.");
                    sc.close();
                    System.exit(0);
                }
                case 12 -> {
                    Enquiry.displayAllEnquiries();
                    System.out.println("Input enquiry ID:");
                    int eid = sc.nextInt(); sc.nextLine();
                    if (Enquiry.getByID(eid) != null) {
                        Enquiry.getByID(eid).display();
                        System.out.println("Input reply:");
                        Enquiry.getByID(eid).reply(sc.nextLine());
                    } else System.out.println("Invalid ID.");
                }
                case 13 -> {
                    System.out.println("Input project ID:");
                    int pid = sc.nextInt(); sc.nextLine();
                    System.out.println("Filter by (married, single, 2-room, 3-room, all):");
                    m.generateReport(pid, sc.nextLine());
                }
                case 14 -> m.viewOwnStatus();
                default -> System.out.println("Invalid choice.");
            }
        } while (selection != 8 && selection != 11);
    }
}
