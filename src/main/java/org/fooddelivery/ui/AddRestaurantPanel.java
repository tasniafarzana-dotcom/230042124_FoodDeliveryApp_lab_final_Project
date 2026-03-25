package org.fooddelivery.ui;

import org.fooddelivery.model.Address;
import org.fooddelivery.model.User;
import org.fooddelivery.service.IRestaurantService;
import org.fooddelivery.service.RestaurantService;
import org.fooddelivery.util.IdGenerator;

import javax.swing.*;
import java.awt.*;

public class AddRestaurantPanel extends JPanel {

    private final User currentUser;
    private final IRestaurantService restaurantService;

    private JTextField nameField;
    private JTextField cuisineField;
    private JTextField phoneField;
    private JTextField streetField;
    private JTextField areaField;
    private JTextField cityField;
    private JTextField latField;
    private JTextField lngField;
    private JLabel messageLabel;

    public AddRestaurantPanel(User user) {
        this.currentUser = user;
        this.restaurantService = new RestaurantService();
        setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add New Restaurant", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Restaurant Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Cuisine Type:"), gbc);
        cuisineField = new JTextField(20);
        gbc.gridx = 1;
        add(cuisineField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Street:"), gbc);
        streetField = new JTextField(20);
        gbc.gridx = 1;
        add(streetField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Area:"), gbc);
        areaField = new JTextField(20);
        gbc.gridx = 1;
        add(areaField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("City:"), gbc);
        cityField = new JTextField(20);
        gbc.gridx = 1;
        add(cityField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Latitude:"), gbc);
        latField = new JTextField("23.8103", 20);
        gbc.gridx = 1;
        add(latField, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Longitude:"), gbc);
        lngField = new JTextField("90.4125", 20);
        gbc.gridx = 1;
        add(lngField, gbc);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        add(messageLabel, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e ->
                MainFrame.getInstance().showPanel(new RestaurantDashboard(currentUser)));
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 1;
        add(backButton, gbc);

        JButton saveButton = new JButton("Save Restaurant");
        saveButton.setBackground(new Color(0, 150, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> handleSave());
        gbc.gridx = 1;
        add(saveButton, gbc);
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        String cuisine = cuisineField.getText().trim();
        String phone = phoneField.getText().trim();
        String street = streetField.getText().trim();
        String area = areaField.getText().trim();
        String city = cityField.getText().trim();

        if (name.isEmpty() || cuisine.isEmpty() || phone.isEmpty() ||
                street.isEmpty() || area.isEmpty() || city.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        try {
            double lat = Double.parseDouble(latField.getText().trim());
            double lng = Double.parseDouble(lngField.getText().trim());

            Address address = new Address(IdGenerator.generateAddressId(),
                    "Main", street, area, city, lat, lng);

            restaurantService.registerRestaurant(currentUser.getId(), name, cuisine, phone, address);

            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Restaurant added successfully!");
            Timer timer = new Timer(1500, e ->
                    MainFrame.getInstance().showPanel(new RestaurantDashboard(currentUser)));
            timer.setRepeats(false);
            timer.start();

        } catch (NumberFormatException ex) {
            messageLabel.setText("Invalid latitude or longitude");
        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }
}
