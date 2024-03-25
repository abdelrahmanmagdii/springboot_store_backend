package com.StoreExample.demo.service.template;

import com.StoreExample.demo.model.dao.ProductDao;
import com.StoreExample.demo.model.dao.SaleDao;
import com.StoreExample.demo.model.pojo.Sale;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DatabaseTemplate {
    Mono<ProductDao> find(Long id);

    Mono<List<ProductDao>> findByName(String namePart);

    Mono<Sale> buyProduct(Long productId, int quantity);

}
