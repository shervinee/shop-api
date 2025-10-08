package com.shervinee.shop.products;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/products")
public class ProductsController {

    private final ProductsService productsService;

    @GetMapping
    public List<ProductDto> list() {
        return productsService.getAllProducts();
    }
}
