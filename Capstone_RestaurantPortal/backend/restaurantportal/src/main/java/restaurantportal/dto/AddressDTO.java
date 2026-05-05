package restaurantportal.dto;

import lombok.*;

/**
 * DTO for transferring address data between layers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private Long id;

    private String street;

    private String city;

    private String state;

    private String pincode;

    private Long userId;
}