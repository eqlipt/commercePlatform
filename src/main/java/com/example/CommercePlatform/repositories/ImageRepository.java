package com.example.CommercePlatform.repositories;

import com.example.CommercePlatform.models.Image;
import com.example.CommercePlatform.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    void deleteImageByFileName(String imageFileName);

    List<Image> findAllByProduct(Product product);
}
