package org.fooddelivery.ui;

import org.fooddelivery.model.Cart;
import org.fooddelivery.model.CartItem;
import org.fooddelivery.model.User;
import org.fooddelivery.service.CartService;
import org.fooddelivery.service.ICartService;

import javax.swing.*;
import java.awt.*;

public class CartPanel extends JPanel {

    private final User currentUser;
    private final Cart cart;
    private final ICartService cartService;

    private JPanel cartItemsPanel;
    private JLabel totalLabel;
    private JTextField couponField;

    public CartPanel(User user, Cart cart) {
        this.currentUser = user;
        this.cart = cart;
        this.cartService = new CartService();
        setLayout(new BorderLayout());
        buildUI();
        loadCartItems();
    }

    private void buildUI() {
        // top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Your Cart", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> MainFrame.getInstance().showPanel(new CustomerDashboard(currentUser)));
        topPanel.add(backButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // cart items
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Items"));
        add(scrollPane, BorderLayout.CENTER);

        // bottom panel
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // coupon
        gbc.gridx = 0; gbc.gridy = 0;
        bottomPanel.add(new JLabel("Coupon:"), gbc);
        couponField = new JTextField(15);
        gbc.gridx = 1;
        bottomPanel.add(couponField, gbc);
        JButton applyCouponButton = new JButton("Apply");
        applyCouponButton.addActionListener(e -> handleApplyCoupon());
        gbc.gridx = 2;
        bottomPanel.add(applyCouponButton, gbc);

        // total
        totalLabel = new JLabel("Total: 0.00 TK");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        bottomPanel.add(totalLabel, gbc);

        // checkout button
        JButton checkoutButton = new JButton("Proceed to Payment");
        checkoutButton.setBackground(new Color(0, 150, 0));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.addActionListener(e -> handleCheckout());
        gbc.gridx = 2; gbc.gridy = 1; gbc.gridwidth = 1;
        bottomPanel.add(checkoutButton, gbc);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadCartItems() {
        cartItemsPanel.removeAll();
        if (cart.getItems().isEmpty()) {
            cartItemsPanel.add(new JLabel("  Cart is empty"));
        } else {
            for (CartItem item : cart.getItems()) {
                cartItemsPanel.add(createCartItemCard(item));
                cartItemsPanel.add(Box.createVerticalStrut(5));
            }
        }
        updateTotal();
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private JPanel createCartItemCard(CartItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel nameLabel = new JLabel(item.getMenuItem().getName() +
                " x" + item.getQuantity());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel priceLabel = new JLabel(String.format("%.2f TK", item.getTotalPrice()));

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> {
            cartService.removeItem(cart, item.getMenuItem().getId());
            loadCartItems();
        });

        card.add(nameLabel, BorderLayout.WEST);
        card.add(priceLabel, BorderLayout.CENTER);
        card.add(removeButton, BorderLayout.EAST);
        return card;
    }

    private void handleApplyCoupon() {
        String code = couponField.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a coupon code");
            return;
        }
        try {
            cartService.applyCoupon(cart, code);
            updateTotal();
            JOptionPane.showMessageDialog(this, "Coupon applied!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCheckout() {
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }
        MainFrame.getInstance().showPanel(new PaymentPanel(currentUser, cart));
    }

    private void updateTotal() {
        double total = cartService.calculateTotal(cart);
        totalLabel.setText("Total: " + String.format("%.2f", total) + " TK");
    }
}
