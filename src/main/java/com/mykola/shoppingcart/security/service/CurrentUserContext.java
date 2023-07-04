package com.mykola.shoppingcart.security.service;

import com.mykola.shoppingcart.api.dto.UserDetailDto;
import com.mykola.shoppingcart.entities.User;
import org.springframework.stereotype.Component;


/**
 * Context with current user (TODO)
 */
@Component
public class CurrentUserContext {

    public UserDetailDto getCurrentUser() {
        //TODO getting a current user
        return UserDetailDto.fromUser(new User());
    }
}
