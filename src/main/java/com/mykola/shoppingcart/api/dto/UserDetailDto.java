package com.mykola.shoppingcart.api.dto;

import com.mykola.shoppingcart.entities.ShoppingCart;
import com.mykola.shoppingcart.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User Dto
 */
@Data
@AllArgsConstructor
public class UserDetailDto {
    private Long id;
    private String username;
    private String email;
    private ShoppingCart shoppingCart;

    /**
     * get user dto from user
     *
     * @param user - current user
     * @return User Dto
     */
    public static UserDetailDto fromUser(User user) {
        UserDetailDto userDto = new UserDetailDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getShoppingCart());
        return userDto;
    }
}
