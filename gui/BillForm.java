package com.gui;

import com.dao.BillDAO;
import com.dao.DBConnection;
import com.model.Bill;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class BillForm extends JFrame {

    // Custom Button with rounded corners and hover effect
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

    private JTextField txtSubscriberID, txtServiceID, txtAmount, txtType, txtReference;
    private JComboBox cmbStatus;
    private JTable table;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;
    private BillDAO billDAO;

    public BillForm() {
        setTitle("Bill Management - Utilities Automation System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 248, 252));

        // Initialize DAO
        try {
            Connection conn = DBConnection.getConnection();
            billDAO = new BillDAO(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }

        // Header Panel
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
        JLabel lblTitle = new JLabel("Bill Management", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelHeader.add(lblTitle, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // Input Panel
        JPanel panelInput = new JPanel(new GridLayout(6, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            "Bill Details", 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
            javax.swing.border.TitledBorder.DEFAULT_POSITION, 
            new Font("Segoe UI", Font.PLAIN, 16)
        ));
        panelInput.setBackground(Color.WHITE);
        panelInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 20, 10, 20), 
            panelInput.getBorder()
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        panelInput.add(createLabel("Subscriber ID:", labelFont));
        txtSubscriberID = createTextField(fieldFont);
        panelInput.add(txtSubscriberID);

        panelInput.add(createLabel("Service ID:", labelFont));
        txtServiceID = createTextField(fieldFont);
        panelInput.add(txtServiceID);

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
        cmbStatus = new JComboBox(new String[]{"Pending", "Paid", "Cancelled"});
        cmbStatus.setFont(fieldFont);
        cmbStatus.setBackground(Color.WHITE);
        panelInput.add(cmbStatus);

        add(panelInput, BorderLayout.WEST);

        // Table Panel
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
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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

        // Button Panel
        JPanel panelButtons = new JPanel();
        panelButtons.setBackground(new Color(245, 248, 252));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnAdd = new ModernButton("Add", new Color(46, 204, 113), new Color(39, 174, 96));
        btnUpdate = new ModernButton("Update", new Color(52, 152, 219), new Color(41, 128, 185));
        btnDelete = new ModernButton("Delete", new Color(231, 76, 60), new Color(192, 57, 43));
        btnRefresh = new ModernButton("Refresh", new Color(241, 196, 15), new Color(212, 172, 13));

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);
        add(panelButtons, BorderLayout.SOUTH);

        // Event Listeners
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    txtSubscriberID.setText(table.getValueAt(selectedRow, 1).toString());
                    txtServiceID.setText(table.getValueAt(selectedRow, 2).toString());
                    txtAmount.setText(table.getValueAt(selectedRow, 3).toString());
                    txtType.setText(table.getValueAt(selectedRow, 4).toString());
                    txtReference.setText(table.getValueAt(selectedRow, 5).toString());
                    cmbStatus.setSelectedItem(table.getValueAt(selectedRow, 6).toString());
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBill();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBill();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteBill();
            }
        });

        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadBills();
            }
        });

        // Load initial data
        loadBills();
        setVisible(true);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return textField;
    }

    private void addBill() {
        try {
            Bill b = new Bill();
            b.setSubscriberID(Integer.parseInt(txtSubscriberID.getText()));
            b.setServiceID(Integer.parseInt(txtServiceID.getText()));
            b.setAmount(Double.parseDouble(txtAmount.getText()));
            b.setType(txtType.getText());
            b.setReference(txtReference.getText());
            b.setStatus(cmbStatus.getSelectedItem().toString());
            b.setDate(new Date());
            if (billDAO.addBill(b)) {
                JOptionPane.showMessageDialog(this, "Bill added successfully");
                loadBills();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add bill");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateBill() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int billID = ((Integer) table.getValueAt(selectedRow, 0)).intValue();
            Bill b = new Bill();
            b.setBillID(billID);
            b.setSubscriberID(Integer.parseInt(txtSubscriberID.getText()));
            b.setServiceID(Integer.parseInt(txtServiceID.getText()));
            b.setAmount(Double.parseDouble(txtAmount.getText()));
            b.setType(txtType.getText());
            b.setReference(txtReference.getText());
            b.setStatus(cmbStatus.getSelectedItem().toString());
            b.setDate(new Date());
            if (billDAO.updateBill(b)) {
                JOptionPane.showMessageDialog(this, "Bill updated successfully");
                loadBills();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update bill");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a bill to update");
        }
    }

    private void deleteBill() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int billID = ((Integer) table.getValueAt(selectedRow, 0)).intValue();
            if (billDAO.deleteBill(billID)) {
                JOptionPane.showMessageDialog(this, "Bill deleted successfully");
                loadBills();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete bill");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a bill to delete");
        }
    }

    private void loadBills() {
        List bills = billDAO.getAllBills();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("BillID");
        model.addColumn("SubscriberID");
        model.addColumn("ServiceID");
        model.addColumn("Amount");
        model.addColumn("Type");
        model.addColumn("Reference");
        model.addColumn("Status");
        model.addColumn("Date");
        for (Object obj : bills) {
            Bill b = (Bill) obj;
            model.addRow(new Object[]{
                b.getBillID(),
                b.getSubscriberID(),
                b.getServiceID(),
                b.getAmount(),
                b.getType(),
                b.getReference(),
                b.getStatus(),
                b.getDate()
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
                new BillForm();
            }
        });
    }
}