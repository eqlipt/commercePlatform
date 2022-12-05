package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.models.Cart;
import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.security.PersonDetails;
import com.example.CommercePlatform.services.CartService;
import com.example.CommercePlatform.services.PersonService;
import com.example.CommercePlatform.services.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;
    private final CartService cartService;
    private final PersonService personService;

    public CartController(ProductService productService, CartService cartService, PersonService personService) {
        this.productService = productService;
        this.cartService = cartService;
        this.personService = personService;
    }
    @GetMapping()
    public String index(Model model) {
        Person person = personService.getPersonFromAuthentication();

        List<Product> productList = new ArrayList<>();
        int totalPrice = 0;
        List<Cart> cartList = cartService.fill(person.getId());
        if (!cartList.isEmpty()) {
            for (Cart cart: cartList) {
                productList.add(productService.findOne(cart.getProductId()));
            }
            totalPrice = productList.stream().map(product -> product.getPrice()).reduce((a,b) -> a + b).get();
        }
        model.addAttribute("products", productList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("user", person);
        return "/cart";
    }

    @GetMapping("/add/{id}")
    public String addProductToCart(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int person_id = personDetails.getPerson().getId();

        Product productToAdd = productService.findOne(id);
        Cart cart = new Cart(person_id, productToAdd.getId());
        cartService.save(cart);
        return "redirect:/shop";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int product_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        int person_id = personDetails.getPerson().getId();

        cartService.delete(person_id, product_id);

        return "redirect:/cart";
    }



}
