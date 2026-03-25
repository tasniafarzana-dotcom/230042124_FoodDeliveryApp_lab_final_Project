package org.fooddelivery.ui;

import org.fooddelivery.model.Cart;
import org.fooddelivery.model.MenuItem;
import org.fooddelivery.model.Restaurant;
import org.fooddelivery.model.User;
import org.fooddelivery.service.CartService;
import org.fooddelivery.service.ICartService;
import org.fooddelivery.service.IMenuService;
import org.fooddelivery.service.MenuService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuPanel extends JPanel {

    private final User currentUser;
    private final Restaurant restaurant;
    private final IMenuService menuService;
    private final ICartService cartService;
    private final Cart cart;

    private JPanel menuListPanel;
    private JLabel totalLabel;

    public MenuPanel(User user, Restaurant restaurant) {
        this.currentUser = user;
        this.restaurant = restaurant;
        this.menuService = new MenuService();
        this.cartService = new CartService();
        this.cart = new Cart(user.getId(), restaurant.getId());
        setLayout(new BorderLayout());
        buildUI();
        loadMenuItems();
    }

    private void buildUI() {
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(restaurant.getName(), SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> MainFrame.getInstance().showPanel(new CustomerDashboard(currentUser)));
        topPanel.add(backButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // menu list
        menuListPanel = new JPanel();
        menuListPanel.setLayout(new BoxLayout(menuListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(menuListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Menu Items"));
        add(scrollPane, BorderLayout.CENTER);

        // bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: 0.00 TK");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JButton viewCartButton = new JButton("View Cart");
        viewCartButton.addActionListener(e -> MainFrame.getInstance().showPanel(new CartPanel(currentUser, cart)));
        bottomPanel.add(viewCartButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMenuItems() {
        List<MenuItem> items = menuService.getAvailableItems(restaurant.getId());
        menuListPanel.removeAll();
        if (items.isEmpty()) {
            menuListPanel.add(new JLabel("No items available"));
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

        JLabel infoLabel = new JLabel(item.getDescription() + " | " +
                item.getPrice() + " TK | " + item.getCategory());

        JPanel rightPanel = new JPanel(new FlowLayout());
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> {
            int qty = (int) quantitySpinner.getValue();
            try {
                cartService.addItem(cart, item, qty, null);
                totalLabel.setText("Total: " + String.format("%.2f", cartService.calculateTotal(cart)) + " TK");
                JOptionPane.showMessageDialog(this, item.getName() + " added to cart!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        rightPanel.add(new JLabel("Qty:"));
        rightPanel.add(quantitySpinner);
        rightPanel.add(addButton);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(infoLabel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);
        return card;
    }
}