package org.fooddelivery.ui;

import org.fooddelivery.model.Cart;
import org.fooddelivery.model.Order;
import org.fooddelivery.model.Payment;
import org.fooddelivery.model.User;
import org.fooddelivery.service.*;

import javax.swing.*;
import java.awt.*;

public class PaymentPanel extends JPanel {

    private final User currentUser;
    private final Cart cart;
    private final IOrderService orderService;
    private final IPaymentService paymentService;

    private JComboBox<String> paymentMethodCombo;
    private JLabel totalLabel;

    public PaymentPanel(User user, Cart cart) {
        this.currentUser = user;
        this.cart = cart;
        this.orderService = new OrderService();
        this.paymentService = new PaymentService();
        setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        double total = new CartService().calculateTotal(cart);
        totalLabel = new JLabel("Amount to Pay: " + String.format("%.2f", total) + " TK",
                SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 1;
        add(totalLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Payment Method:"), gbc);
        paymentMethodCombo = new JComboBox<>(new String[]{"CASH", "CARD", "MOBILE_BANKING"});
        gbc.gridx = 1;
        add(paymentMethodCombo, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> MainFrame.getInstance().showPanel(new CartPanel(currentUser, cart)));
        gbc.gridx = 0; gbc.gridy = 3;
        add(backButton, gbc);

        JButton confirmButton = new JButton("Confirm Payment");
        confirmButton.setBackground(new Color(0, 150, 0));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addActionListener(e -> handlePayment());
        gbc.gridx = 1;
        add(confirmButton, gbc);
    }

    private void handlePayment() {
        try {
            // place order
            Order order = orderService.placeOrder(cart, currentUser.getAddressIds().isEmpty()
                    ? "DEFAULT" : currentUser.getAddressIds().get(0));

            // process payment
            String method = (String) paymentMethodCombo.getSelectedItem();
            Payment payment = paymentService.processPayment(order.getId(), method, order.getTotalPrice());

            JOptionPane.showMessageDialog(this,
                    "Payment Successful!\nOrder ID: " + order.getId() +
                            "\nTransaction ID: " + payment.getTransactionId());

            MainFrame.getInstance().showPanel(new OrderTrackingPanel(currentUser, order));

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}