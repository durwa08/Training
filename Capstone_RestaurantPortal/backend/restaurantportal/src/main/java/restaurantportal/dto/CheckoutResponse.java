package restaurantportal.dto;

import java.util.List;

/**
 * Response DTO used during checkout to show order summary and payment status.
 */
public class CheckoutResponse {

    private double totalAmount;
    private double walletBalance;
    private boolean canPlaceOrder;
    private List<CartItemResponse> items;

    /**
     * Creates a checkout response with order summary and wallet status.
     */
    public CheckoutResponse(double totalAmount, double walletBalance,
                            boolean canPlaceOrder, List<CartItemResponse> items) {
        this.totalAmount = totalAmount;
        this.walletBalance = walletBalance;
        this.canPlaceOrder = canPlaceOrder;
        this.items = items;
    }

    /**
     * Returns total amount of the cart.
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Returns available wallet balance of the user.
     */
    public double getWalletBalance() {
        return walletBalance;
    }

    /**
     * Indicates whether the user can place the order or not.
     */
    public boolean isCanPlaceOrder() {
        return canPlaceOrder;
    }

    /**
     * Returns list of items in the cart.
     */
    public List<CartItemResponse> getItems() {
        return items;
    }
}