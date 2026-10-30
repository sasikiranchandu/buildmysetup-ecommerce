package com.vube.buildMySetup.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vube.buildMySetup.model.Component;
import com.vube.buildMySetup.model.User;
import com.vube.buildMySetup.service.CartService;
import com.vube.buildMySetup.service.Componentservice;
import com.vube.buildMySetup.service.UserService;

@Controller
@RequestMapping("/user")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private Componentservice componentService;

    @Autowired
    private UserService userService;

    // ✅ ADD TO CART
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long componentId,
                           @RequestParam int quantity,
                           Authentication auth) {

        if (auth == null || !auth.isAuthenticated() 
            || auth.getName().equals("anonymousUser")) {
            return "redirect:/login";
        }

        String loginValue = auth.getName();

        User user = userService.findByEmail(loginValue)
                .orElseGet(() -> userService.findByName(loginValue)
                .orElseThrow(() -> new RuntimeException("User not found: " + loginValue)));

        Component component = componentService.getComponentById(componentId);

        cartService.addToCart(user, component, quantity);

        return "redirect:/user/cart";
    }
    // ✅ VIEW CART
    @GetMapping("/cart")
    public String viewCart(Model model, Authentication auth) {

        String loginValue = auth.getName();

        User user = userService.findByEmail(loginValue)
                .orElseGet(() -> userService.findByName(loginValue)
                .orElseThrow(() -> new RuntimeException("User not found")));

        var cartItems = cartService.getCartByUser(user);

        double total = cartItems.stream()
                .mapToDouble(c -> c.getComponent().getPrice() * c.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);

        return "cart";
    }

    // ✅ REMOVE ITEM
    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return "redirect:/user/cart";
    }
    
 // ➕ Increase Quantity
    @PostMapping("/increase/{id}")
    public String increaseQty(@PathVariable Long id) {
        cartService.updateQuantity(id, 1);
        return "redirect:/user/cart";
    }

    // ➖ Decrease Quantity
    @PostMapping("/decrease/{id}")
    public String decreaseQty(@PathVariable Long id) {
        cartService.updateQuantity(id, -1);
        return "redirect:/user/cart";
    }

    // 🧹 Clear Cart

    @PostMapping("/clear")
    public String clearCart(Authentication auth) {

        String loginValue = auth.getName();

        User user = userService.findByEmail(loginValue)
            .orElseGet(() -> userService.findByName(loginValue)
            .orElseThrow(() -> new RuntimeException("User not found")));

        cartService.clearCart(user);

        return "redirect:/user/cart";
    }
}