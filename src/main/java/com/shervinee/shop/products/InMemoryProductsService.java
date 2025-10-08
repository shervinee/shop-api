package com.shervinee.shop.products;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service @Profile("dev")
public class InMemoryProductsService implements ProductsService {

    @Override
    public List<ProductDto> getAllProducts() {
        return List.of(
            new ProductDto(1L, "desk", new BigDecimal("122.67"), true ),
            new ProductDto(1L, "monitor", new BigDecimal("252.31"), true ),
            new ProductDto(1L, "chair", new BigDecimal("50.31"), false )
        );
    }
}
