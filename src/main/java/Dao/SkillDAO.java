package Dao;

import Model.Developer;
import Model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 28.06.2017.
 */
public class SkillDAO implements DAO<Skill> {

    static {
        try {
            Class.forName(P.jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Skill> getSkillsByDeveloperId(int id){
        Statement stmt = null;
        List<Skill> skills = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT\n" +
                    "skills.id,\n" +
                    "skills.`name`\n" +
                    "FROM\n" +
                    "developers_has_skills\n" +
                    "LEFT JOIN skills ON skills.id = developers_has_skills.skills_id\n" +
                    "WHERE\n" +
                    "developers_has_skills.developers_id = "+id);

            while (rs.next()) {
                Skill skill = new Skill(rs.getInt("id"), rs.getString("name"));
                skills.add(skill);
            }
            return skills;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateDeveloperSkills(Developer developer) {
        deleteByDeveloper(developer.getId());
        insertDeveloperSkills(developer.getId(), developer.getSklls());
    }

    private void deleteByDeveloper(int developerId) {
        Statement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE from developers_has_skills where developer_id = " + developerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDeveloperSkills(int developerId, List<Skill> skills) {
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            conn.setAutoCommit(false);
            String query = "INSERT INTO developers_has_skills (developers_id,skills_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            for (Skill skill : skills) {
                ps.setInt(1, developerId);
                ps.setInt(2, skill.getId());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Skill> getAll() {
        Statement stmt = null;
        List<Skill> skills = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from developers");

            while (rs.next()) {
                Skill skill = new Skill(rs.getInt("id"), rs.getString("name"));
                skills.add(skill);
            }
            return skills;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Skill skill) {
        PreparedStatement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("INSERT INTO skills (name) VALUES (?)");
            stmt.setString(1, skill.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Skill read(int id) {
        Statement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from skills where id = " + id);
            while (rs.next()) {
                Skill skill = new Skill(rs.getInt("id"), rs.getString("name"));
                return skill;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Skill skill) {
        if (skill.getId() == 0) return false;
        PreparedStatement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("UPDATE skills SET name = ?where id = " + skill.getId());
            stmt.setString(1, skill.getName());
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
            stmt.executeUpdate("DELETE from skills where id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }
}
