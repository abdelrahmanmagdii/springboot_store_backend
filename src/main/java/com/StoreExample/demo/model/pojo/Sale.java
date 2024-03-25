package com.StoreExample.demo.model.pojo;

import com.StoreExample.demo.model.dao.SaleDao;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {
    private Long saleId;
    private Product product;
    private int quantity;
    private int price;

    public Sale(SaleDao sale) {
        this.saleId = sale.getSaleId();
        this.product = null;
        this.quantity = sale.getQuantity();
        this.price = sale.getPrice();
    }
}
