package com.example.CommercePlatform.services;

import com.example.CommercePlatform.enumerate.Status;
import com.example.CommercePlatform.models.Order;
import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAllByUser(Person person) {
        return orderRepository.findByPerson(person);
    }

    public List<Order> findAllByNumber(String orderNumber) {
        return orderRepository.findByNumber(orderNumber);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> filter(String search) {
        System.out.println(search.toLowerCase());
        return orderRepository.findByNumberEndingWithIgnoreCase(search.toLowerCase());
    }
    @Transactional
    public void create(Order order) {
        orderRepository.save(order);
    }
    @Transactional
    public void changeOrderStatus(String orderNumber, Status status) {
        orderRepository.updateStatusByOrderNumber(orderNumber, status.ordinal());
    }
    @Transactional
    public void cancel(String orderNumber) {
        this.changeOrderStatus(orderNumber, Status.Отменён);
    }
}
