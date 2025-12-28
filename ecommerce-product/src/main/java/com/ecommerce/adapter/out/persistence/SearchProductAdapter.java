package com.ecommerce.adapter.out.persistence;

import com.ecommerce.adapter.out.persistence.payload.ProductPayload;
import com.ecommerce.application.port.out.SearchProductPort;
import com.ecommerce.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchProductAdapter implements SearchProductPort {

    private final ProductJpaQuerySupport productJpaQuerySupport;

    @Override
    public Slice<Product> searchProducts(Long categoryId, Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 20);
        Slice<ProductPayload.Get> products = productJpaQuerySupport.getProducts(categoryId, pageable);
        return products.map(ProductPayload.Get::toDomain);
    }

}
