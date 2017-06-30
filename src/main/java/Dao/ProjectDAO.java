package Dao;

import Model.Company;
import Model.Customer;
import Model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 28.06.2017.
 */
public class ProjectDAO implements DAO<Project> {
    static {
        try {
            Class.forName(P.jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Statement stmt = null;


    /**
     * Update pCompany projects
     * @param company
     */
    public void updateProjectsInCompany(Company company) {
        deleteByCompany(company.getId());
        insertCompanyProjects(company.getId(), company.getProjects());
    }

    /**
     * Update Customers projects
     * @param customer
     */
    public void updateProjectsOfCustomer(Customer customer) {
        deleteByCustomer(customer.getId());
        insertCustomerProjects(customer.getId(), customer.getProjects());
    }

    private void deleteByCustomer(int customerId) {
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE from customers_has_projects where customers_id = " + customerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertCustomerProjects(int customerId, List<Project> projectList) {

        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            conn.setAutoCommit(false);
            String query = "INSERT INTO customers_has_projects (customers_id,project_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            for (Project project : projectList) {
                ps.setInt(1, customerId);
                ps.setInt(2, project.getId());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Deletes all projects by company
     *
     * @param companyId
     */
    private void deleteByCompany(int companyId) {
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE from companies_has_projects where companies_id = " + companyId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Inserts company projects list
     *
     * @param projectList
     */
    private void insertCompanyProjects(int companyId, List<Project> projectList) {

        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            conn.setAutoCommit(false);
            String query = "INSERT INTO companies_has_projects (companies_id,project_id) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            for (Project project : projectList) {
                ps.setInt(1, companyId);
                ps.setInt(2, project.getId());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Project> getByCustomer(int customerId) {

        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT\n" +
                    "    projects.`name`,\n" +
                    "    projects.cost\n" +
                    "            FROM\n" +
                    "    customers_has_projects\n" +
                    "    LEFT JOIN projects ON projects.id = customers_has_projects.projects_id\n" +
                    "            WHERE\n" +
                    "    customers_has_projects.customers_id = " + customerId);

            return generateProjectList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Project> getByCompany(int companyId) {

        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT\n" +
                    "projects.`name` as name,\n" +
                    "projects.cost as cost,\n" +
                    "projects.id as id\n" +
                    "FROM\n" +
                    "companies_has_projects\n" +
                    "LEFT JOIN projects ON projects.id = companies_has_projects.projects_id\n" +
                    "where companies_has_projects.companies_id = " + companyId);

            return generateProjectList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Project> getAll() {
        Statement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from projects");
            return generateProjectList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Project project) {
        PreparedStatement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("INSERT INTO projects (name,cost) VALUES (?,?)");
            stmt.setString(1, project.getName());
            stmt.setInt(2, project.getCost());
            stmt.executeQuery();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Project read(int id) {
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from projects where id = " + id);
            while (rs.next()) {
                Project project = new Project(id, rs.getString("name"), rs.getInt("cost"), null);
                return project;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Project project) {
        if (project.getId() == 0) return false;
        PreparedStatement stmt = null;
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("UPDATE projects SET name = ? where id = ?");
            stmt.setString(1, project.getName());
            stmt.setInt(2, project.getId());
            stmt.executeQuery();
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            stmt.executeUpdate("DELETE from projects where id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    private List<Project> generateProjectList(ResultSet rs) {
        List<Project> projects = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int cost = rs.getInt("cost");
                Project project = new Project(id, name, cost, null);
                projects.add(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }
}
