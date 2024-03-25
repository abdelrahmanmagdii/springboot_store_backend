package com.StoreExample.demo.service;
import com.StoreExample.demo.controller.MainController;
import com.StoreExample.demo.model.dao.ProductDao;
import com.StoreExample.demo.model.pojo.Product;
import com.StoreExample.demo.model.pojo.Sale;
import com.StoreExample.demo.repository.ProductRepo;
import com.StoreExample.demo.repository.SaleRepo;
import com.StoreExample.demo.service.impl.MySqlDb;
import com.StoreExample.demo.service.template.DatabaseTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import com.StoreExample.demo.DemoApplicationTests;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.lenient;

@WebFluxTest(controllers = MainController.class)

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { DemoApplicationTests.class })
public class MySqlDbTest {

    @InjectMocks
    MySqlDb service;

    @Autowired
    WebTestClient webClient;

    @MockBean
    DatabaseTemplate db;

    @MockBean
    ProductRepo productRepo;

    @MockBean
    SaleRepo saleRepo;

    @Test
    void getProduct() {
        lenient().when(productRepo.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(ProductDao.builder()
                        .productId(Long.valueOf(22))
                        .quantity(10)
                        .name("a")
                        .unitPrice(5)
                        .description("aa")
                        .build()))
        ;

        Mono<ProductDao> productMono = service.find(Long.valueOf(1));

        StepVerifier
                .create(productMono)
                .assertNext(Product -> {
                    assertEquals(Long.valueOf(22), Product.getProductId());
                    assertEquals("a", Product.getName());
                    assertEquals(10, Product.getQuantity());
                    assertEquals(5, Product.getUnitPrice());
                    assertEquals("aa", Product.getDescription());
                })
                .verifyComplete();

    }

    @Test
    void findEmptyRepo() {
        lenient().when(productRepo.findById(Mockito.anyLong()))
                .thenReturn(Mono.empty());

        Mono<ProductDao> productMono = productRepo.findById(Long.valueOf(2625));

        StepVerifier
                .create(productMono)
//                .assertNext(product -> {
//                    assertEquals(Long.valueOf(0), product.getProductId());
//                    assertNull(product.getDescription());
//                })
                .verifyComplete();
    }

    @Test
    void findEmptyService() {
        lenient().when(db.find(Mockito.anyLong()))
                .thenReturn(Mono.empty());

        Mono<ProductDao> productMono = db.find(Long.valueOf(2625));

        StepVerifier
                .create(productMono)
//                .assertNext(product -> {
//                    assertEquals(Long.valueOf(0), product.getProductId());
//                    assertNull(product.getDescription());
//                })
                .verifyComplete();
    }

@Test
    void getProductName() {
        lenient().when(productRepo.findByNameContains(Mockito.anyString()))
                .thenReturn(Flux.just(ProductDao.builder()
                        .productId(22L)
                        .quantity(10)
                        .name("ahmed")
                        .unitPrice(5)
                        .description("aa")
                        .build()))
        ;
    Flux<ProductDao> productMono = productRepo.findByNameContains("b");

    StepVerifier
            .create(productMono)
            .assertNext(Product -> {
                assertEquals(22L, Product.getProductId());
                assertEquals("ahmed", Product.getName());
            })
            .verifyComplete();
    }
    @Test
    void productBuy()
    {

        Mono<Product> productMono = Mono.just(Product.builder()
                .productId(1L)
                .name("testName")
                .description("testDesc")
                .unitPrice(10)
                .quantity(20)
                .build());

        Mono<Sale> saleMono = productMono.map(product -> Sale.builder().quantity(5).price(500).product(product).build());

        lenient().when(db.buyProduct(Mockito.anyLong(), Mockito.anyInt())).thenReturn(saleMono);


        Mono<Sale> sale = db.buyProduct(3L, 2);

        StepVerifier.create(sale).assertNext( saleDto -> {

            assertEquals(5, saleDto.getQuantity());
            assertEquals(500.0, saleDto.getPrice());
            //assertNotNull(saleDto.getProduct());

        }).verifyComplete();

    }
}
