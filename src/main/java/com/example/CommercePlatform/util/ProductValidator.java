package com.example.CommercePlatform.util;

import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {
    private final ProductService productService;

    public ProductValidator(ProductService productService) {
        this.productService = productService;
    }

    // В данно методе указываем для какой модели предназначен данный валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("Validator working");
        Product submittedProduct = (Product) target;
        Product dbProduct = productService.findProductByTitle(submittedProduct.getTitle());
        if(
            dbProduct != null
                && submittedProduct.getDescription().equals(dbProduct.getDescription())
                && submittedProduct.getPrice() == (dbProduct.getPrice())
                && submittedProduct.getSupplier().equals(dbProduct.getSupplier())
                && submittedProduct.getId() != dbProduct.getId()
        ){
            errors.rejectValue("title", "", "Вы уже продаёте товар с такими же параметрами");
        }
    }
}
