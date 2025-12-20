package com.gui;

import com.dao.BillDAO;
import com.dao.ComplaintDAO;
import com.dao.DBConnection;
import com.dao.PaymentDAO;
import com.dao.SubscriberDAO;
import com.model.Subscriber;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDashboard extends JFrame {

    private final int subscriberID;

    public CustomerDashboard(int subscriberID) {
        this.subscriberID = subscriberID;

        setTitle("My Account - Customer Dashboard");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient Header
        JPanel panelHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 80, 180), 0, getHeight(), new Color(0, 120, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelHeader.setLayout(new BorderLayout());
        panelHeader.setPreferredSize(new Dimension(1100, 120));

        JLabel lblWelcome = new JLabel("Welcome Back, Customer!", SwingConstants.CENTER);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 36));

        JLabel lblID = new JLabel("Subscriber ID: " + subscriberID, SwingConstants.CENTER);
        lblID.setForeground(new Color(220, 240, 255));
        lblID.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JPanel headerText = new JPanel(new BorderLayout());
        headerText.setOpaque(false);
        headerText.add(lblWelcome, BorderLayout.CENTER);
        headerText.add(lblID, BorderLayout.SOUTH);

        panelHeader.add(headerText, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // Card-style Options
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 40, 40));
        cardsPanel.setBackground(new Color(245, 248, 252));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 80, 100));

        // Titles and descriptions
        String[] titles = {"My Profile", "My Bills", "My Payments", "My Complaints"};
        String[] descriptions = {
            "View and manage your personal information",
            "View your billing history and current charges",
            "View your payment history",
            "Submit and track your service complaints"
        };

        // Separate color array
        Color[] cardColors = {
            new Color(52, 152, 219),   // Blue
            new Color(243, 156, 18),   // Orange
            new Color(46, 204, 113),  // Green
            new Color(231, 76, 60)     // Red
        };

        for (int i = 0; i < 4; i++) {
            final int cardIndex = i;
            final Color cardColor = cardColors[i];

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));

            JLabel title = new JLabel(titles[i], SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(cardColor);

            JLabel desc = new JLabel("<html><center>" + descriptions[i] + "</center></html>", SwingConstants.CENTER);
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            desc.setForeground(new Color(80, 80, 80));

            JButton btnView = new JButton("View →");
            btnView.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnView.setBackground(cardColor);
            btnView.setForeground(Color.WHITE);
            btnView.setFocusPainted(false);
            btnView.setBorderPainted(false);
            btnView.setOpaque(true);
            btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnView.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switch (cardIndex) {
                        case 0:
                            showMyProfile();
                            break;
                        case 1:
                            showMyBills();
                            break;
                        case 2:
                            showMyPayments();
                            break;
                        case 3:
                            showMyComplaints();
                            break;
                    }
                }
            });

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            bottomPanel.add(btnView, BorderLayout.EAST);

            card.add(title, BorderLayout.NORTH);
            card.add(desc, BorderLayout.CENTER);
            card.add(bottomPanel, BorderLayout.SOUTH);

            cardsPanel.add(card);
        }

        add(cardsPanel, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(52, 73, 94));
        JLabel footerLabel = new JLabel("© 2025 Utilities Automation System | Customer Portal", SwingConstants.CENTER);
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showMyProfile() {
        try {
            Connection conn = DBConnection.getConnection();
            SubscriberDAO dao = new SubscriberDAO(conn);
            Subscriber sub = dao.getSubscriberByID(subscriberID); // Now this method exists

            if (sub != null) {
                String info = "<html>" +
                        "<b>Username:</b> " + sub.getUsername() + "<br><br>" +
                        "<b>Full Name:</b> " + sub.getFullName() + "<br><br>" +
                        "<b>Email:</b> " + sub.getEmail() + "<br><br>" +
                        "<b>Role:</b> " + sub.getRole() + "<br><br>" +
                        "<b>Created On:</b> " + sub.getCreatedAt() + "<br>" +
                        "</html>";

                JOptionPane.showMessageDialog(this, info, "My Profile", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Profile not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading profile: " + ex.getMessage());
        }
    }

    private void showMyBills() {
        new FilteredViewForm("My Bills",
                "SELECT BillID, Amount, Type, Status, Date FROM bill WHERE SubscriberID = ?",
                new String[]{"Bill ID", "Amount", "Type", "Status", "Date"});
    }

    private void showMyPayments() {
        new FilteredViewForm("My Payments",
                "SELECT PaymentID, Amount, Type, Reference, Status, Date FROM payment WHERE SubscriberID = ?",
                new String[]{"Payment ID", "Amount", "Type", "Reference", "Status", "Date"});
    }

    private void showMyComplaints() {
        new FilteredViewForm("My Complaints",
                "SELECT ComplaintID, Subject, Description, Status, CreatedAt FROM complaint WHERE SubscriberID = ?",
                new String[]{"ID", "Subject", "Description", "Status", "Created On"});
    }

    // Reusable filtered table viewer for customer
    private class FilteredViewForm extends JFrame {
        public FilteredViewForm(String title, String query, String[] columns) {
            setTitle(title);
            setSize(900, 600);
            setLocationRelativeTo(CustomerDashboard.this);
            setLayout(new BorderLayout());

            JTable table = new JTable();
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            table.setModel(model);
            table.setRowHeight(30);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            try {
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, subscriberID);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    Object[] row = new Object[columns.length];
                    for (int i = 0; i < columns.length; i++) {
                        row[i] = rs.getObject(i + 1);
                    }
                    model.addRow(row);
                }

                rs.close();
                ps.close();
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
            }

            add(new JScrollPane(table), BorderLayout.CENTER);

            JPanel bottom = new JPanel();
            bottom.setBackground(new Color(245, 248, 252));
            bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(bottom, BorderLayout.SOUTH);

            setVisible(true);
        }
    }
}