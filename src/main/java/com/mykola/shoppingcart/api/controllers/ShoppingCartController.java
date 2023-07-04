package com.mykola.shoppingcart.api.controllers;

import com.mykola.shoppingcart.api.dto.ShoppingCartDto;
import com.mykola.shoppingcart.entities.ShoppingCartItem;
import com.mykola.shoppingcart.security.service.CurrentUserContext;
import com.mykola.shoppingcart.service.ShoppingCartItemService;
import com.mykola.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller of shopping-cart
 */
@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartItemService shoppingCartItemService;

    @Autowired
    private CurrentUserContext currentUserContext;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * Get shopping cart's dto with all items
     *
     * @return response entity with status ok and with shopping cart's dto
     */
    @GetMapping()
    public ResponseEntity<ShoppingCartDto> getShoppingCart() {
        Long shoppingCartId = currentUserContext.getCurrentUser().getShoppingCart().getId();
        ShoppingCartDto shoppingCartDto = ShoppingCartDto.fromShoppingCart(shoppingCartService.findById(shoppingCartId));
        return ResponseEntity.ok(shoppingCartDto);
    }

    /**
     * Add the item and its quantity to the cart
     *
     * @param item
     * @return response entity with status ok
     */
    @PostMapping()
    public ResponseEntity<Object> addItemToCart(@RequestBody ShoppingCartItem item) {
        item.setShoppingCart(currentUserContext.getCurrentUser().getShoppingCart());
        shoppingCartItemService.addItemToCart(item);
        return ResponseEntity.ok("Item added successfully");
    }

    /**
     * Delete the item from the cart
     *
     * @param item
     * @return response entity with status ok
     */
    @DeleteMapping()
    public ResponseEntity<String> deleteItem(@RequestBody ShoppingCartItem item) {
        shoppingCartItemService.deleteItemFromCart(item);
        return ResponseEntity.ok("Deletion was successful");
    }

    /**
     * Change the quantity in the cart of one item
     *
     * @param item
     * @return response entity with status ok
     */
    @PatchMapping()
    public ResponseEntity<Object> changeQuantity(@RequestBody ShoppingCartItem item) {
        item.setShoppingCart(currentUserContext.getCurrentUser().getShoppingCart());
        shoppingCartItemService.changeQuantity(item);
        return ResponseEntity.ok("Update was successful");
    }
}
