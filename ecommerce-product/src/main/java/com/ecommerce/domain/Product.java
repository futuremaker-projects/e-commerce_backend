package com.ecommerce.domain;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

    private Long productId;
    private String name;
    private String description;
    private Long price;
    private Integer quantity;

    private Long categoryId;

    public static Product of(Long productId, String name, String description, Long price, Integer quantity, Long categoryId) {
        return Product.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .price(price)
                .categoryId(categoryId)
                .quantity(quantity)
                .build();
    }

}
