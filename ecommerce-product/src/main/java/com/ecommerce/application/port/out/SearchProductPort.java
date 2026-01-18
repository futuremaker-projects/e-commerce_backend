package com.ecommerce.application.port.out;

import com.ecommerce.domain.Product;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface SearchProductPort {

    Slice<Product> searchProducts(Long categoryId, int pageNum);

    Slice<Product> searchProducts(String productName, int pageNum);

    List<Product> searchProductsBySql(String productName);

    List<Product> searchProductsByEs(String productName);
}
