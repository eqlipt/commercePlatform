package com.example.CommercePlatform.services;

import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.repositories.ImageRepository;
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
    private final ImageRepository imageRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAllOrderByPriceAsc();
    }

    public Product findOne(int id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

    public Product findProductByTitle(String title) {
        Optional<Product> optionalProduct = productRepository.findByTitle(title);
        return optionalProduct.orElse(null);
    }

    public List<Product> filter(String search, float priceFrom, float priceTo) {
        return productRepository.findByTitleContainingIgnoreCaseAndPriceRange(search.toLowerCase(), priceFrom, priceTo);
    }

    public List<Product> filter(String search, float priceFrom, float priceTo, int category_id) {
        return productRepository.findByTitleContainingIgnoreCaseAndPriceRangeAndCategory(search.toLowerCase(), priceFrom, priceTo, category_id);
    }

    @Transactional
    public void save(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void update(int id, Product updatedProduct) {
        updatedProduct.setId(id);
        productRepository.save(updatedProduct);
//        productRepository.updateProduct(
//                id,
//                updatedProduct.getTitle(),
//                updatedProduct.getDescription(),
//                updatedProduct.getPrice(),
//                updatedProduct.getWarehouse(),
//                updatedProduct.getSupplier(),
//                updatedProduct.getCategory().getId()
//        );

    }

    @Transactional
    public void delete(int id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public void deleteImageFromProduct(String imageFileName) {
        imageRepository.deleteImageByFileName(imageFileName);
    }

}
