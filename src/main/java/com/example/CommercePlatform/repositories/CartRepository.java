package com.example.CommercePlatform.repositories;

import com.example.CommercePlatform.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByPersonId(int id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM product_cart WHERE person_id = ?1 AND product_id = ?2")
    void delete(int personId, int productId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM product_cart WHERE person_id = ?1")
    void delete(int personId);

}
