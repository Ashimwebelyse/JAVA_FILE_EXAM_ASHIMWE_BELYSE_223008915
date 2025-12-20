package com.dao;

import com.model.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private Connection conn;

    public BillDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addBill(Bill bill) {
        String sql = "INSERT INTO bill (SubscriberID, ServiceID, Amount, Date, Type, Reference, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bill.getSubscriberID());
            ps.setInt(2, bill.getServiceID());
            ps.setDouble(3, bill.getAmount());
            ps.setTimestamp(4, new Timestamp(bill.getDate().getTime()));
            ps.setString(5, bill.getType());
            ps.setString(6, bill.getReference());
            ps.setString(7, bill.getStatus());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBill(Bill bill) {
        String sql = "UPDATE bill SET SubscriberID=?, ServiceID=?, Amount=?, Date=?, Type=?, Reference=?, Status=? WHERE BillID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bill.getSubscriberID());
            ps.setInt(2, bill.getServiceID());
            ps.setDouble(3, bill.getAmount());
            ps.setTimestamp(4, new Timestamp(bill.getDate().getTime()));
            ps.setString(5, bill.getType());
            ps.setString(6, bill.getReference());
            ps.setString(7, bill.getStatus());
            ps.setInt(8, bill.getBillID());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBill(int billID) {
        String sql = "DELETE FROM bill WHERE BillID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, billID);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Bill getBillById(int billID) {
        String sql = "SELECT * FROM bill WHERE BillID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, billID);
            ResultSet rs = ps.executeQuery();
            Bill bill = null;
            if (rs.next()) {
                bill = new Bill(
                    rs.getInt("BillID"),
                    rs.getInt("SubscriberID"),
                    rs.getInt("ServiceID"),
                    rs.getDouble("Amount"),
                    rs.getTimestamp("Date"),
                    rs.getString("Type"),
                    rs.getString("Reference"),
                    rs.getString("Status")
                );
            }
            rs.close();
            ps.close();
            return bill;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<Bill>();
        String sql = "SELECT * FROM bill";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Bill bill = new Bill(
                    rs.getInt("BillID"),
                    rs.getInt("SubscriberID"),
                    rs.getInt("ServiceID"),
                    rs.getDouble("Amount"),
                    rs.getTimestamp("Date"),
                    rs.getString("Type"),
                    rs.getString("Reference"),
                    rs.getString("Status")
                );
                list.add(bill);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
