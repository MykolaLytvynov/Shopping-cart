package com.mykola.shoppingcart.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mykola.shoppingcart.api.dto.UserDetailDto;
import com.mykola.shoppingcart.entities.Product;
import com.mykola.shoppingcart.entities.ShoppingCart;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import com.mykola.shoppingcart.entities.User;
import com.mykola.shoppingcart.security.service.CurrentUserContext;
import com.mykola.shoppingcart.service.ShoppingCartItemService;
import com.mykola.shoppingcart.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ShoppingCartController.class)
class ShoppingCartControllerTest {

    @MockBean
    private ShoppingCartItemService shoppingCartItemService;

    @MockBean
    private CurrentUserContext currentUserContext;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Autowired
    private MockMvc mockMvc;

    private User currentUser;

    @BeforeEach
    void initializeData () {
        currentUser = new User().builder()
                .id(1l)
                .username("User 1")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        ShoppingCart shoppingCart = new ShoppingCart().builder()
                .id(1l)
                .user(currentUser)
                .itemList(new ArrayList<>())
                .build();

        List<ShoppingCartItem> itemList = new ArrayList<>();
        itemList.add(new ShoppingCartItem().builder()
                        .product(new Product().builder()
                                .title("Table")
                                .price(new BigDecimal("40"))
                                .build())
                        .quantity(2)
                .build());
        itemList.add(new ShoppingCartItem().builder()
                .product(new Product().builder()
                        .title("Tablet")
                        .price(new BigDecimal("13"))
                        .build())
                .quantity(1)
                .build());
        shoppingCart.setItemList(itemList);
        currentUser.setShoppingCart(shoppingCart);
    }

    @Test
    @DisplayName("Get shopping-cart")
    public void getShoppingCartShouldGetShoppingCart() throws Exception {
        when(currentUserContext.getCurrentUser()).thenReturn(UserDetailDto.fromUser(currentUser));
        when(shoppingCartService.findById(1l)).thenReturn(currentUser.getShoppingCart());

        mockMvc.perform(MockMvcRequestBuilders.get("/shopping-cart"))
                .andExpect(jsonPath("$.id").value(currentUser.getShoppingCart().getId()))
                //sumOfAllGoods - (40*2) + (13*1) = 93
                .andExpect(jsonPath("$.sumOfAllGoods").value(93))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Add products to the shopping cart")
    public void addItemToCartShouldReturnOk() throws Exception {
        when(currentUserContext.getCurrentUser()).thenReturn(UserDetailDto.fromUser(currentUser));

        ShoppingCartItem item = new ShoppingCartItem();
        doNothing().when(shoppingCartItemService).addItemToCart(item);
        mockMvc.perform(MockMvcRequestBuilders.post("/shopping-cart")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(item)))
                .andExpect(status().isOk());
        verify(shoppingCartItemService).addItemToCart(ArgumentMatchers.any(ShoppingCartItem.class));
    }

    @Test
    @DisplayName("Removing products from the shopping cart")
    public void deleteItemShouldReturnOk() throws Exception {

        ShoppingCartItem item = new ShoppingCartItem();
        doNothing().when(shoppingCartItemService).deleteItemFromCart(item);
        mockMvc.perform(MockMvcRequestBuilders.delete("/shopping-cart")
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(item)))
                .andExpect(status().isOk());
        verify(shoppingCartItemService).deleteItemFromCart(ArgumentMatchers.any(ShoppingCartItem.class));
    }

    @Test
    @DisplayName("Removing products from the shopping cart")
    public void changeQuantityShouldReturnOk() throws Exception {
        when(currentUserContext.getCurrentUser()).thenReturn(UserDetailDto.fromUser(currentUser));

        ShoppingCartItem item = new ShoppingCartItem();
        doNothing().when(shoppingCartItemService).changeQuantity(item);
        mockMvc.perform(MockMvcRequestBuilders.patch("/shopping-cart")
                        .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(item)))
                .andExpect(status().isOk());
        verify(shoppingCartItemService).changeQuantity(ArgumentMatchers.any(ShoppingCartItem.class));
    }

}
