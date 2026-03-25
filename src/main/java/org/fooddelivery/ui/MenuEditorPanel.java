package org.fooddelivery.ui;

import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.User;
import org.fooddelivery.service.IMenuService;
import org.fooddelivery.service.MenuService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuEditorPanel extends JPanel {

    private final User currentUser;
    private final Restaurant restaurant;
    private final IMenuService menuService;

    private JPanel menuListPanel;

    public MenuEditorPanel(User user, Restaurant restaurant) {
        this.currentUser = user;
        this.restaurant = restaurant;
        this.menuService = new MenuService();
        setLayout(new BorderLayout());
        buildUI();
        loadMenuItems();
    }

    private void buildUI() {
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Menu: " + restaurant.getName(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> showAddItemDialog());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e ->
                MainFrame.getInstance().showPanel(new RestaurantDashboard(currentUser)));
        buttonPanel.add(addItemButton);
        buttonPanel.add(backButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // menu list
        menuListPanel = new JPanel();
        menuListPanel.setLayout(new BoxLayout(menuListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(menuListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadMenuItems() {
        List<MenuItem> items = menuService.getAvailableItems(restaurant.getId());
        menuListPanel.removeAll();
        if (items.isEmpty()) {
            menuListPanel.add(new JLabel("  No items yet. Click 'Add Item' to add."));
        } else {
            for (MenuItem item : items) {
                menuListPanel.add(createMenuItemCard(item));
                menuListPanel.add(Box.createVerticalStrut(5));
            }
        }
        menuListPanel.revalidate();
        menuListPanel.repaint();
    }

    private JPanel createMenuItemCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel infoLabel = new JLabel(item.getCategory() + " | " +
                item.getPrice() + " TK | Stock: " + item.getQuantity());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JToggleButton availableToggle = new JToggleButton(
                item.isAvailable() ? "Available" : "Unavailable", item.isAvailable());
        availableToggle.addActionListener(e -> {
            menuService.updateAvailability(item.getId(), availableToggle.isSelected());
            availableToggle.setText(availableToggle.isSelected() ? "Available" : "Unavailable");
        });

        JButton updateQtyButton = new JButton("Update Stock");
        updateQtyButton.addActionListener(e -> showUpdateQuantityDialog(item));

        buttonPanel.add(availableToggle);
        buttonPanel.add(updateQtyButton);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(infoLabel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        return card;
    }

    private void showAddItemDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add Menu Item");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(20);
        JTextField descField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField categoryField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        dialog.add(descField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Price (TK):"), gbc);
        gbc.gridx = 1;
        dialog.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        dialog.add(categoryField, gbc);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                String category = categoryField.getText().trim();

                menuService.addMenuItem(restaurant.getId(), name, desc, price, category);
                dialog.dispose();
                loadMenuItems();
                JOptionPane.showMessageDialog(this, "Item added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage());
            }
        });

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        dialog.setVisible(true);
    }

    private void showUpdateQuantityDialog(MenuItem item) {
        String input = JOptionPane.showInputDialog(this,
                "Enter new stock quantity for " + item.getName() + ":",
                item.getQuantity());
        if (input != null) {
            try {
                int qty = Integer.parseInt(input.trim());
                menuService.updateQuantity(item.getId(), qty);
                loadMenuItems();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }
}