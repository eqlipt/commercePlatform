package com.example.CommercePlatform.repositories;

import com.example.CommercePlatform.models.Order;
import com.example.CommercePlatform.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByPerson(Person person);

    List<Order> findByNumber(String orderNumber);
//
//    @Query(nativeQuery = true, value = "SELECT date_time FROM orders WHERE number = ?1")
//    List<Timestamp> getOrderDate(String orderNumber);
//
//    @Override
//    @Query(nativeQuery = true, value = "SELECT * FROM orders order by date_time DESC")
//    List<Order> findAll();
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE orders SET status = ?2 WHERE number = ?1")
    void updateStatusByOrderNumber(String orderNumber, int statusId);
}
