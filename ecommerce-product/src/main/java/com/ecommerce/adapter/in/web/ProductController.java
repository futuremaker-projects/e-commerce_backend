package com.ecommerce.adapter.in.web;

import com.ecommerce.adapter.in.web.dto.ProductDto;
import com.ecommerce.application.port.in.SearchProductsUsecase;
import com.ecommerce.domain.Product;
import com.ecommerce.support.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Slice<Product> products = searchProductsUsecase.searchProductsByEs(categoryId, pageNum);
        Slice<ProductDto.Response> responses = products.map(ProductDto.Response::from);
        return Response.success(responses);
    }

    @GetMapping("/product/{productId}")
    public Response<ProductDto.Response> getProduct(@PathVariable Long productId) {
        Product product = searchProductsUsecase.getProduct(productId);
        ProductDto.Response productDto = ProductDto.Response.from(product);
        return Response.success(productDto);
    }

    @GetMapping("/products/sql")
    public Response<List<ProductDto.Response>> getAllProductsBySql(
            @RequestParam String productName
    ){
        List<Product> products = searchProductsUsecase.searchProductsBySql(productName);
        List<ProductDto.Response> result = products.stream().map(ProductDto.Response::from).toList();

        return Response.success(result);
    }

    @GetMapping("/products/es")
    public Response<List<ProductDto.Response>> getProductsByEs(
            @RequestParam String productName
    ) {
        List<Product> products = searchProductsUsecase.searchProductsByEs(productName);
        List<ProductDto.Response> responses = products.stream().map(ProductDto.Response::from).toList();
        return Response.success(responses);
    }

}
