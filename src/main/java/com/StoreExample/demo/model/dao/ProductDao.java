package com.StoreExample.demo.model.dao;

import com.StoreExample.demo.model.pojo.Product;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("Product")
public class ProductDao {
    @Id
    @Column("productId")
    private Long productId;
    @Column("name")
    private String name;
    @Column("description")
    private String description;
    @Column("unit_price")
    private int unitPrice;
    @Column("quantity")
    private int quantity;

    public ProductDao(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.unitPrice = product.getUnitPrice();
        this.quantity = product.getQuantity();
    }

    @Override
    public String toString() {
        return "ProductDao{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}
