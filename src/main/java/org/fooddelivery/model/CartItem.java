package org.fooddelivery.model;

import java.util.ArrayList;
import java.util.List;

public class CartItem {
    private MenuItem menuItem;
    private int quantity;
    private List<AddOn> selectedAddOns;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.selectedAddOns = new ArrayList<>();
    }

    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public List<AddOn> getSelectedAddOns() { return selectedAddOns; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void addSelectedAddOn(AddOn addOn) { this.selectedAddOns.add(addOn); }

    public double getTotalPrice() {
        double addOnTotal = selectedAddOns.stream().mapToDouble(AddOn::getPrice).sum();
        return (menuItem.getPrice() + addOnTotal) * quantity;
    }
}
