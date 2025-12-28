package com.ecommerce.application.port.out;

import com.ecommerce.domain.Product;
import org.springframework.data.domain.Slice;

public interface SearchProductPort {

    Slice<Product> searchProducts(Long categoryId, Integer pageNum);

}
