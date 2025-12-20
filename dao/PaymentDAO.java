package com.dao;

import com.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection conn;

    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payment (BillID, SubscriberID, Amount, Date, Type, Reference, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, payment.getBillID());
            ps.setInt(2, payment.getSubscriberID());
            ps.setDouble(3, payment.getAmount());
            ps.setTimestamp(4, new Timestamp(payment.getDate().getTime()));
            ps.setString(5, payment.getType());
            ps.setString(6, payment.getReference());
            ps.setString(7, payment.getStatus());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payment SET BillID=?, SubscriberID=?, Amount=?, Date=?, Type=?, Reference=?, Status=? WHERE PaymentID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, payment.getBillID());
            ps.setInt(2, payment.getSubscriberID());
            ps.setDouble(3, payment.getAmount());
            ps.setTimestamp(4, new Timestamp(payment.getDate().getTime()));
            ps.setString(5, payment.getType());
            ps.setString(6, payment.getReference());
            ps.setString(7, payment.getStatus());
            ps.setInt(8, payment.getPaymentID());
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePayment(int paymentID) {
        String sql = "DELETE FROM payment WHERE PaymentID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, paymentID);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Payment getPaymentById(int paymentID) {
        String sql = "SELECT * FROM payment WHERE PaymentID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, paymentID);
            ResultSet rs = ps.executeQuery();
            Payment payment = null;
            if (rs.next()) {
                payment = new Payment(
                    rs.getInt("PaymentID"),
                    rs.getInt("BillID"),
                    rs.getInt("SubscriberID"),
                    rs.getDouble("Amount"),
                    rs.getTimestamp("Date"),
                    rs.getString("Type"),
                    rs.getString("Reference"),
                    rs.getString("Status")
                );
            }
            rs.close();
            ps.close();
            return payment;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<Payment>();
        String sql = "SELECT * FROM payment";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Payment payment = new Payment(
                    rs.getInt("PaymentID"),
                    rs.getInt("BillID"),
                    rs.getInt("SubscriberID"),
                    rs.getDouble("Amount"),
                    rs.getTimestamp("Date"),
                    rs.getString("Type"),
                    rs.getString("Reference"),
                    rs.getString("Status")
                );
                list.add(payment);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

