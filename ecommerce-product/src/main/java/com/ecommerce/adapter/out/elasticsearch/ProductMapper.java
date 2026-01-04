package com.ecommerce.adapter.out.elasticsearch;

import com.ecommerce.domain.Product;

public class ProductMapper {

    public record Get() {
        public static Product toDomain(ProductSearchDocument doc) {
            return Product.of(
                    doc.getId(), doc.getProductName(),
                    doc.getDescription(), doc.getPrice(),
                    doc.getStock(), doc.getCategoryId()
            );
        }
    }

}
