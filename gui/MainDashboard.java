package com.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainDashboard extends JFrame {

    // Custom Button with rounded corners and hover effect
    class ModernButton extends JButton {
        private Color backgroundColor;
        private Color hoverColor;

        public ModernButton(String text, Color bg, final Color hover) {
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

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBackground(hover);
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No border
        }
    }

    public MainDashboard() {
        setTitle("Utilities Automation System Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Modern Header with gradient effect feel
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
        panelHeader.setPreferredSize(new Dimension(1000, 100));

        JLabel lblTitle = new JLabel("Utilities Automation System", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblSubtitle = new JLabel("Smart Management Dashboard", SwingConstants.CENTER);
        lblSubtitle.setForeground(new Color(200, 230, 255));
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        titlePanel.add(lblSubtitle, BorderLayout.SOUTH);

        panelHeader.add(titlePanel, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        // Main Buttons Panel with Card Layout
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(2, 3, 30, 30));
        panelButtons.setBorder(BorderFactory.createEmptyBorder(40, 60, 60, 60));
        panelButtons.setBackground(new Color(245, 248, 252));

        // Define modern colors
        Color[] colors = {
            new Color(52, 152, 219),   // Blue
            new Color(46, 204, 113),   // Green
            new Color(243, 156, 18),   // Orange
            new Color(231, 76, 60),    // Red
            new Color(142, 68, 173),   // Purple
            new Color(241, 196, 15)    // Yellow
        };

        Color[] hoverColors = {
            new Color(41, 128, 185),
            new Color(39, 174, 96),
            new Color(211, 84, 0),
            new Color(192, 57, 43),
            new Color(115, 54, 140),
            new Color(212, 172, 13)
        };

        String[] labels = {
            "Subscriber Module",
            "Service Module",
            "Bill Module",
            "Payment Module",
            "Meter Module",
            "Complaint Module"
        };

        String[] tooltips = {
            "Add, view, and manage subscribers",
            "Configure and manage services",
            "Generate and track billing",
            "Process and record payments",
            "Manage meter readings and devices",
            "Handle customer complaints efficiently"
        };

        // Create buttons
        JButton[] buttons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            buttons[i] = new ModernButton(labels[i], colors[i], hoverColors[i]);
            buttons[i].setToolTipText(tooltips[i]);
            buttons[i].setPreferredSize(new Dimension(200, 120));
            panelButtons.add(buttons[i]);
        }

        // Action Listeners (without lambda)
        buttons[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SubscriberForm();
            }
        });

        buttons[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ServiceForm();
            }
        });

        buttons[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BillForm();
            }
        });

        buttons[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PaymentForm();
            }
        });

        buttons[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MeterForm();
            }
        });

        buttons[5].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ComplaintForm();
            }
        });

        add(panelButtons, BorderLayout.CENTER);

        // Footer (optional subtle status)
        JPanel footer = new JPanel();
        footer.setBackground(new Color(52, 73, 94));
        JLabel footerLabel = new JLabel("© 2025 Utilities Automation System | Version 2.0", SwingConstants.CENTER);
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Use Swing thread safety
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Optional: Set Nimbus Look and Feel for modern appearance
                    UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MainDashboard();
            }
        });
    }
}