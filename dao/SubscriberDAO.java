package com.dao;

import com.model.Subscriber;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriberDAO {
    private Connection conn;

    public SubscriberDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addSubscriber(Subscriber sub) {
        String sql = "INSERT INTO subscriber (Username, PasswordHash, Email, FullName, Role, CreatedAt, LastLogin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sub.getUsername());
            ps.setString(2, sub.getPasswordHash());
            ps.setString(3, sub.getEmail());
            ps.setString(4, sub.getFullName());
            ps.setString(5, sub.getRole());
            ps.setTimestamp(6, new Timestamp(sub.getCreatedAt().getTime()));
            ps.setTimestamp(7, sub.getLastLogin() == null ? null : new Timestamp(sub.getLastLogin().getTime()));
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubscriber(Subscriber sub) {
        String sql = "UPDATE subscriber SET Username=?, PasswordHash=?, Email=?, FullName=?, Role=?, LastLogin=? WHERE SubscriberID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sub.getUsername());
            ps.setString(2, sub.getPasswordHash());
            ps.setString(3, sub.getEmail());
            ps.setString(4, sub.getFullName());
            ps.setString(5, sub.getRole());
            ps.setTimestamp(6, sub.getLastLogin() == null ? null : new Timestamp(sub.getLastLogin().getTime()));
            ps.setInt(7, sub.getSubscriberID());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubscriber(int subscriberID) {
        String sql = "DELETE FROM subscriber WHERE SubscriberID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, subscriberID);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Subscriber getSubscriberById(int subscriberID) {
        String sql = "SELECT * FROM subscriber WHERE SubscriberID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, subscriberID);
            ResultSet rs = ps.executeQuery();
            Subscriber sub = null;
            if (rs.next()) {
                sub = new Subscriber(
                    rs.getInt("SubscriberID"),
                    rs.getString("Username"),
                    rs.getString("PasswordHash"),
                    rs.getString("Email"),
                    rs.getString("FullName"),
                    rs.getString("Role"),
                    rs.getTimestamp("CreatedAt"),
                    rs.getTimestamp("LastLogin")
                );
            }
            rs.close();
            ps.close();
            return sub;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Subscriber> getAllSubscribers() {
        List<Subscriber> list = new ArrayList<Subscriber>();
        String sql = "SELECT * FROM subscriber";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Subscriber sub = new Subscriber(
                    rs.getInt("SubscriberID"),
                    rs.getString("Username"),
                    rs.getString("PasswordHash"),
                    rs.getString("Email"),
                    rs.getString("FullName"),
                    rs.getString("Role"),
                    rs.getTimestamp("CreatedAt"),
                    rs.getTimestamp("LastLogin")
                );
                list.add(sub);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

	public Subscriber getSubscriberByID(int subscriberID) {
		// TODO Auto-generated method stub
		return null;
	}
}
