package com.dao;

import com.model.Complaint;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {
    private Connection conn;

    public ComplaintDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaint (SubscriberID, Subject, Description, Status, CreatedAt) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, complaint.getSubscriberID());
            ps.setString(2, complaint.getSubject());
            ps.setString(3, complaint.getDescription());
            ps.setString(4, complaint.getStatus());
            ps.setTimestamp(5, new Timestamp(complaint.getCreatedAt().getTime()));
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateComplaint(Complaint complaint) {
        String sql = "UPDATE complaint SET SubscriberID=?, Subject=?, Description=?, Status=? WHERE ComplaintID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, complaint.getSubscriberID());
            ps.setString(2, complaint.getSubject());
            ps.setString(3, complaint.getDescription());
            ps.setString(4, complaint.getStatus());
            ps.setInt(5, complaint.getComplaintID());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteComplaint(int complaintID) {
        String sql = "DELETE FROM complaint WHERE ComplaintID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, complaintID);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Complaint getComplaintById(int complaintID) {
        String sql = "SELECT * FROM complaint WHERE ComplaintID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, complaintID);
            ResultSet rs = ps.executeQuery();
            Complaint complaint = null;
            if (rs.next()) {
                complaint = new Complaint(
                    rs.getInt("ComplaintID"),
                    rs.getInt("SubscriberID"),
                    rs.getString("Subject"),
                    rs.getString("Description"),
                    rs.getString("Status"),
                    rs.getTimestamp("CreatedAt")
                );
            }
            rs.close();
            ps.close();
            return complaint;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<Complaint>();
        String sql = "SELECT * FROM complaint";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Complaint complaint = new Complaint(
                    rs.getInt("ComplaintID"),
                    rs.getInt("SubscriberID"),
                    rs.getString("Subject"),
                    rs.getString("Description"),
                    rs.getString("Status"),
                    rs.getTimestamp("CreatedAt")
                );
                list.add(complaint);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
