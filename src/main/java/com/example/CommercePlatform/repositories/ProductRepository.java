package com.example.CommercePlatform.repositories;

import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByTitle(String title);

    Optional<Product> findById(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM product ORDER BY price asc")
    List<Product> findAllOrderByPriceAsc();
    @Query(nativeQuery = true, value =
            "SELECT * FROM product WHERE " +
                    "( (lower(title) LIKE '?1%') OR (lower(title) LIKE %?1%) OR (lower(title) LIKE '%?1') ) AND " +
                    "(price BETWEEN ?2 and ?3) " +
                    "ORDER BY price asc")
    List<Product> findByTitleContainingIgnoreCaseAndPriceRange(
            String search,
            float priceFrom,
            float priceTo);
    @Query(nativeQuery = true, value =
            "SELECT * FROM product WHERE " +
                    "( (lower(title) LIKE '?1%') OR (lower(title) LIKE %?1%) OR (lower(title) LIKE '%?1') ) AND " +
                    "(price BETWEEN ?2 and ?3) AND " +
                    "category_id = ?4 " +
                    "ORDER BY price asc"
    )
    List<Product> findByTitleContainingIgnoreCaseAndPriceRangeAndCategory(
            String search,
            float priceFrom,
            float priceTo,
            int category_id);

}
