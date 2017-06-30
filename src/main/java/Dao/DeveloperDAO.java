package Dao;

import Model.Developer;
import Model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 26.06.2017.
 */
public class DeveloperDAO implements DAO<Developer> {

    static {
        try {
            Class.forName(P.jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    SkillDAO skillDAO = new SkillDAO();

    public List<Developer> getByProjectId(int projectId) {
        Statement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT\n" +
                    "developers.id,\n" +
                    "developers.`name`,\n" +
                    "developers.salary\n" +
                    "FROM\n" +
                    "projects_has_developers\n" +
                    "INNER JOIN developers ON developers.id = projects_has_developers.developers_id\n" +
                    "WHERE\n" +
                    "projects_has_developers.projects_id = " + projectId);

            return generateDevelopersList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Developer> getAll() {
        Statement stmt = null;

        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from developers");
            return generateDevelopersList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<Developer> generateDevelopersList(ResultSet rs) {
        List<Developer> developers = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                List<Skill> skills = skillDAO.getSkillsByDeveloperId(id);
                Developer developer = new Developer(id, rs.getInt("salary"), rs.getString("name"), skills);
                developers.add(developer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return developers;
    }

    @Override
    public boolean create(Developer developer) {
        PreparedStatement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("INSERT INTO developers (name,salary) VALUES (?,?)");
            stmt.setString(1, developer.getName());
            stmt.setInt(2, developer.getSalary());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Developer read(int id) {
        Statement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from developers where id = " + id);
            while (rs.next()) {
                Developer developer = new Developer(id, rs.getInt("salary"), rs.getString("name"), skillDAO.getSkillsByDeveloperId(id));
                return developer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Developer developer) {
        if (developer.getId() == 0) return false;
        skillDAO.updateDeveloperSkills(developer);
        PreparedStatement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("UPDATE developers SET name = ?,salary = ? where id = " + developer.getId());
            stmt.setString(1, developer.getName());
            stmt.setInt(2, developer.getSalary());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        Statement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE from developers where id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }
}
