package com.ecommerce.application.service;

import com.ecommerce.application.port.in.SearchProductsUsecase;
import com.ecommerce.application.port.out.SearchProductPort;
import com.ecommerce.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchProductService implements SearchProductsUsecase {

    private final SearchProductPort searchProductPort;

    @Override
    public Slice<Product> searchProducts(Long categoryId, Integer pageNum) {

        return searchProductPort.searchProducts(categoryId, pageNum);
    }

}
