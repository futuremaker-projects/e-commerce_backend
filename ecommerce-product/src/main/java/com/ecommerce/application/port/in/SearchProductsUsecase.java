package com.ecommerce.application.port.in;

import com.ecommerce.domain.Product;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface SearchProductsUsecase {

    Slice<Product> searchProductsByEs(Long categoryId, Integer pageNum);
    Slice<Product> searchProductsByEs(String productName, Integer pageNum);

    Product getProduct(Long productId);

    List<Product> searchProductsBySql(String productName);
    List<Product> searchProductsByEs(String productName);
}