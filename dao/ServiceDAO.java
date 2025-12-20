package com.dao;

import com.model.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private Connection conn;

    public ServiceDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addService(Service service) {
        String sql = "INSERT INTO services (Name, Description, Category, PriceOrValue, Status, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, service.getName());
            ps.setString(2, service.getDescription());
            ps.setString(3, service.getCategory());
            ps.setDouble(4, service.getPriceOrValue());
            ps.setString(5, service.getStatus());
            ps.setTimestamp(6, new Timestamp(service.getCreatedAt().getTime()));
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateService(Service service) {
        String sql = "UPDATE services SET Name=?, Description=?, Category=?, PriceOrValue=?, Status=? WHERE ServiceID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, service.getName());
            ps.setString(2, service.getDescription());
            ps.setString(3, service.getCategory());
            ps.setDouble(4, service.getPriceOrValue());
            ps.setString(5, service.getStatus());
            ps.setInt(6, service.getServiceID());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteService(int serviceID) {
        String sql = "DELETE FROM services WHERE ServiceID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, serviceID);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Service getServiceById(int serviceID) {
        String sql = "SELECT * FROM services WHERE ServiceID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, serviceID);
            ResultSet rs = ps.executeQuery();
            Service service = null;
            if (rs.next()) {
                service = new Service(
                    rs.getInt("ServiceID"),
                    rs.getString("Name"),
                    rs.getString("Description"),
                    rs.getString("Category"),
                    rs.getDouble("PriceOrValue"),
                    rs.getString("Status"),
                    rs.getTimestamp("CreatedAt")
                );
            }
            rs.close();
            ps.close();
            return service;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Service> getAllServices() {
        List<Service> list = new ArrayList<Service>();
        String sql = "SELECT * FROM services";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Service service = new Service(
                    rs.getInt("ServiceID"),
                    rs.getString("Name"),
                    rs.getString("Description"),
                    rs.getString("Category"),
                    rs.getDouble("PriceOrValue"),
                    rs.getString("Status"),
                    rs.getTimestamp("CreatedAt")
                );
                list.add(service);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
