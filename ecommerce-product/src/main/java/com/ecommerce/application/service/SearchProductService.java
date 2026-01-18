package com.ecommerce.application.service;

import com.ecommerce.application.port.in.SearchProductsUsecase;
import com.ecommerce.application.port.out.SearchProductPort;
import com.ecommerce.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchProductService implements SearchProductsUsecase {

    private final SearchProductPort searchProductPort;

    @Override
    public Slice<Product> searchProductsByEs(Long categoryId, Integer pageNum) {
        return searchProductPort.searchProducts(categoryId, pageNum);
    }

    @Override
    public Slice<Product> searchProductsByEs(String productName, Integer pageNum) {
        return searchProductPort.searchProducts(productName, pageNum);
    }

    @Override
    public Product getProduct(Long productId) {
        return null;
    }

    @Override
    public List<Product> searchProductsBySql(String productName) {
        return searchProductPort.searchProductsBySql(productName);
    }

    @Override
    public List<Product> searchProductsByEs(String productName) {
        return searchProductPort.searchProductsByEs(productName);
    }

}
