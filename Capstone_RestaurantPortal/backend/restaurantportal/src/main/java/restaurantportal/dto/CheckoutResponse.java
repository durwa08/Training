
package restaurantportal.dto;

import java.util.List;

public class CheckoutResponse {

    private double totalAmount;
    private double walletBalance;
    private boolean canPlaceOrder;
    private List<CartItemResponse> items;

    public CheckoutResponse(double totalAmount, double walletBalance,
                            boolean canPlaceOrder, List<CartItemResponse> items) {
        this.totalAmount = totalAmount;
        this.walletBalance = walletBalance;
        this.canPlaceOrder = canPlaceOrder;
        this.items = items;
    }

    public double getTotalAmount() { return totalAmount; }
    public double getWalletBalance() { return walletBalance; }
    public boolean isCanPlaceOrder() { return canPlaceOrder; }
    public List<CartItemResponse> getItems() { return items; }
}