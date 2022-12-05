package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.models.ProductCategory;
import com.example.CommercePlatform.services.ProductCategoryService;
import com.example.CommercePlatform.util.ProductCategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;
    private final ProductCategoryValidator productCategoryValidator;

    @Autowired
    public ProductCategoryController(ProductCategoryService productCategoryService, ProductCategoryValidator productCategoryValidator) {
        this.productCategoryService = productCategoryService;
        this.productCategoryValidator = productCategoryValidator;
    }

    // Read
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("category", productCategoryService.findAll());
        return "/category/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("category", productCategoryService.findOne(id));
        return "/category/show";
    }

    // Create
    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("category", new ProductCategory());
        return "/category/new";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("category") @Valid ProductCategory productCategory,
                         BindingResult bindingResult) {
        productCategoryValidator.validate(productCategory, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/category/new";
        }
        productCategoryService.save(productCategory);
        return "redirect:/category";
    }

    // Update
    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("category", productCategoryService.findOne(id));
        return "/category/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") int id,
            @ModelAttribute("category") @Valid ProductCategory productCategory,
            BindingResult bindingResult) {
        productCategoryValidator.validate(productCategory, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/category/edit";
        }
        productCategoryService.update(id, productCategory);
        return  "redirect:/category/{id}";
    }

    //Delete
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, Model model) {
        model.addAttribute("category", productCategoryService.findOne(id));
        return "/category/delete";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        productCategoryService.delete(id);
        return "redirect:/category";
    }
}
