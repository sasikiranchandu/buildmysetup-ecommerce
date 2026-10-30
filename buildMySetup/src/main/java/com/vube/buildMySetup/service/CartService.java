package com.vube.buildMySetup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vube.buildMySetup.model.Cart;
import com.vube.buildMySetup.model.Component;
import com.vube.buildMySetup.model.User;
import com.vube.buildMySetup.repository.CartRepo;
import com.vube.buildMySetup.repository.ComponentRepo;
import com.vube.buildMySetup.repository.OrderRepo;
import com.vube.buildMySetup.repository.UserRepo;

@Service
public class CartService {
	
	@Autowired
	private CartRepo  cartRepo;
	
	@Autowired
	private ComponentRepo componentRepo;
	
	@Autowired
	OrderRepo orderRepo;

	@Autowired
	UserRepo userRepo;

//	add to cart
	public void addToCart(User user, Component component, int quantity) {

        Cart cart = cartRepo.findByUserAndComponent(user, component).orElse(null);

        if (cart != null) {
            cart.setQuantity(cart.getQuantity() + quantity);
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setComponent(component);
            cart.setQuantity(quantity);
        }

        cartRepo.save(cart);
    }

    // GET CART
    public List<Cart> getCartByUser(User user) {
        return cartRepo.findByUser(user);
    }

    // REMOVE
    public void removeItem(Long id) {
        cartRepo.deleteById(id);
    }

    // CLEAR
    public void clearCart(User user) {
        cartRepo.deleteByUser(user);
    }
    public void updateQuantity(Long cartId, int change) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        int newQty = cart.getQuantity() + change;

        if (newQty <= 0) {
            cartRepo.delete(cart);
        } else {
            cart.setQuantity(newQty);
            cartRepo.save(cart);
        }
    }

	
}
