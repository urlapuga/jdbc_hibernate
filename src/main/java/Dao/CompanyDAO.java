package Dao;

import Model.Company;
import Model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 28.06.2017.
 */
public class CompanyDAO implements DAO<Company> {

    private List<Project> projects = new ArrayList<>();
    private ProjectDAO projectDAO = new ProjectDAO();
    static {
        try {
            Class.forName(P.jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Company> getAll() {

        Statement stmt = null;
        List<Company> companies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from companies");

            while (rs.next()) {
                projects = projectDAO.getByCompany(rs.getInt("id"));
                Company company = new Company(rs.getInt("id"),rs.getString("name"),projects);
                companies.add(company);
            }
            return companies;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Company company) {
        PreparedStatement stmt = null;
        try(Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("INSERT INTO companies (name) VALUES (?)" );
            stmt.setString(1,company.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Company read(int id){
        Statement stmt = null;
        try(Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from companies where id = "+id);
            while (rs.next()) {
                projects = projectDAO.getByCompany(rs.getInt("id"));
                Company skill = new Company(rs.getInt("id"),rs.getString("name"),projects);
                return skill;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Company company) {
        if(company.getId()==0)return false;
        projectDAO.updateProjectsInCompany(company);
        PreparedStatement stmt = null;
        try(Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("UPDATE companies SET name = ? where id = "+company.getId() );
            stmt.setString(1,company.getName());
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
        try(Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE from companies where id = "+id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;

    }
}
