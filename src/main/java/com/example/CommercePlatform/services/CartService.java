package com.example.CommercePlatform.services;

import com.example.CommercePlatform.models.Cart;
import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> fill(int personId) {
        return cartRepository.findByPersonId(personId);
    }

    @Transactional
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Transactional
    public void delete(int personId, int productId) {
        cartRepository.delete(personId, productId);
    }

    @Transactional
    public void empty(int personId) {cartRepository.delete(personId);}
}
