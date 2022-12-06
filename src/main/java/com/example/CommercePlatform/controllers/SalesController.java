package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.enumerate.Status;
import com.example.CommercePlatform.models.Order;
import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.services.OrderService;
import com.example.CommercePlatform.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales")
public class SalesController {
    private final OrderService orderService;

    public SalesController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public String index(Model model) {
        var orders = orderService.findAll().stream().collect(Collectors.groupingBy(Order::getNumber));
        model.addAttribute("orders", orders);
        model.addAttribute("search", "");
        return "/order/index";
    }

    @PostMapping()
    public String filter(@RequestParam(required = false, defaultValue = "", name = "search") String search, Model model) {
        var orders = orderService.filter(search).stream().collect(Collectors.groupingBy(Order::getNumber));
        model.addAttribute("orders", orders);
        model.addAttribute("search", search);
        return "/order/index";
    }


    @GetMapping("/{number}")
    public String show(@PathVariable("number") String orderNumber, Model model) {
        List<Product> productList = new ArrayList<>();
        int totalPrice = 0;
        LocalDateTime orderDate = null;
        Status orderStatus = Status.Принят;

        List<Order> orderRows = orderService.findAllByNumber(orderNumber);
        if(!orderRows.isEmpty()) {
            for (Order row: orderRows) {
                productList.add(row.getProduct());
            }
            totalPrice = productList.stream().map(product -> product.getPrice()).reduce((a,b) -> a + b).get();
            orderDate = orderRows.stream().map(order -> order.getDateTime()).findFirst().get();
            orderStatus = orderRows.stream().map(order -> order.getStatus()).findFirst().get();
        }
        model.addAttribute("products", productList);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderDate", orderDate);
        model.addAttribute("orderStatus", orderStatus);
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("status", new Status[0]);
        return "/order/show";
    }

    @PostMapping("/{number}/changestatus")
    public String changeStatus(@PathVariable("number") String orderNumber, @ModelAttribute("status") Status status) {
        System.out.println(orderNumber);
        System.out.println(status);
        orderService.changeOrderStatus(orderNumber, status);
        return "redirect:/sales/{number}";
    }
}
