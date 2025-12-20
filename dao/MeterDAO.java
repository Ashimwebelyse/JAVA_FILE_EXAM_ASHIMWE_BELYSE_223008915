package com.dao;

import com.model.Meter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeterDAO {
    private Connection conn;

    public MeterDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addMeter(Meter meter) {
        String sql = "INSERT INTO meter (Name, Description, Category, PriceOrValue, Status, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, meter.getName());
            ps.setString(2, meter.getDescription());
            ps.setString(3, meter.getCategory());
            ps.setDouble(4, meter.getPriceOrValue());
            ps.setString(5, meter.getStatus());
            ps.setTimestamp(6, new Timestamp(meter.getCreatedAt().getTime()));
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMeter(Meter meter) {
        String sql = "UPDATE meter SET Name=?, Description=?, Category=?, PriceOrValue=?, Status=? WHERE MeterID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, meter.getName());
            ps.setString(2, meter.getDescription());
            ps.setString(3, meter.getCategory());
            ps.setDouble(4, meter.getPriceOrValue());
            ps.setString(5, meter.getStatus());
            ps.setInt(6, meter.getMeterID());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMeter(int meterID) {
        String sql = "DELETE FROM meter WHERE MeterID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, meterID);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Meter getMeterById(int meterID) {
        String sql = "SELECT * FROM meter WHERE MeterID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, meterID);
            ResultSet rs = ps.executeQuery();
            Meter meter = null;
            if (rs.next()) {
                meter = new Meter(
                    rs.getInt("MeterID"),
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
            return meter;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Meter> getAllMeters() {
        List<Meter> list = new ArrayList<Meter>();
        String sql = "SELECT * FROM meter";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Meter meter = new Meter(
                    rs.getInt("MeterID"),
                    rs.getString("Name"),
                    rs.getString("Description"),
                    rs.getString("Category"),
                    rs.getDouble("PriceOrValue"),
                    rs.getString("Status"),
                    rs.getTimestamp("CreatedAt")
                );
                list.add(meter);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
