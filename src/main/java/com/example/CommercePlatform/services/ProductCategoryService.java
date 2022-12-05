package com.example.CommercePlatform.services;

import com.example.CommercePlatform.models.ProductCategory;
import com.example.CommercePlatform.repositories.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

    public ProductCategory findOne(int id) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(id);
        return optionalProductCategory.orElse(null);
    }

    public ProductCategory findByName(String name) {
        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findByName(name);
        return optionalProductCategory.orElse(null);
    }

    @Transactional
    public void save(ProductCategory productCategory) {
        productCategoryRepository.save(productCategory);
    }

    @Transactional
    public void update(int id, ProductCategory updatedProductCategory) {
        updatedProductCategory.setId(id);
        productCategoryRepository.save(updatedProductCategory);
    }

    @Transactional
    public void delete(int id) {
        productCategoryRepository.deleteById(id);
    }
}
