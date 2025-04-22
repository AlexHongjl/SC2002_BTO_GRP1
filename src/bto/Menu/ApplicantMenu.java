package bto.Menu;

import bto.Main;
import bto.model.*;
import bto.util.changePW;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Generates a list of actions available to Applicants
 * Depending on the user's selection it will invoke different actions
 */
public class ApplicantMenu {
	/**
	   * Applicants are able to view BTO projects using filters
	   * Apply or withdraw their BTO application
	   * Create/Edit/View enquiries
	   * View their own profile, which will be updated when they apply for BTO projects
	   * Change password and Logout functions
	   * 
	   * @param a an applicant
	   * @param sc scanner input
	   * @param Users list of users
	   */
    public static void handleApplicant(Applicant a, Scanner sc,  ArrayList<UserPerson> Users) {
        int selection;
        String saved_filter = null;

        do {
        	System.out.println(); 
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
            System.out.println("10. Sort by area/flat type availability/px/date");
            System.out.println("11. End program");
            System.out.println("12. View own profile");
            System.out.println("13. Filter projects");

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
                    Project.displayAllProjectsApplicant(saved_filter, a);
                    try {
                        System.out.print("Enter project ID: ");
                        int pid = sc.nextInt(); sc.nextLine();
                        Project p = Project.getProjectById(pid);
                        if (p == null) {
                            System.out.println("Invalid project ID.");
                            break;
                        }
                        System.out.println("1. 2-Room: " + p.getTwoRoomCount());
                        System.out.println("2. 3-Room: " + p.getThreeRoomCount());
                        int type = sc.nextInt(); sc.nextLine();
                        String unit = (type == 1) ? "2-Room" : "3-Room";

                        if (a.isEligible(p, unit) && a.applyForProject(p, unit)) {
                            BTOapplication app = new BTOapplication(a.getNRIC(), pid, a);
                            app.updateStatus("Pending Approval", unit);
                            BTOapplication.addApplication(app);
                            app.display();
                        } else {
                            System.out.println("Ineligible or already applied.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Application cancelled.");
                        sc.nextLine();
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
                	System.out.print("Enter project ID for the project you have enquiry on: ");
                	int projectid = sc.nextInt(); sc.nextLine();
                	if(Project.getProjectById(projectid)==null) {
                		System.out.print("invalid project ID ");
                		break;
                	}
                    System.out.print("Enter enquiry message: ");
                    Enquiry.addEnquiry(new Enquiry(Enquiry.getCount(), a.getName(), sc.nextLine(),projectid));
                }
                case 6 -> {
                    if (Enquiry.displayByUser(a.getName())) {
                        try {
                            System.out.print("Enter enquiry ID to edit: ");
                            int id = sc.nextInt(); sc.nextLine();
                            if (Enquiry.getByID(id) != null && Enquiry.getByID(id).getApplicantName().equals(a.getName())) {
                                System.out.println("Current:");
                                Enquiry.getByID(id).display();
                                System.out.print("New message: ");
                                Enquiry.editEnquiry(id, sc.nextLine());
                            } else {
                                System.out.println("Invalid enquiry ID.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input.");
                            sc.nextLine();
                        }
                    }
                }
                case 7 -> {
                    if (Enquiry.displayByUser(a.getName())) {
                        try {
                            System.out.print("Enter enquiry ID to delete: ");
                            int id = sc.nextInt(); sc.nextLine();
                            if (Enquiry.getByID(id) != null && Enquiry.getByID(id).getApplicantName().equals(a.getName())) {
                                Enquiry.getByID(id).display();
                                Enquiry.deleteEnquiry(id);
                            } else {
                                System.out.println("Invalid enquiry ID.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input.");
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
                	changePW.changePassword(a, sc);
                }
                case 10 -> {
                    System.out.println("1. projectid\n2. projectname\n3. neighborhood\n4. tworoomcount\n5. threeroomcount\n7. openingdate\n8. closingdate\n9. officerslots");
                    try {
                        int ftsel = sc.nextInt(); sc.nextLine();
                        String ft = switch (ftsel) {
                            case 1 -> "projectid"; case 2 -> "projectname"; case 3 -> "neighborhood";
                            case 4 -> "tworoomcount"; case 5 -> "threeroomcount"; case 7 -> "openingdate";
                            case 8 -> "closingdate"; case 9 -> "officerslots"; default -> null;
                        };
                        if (ft != null) {
                            saved_filter = ft;
                            Project.displayAllProjects(ft);
                        } else {
                            System.out.println("Invalid filter selection.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                        sc.nextLine();
                    }
                }
                case 11 -> {
                    System.out.println("Ending...");
                    System.exit(0);
                }
                case 12 -> a.viewOwnStatus();
                case 13 -> {
                    System.out.println("Filter BTO Projects");
                    System.out.println("Select filter field:");
                    System.out.println("1. Project ID");
                    System.out.println("2. Project Name");
                    System.out.println("3. Neighborhood");
                    System.out.println("4. Two-Room Count");
                    System.out.println("5. Two-Room Price");
                    System.out.println("6. Three-Room Count");
                    System.out.println("7. Three-Room Price");
                    System.out.println("8. Opening Date");
                    System.out.println("9. Closing Date");
              
                    
                    try {
                        int fieldChoice = sc.nextInt();
                        sc.nextLine(); // consume newline
                        
             
                        String field = switch (fieldChoice) {
                            case 1 -> "projectid";
                            case 2 -> "projectname";
                            case 3 -> "neighborhood";
                            case 4 -> "tworoomcount";
                            case 5 -> "tworoompx";
                            case 6 -> "threeroomcount";
                            case 7 -> "threeroompx";
                            case 8 -> "openingdate";
                            case 9 -> "closingdate";
                            default -> null;
                        };
                        
                        if (field != null) {
                            System.out.println("Enter filter value:");
                            String value = sc.nextLine();
                            Project.filterProjectsApplicant(field, value, a);
                            saved_filter = field; // Save last used filter
                        } else {
                            System.out.println("Invalid field selection.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number.");
                        sc.nextLine(); // Clear buffer
                    }
                }
                default -> System.out.println("Invalid.");
            }

        } while (selection != 8 && selection != 11);
    }
}
