package com.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperatorDashboard extends JFrame {

    // Reusable ModernButton from previous forms
    class ModernButton extends JButton {
        private Color backgroundColor;
        private Color hoverColor;

        public ModernButton(String text, Color bg, Color hover) {
            super(text);
            this.backgroundColor = bg;
            this.hoverColor = hover;
            setFont(new Font("Segoe UI", Font.BOLD, 18));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(true);
            setBackground(bg);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    setBackground(hoverColor);
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    setBackground(backgroundColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {}
    }

    public OperatorDashboard() {
        setTitle("Operator Dashboard - Utilities Automation System");
        setSize(1000, 700);
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
        panelHeader.setPreferredSize(new Dimension(1000, 100));

        JLabel lblTitle = new JLabel("Operator Dashboard", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblSubtitle = new JLabel("Manage daily operations efficiently", SwingConstants.CENTER);
        lblSubtitle.setForeground(new Color(200, 230, 255));
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        titlePanel.add(lblSubtitle, BorderLayout.SOUTH);

        panelHeader.add(titlePanel, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // Buttons Grid (2x3) - No Subscriber Module
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(2, 3, 30, 30));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(40, 60, 60, 60));
        panelButtons.setBackground(new Color(245, 248, 252));

        Color[] colors = {
            new Color(46, 204, 113),   // Service - Green
            new Color(243, 156, 18),   // Bill - Orange
            new Color(231, 76, 60),    // Payment - Red
            new Color(142, 68, 173),   // Meter - Purple
            new Color(241, 196, 15),   // Complaint - Yellow
            new Color(52, 152, 219)    // Placeholder or Reports - Blue
        };

        Color[] hoverColors = {
            new Color(39, 174, 96),
            new Color(211, 84, 0),
            new Color(192, 57, 43),
            new Color(115, 54, 140),
            new Color(212, 172, 13),
            new Color(41, 128, 185)
        };

        String[] labels = {
            "Service Module",
            "Bill Module",
            "Payment Module",
            "Meter Module",
            "Complaint Module",
            "Reports & Summary"
        };

        JButton[] buttons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            buttons[i] = new ModernButton(labels[i], colors[i], hoverColors[i]);
            buttons[i].setToolTipText("Manage " + labels[i].toLowerCase().replace(" module", "s"));
            panelButtons.add(buttons[i]);
        }

        // Action Listeners
        buttons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { new ServiceForm(); }
        });
        buttons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { new BillForm(); }
        });
        buttons[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { new PaymentForm(); }
        });
        buttons[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { new MeterForm(); }
        });
        buttons[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { new ComplaintForm(); }
        });
        buttons[5].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(OperatorDashboard.this, "Reports module coming soon!", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        add(panelButtons, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(52, 73, 94));
        JLabel footerLabel = new JLabel("© 2025 Utilities Automation System | Operator Access", SwingConstants.CENTER);
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }
}