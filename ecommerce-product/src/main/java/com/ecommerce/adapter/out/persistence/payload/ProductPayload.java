package com.ecommerce.adapter.out.persistence.payload;

import com.ecommerce.domain.Product;

public class ProductPayload {

    public record Get(Long productId, String name,
                      String description, Long price, Integer quantity, Long categoryId) {

        public Product toDomain() {
            return Product.of(productId, name, description, price, quantity, categoryId);
        }
    }

}
