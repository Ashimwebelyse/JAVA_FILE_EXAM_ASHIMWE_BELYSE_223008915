package com.gui;

import com.dao.DBConnection;
import com.dao.ServiceDAO;
import com.model.Service;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

public class ServiceForm extends JFrame {

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

    private JTextField txtName, txtCategory, txtPrice;
    private JTextArea txtDescription;
    private JComboBox<String> cmbStatus;
    private JTable table;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnClear;
    private ServiceDAO serviceDAO;

    public ServiceForm() {
        setTitle("Service Management - Utilities Automation System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 248, 252));

        // Initialize DAO
        try {
            Connection conn = DBConnection.getConnection();
            serviceDAO = new ServiceDAO(conn);
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

        JLabel lblTitle = new JLabel("Service Management", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        panelHeader.add(lblTitle, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // Input Panel (Left)
        JPanel panelInput = new JPanel(new GridLayout(6, 2, 10, 15));
        panelInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Service Details",
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

        panelInput.add(createLabel("Name:", labelFont));
        txtName = createTextField(fieldFont);
        panelInput.add(txtName);

        panelInput.add(createLabel("Description:", labelFont));
        txtDescription = new JTextArea(4, 20);
        txtDescription.setFont(fieldFont);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setPreferredSize(new Dimension(250, 80));
        panelInput.add(descScroll);

        panelInput.add(createLabel("Category:", labelFont));
        txtCategory = createTextField(fieldFont);
        panelInput.add(txtCategory);

        panelInput.add(createLabel("Price:", labelFont));
        txtPrice = createTextField(fieldFont);
        panelInput.add(txtPrice);

        panelInput.add(createLabel("Status:", labelFont));
        cmbStatus = new JComboBox<>(new String[]{"Active", "Inactive"});
        cmbStatus.setFont(fieldFont);
        panelInput.add(cmbStatus);

        add(panelInput, BorderLayout.WEST);

        
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

        // Table row click to fill form
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtName.setText(table.getValueAt(row, 1).toString());
                    txtDescription.setText(table.getValueAt(row, 2).toString());
                    txtCategory.setText(table.getValueAt(row, 3).toString());
                    txtPrice.setText(table.getValueAt(row, 4).toString());
                    cmbStatus.setSelectedItem(table.getValueAt(row, 5).toString());
                }
            }
        });

        // Button Actions
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { addService(); }
        });
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateService(); }
        });
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { deleteService(); }
        });
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadServices();
                clearForm();
            }
        });
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { clearForm(); }
        });

        loadServices();
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
        txtName.setText("");
        txtDescription.setText("");
        txtCategory.setText("");
        txtPrice.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Service Name is required.");
            txtName.requestFocus();
            return false;
        }
        if (txtPrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Price is required.");
            txtPrice.requestFocus();
            return false;
        }
        try {
            Double.parseDouble(txtPrice.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number.");
            txtPrice.requestFocus();
            return false;
        }
        return true;
    }

    private void addService() {
        if (!validateInput()) return;
        Service s = new Service();
        s.setName(txtName.getText().trim());
        s.setDescription(txtDescription.getText().trim());
        s.setCategory(txtCategory.getText().trim());
        s.setPriceOrValue(Double.parseDouble(txtPrice.getText().trim()));
        s.setStatus((String) cmbStatus.getSelectedItem());
        s.setCreatedAt(new Date());

        if (serviceDAO.addService(s)) {
            JOptionPane.showMessageDialog(this, "Service added successfully!");
            loadServices();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add service.");
        }
    }

    private void updateService() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a service to update.");
            return;
        }
        if (!validateInput()) return;

        int serviceID = ((Integer) table.getValueAt(row, 0)).intValue();
        Service s = new Service();
        s.setServiceID(serviceID);
        s.setName(txtName.getText().trim());
        s.setDescription(txtDescription.getText().trim());
        s.setCategory(txtCategory.getText().trim());
        s.setPriceOrValue(Double.parseDouble(txtPrice.getText().trim()));
        s.setStatus((String) cmbStatus.getSelectedItem());

        if (serviceDAO.updateService(s)) {
            JOptionPane.showMessageDialog(this, "Service updated successfully!");
            loadServices();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update service.");
        }
    }

    private void deleteService() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a service to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this service?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int serviceID = ((Integer) table.getValueAt(row, 0)).intValue();
            if (serviceDAO.deleteService(serviceID)) {
                JOptionPane.showMessageDialog(this, "Service deleted successfully!");
                loadServices();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete service.");
            }
        }
    }

    private void loadServices() {
        List<Service> services = serviceDAO.getAllServices();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Category");
        model.addColumn("Price");
        model.addColumn("Status");
        model.addColumn("Created At");

        for (Service s : services) {
            model.addRow(new Object[]{
                    s.getServiceID(),
                    s.getName(),
                    s.getDescription(),
                    s.getCategory(),
                    s.getPriceOrValue(),
                    s.getStatus(),
                    s.getCreatedAt()
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
                new ServiceForm();
            }
        });
    }
}