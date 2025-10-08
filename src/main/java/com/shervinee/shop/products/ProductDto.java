package com.shervinee.shop.products;

import java.math.BigDecimal;

public record ProductDto(Long id, String name, BigDecimal price, boolean inStock) {}
