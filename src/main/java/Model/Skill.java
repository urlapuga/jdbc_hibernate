package Model;

/**
 * Created by Андрей on 25.06.2017.
 */
public class Skill {
    private int id;
    private String name;


    public Skill() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public Skill(String name) {
        this.name = name;
    }

    public Skill(int id, String name) {
        this.id = id;
        this.name = name;
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
}
