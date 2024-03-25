package com.StoreExample.demo.model.dao;

import com.StoreExample.demo.model.pojo.Sale;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("Sale")
public class SaleDao {
    @Id
    @Column("saleId")
    private Long saleId;
    @Column("productId")
    private Long productId;
    @Column("quantity")
    private int quantity;
    @Column("price")
    private int price;

    public SaleDao(Sale sale) {
        this.saleId = sale.getSaleId();
        this.productId = (sale.getProduct()!=null)? sale.getProduct().getProductId(): null;
        this.quantity = sale.getQuantity();
        this.price = sale.getPrice();
    }
}
