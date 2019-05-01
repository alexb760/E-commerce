package com.project.ecommerce.repository;

import com.project.ecommerce.model.OrderProduct;
import com.project.ecommerce.model.OrderProductPK;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {
}
