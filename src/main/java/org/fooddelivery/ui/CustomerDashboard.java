package org.fooddelivery.ui;

import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.User;
import org.fooddelivery.service.IRestaurantService;
import org.fooddelivery.service.ISearchService;
import org.fooddelivery.service.RestaurantService;
import org.fooddelivery.service.SearchService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerDashboard extends JPanel {

    private final User currentUser;
    private final IRestaurantService restaurantService;
    private final ISearchService searchService;

    private JTextField searchField;
    private JComboBox<String> cuisineFilter;
    private JPanel restaurantListPanel;

    public CustomerDashboard(User user) {
        this.currentUser = user;
        this.restaurantService = new RestaurantService();
        this.searchService = new SearchService();
        setLayout(new BorderLayout());
        buildUI();
        loadRestaurants();
    }

    private void buildUI() {
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> MainFrame.getInstance().showPanel(new LoginScreen()));
        topPanel.add(logoutButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        cuisineFilter = new JComboBox<>(new String[]{"All", "Bangladeshi", "Chinese", "Indian", "Fast Food", "Italian"});
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> handleSearch());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Cuisine:"));
        searchPanel.add(cuisineFilter);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.CENTER);

        // restaurant list
        restaurantListPanel = new JPanel();
        restaurantListPanel.setLayout(new BoxLayout(restaurantListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(restaurantListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Restaurants"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void loadRestaurants() {
        List<Restaurant> restaurants = restaurantService.findNearestRestaurants(23.8103, 90.4125, 50);
        displayRestaurants(restaurants);
    }

    private void handleSearch() {
        String query = searchField.getText().trim();
        String cuisine = cuisineFilter.getSelectedItem().equals("All") ? "" : (String) cuisineFilter.getSelectedItem();
        List<Restaurant> results = searchService.searchRestaurants(query, cuisine);
        displayRestaurants(results);
    }

    private void displayRestaurants(List<Restaurant> restaurants) {
        restaurantListPanel.removeAll();
        if (restaurants.isEmpty()) {
            restaurantListPanel.add(new JLabel("No restaurants found"));
        } else {
            for (Restaurant r : restaurants) {
                JPanel card = createRestaurantCard(r);
                restaurantListPanel.add(card);
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
                restaurant.getAddress().getArea() + " | Rating: " +
                String.format("%.1f", restaurant.getRating()));

        JButton viewMenuButton = new JButton("View Menu");
        viewMenuButton.addActionListener(e ->
                MainFrame.getInstance().showPanel(new MenuPanel(currentUser, restaurant)));

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(infoLabel, BorderLayout.CENTER);
        card.add(viewMenuButton, BorderLayout.EAST);
        return card;
    }
}