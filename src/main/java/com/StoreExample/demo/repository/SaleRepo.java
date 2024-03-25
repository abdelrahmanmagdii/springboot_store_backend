package com.StoreExample.demo.repository;

import com.StoreExample.demo.model.dao.SaleDao;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepo extends ReactiveCrudRepository<SaleDao, Integer> {
//    Mono<Sale> findById(Long id);
//
//    Flux<Sale> findByNameContains(String namePart);

//    Mono<Sale> buyProduct(Long productId, int quantity);

}
