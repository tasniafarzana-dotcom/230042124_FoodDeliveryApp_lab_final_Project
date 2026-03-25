package org.fooddelivery.ui;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.OrderStatus;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.User;
import org.fooddelivery.service.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RestaurantOrdersPanel extends JPanel {

    private final User currentUser;
    private final Restaurant restaurant;
    private final IOrderService orderService;
    private final IDeliveryService deliveryService;

    private JPanel ordersListPanel;
    private Timer refreshTimer;

    public RestaurantOrdersPanel(User user, Restaurant restaurant) {
        this.currentUser = user;
        this.restaurant = restaurant;
        this.orderService = new OrderService();
        this.deliveryService = new DeliveryService();
        setLayout(new BorderLayout());
        buildUI();
        loadOrders();
        startAutoRefresh();
    }

    private void buildUI() {
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Orders: " + restaurant.getName(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadOrders());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (refreshTimer != null) refreshTimer.stop();
            MainFrame.getInstance().showPanel(new RestaurantDashboard(currentUser));
        });
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // orders list
        ordersListPanel = new JPanel();
        ordersListPanel.setLayout(new BoxLayout(ordersListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(ordersListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Active Orders"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadOrders() {
        List<Order> orders = orderService.getActiveOrdersByRestaurant(restaurant.getId());
        ordersListPanel.removeAll();
        if (orders.isEmpty()) {
            ordersListPanel.add(new JLabel("  No active orders"));
        } else {
            for (Order order : orders) {
                ordersListPanel.add(createOrderCard(order));
                ordersListPanel.add(Box.createVerticalStrut(5));
            }
        }
        ordersListPanel.revalidate();
        ordersListPanel.repaint();
    }

    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel orderIdLabel = new JLabel("Order: " + order.getId());
        orderIdLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel infoLabel = new JLabel("Total: " + String.format("%.2f", order.getTotalPrice()) +
                " TK | Status: " + order.getStatus() +
                " | Items: " + order.getItems().size());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        if (order.getStatus() == OrderStatus.PLACED) {
            JButton confirmButton = new JButton("Confirm");
            confirmButton.setBackground(new Color(0, 150, 200));
            confirmButton.setForeground(Color.WHITE);
            confirmButton.addActionListener(e -> {
                orderService.updateStatus(order.getId(), OrderStatus.CONFIRMED);
                loadOrders();
            });
            buttonPanel.add(confirmButton);
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            JButton prepareButton = new JButton("Start Preparing");
            prepareButton.setBackground(new Color(200, 150, 0));
            prepareButton.setForeground(Color.WHITE);
            prepareButton.addActionListener(e -> {
                orderService.updateStatus(order.getId(), OrderStatus.PREPARING);
                loadOrders();
            });
            buttonPanel.add(prepareButton);
        }

        if (order.getStatus() == OrderStatus.PREPARING) {
            JButton assignRiderButton = new JButton("Assign Rider");
            assignRiderButton.setBackground(new Color(0, 150, 0));
            assignRiderButton.setForeground(Color.WHITE);
            assignRiderButton.addActionListener(e -> {
                try {
                    deliveryService.assignRider(order.getId());
                    loadOrders();
                    JOptionPane.showMessageDialog(this, "Rider assigned successfully!");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(assignRiderButton);
        }

        if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            JButton deliveredButton = new JButton("Mark Delivered");
            deliveredButton.setBackground(new Color(0, 200, 0));
            deliveredButton.setForeground(Color.WHITE);
            deliveredButton.addActionListener(e -> {
                orderService.updateStatus(order.getId(), OrderStatus.DELIVERED);
                loadOrders();
            });
            buttonPanel.add(deliveredButton);
        }

        card.add(orderIdLabel, BorderLayout.NORTH);
        card.add(infoLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        return card;
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(15000, e -> loadOrders());
        refreshTimer.start();
    }
}
