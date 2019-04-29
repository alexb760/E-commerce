package com.project.ecommerce.controller;

import com.project.ecommerce.model.Order;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private ProductService productService;
    private OrderService orderService;

    public OrderController(ProductService productService, OrderService orderService)
    {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<Order> list(){
        return orderService.getAllOrders();
    }


}
