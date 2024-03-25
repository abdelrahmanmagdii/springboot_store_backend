package com.StoreExample.demo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import com.StoreExample.demo.model.dao.ProductDao;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepo extends ReactiveCrudRepository<ProductDao, Long> {
    Mono<ProductDao> findById(Long id);

    Flux<ProductDao> findByNameContains(String namePart);

//    Mono<Sale> buyProduct(Long productId, int quantity);

}
