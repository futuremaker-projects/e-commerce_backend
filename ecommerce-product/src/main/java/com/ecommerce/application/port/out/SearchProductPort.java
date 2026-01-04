package com.ecommerce.application.port.out;

import com.ecommerce.domain.Product;
import org.springframework.data.domain.Slice;

public interface SearchProductPort {

    Slice<Product> searchProducts(Long categoryId, int pageNum);

    Slice<Product> searchProducts(String productName, int pageNum);

}
