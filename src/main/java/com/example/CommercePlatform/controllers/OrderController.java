package com.example.CommercePlatform.controllers;

import com.example.CommercePlatform.enumerate.Status;
import com.example.CommercePlatform.models.Cart;
import com.example.CommercePlatform.models.Order;
import com.example.CommercePlatform.models.Person;
import com.example.CommercePlatform.models.Product;
import com.example.CommercePlatform.services.CartService;
import com.example.CommercePlatform.services.OrderService;
import com.example.CommercePlatform.services.PersonService;
import com.example.CommercePlatform.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final PersonService personService;
    private final CartService cartService;
    private final ProductService productService;

    public OrderController(OrderService orderService, PersonService personService, CartService cartService, ProductService productService) {
        this.orderService = orderService;
        this.personService = personService;
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping()
    public String index(Model model) {
        Person person = personService.getPersonFromAuthentication();
        List<Order> orderList = orderService.findAllByUser(person);
        var orders = orderList.stream().collect(Collectors.groupingBy(Order::getNumber));
//        orders.forEach((innerKey, innerList) -> innerList.forEach((listItem) -> System.out.println(listItem.getProduct().getPrice())));
//        var sum = orders.values().stream().flatMapToInt(list -> list.stream().mapToInt(Order::getPrice)).sum();
        model.addAttribute("orders", orders);
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

    @GetMapping("/create")
    public String create(Model model) {
        Person person = personService.getPersonFromAuthentication();

        List<Cart> cartRows = cartService.fill(person.getId());
        if(!cartRows.isEmpty()) {
            List<Product> productList = new ArrayList<>();
            cartRows.stream().forEach(cart -> productList.add(productService.findOne(cart.getProductId())));
            String orderNumber = UUID.randomUUID().toString();
            productList.stream().forEach(product -> {
                Order order = new Order(orderNumber, 1, product.getPrice(), Status.Принят, product, person);
                orderService.create(order);
            });
            cartService.empty(person.getId());
            model.addAttribute("orderNumber", orderNumber);
            return "/order/create";
        }

        return "redirect:/shop";
    }


}
