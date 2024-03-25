package com.StoreExample.demo.controller;

import com.StoreExample.demo.model.dao.ProductDao;
import com.StoreExample.demo.model.dao.SaleDao;
import com.StoreExample.demo.model.pojo.Sale;
import com.StoreExample.demo.service.template.DatabaseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    DatabaseTemplate db;

    @GetMapping("/")
    public String root(){
        return "HI";
    }

    @PostMapping("/findId/{id}")
    public Mono<ProductDao> getProduct(@PathVariable("id") long id)
    {
        System.out.println("Starting");

        return db.find(id);
    }

    @PostMapping("/findName/{name}")
    public Mono<List<ProductDao>> getByName(@PathVariable("name") String namePart)
    {
        System.out.println("Starting");

        return db.findByName(namePart);
    }

    @PostMapping("/buy")
    public Mono<Sale> buyProduct(@RequestParam Long productId, @RequestParam int quantity)
    {
        System.out.println("productId = "+productId+", quantity = "+quantity);
        return db.buyProduct(productId, quantity).switchIfEmpty(
                Mono.just(Sale.builder()
                                .quantity(0)
                        .build())
        );
    }
   // @RequestParam String msg, @RequestParam String id)

}
