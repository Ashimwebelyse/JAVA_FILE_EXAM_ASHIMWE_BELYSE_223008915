package com.gui;

import com.dao.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private String loggedInRole;
    private int loggedInSubscriberID;

    public LoginForm() {
        // Set System Look and Feel for modern appearance (works well in Java 7)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Utilities Automation System - Login");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with split layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // LEFT: Beautiful Gradient Illustration Panel
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 80, 180), 0, h, new Color(0, 140, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        leftPanel.setLayout(new GridBagLayout());

        JLabel lblWelcome = new JLabel("Welcome to");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 40));
        lblWelcome.setForeground(Color.WHITE);

        JLabel lblSystem = new JLabel("Utilities Automation System");
        lblSystem.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblSystem.setForeground(new Color(200, 230, 255));

        JLabel lblTagline = new JLabel("Manage services efficiently and securely");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblTagline.setForeground(new Color(220, 240, 255));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(Box.createVerticalStrut(100));
        textPanel.add(lblWelcome);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(lblSystem);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(lblTagline);

        leftPanel.add(textPanel);

        gbc.gridx = 0;
        gbc.weightx = 0.55;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(leftPanel, gbc);

        // RIGHT: Login Card Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(15, 10, 25, 10);
        rightGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Sign In");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(new Color(0, 80, 180));
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.gridwidth = 2;
        rightPanel.add(lblTitle, rightGbc);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightGbc.gridy = 1;
        rightGbc.gridwidth = 1;
        rightPanel.add(lblUser, rightGbc);

        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(150, 150, 150), 1, 15),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        rightGbc.gridy = 2;
        rightGbc.gridwidth = 2;
        rightPanel.add(txtUsername, rightGbc);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        rightGbc.gridy = 3;
        rightGbc.gridwidth = 1;
        rightPanel.add(lblPass, rightGbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(150, 150, 150), 1, 15),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        rightGbc.gridy = 4;
        rightGbc.gridwidth = 2;
        rightPanel.add(txtPassword, rightGbc);

        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new RoundedBorder(new Color(0, 102, 204), 0, 15));
        btnLogin.setPreferredSize(new Dimension(300, 55));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(0, 120, 255));
            }

            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(0, 102, 204));
            }
        });

        rightGbc.gridy = 5;
        rightGbc.insets = new Insets(30, 10, 10, 10);
        rightPanel.add(btnLogin, rightGbc);

        JLabel lblForgot = new JLabel("<html><u>Forgot Password?</u></html>");
        lblForgot.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblForgot.setForeground(new Color(0, 102, 204));
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightGbc.gridy = 6;
        rightPanel.add(lblForgot, rightGbc);

        gbc.gridx = 1;
        gbc.weightx = 0.45;
        mainPanel.add(rightPanel, gbc);

        add(mainPanel);

        // Action Listeners (no lambda)
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        txtPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        setVisible(true);
    }

    // Rounded Border Class
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color;
        private int thickness;
        private int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2 + thickness, radius + thickness, radius / 2 + thickness, radius + thickness);
        }
    }

    private void authenticateUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT SubscriberID, Role FROM subscriber WHERE Username = ? AND PasswordHash = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // In real app: use hashed password!

            rs = ps.executeQuery();

            if (rs.next()) {
                loggedInSubscriberID = rs.getInt("SubscriberID");
                loggedInRole = rs.getString("Role").trim();

                JOptionPane.showMessageDialog(this, "Login Successful!\nWelcome, " + loggedInRole, "Success", JOptionPane.INFORMATION_MESSAGE);

                dispose(); // Close login form

                // Redirect based on role
                if ("Admin".equalsIgnoreCase(loggedInRole)) {
                    new MainDashboard(); // Full access
                } else if ("Operator".equalsIgnoreCase(loggedInRole)) {
                    new OperatorDashboard();
                } else if ("Customer".equalsIgnoreCase(loggedInRole)) {
                    new CustomerDashboard(loggedInSubscriberID);
                } else {
                    JOptionPane.showMessageDialog(null, "Unknown role. Contact admin.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {}
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginForm();
            }
        });
    }
}