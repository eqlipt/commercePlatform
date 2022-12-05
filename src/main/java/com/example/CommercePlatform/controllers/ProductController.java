package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.models.Image;
import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.security.PersonDetails;
import com.example.CommercePlatform.services.ProductCategoryService;
import com.example.CommercePlatform.services.ProductService;
import com.example.CommercePlatform.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductValidator productValidator;

    @Autowired
    public ProductController(ProductService productService, ProductCategoryService productCategoryService, ProductValidator productValidator) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.productValidator = productValidator;
    }

    // Read
    @GetMapping("")
    public String index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("user", personDetails.getPerson());

        model.addAttribute("products", productService.findAll());
        return "/product/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        model.addAttribute("user", personDetails.getPerson());

        model.addAttribute("product", productService.findOne(id));
        return "/product/show";
    }

    // Create
    @GetMapping("/add")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("category", productCategoryService.findAll());
        return "/product/add";
    }

    @PostMapping("/add")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String create(@ModelAttribute("product") @Valid Product product,
                         BindingResult bindingResult,
                         @RequestParam("file_one") MultipartFile file_one,
                         Model model
    ) throws IOException {
        model.addAttribute("category", productCategoryService.findAll());

        productValidator.validate(product, bindingResult);
        if(bindingResult.hasErrors()) return "/product/add";

        // Проверка на пустоту файла
        if(file_one != null){
            // Дирректория по сохранению файла
            File uploadDir = new File(uploadPath);
            // Если данной дирректории по пути не сущетсвует
            if(!uploadDir.exists()){
                // Создаем данную дирректорию
                uploadDir.mkdir();
            }
            // Создаем уникальное имя файла
            // UUID представляет неизменный универсальный уникальный идентификатор
            String uuidFile = UUID.randomUUID().toString();
            // file_one.getOriginalFilename() - наименование файла с формы
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
            // Загружаем файл по указаннопу пути
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }

        productService.save(product);
        return "redirect:/product";
    }

    // Update
    @GetMapping("/{id}/edit")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.findOne(id));
        model.addAttribute("category", productCategoryService.findAll());
        return "/product/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@ModelAttribute("product") @Valid Product product,
                         BindingResult bindingResult,
                         @PathVariable("id") int id,
                         Model model) {
        model.addAttribute("category", productCategoryService.findAll());

        productValidator.validate(product, bindingResult);
        if(bindingResult.hasErrors()) return "/product/edit";

        productService.update(id, product);
        return  "redirect:/product/{id}";
    }

    // Delete
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.findOne(id));
        return "/product/delete";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        productService.delete(id);
        return "redirect:/product";
    }
}
