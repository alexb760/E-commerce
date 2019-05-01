package com.project.ecommerce.dto;

import com.project.ecommerce.model.Product;
import lombok.Data;

@Data
public class OrderProductDTO {

    private Product product;
    private Integer quantity;
}
