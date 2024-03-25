package com.StoreExample.demo.controller;


import com.StoreExample.demo.DemoApplication;
import com.StoreExample.demo.model.dao.ProductDao;
import com.StoreExample.demo.model.dao.SaleDao;
import com.StoreExample.demo.model.pojo.Product;
import com.StoreExample.demo.model.pojo.Sale;
import com.StoreExample.demo.repository.ProductRepo;
import com.StoreExample.demo.repository.SaleRepo;
import com.StoreExample.demo.service.template.DatabaseTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@WebFluxTest(controllers = MainController.class)

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { DemoApplication.class })
class MainControllerTest {

//    @InjectMocks
//    MySqlDb service;
    @Autowired
    WebTestClient webClient;

    @MockBean
    DatabaseTemplate db;
//
//    @MockBean
//    ProductRepo productRepo;
//
//    @MockBean
//    SaleRepo saleRepo;

    @Test
    void testRoot() {
        String response = webClient
                .get()
                .uri("/")
                //.body(BodyInserters.fromObject(mo))
                .exchange().expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
        assertEquals("HI", response);
    }
    @Test
    void getProduct() {
        lenient().when(db.find(Mockito.anyLong()))
                .thenReturn(Mono.just(ProductDao.builder()
                        .productId(1L)
                        .name("a")
                        .description("a")
                        .unitPrice(1)
                        .quantity(5)
                        .build()));
        ProductDao response = webClient
                .post()
                .uri("/findId/1")
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(ProductDao.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
            assertEquals("a", response.getName());
    }

    @Test
    void getProductName() {
        lenient().when(db.findByName(Mockito.anyString()))
                .thenReturn(Mono.just(
                        Arrays.asList(ProductDao.builder()
                                .productId(1L)
                                .name("b11")
                                .description("a1")
                                .unitPrice(1)
                                .quantity(5)
                                .build(),
                                ProductDao.builder()
                                        .productId(2L)
                                        .name("b22")
                                        .description("a2")
                                        .unitPrice(1)
                                        .quantity(5)
                                        .build()

                                ) )
                        );
        List<ProductDao> response = webClient
                .post()
                .uri("/findName/b")
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(ProductDao.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
        System.out.println(response);
                assertEquals("b11", response.get(0).getName());
        assertEquals("b22", response.get(1).getName());

    }

    //productMono.map( product -> SaleDao.builder().quantity(5).price(500).product(product).build());
    //Mono<SaleDao> saleDaoMono = productMono.map( product -> SaleDao.builder().quantity(5).price(500).product(product).build());
    @Test
    void testBuy() {
        Mono<Product> productMono = Mono.just(Product.builder()
                .productId(1L)
                .name("a")
                .description("a")
                .unitPrice(1)
                .quantity(5)
                .build());
//        productMono.map( product -> { System.out.println(product.getName());
//            return null;
//        } );
        Mono<Sale> saleMono = productMono.map(
                product -> Sale.builder().
                quantity(5).
                price(500).
                product(product).
                build());
//        saleMono.map( sale -> { System.out.println(sale.getSaleId());
//            return null;
//        } );
        System.out.println(saleMono);
        lenient().when(db.buyProduct(Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(saleMono);
        System.out.println("yarab");
        Sale response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/buy")
                        .queryParam("productId", 1L)
                        .queryParam("quantity", 5)
                        .build())
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(Sale.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
        System.out.println(response);
    }
}
