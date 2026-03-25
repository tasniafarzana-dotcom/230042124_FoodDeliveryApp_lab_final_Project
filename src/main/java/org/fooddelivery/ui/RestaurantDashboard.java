package org.fooddelivery.ui;

import org.fooddelivery.model.Order;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.User;
import org.fooddelivery.service.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RestaurantDashboard extends JPanel {

    private final User currentUser;
    private final IRestaurantService restaurantService;
    private final IOrderService orderService;

    private JPanel restaurantListPanel;

    public RestaurantDashboard(User user) {
        this.currentUser = user;
        this.restaurantService = new RestaurantService();
        this.orderService = new OrderService();
        setLayout(new BorderLayout());
        buildUI();
        loadRestaurants();
    }

    private void buildUI() {
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Restaurant Owner: " + currentUser.getName(),
                SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addRestaurantButton = new JButton("Add Restaurant");
        addRestaurantButton.addActionListener(e ->
                MainFrame.getInstance().showPanel(new AddRestaurantPanel(currentUser)));
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> MainFrame.getInstance().showPanel(new LoginScreen()));
        buttonPanel.add(addRestaurantButton);
        buttonPanel.add(logoutButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // restaurant list
        restaurantListPanel = new JPanel();
        restaurantListPanel.setLayout(new BoxLayout(restaurantListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(restaurantListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("My Restaurants"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadRestaurants() {
        List<Restaurant> restaurants = restaurantService.getOwnerRestaurants(currentUser.getId());
        restaurantListPanel.removeAll();
        if (restaurants.isEmpty()) {
            restaurantListPanel.add(new JLabel("  No restaurants yet. Click 'Add Restaurant' to get started."));
        } else {
            for (Restaurant r : restaurants) {
                restaurantListPanel.add(createRestaurantCard(r));
                restaurantListPanel.add(Box.createVerticalStrut(5));
            }
        }
        restaurantListPanel.revalidate();
        restaurantListPanel.repaint();
    }

    private JPanel createRestaurantCard(Restaurant restaurant) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel nameLabel = new JLabel(restaurant.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel infoLabel = new JLabel(restaurant.getCuisineType() + " | " +
                restaurant.getAddress().getArea() + " | " +
                (restaurant.isOpen() ? "OPEN" : "CLOSED"));

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JToggleButton openToggle = new JToggleButton(restaurant.isOpen() ? "Close" : "Open",
                restaurant.isOpen());
        openToggle.addActionListener(e -> {
            restaurantService.setOpenStatus(restaurant.getId(), openToggle.isSelected());
            openToggle.setText(openToggle.isSelected() ? "Close" : "Open");
            loadRestaurants();
        });

        JButton manageMenuButton = new JButton("Manage Menu");
        manageMenuButton.addActionListener(e ->
                MainFrame.getInstance().showPanel(new MenuEditorPanel(currentUser, restaurant)));

        JButton viewOrdersButton = new JButton("View Orders");
        viewOrdersButton.addActionListener(e ->
                MainFrame.getInstance().showPanel(new RestaurantOrdersPanel(currentUser, restaurant)));

        buttonPanel.add(openToggle);
        buttonPanel.add(manageMenuButton);
        buttonPanel.add(viewOrdersButton);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(infoLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        return card;
    }
}