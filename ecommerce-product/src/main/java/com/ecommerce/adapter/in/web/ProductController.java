package com.ecommerce.adapter.in.web;

import com.ecommerce.adapter.in.web.dto.ProductDto;
import com.ecommerce.application.port.in.SearchProductsUsecase;
import com.ecommerce.domain.Product;
import com.ecommerce.support.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final SearchProductsUsecase searchProductsUsecase;

    @GetMapping("/products/categories/{categoryId}")
    public Response<Slice<ProductDto.Response>> getProduct(
            @PathVariable Long categoryId,
            @RequestParam Integer pageNum
    ) {
        Slice<Product> products = searchProductsUsecase.searchProducts(categoryId, pageNum);
        Slice<ProductDto.Response> responses = products.map(ProductDto.Response::from);
        return Response.success(responses);
    }

    @GetMapping("/products")
    public Response<Slice<ProductDto.Response>> getProducts(
            @RequestParam(required = false) String productName,
            @RequestParam Integer pageNum
    ) {
        Slice<Product> products = searchProductsUsecase.searchProducts(productName, pageNum);
        Slice<ProductDto.Response> responses = products.map(ProductDto.Response::from);
        return Response.success(responses);
    }

}
