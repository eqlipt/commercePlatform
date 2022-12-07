package com.example.CommercePlatform.util;

import com.example.CommercePlatform.models.Order;

import java.util.List;

public class OrderReducer {
    public static final int sum(List<Order> ordersList) {
        return ordersList.stream().map(Order::getPrice).reduce(0, (a,b) -> a + b);
    }
}
