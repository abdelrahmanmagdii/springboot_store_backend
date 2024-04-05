package com.StoreExample.demo.service.impl;

import com.StoreExample.demo.model.dao.ProductDao;
import com.StoreExample.demo.model.dao.SaleDao;
import com.StoreExample.demo.model.pojo.Product;
import com.StoreExample.demo.model.pojo.Sale;
import com.StoreExample.demo.repository.ProductRepo;
import com.StoreExample.demo.repository.SaleRepo;
import com.StoreExample.demo.service.template.DatabaseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MySqlDb implements DatabaseTemplate {
    @Autowired
    ProductRepo productRepo;
    DatabaseTemplate db;

    @Autowired
    SaleRepo saleRepo;

    @Override
    public Mono<ProductDao> find(Long id) {
        System.out.println("\nThe ID is "+id);
        return productRepo.findById(id);
    }

    @Override
    public Mono<List<ProductDao>> findByName(String name) {
        return productRepo.findByNameContains(name).collectList();
    }
    @Override
    public Mono<Sale> buyProduct(Long productIdd, int quantity) {
        Mono<ProductDao> p = productRepo.findById(productIdd);
        return p.mapNotNull(product -> {

                    System.out.println("yarab1");
                    int salePrice = product.getUnitPrice() * quantity;
                    System.out.println("yarab2");
                    updateProduct( product, quantity);
                    Product productPojo = new Product(product);
                    productRepo.save(product).subscribe();
                    System.out.println("yarab3");
                    SaleDao newSale = SaleDao.builder()
                            .productId(productIdd)
                            .price(salePrice)
                            .quantity(quantity)
                            .build();
                    Sale salePojo = new Sale(newSale);
                    salePojo.setProduct(productPojo);
                    Mono<SaleDao> saleResult = saleRepo.save(newSale);
                    return saleResult.map(s -> {
                        salePojo.setSaleId(s.getSaleId());
                        return salePojo;
                    });

                }
        ).flatMap(result -> result);
//            Mono<Sale> s =
    }

    private Mono<ProductDao> updateProduct( ProductDao product, int quantity)
    {
        if(quantity> product.getQuantity())
            return Mono.error(new RuntimeException("Not enough products"));
        product.setQuantity(product.getQuantity()- quantity);
        return productRepo.save(product);
    }
}
//        if(productQuantity> quantity){
//            product.setQuantity(productQuantity - quantity);
////            Sale sale = new Sale();
//////            sale.setProduct(product);
//////            sale.setQuantity(quantity);
//            return Mono.just(Sale.builder()
//                            .product((product))
//                            .quantity(quantity)
//                    .build()
//            );
//            return Mono.just(Message.builder()
//                    .id(msg.getId())
//                    .text(msg.getText()+" was read")
//                    .build());






//       return p.map(
//                product-> {
//                    //if productId exists
//                    int productQuantity = product.getQuantity();
//                    if (productQuantity >= quantity) {
//                        product.setQuantity(productQuantity - quantity);
//
//                       Sale sale = new Sale();
//                      // sale.setProduct(product);
//                       //sale.setQuantity(quantity);
//
////                       database.save.subscribe
////                        return Mono.just(Sale.builder()
////                                .product((product))
////                                .quantity(quantity)
////                                 .build()
//                        return sale;
////                        );
//                    }
//                    return null;
//                }
//        );