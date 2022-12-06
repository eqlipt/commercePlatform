package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.services.ProductCategoryService;
import com.example.CommercePlatform.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private List<Product> products;


    @Autowired
    public ShopController(ProductService productService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    // Read
    @GetMapping("")
    public String shop(Model model){
        model.addAttribute("products", productService.findAll());
        model.addAttribute("category", productCategoryService.findAll());

        return "/shop/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.findOne(id));
        return "/shop/show";
    }

    @PostMapping("")
    public String filter(
            @RequestParam(required = false, defaultValue = "", name = "search") String search,
            @RequestParam(required = false, defaultValue = "100.0", name = "price_from") String priceFrom,
            @RequestParam(required = false, defaultValue = "100000.0", name = "price_to") String priceTo,
            @RequestParam(required = false, name = "category") int category_id,
            Model model) {
        if(category_id == 0) {
            this.products = productService.filter(search, Float.parseFloat(priceFrom), Float.parseFloat(priceTo));
        } else {
            this.products = productService.filter(search, Float.parseFloat(priceFrom), Float.parseFloat(priceTo), category_id);
        }
        model.addAttribute("products", products);
        model.addAttribute("category", productCategoryService.findAll());
        model.addAttribute("search", search);
        model.addAttribute("price_from", priceFrom);
        model.addAttribute("price_to", priceTo);
        model.addAttribute("selected_category", category_id);
        return "/shop/index";
    }

}
