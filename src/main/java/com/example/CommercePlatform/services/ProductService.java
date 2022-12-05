package com.example.CommercePlatform.services;

import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAllOrderByPriceAsc();
    }

    public Product findOne(int id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public Product findProductByTitle(String title) {
        System.out.println("Service working: finding product by title");
        Optional<Product> optionalProduct = productRepository.findByTitle(title);
        return optionalProduct.orElse(null);
    }

    public List<Product> filter(String search, float priceFrom, float priceTo) {
        return productRepository.findByTitleContainingIgnoreCaseAndPriceRange(search.toLowerCase(), priceFrom, priceTo);
    }

    public List<Product> filter(String search, float priceFrom, float priceTo, int category_id) {
        return productRepository.findByTitleContainingIgnoreCaseAndPriceRangeAndCategory(search.toLowerCase(), priceFrom, priceTo, category_id);
    }
//
//    public static float findMaxPrice(List<Product> products) {
//        Optional<Float> maxPrice;
//        maxPrice = products.stream().map(product -> product.getPrice()).reduce(Float::max);
//        return maxPrice.orElse(null);
//    }
    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void update(int id, Product updatedProduct) {
        updatedProduct.setId(id);
        productRepository.save(updatedProduct);
    }

    @Transactional
    public void delete(int id) {
        productRepository.deleteById(id);
    }

}
