package com.example.CommercePlatform.util;

import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.models.ProductCategory;
import com.example.CommercePlatform.services.ProductCategoryService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductCategoryValidator implements Validator {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryValidator(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ProductCategory.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductCategory productCategory = (ProductCategory) target;
        if(productCategoryService.findByName(productCategory.getName()) != null){
            errors.rejectValue("name", "", "Такая категория уже существует");
        }
    }
}
