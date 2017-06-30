package Model;

import java.util.List;

/**
 * Created by Андрей on 28.06.2017.
 */



public class Customer {
    private int id;
    private String name;
    private List<Project> projects;

    public Customer() {
    }

    public Customer(int id, String name, List<Project> projects) {
        this.id = id;
        this.name = name;
        this.projects = projects;
    }

    public Customer(String name, List<Project> projects) {
        this.name = name;
        this.projects = projects;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
