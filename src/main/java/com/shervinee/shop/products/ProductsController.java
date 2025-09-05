package com.shervinee.shop.products;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductsController {
    @GetMapping
    public List<ProductDto> list() {
        return List.of(
            new ProductDto(1L, "desk", new BigDecimal("122.67"), true ),
            new ProductDto(1L, "monitor", new BigDecimal("252.31"), true ),
            new ProductDto(1L, "chair", new BigDecimal("50.31"), false )
        );
    }
}
