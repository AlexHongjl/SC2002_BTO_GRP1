package bto;
import java.util.stream.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//implement hdb manager functionality
public class HDBmanager extends User {
    private List<Project> managedProjects; // Only tracks projects this manager owns uses all project list under project to access all projects

    public HDBmanager(String name, String nric, String password) {
        super(name, nric, password);
        this.managedProjects = new ArrayList<>();
    }

    // === Project Management ===
    public Project createBTOListings(int projectID , String projectName, String neighborhood,
                                    int twoRoomCount,
                                    int threeRoomCount, boolean projectVisibility, 
                                   LocalDate openingDate, LocalDate closingDate,
                                   int officerSlots) {
        if (isManagingProjectDuringPeriod(openingDate, closingDate)) {
            System.out.println("Error: You are already managing a project during this period.");
            return null;
        }

        Project newProject = new Project(projectID , projectName, neighborhood,
                twoRoomCount,
                threeRoomCount, projectVisibility, 
               openingDate, closingDate,
               officerSlots);
        managedProjects.add(newProject);
        return newProject;
    }

    public void editBTOListings(int projectID, String field, Object newValue) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission to edit it.");
            return;
        }

        try {
            switch (field.toLowerCase()) {
                case "projectname":
                    project.setProjectName((String) newValue);
                    break;
                case "neighborhood":
                    project.setNeighbourhood((String) newValue);
                    break;
                case "twoRoomCount":
                    int twoRoomCount = (Integer) newValue;
                    if (twoRoomCount >= 0) {
                        project.setTwoRoomCount(twoRoomCount);
                    } else {
                        System.out.println("Error: Room count cannot be negative.");
                    }
                    break;
                case "threeRoomCount":
                    int threeRoomCount = (Integer) newValue;
                    if (threeRoomCount >= 0) {
                        project.setThreeRoomCount(threeRoomCount);
                    } else {
                        System.out.println("Error: Room count cannot be negative.");
                    }
                    break;
                case "visiblity":
                    project.setProjectVisibility((Boolean) newValue);
                    break;
                case "openingdate":
                    LocalDate newOpening = (LocalDate) newValue;
                    if (newOpening.isAfter(project.getClosingDate())) {
                        System.out.println("Error: Opening date must be before closing date.");
                    } else if (isManagingProjectDuringPeriod(newOpening, project.getClosingDate())) {
                        System.out.println("Error: You are already managing another project during this period.");
                    } else {
                        project.setOpeningDate(newOpening);
                    }
                    break;
                case "closingdate":
                    LocalDate newClosing = (LocalDate) newValue;
                    if (newClosing.isBefore(project.getOpeningDate())) {
                        System.out.println("Error: Closing date must be after opening date.");
                    } else if (isManagingProjectDuringPeriod(project.getOpeningDate(), newClosing)) {
                        System.out.println("Error: You are already managing another project during this period.");
                    } else {
                        project.setClosingDate(newClosing);
                    }
                    break;
                case "officerslots":
                    int slots = (Integer) newValue;
                    if (slots >= project.getOfficers().size()) {
                        project.setOfficerSlots(slots);
                    } else {
                        System.out.println("Error: Cannot set slots below current number of registered officers.");
                    }
                    break;
                default:
                    System.out.println("Error: Invalid field specified.");
            }
        } catch (ClassCastException e) {
            System.out.println("Error: Invalid value type for field " + field);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteBTOListings(int projectID) {
        // Find project in both system and manager's list
        Project project = Project.getProjectById(projectID);
        
        if (project == null) {
            System.out.println("Error: Project not found.");
            return;
        }
        
        if (!managedProjects.contains(project)) {
            System.out.println("Error: You don't have permission to delete this project.");
            return;
        }
        
        // Remove from both collections
        boolean removedFromSystem = Project.removeProjectById(projectID);  //need project to have
        boolean removedFromManaged = managedProjects.remove(project);
        
        if (removedFromSystem && removedFromManaged) {
            System.out.println("Project deleted successfully from all records.");
        } else {
            System.out.println("Error: Project could not be fully deleted.");
        }
    }

    public void toggleProjectVisibility(int projectID, boolean visible) {
        Project project = Project.getProjectById(projectID);
        if (project != null && managedProjects.contains(project)) {
            project.setProjectVisibility(visible);
            System.out.println("Project visibility set to: " + visible);
        } else {
            System.out.println("Error: Project not found or you don't have permission to modify it.");
        }
    }

    // === Officer Registration ===
    public List<OfficerRegistration> viewHDBOfficerReg(String USERID) {
        List<OfficerRegistration> result = new ArrayList<>();
        for (Project project : managedProjects) {
            for (OfficerRegistration reg : project.getOfficerRegistrations()) {
                if (reg.getOfficer().getNric().equals(USERID)) {
                    result.add(reg);
                }
            }
        }
        return result;
    }

    public void approveHDBOfficerReg(String USERID) {
        for (Project project : managedProjects) {
            for (OfficerRegistration reg : project.getOfficerRegistrations()) {
                if (reg.getOfficer().getNric().equals(USERID) && reg.getStatus().equals("Pending")) {
                    if (project.getOfficers().size() < project.getOfficerSlots()) {
                        reg.setStatus("Approved");
                        project.addOfficer(USERID);
                        System.out.println("Officer registration approved for project: " + project.getProjectName());
                    } else {
                        System.out.println("Error: No available slots in project: " + project.getProjectName());
                    }
                    return;
                }
            }
        }
    }

    // === Application Management ===
    public void approveOfficerRegistration(int projectID) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (BTOapplication app : project.getApplications()) {
            if (app.getStatus().equals("Pending")) {
                String flatType = app.getFlatType();
                if (flatType.equals(project.getType1()) && project.getUnitsType1() > 0) {
                    app.setStatus("Successful");
                    project.setUnitsType1(project.getUnitsType1() - 1);
                    System.out.println("Application approved.");
                } else if (flatType.equals(project.getType2()) && project.getUnitsType2() > 0) {
                    app.setStatus("Successful");
                    project.setUnitsType2(project.getUnitsType2() - 1);
                    System.out.println("Application approved.");
                } else {
                    System.out.println("Error: No available units of the requested type.");
                }
                return;
            }
        }
    }

    public void approveBTOwithdrawal(int projectID) {
        Project project = Project.getProjectById(projectID);
        if (project == null || !managedProjects.contains(project)) {
            System.out.println("Error: Project not found or you don't have permission.");
            return;
        }

        for (Application app : project.getApplications()) {
            if (app.getStatus().equals("Withdrawal Requested")) {
                app.setStatus("Withdrawn");
                if (app.getStatus().equals("Booked")) {
                    String flatType = app.getFlatType();
                    if (flatType.equals(project.getType1())) {
                        project.setUnitsType1(project.getUnitsType1() + 1);
                    } else if (flatType.equals(project.getType2())) {
                        project.setUnitsType2(project.getUnitsType2() + 1);
                    }
                }
                System.out.println("Withdrawal approved.");
                return;
            }
        }
    }

    // === Reports & Views (Unimplemented Filters) ===
    public List<BTOapplication> generateReport(String filter) {
        return managedProjects.stream()
            .flatMap(project -> project.getApplications().stream())
            .filter(app -> "booked".equalsIgnoreCase(app.getStatus()))
            .filter(app -> {
                Applicant applicant = app.getApplicant();
                return switch (filter.toLowerCase()) {
                    case "married" -> applicant.isMarried();
                    case "single" -> !applicant.isMarried();
                    case "2-room", "3-room" -> app.getFlatType().equalsIgnoreCase(filter);
                    case "all" -> true;
                    default -> false;
                };
            })
            .toList();
    }
    public List<Project> viewAllProjects(String field) {
        if (field == null || field.isEmpty()) {
            // Default to sorting by project name alphabetically if no field is given.
            return Project.getProjectList().stream()
                .sorted(Comparator.comparing(Project::getProjectName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
        }

        return Project.getProjectList().stream()
            .sorted((p1, p2) -> {
                switch (field.toLowerCase()) {
                    case "projectid":
                        return Integer.compare(p1.getProjectID(), p2.getProjectID());
                    case "projectname":
                        return p1.getProjectName().compareToIgnoreCase(p2.getProjectName());
                    case "neighborhood":
                        return p1.getNeighbourhood().compareToIgnoreCase(p2.getNeighbourhood());
                    case "tworoomcount":
                        return Integer.compare(p1.getTwoRoomCount(), p2.getTwoRoomCount());
                    case "threeroomcount":
                        return Integer.compare(p1.getThreeRoomCount(), p2.getThreeRoomCount());
                    case "projectvisibility":
                        return Boolean.compare(p1.isProjectVisibility(), p2.isProjectVisibility());
                    case "openingdate":
                        return p1.getOpeningDate().compareTo(p2.getOpeningDate());
                    case "closingdate":
                        return p1.getClosingDate().compareTo(p2.getClosingDate());
                    case "officerslots":
                        return Integer.compare(p1.getOfficerSlots(), p2.getOfficerSlots());
                    default:
                        System.out.println("Unknown sorting field: " + field);
                        return 0;
                }
            })
            .collect(Collectors.toList());
    }
    // === Helper Methods ===
    private Project ProjectArray.getProjectById(int projectID) {
        for (Project project : Project.getProjectList()) { // Access all projects via Project class
            if (project.getProjectID() == projectID) {
                return project;
            }
        }
        return null;
    }

    private boolean isManagingProjectDuringPeriod(LocalDate newOpening, LocalDate newClosing) {
        for (Project project : managedProjects) {
            if (datesOverlap(project.getOpeningDate(), project.getClosingDate(), newOpening, newClosing)) {
                return true;
            }
        }
        return false;
    }

    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}