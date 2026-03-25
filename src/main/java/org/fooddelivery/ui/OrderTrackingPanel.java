package org.fooddelivery.ui;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.model.User;
import org.fooddelivery.service.IOrderService;
import org.fooddelivery.service.OrderService;

import javax.swing.*;
import java.awt.*;

public class OrderTrackingPanel extends JPanel {

    private final User currentUser;
    private final Order order;
    private final IOrderService orderService;

    private JLabel statusLabel;
    private JProgressBar progressBar;
    private Timer refreshTimer;

    public OrderTrackingPanel(User user, Order order) {
        this.currentUser = user;
        this.order = order;
        this.orderService = new OrderService();
        setLayout(new BorderLayout());
        buildUI();
        startAutoRefresh();
    }

    private void buildUI() {
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Order Tracking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> {
            if (refreshTimer != null) refreshTimer.stop();
            MainFrame.getInstance().showPanel(new CustomerDashboard(currentUser));
        });
        topPanel.add(backButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // center panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel orderIdLabel = new JLabel("Order ID: " + order.getId(), SwingConstants.CENTER);
        orderIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(orderIdLabel, gbc);

        JLabel amountLabel = new JLabel("Total: " + String.format("%.2f", order.getTotalPrice()) + " TK",
                SwingConstants.CENTER);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 1;
        centerPanel.add(amountLabel, gbc);

        statusLabel = new JLabel("Status: " + order.getStatus(), SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(new Color(0, 100, 200));
        gbc.gridy = 2;
        centerPanel.add(statusLabel, gbc);

        progressBar = new JProgressBar(0, 4);
        progressBar.setValue(getProgressValue(order.getStatus()));
        progressBar.setStringPainted(true);
        progressBar.setString(order.getStatus().toString());
        progressBar.setPreferredSize(new Dimension(400, 30));
        gbc.gridy = 3;
        centerPanel.add(progressBar, gbc);

        // status steps
        JPanel stepsPanel = new JPanel(new GridLayout(1, 5));
        stepsPanel.add(createStepLabel("PLACED", order.getStatus()));
        stepsPanel.add(createStepLabel("CONFIRMED", order.getStatus()));
        stepsPanel.add(createStepLabel("PREPARING", order.getStatus()));
        stepsPanel.add(createStepLabel("ON THE WAY", order.getStatus()));
        stepsPanel.add(createStepLabel("DELIVERED", order.getStatus()));
        gbc.gridy = 4;
        centerPanel.add(stepsPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshStatus());
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JLabel createStepLabel(String step, OrderStatus currentStatus) {
        JLabel label = new JLabel(step, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label.setOpaque(true);

        if (isStepCompleted(step, currentStatus)) {
            label.setBackground(new Color(0, 200, 0));
            label.setForeground(Color.WHITE);
        } else {
            label.setBackground(Color.LIGHT_GRAY);
        }
        return label;
    }

    private boolean isStepCompleted(String step, OrderStatus status) {
        int stepValue = getStepValue(step);
        int statusValue = getProgressValue(status);
        return stepValue <= statusValue;
    }

    private int getStepValue(String step) {
        return switch (step) {
            case "PLACED" -> 1;
            case "CONFIRMED" -> 2;
            case "PREPARING" -> 3;
            case "ON THE WAY" -> 4;
            case "DELIVERED" -> 5;
            default -> 0;
        };
    }

    private int getProgressValue(OrderStatus status) {
        return switch (status) {
            case PLACED -> 1;
            case CONFIRMED -> 2;
            case PREPARING -> 3;
            case OUT_FOR_DELIVERY -> 4;
            case DELIVERED -> 5;
            default -> 0;
        };
    }

    private void refreshStatus() {
        orderService.getOrdersByUser(currentUser.getId()).stream()
                .filter(o -> o.getId().equals(order.getId()))
                .findFirst()
                .ifPresent(updated -> {
                    statusLabel.setText("Status: " + updated.getStatus());
                    progressBar.setValue(getProgressValue(updated.getStatus()));
                    progressBar.setString(updated.getStatus().toString());
                    buildUI();
                });
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(10000, e -> refreshStatus());
        refreshTimer.start();
    }
}