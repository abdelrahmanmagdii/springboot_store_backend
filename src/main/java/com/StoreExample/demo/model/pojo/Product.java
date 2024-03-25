package com.StoreExample.demo.model.pojo;

import com.StoreExample.demo.model.dao.ProductDao;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product {

    private Long productId;
    private String name;
    private String description;
    private int unitPrice;
    private int quantity;

    public Product(ProductDao product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.unitPrice = product.getUnitPrice();
        this.quantity = product.getQuantity();
    }
}
