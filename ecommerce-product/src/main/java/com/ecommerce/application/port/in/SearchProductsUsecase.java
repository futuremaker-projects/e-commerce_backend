package com.ecommerce.application.port.in;

import com.ecommerce.domain.Product;
import org.springframework.data.domain.Slice;

public interface SearchProductsUsecase {

    Slice<Product> searchProducts(Long categoryId, Integer pageNum);

    Slice<Product> searchProducts(String productName, Integer pageNum);

}