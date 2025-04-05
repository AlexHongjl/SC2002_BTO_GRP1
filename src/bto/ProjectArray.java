package bto;

import java.util.ArrayList;
import java.util.List;

public class ProjectArray {
    private List<Project> projects;

    public ProjectArray() {
        this.projects = new ArrayList<>();
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public boolean removeProjectById(int projectId) {
        return projects.removeIf(p -> p.getProjectId() == projectId);
    }

    public Project getProjectById(int projectId) {
        for (Project p : projects) {
            if (p.getProjectId() == projectId) {
                return p;
            }
        }
        return null;
    }

    public void displayAllProjects() {
        for (Project p : projects) {
            System.out.println("Project ID: " + p.getProjectId() + ", Name: " + p.getProjectName());
        }
    }

    
}

