package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.models.Image;
import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.repositories.ImageRepository;
import com.example.CommercePlatform.services.FileService;
import com.example.CommercePlatform.services.PersonService;
import com.example.CommercePlatform.services.ProductCategoryService;
import com.example.CommercePlatform.services.ProductService;
import com.example.CommercePlatform.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final ProductValidator productValidator;
    private final PersonService personService;
    private final FileService fileService;
    private final ImageRepository imageRepository;

    @Autowired
    public ProductController(ProductService productService,
                             ProductCategoryService productCategoryService,
                             ProductValidator productValidator,
                             PersonService personService,
                             FileService fileService,
                             ImageRepository imageRepository) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
        this.productValidator = productValidator;
        this.personService = personService;
        this.fileService = fileService;
        this.imageRepository = imageRepository;
    }

    // Read
    @GetMapping("")
    public String index(Model model){
        model.addAttribute("user", personService.getPersonFromAuthentication());
        model.addAttribute("products", productService.findAll());
        return "/product/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", personService.getPersonFromAuthentication());
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
                         @RequestParam("files") MultipartFile[] files,
                         Model model) {
        model.addAttribute("category", productCategoryService.findAll());

        productValidator.validate(product, bindingResult);
        if(bindingResult.hasErrors()) return "/product/add";

        fileService.upload(files, product);

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
                         @RequestParam("files") MultipartFile[] files,
                         @PathVariable("id") int id,
                         Model model
    ) {
        model.addAttribute("category", productCategoryService.findAll());
        productValidator.validate(product, bindingResult);
        if(bindingResult.hasErrors()) return "/product/edit";

        fileService.upload(files, product);

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
        var productImageFileNames = imageRepository.findAllByProduct(productService.findOne(id))
                .stream()
                .map(Image::getFileName)
                .collect(Collectors.toList());
        productService.delete(id);
        fileService.deleteImages(productImageFileNames);
        return "redirect:/product";
    }

    @GetMapping("/{id}/img/delete")
    public String deleteImage(@RequestParam(value = "imageFileName", required = false) String imageFileName) {
        if(!imageFileName.equals("")) {
            productService.deleteImageFromProduct(imageFileName);
            fileService.deleteImage(imageFileName);
        }
        return "redirect:/product/{id}/edit";
    }
}
