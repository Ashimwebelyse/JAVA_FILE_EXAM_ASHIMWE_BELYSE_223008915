package com.gui;

import com.dao.DBConnection;
import com.dao.PaymentDAO;
import com.model.Payment;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class PaymentForm extends JFrame {

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

    private JTextField txtBillID, txtSubscriberID, txtAmount, txtType, txtReference;
    private JComboBox<String> cmbStatus;
    private JTable table;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnClear;
    private PaymentDAO paymentDAO;

    public PaymentForm() {
        setTitle("Payment Management - Utilities Automation System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 248, 252));

        // Initialize DAO
        try {
            Connection conn = DBConnection.getConnection();
            paymentDAO = new PaymentDAO(conn);
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

        JLabel lblTitle = new JLabel("Payment Management", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        panelHeader.add(lblTitle, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // Input Panel (Left)
        JPanel panelInput = new JPanel(new GridLayout(6, 2, 10, 15));
        panelInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Payment Details",
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

        panelInput.add(createLabel("Bill ID:", labelFont));
        txtBillID = createTextField(fieldFont);
        panelInput.add(txtBillID);

        panelInput.add(createLabel("Subscriber ID:", labelFont));
        txtSubscriberID = createTextField(fieldFont);
        panelInput.add(txtSubscriberID);

        panelInput.add(createLabel("Amount:", labelFont));
        txtAmount = createTextField(fieldFont);
        panelInput.add(txtAmount);

        panelInput.add(createLabel("Type:", labelFont));
        txtType = createTextField(fieldFont);
        panelInput.add(txtType);

        panelInput.add(createLabel("Reference:", labelFont));
        txtReference = createTextField(fieldFont);
        panelInput.add(txtReference);

        panelInput.add(createLabel("Status:", labelFont));
        cmbStatus = new JComboBox<>(new String[]{"Pending", "Completed", "Failed"});
        cmbStatus.setFont(fieldFont);
        panelInput.add(cmbStatus);

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

        // Table click to populate form
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtBillID.setText(table.getValueAt(row, 1).toString());
                    txtSubscriberID.setText(table.getValueAt(row, 2).toString());
                    txtAmount.setText(table.getValueAt(row, 3).toString());
                    txtType.setText(table.getValueAt(row, 4).toString());
                    txtReference.setText(table.getValueAt(row, 5).toString());
                    cmbStatus.setSelectedItem(table.getValueAt(row, 6).toString());
                }
            }
        });

        // Button Actions
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addPayment(); }
        });
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updatePayment(); }
        });
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { deletePayment(); }
        });
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadPayments();
                clearForm();
            }
        });
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearForm(); }
        });

        loadPayments();
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
        txtBillID.setText("");
        txtSubscriberID.setText("");
        txtAmount.setText("");
        txtType.setText("");
        txtReference.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateInput() {
        if (txtBillID.getText().trim().isEmpty() || txtSubscriberID.getText().trim().isEmpty() ||
            txtAmount.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bill ID, Subscriber ID, and Amount are required.");
            return false;
        }
        try {
            Integer.parseInt(txtBillID.getText().trim());
            Integer.parseInt(txtSubscriberID.getText().trim());
            Double.parseDouble(txtAmount.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Bill ID and Subscriber ID must be integers.\nAmount must be a valid number.");
            return false;
        }
        return true;
    }

    private void addPayment() {
        if (!validateInput()) return;
        Payment p = new Payment();
        p.setBillID(Integer.parseInt(txtBillID.getText().trim()));
        p.setSubscriberID(Integer.parseInt(txtSubscriberID.getText().trim()));
        p.setAmount(Double.parseDouble(txtAmount.getText().trim()));
        p.setType(txtType.getText().trim());
        p.setReference(txtReference.getText().trim());
        p.setStatus((String) cmbStatus.getSelectedItem());
        p.setDate(new Date());

        if (paymentDAO.addPayment(p)) {
            JOptionPane.showMessageDialog(this, "Payment added successfully!");
            loadPayments();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add payment.");
        }
    }

    private void updatePayment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a payment to update.");
            return;
        }
        if (!validateInput()) return;

        int paymentID = ((Integer) table.getValueAt(row, 0)).intValue();
        Payment p = new Payment();
        p.setPaymentID(paymentID);
        p.setBillID(Integer.parseInt(txtBillID.getText().trim()));
        p.setSubscriberID(Integer.parseInt(txtSubscriberID.getText().trim()));
        p.setAmount(Double.parseDouble(txtAmount.getText().trim()));
        p.setType(txtType.getText().trim());
        p.setReference(txtReference.getText().trim());
        p.setStatus((String) cmbStatus.getSelectedItem());

        if (paymentDAO.updatePayment(p)) {
            JOptionPane.showMessageDialog(this, "Payment updated successfully!");
            loadPayments();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update payment.");
        }
    }

    private void deletePayment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a payment to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this payment record?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int paymentID = ((Integer) table.getValueAt(row, 0)).intValue();
            if (paymentDAO.deletePayment(paymentID)) {
                JOptionPane.showMessageDialog(this, "Payment deleted successfully!");
                loadPayments();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete payment.");
            }
        }
    }

    private void loadPayments() {
        List<Payment> payments = paymentDAO.getAllPayments();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Payment ID");
        model.addColumn("Bill ID");
        model.addColumn("Subscriber ID");
        model.addColumn("Amount");
        model.addColumn("Type");
        model.addColumn("Reference");
        model.addColumn("Status");
        model.addColumn("Date");

        for (Payment p : payments) {
            model.addRow(new Object[]{
                    p.getPaymentID(),
                    p.getBillID(),
                    p.getSubscriberID(),
                    p.getAmount(),
                    p.getType(),
                    p.getReference(),
                    p.getStatus(),
                    p.getDate()
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
                new PaymentForm();
            }
        });
    }
}