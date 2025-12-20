package com.gui;

import com.dao.DBConnection;
import com.dao.SubscriberDAO;
import com.model.Subscriber;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class SubscriberForm extends JFrame {

    // Reusable Modern Button with rounded corners and hover effect
    class ModernButton extends JButton {
        private Color backgroundColor;
        private Color hoverColor;

        public ModernButton(String text, Color bg, Color hover) {
            super(text);
            this.backgroundColor = bg;
            this.hoverColor = hover;
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(true);
            setBackground(bg);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }

                public void mouseExited(MouseEvent e) {
                    setBackground(backgroundColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No border
        }
    }

    private JTextField txtUsername, txtEmail, txtFullName;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JTable table;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnClear;
    private SubscriberDAO subscriberDAO;

    public SubscriberForm() {
        setTitle("Subscriber Management - Utilities Automation System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 248, 252));

        // Initialize DAO
        try {
            Connection conn = DBConnection.getConnection();
            subscriberDAO = new SubscriberDAO(conn);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Database connection failed: " + ex.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Gradient Header
        JPanel panelHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 80, 180),
                        0, getHeight(), new Color(0, 120, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelHeader.setLayout(new BorderLayout());
        panelHeader.setPreferredSize(new Dimension(1000, 80));

        JLabel lblTitle = new JLabel("Subscriber Management", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        panelHeader.add(lblTitle, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // Input Panel (Left)
        JPanel panelInput = new JPanel(new GridLayout(5, 2, 10, 15));
        panelInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Subscriber Details",
                0, 0,
                new Font("Segoe UI", Font.PLAIN, 16)
        ));
        panelInput.setBackground(Color.WHITE);
        panelInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 20, 10, 20),
                panelInput.getBorder()
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        panelInput.add(createLabel("Username:", labelFont));
        txtUsername = createTextField(fieldFont);
        panelInput.add(txtUsername);

        panelInput.add(createLabel("Password:", labelFont));
        txtPassword = new JPasswordField();
        txtPassword.setFont(fieldFont);
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelInput.add(txtPassword);

        panelInput.add(createLabel("Email:", labelFont));
        txtEmail = createTextField(fieldFont);
        panelInput.add(txtEmail);

        panelInput.add(createLabel("Full Name:", labelFont));
        txtFullName = createTextField(fieldFont);
        panelInput.add(txtFullName);

        panelInput.add(createLabel("Role:", labelFont));
        cmbRole = new JComboBox<>(new String[]{"Customer", "Admin", "Operator"});
        cmbRole.setFont(fieldFont);
        panelInput.add(cmbRole);

        add(panelInput, BorderLayout.WEST);

        // Table Panel (Center)
        table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(200, 200, 200));
        table.setShowGrid(true);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(200, 230, 255));

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 250));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel (Bottom)
        JPanel panelButtons = new JPanel();
        panelButtons.setBackground(new Color(245, 248, 252));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        btnAdd = new ModernButton("Add", new Color(46, 204, 113), new Color(39, 174, 96));
        btnUpdate = new ModernButton("Update", new Color(52, 152, 219), new Color(41, 128, 185));
        btnDelete = new ModernButton("Delete", new Color(231, 76, 60), new Color(192, 57, 43));
        btnRefresh = new ModernButton("Refresh", new Color(241, 196, 15), new Color(212, 172, 13));
        btnClear = new ModernButton("Clear", new Color(149, 165, 166), new Color(127, 140, 141));

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);
        panelButtons.add(btnClear);
        add(panelButtons, BorderLayout.SOUTH);

        // Table row click (do NOT auto-fill password for security)
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtUsername.setText(table.getValueAt(row, 1).toString());
                    txtEmail.setText(table.getValueAt(row, 2).toString());
                    txtFullName.setText(table.getValueAt(row, 3).toString());
                    cmbRole.setSelectedItem(table.getValueAt(row, 4).toString());
                    txtPassword.setText(""); // Never show password
                }
            }
        });

        // Button Actions
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addSubscriber(); }
        });
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateSubscriber(); }
        });
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { deleteSubscriber(); }
        });
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSubscribers();
                clearForm();
            }
        });
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearForm(); }
        });

        loadSubscribers();
        setVisible(true);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return field;
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        txtFullName.setText("");
        cmbRole.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateInput() {
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.");
            txtUsername.requestFocus();
            return false;
        }
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required.");
            txtEmail.requestFocus();
            return false;
        }
        if (!txtEmail.getText().trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            txtEmail.requestFocus();
            return false;
        }
        if (new String(txtPassword.getPassword()).trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required.");
            txtPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void addSubscriber() {
        if (!validateInput()) return;
        Subscriber sub = new Subscriber();
        sub.setUsername(txtUsername.getText().trim());
        sub.setPasswordHash(new String(txtPassword.getPassword())); // Note: In production, hash this!
        sub.setEmail(txtEmail.getText().trim());
        sub.setFullName(txtFullName.getText().trim());
        sub.setRole((String) cmbRole.getSelectedItem());
        sub.setCreatedAt(new Date());
        sub.setLastLogin(null);

        if (subscriberDAO.addSubscriber(sub)) {
            JOptionPane.showMessageDialog(this, "Subscriber added successfully!");
            loadSubscribers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add subscriber.");
        }
    }

    private void updateSubscriber() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a subscriber to update.");
            return;
        }
        if (!validateInput()) return;

        int subscriberID = ((Integer) table.getValueAt(row, 0)).intValue();
        Subscriber sub = new Subscriber();
        sub.setSubscriberID(subscriberID);
        sub.setUsername(txtUsername.getText().trim());
        String newPass = new String(txtPassword.getPassword());
        sub.setPasswordHash(newPass.isEmpty() ? null : newPass); // Only update if provided
        sub.setEmail(txtEmail.getText().trim());
        sub.setFullName(txtFullName.getText().trim());
        sub.setRole((String) cmbRole.getSelectedItem());

        if (subscriberDAO.updateSubscriber(sub)) {
            JOptionPane.showMessageDialog(this, "Subscriber updated successfully!");
            loadSubscribers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update subscriber.");
        }
    }

    private void deleteSubscriber() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a subscriber to delete.");
            return;
        }
        String username = table.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete subscriber:\n'" + username + "'?\nThis action cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int subscriberID = ((Integer) table.getValueAt(row, 0)).intValue();
            if (subscriberDAO.deleteSubscriber(subscriberID)) {
                JOptionPane.showMessageDialog(this, "Subscriber deleted successfully!");
                loadSubscribers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete subscriber.");
            }
        }
    }

    private void loadSubscribers() {
        List<Subscriber> list = subscriberDAO.getAllSubscribers();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Email");
        model.addColumn("Full Name");
        model.addColumn("Role");
        model.addColumn("Created At");
        model.addColumn("Last Login");

        for (Subscriber sub : list) {
            model.addRow(new Object[]{
                    sub.getSubscriberID(),
                    sub.getUsername(),
                    sub.getEmail(),
                    sub.getFullName(),
                    sub.getRole(),
                    sub.getCreatedAt(),
                    sub.getLastLogin()
            });
        }
        table.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new SubscriberForm();
            }
        });
    }
}