package com.ecommerce.adapter.in.web.dto;

import com.ecommerce.domain.Product;

public class ProductDto {
    public record Response(Long productId, String productName,
                           String Description, Long price, Integer quantity) {

        public static Response of(Long productId, String productName, String description, Long price, Integer quantity) {
            return new Response(productId, productName, description, price, quantity);
        }

        public static Response from(Product product) {
            return Response.of(
                    product.getProductId(), product.getName(),
                    product.getDescription(), product.getPrice(),
                    product.getStock()
            );
        }
    }
}
