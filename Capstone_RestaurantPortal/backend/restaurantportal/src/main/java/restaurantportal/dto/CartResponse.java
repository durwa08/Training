package restaurantportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {

    private Long cartId;
    private Double totalAmount;
    private List<CartItemResponse> items;
}