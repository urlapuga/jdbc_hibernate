package Model;

import java.util.List;

/**
 * Created by Андрей on 28.06.2017.
 */
public class Project {
    private int id;

    public int getId() {
        return id;
    }


    private String name;
    private int cost;
    private List<Developer> developers;


    public Project() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(List<Developer> developers) {
        this.developers = developers;
    }

    public Project(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public Project(int id, String name, int cost, List<Developer> developers) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.developers = developers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
