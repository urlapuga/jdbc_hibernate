package Dao;

import Model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 28.06.2017.
 */
public class CustomerDAO implements DAO<Customer> {
    ProjectDAO projectDAO = new ProjectDAO();
    static {
        try {
            Class.forName(P.jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getAll() {
        Statement stmt = null;
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from customers");

            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("id"),rs.getString("name"),null);
                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Customer company) {
        PreparedStatement stmt = null;
        try(Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("INSERT INTO customers (name) VALUES (?)" );
            stmt.setString(1,company.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Customer read(int id){
        Statement stmt = null;
        try(Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from customers where id = "+id);
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("id"),rs.getString("name"),null);
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Customer customer) {
        if(customer.getId()==0)return false;
        projectDAO.updateProjectsOfCustomer(customer);
        PreparedStatement stmt = null;
        try(Connection conn = DriverManager.getConnection(P.connection, P.user, P.pass)) {
            stmt = conn.prepareStatement("UPDATE customers SET name = ? where id = "+customer.getId() );
            stmt.setString(1,customer.getName());
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
            stmt.executeUpdate("DELETE from customers where id = "+id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
