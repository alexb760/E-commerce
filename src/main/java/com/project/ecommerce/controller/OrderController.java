package com.project.ecommerce.controller;

import com.project.ecommerce.dto.OrderProductDTO;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.model.Order;
import com.project.ecommerce.model.OrderProduct;
import com.project.ecommerce.model.OrderStatus;
import com.project.ecommerce.service.OrderProductService;
import com.project.ecommerce.service.OrderService;
import com.project.ecommerce.service.ProductService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    private ProductService productService;
    private OrderService orderService;
    private OrderProductService orderProductService;

    public OrderController(ProductService productService,
                           OrderService orderService,
                           OrderProductService OrderProductService)
    {
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = OrderProductService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public @NotNull Iterable<Order> list(){
        return orderService.getAllOrders();
    }

    public ResponseEntity<Order> create(@RequestBody OrderForm orderForm){
        List<OrderProductDTO> formDtos = orderForm.getProductOrders();
        validateProductsExistence(formDtos);
        Order order = new Order();
        order.setStatus(OrderStatus.PAID.name());
        order = this.orderService.create(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDTO dto : formDtos) {
            orderProducts.add(orderProductService
                    .create(new OrderProduct(
                            order, productService.getProduct(
                                    dto.getProduct().getId()), dto.getQuantity())));
        }

        order.setOrderProducts(orderProducts);
        this.orderService.update(order);

        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/orders/{id}")
                .buildAndExpand(order.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
    }

    private void validateProductsExistence(List<OrderProductDTO> orderProducts) {
        List<OrderProductDTO> list = orderProducts
                .stream()
                .filter(op -> Objects.isNull(productService.getProduct(op
                        .getProduct()
                        .getId())))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) {
            new ResourceNotFoundException("Product not found");
        }
    }

    public static class OrderForm {

        private List<OrderProductDTO> productOrders;

        public List<OrderProductDTO> getProductOrders() {
            return productOrders;
        }

        public void setProductOrders(List<OrderProductDTO> productOrders) {
            this.productOrders = productOrders;
        }
    }

}
