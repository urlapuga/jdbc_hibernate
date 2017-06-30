package Model;

import org.hibernate.annotations.Entity;

import java.util.List;

/**
 * Created by Андрей on 25.06.2017.
 */
@Entity
public class Developer {

    private int id;
    private int salary;
    private String name;
    private List<Skill> sklls;


    public Developer() {
    }

    public Developer(String name,int salary) {
        this.salary = salary;
        this.name = name;
    }

    public Developer(int id, int salary, String name, List<Skill> sklls) {
        this.id = id;
        this.salary = salary;
        this.name = name;
        this.sklls = sklls;
    }

    public Developer(int salary, String name, List<Skill> sklls) {
        this.id = 0;
        this.salary = salary;
        this.name = name;
        this.sklls = sklls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Skill> getSklls() {
        return sklls;
    }

    public void setSklls(List<Skill> sklls) {
        this.sklls = sklls;
    }
}
