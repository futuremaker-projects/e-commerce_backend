package com.ecommerce.adapter.out.persistence;

import co.elastic.clients.elasticsearch._types.SortOrder;
import com.ecommerce.adapter.out.elasticsearch.ProductMapper;
import com.ecommerce.adapter.out.elasticsearch.ProductSearchDocument;
import com.ecommerce.adapter.out.persistence.payload.ProductPayload;
import com.ecommerce.application.port.out.SearchProductPort;
import com.ecommerce.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchProductAdapter implements SearchProductPort {

    private final ProductJpaQuerySupport productJpaQuerySupport;
    private final ElasticsearchOperations elasticsearchOperations;

    private final int PAGE_NUM = 20;

    /**
     * JPA를 이용한 상품 검색
     */
    @Override
    public Slice<Product> searchProducts(Long categoryId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_NUM);
        Slice<ProductPayload.Get> products = productJpaQuerySupport.getProducts(categoryId, pageable);
        return products.map(ProductPayload.Get::toDomain);
    }

    /**
     * Elasticsearch 를 이용한 상품 검색
     */
    @Override
    public Slice<Product> searchProducts(String productName, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_NUM);
        NativeQueryBuilder queryBuilder = NativeQuery.builder();

        if (productName == null || productName.isEmpty()) {
            queryBuilder
                    .withSort(s -> s.field(f -> f.field("indexedAt").order(SortOrder.Desc)))
                    .build();
        } else {
            queryBuilder
                    .withQuery(q -> q
                            .match(m -> m
                                    .field("productName")
                                    .query(productName)
                            )
                    )
                    .withPageable(pageable)     // es pageable
                    .build();
        }

        NativeQuery query = queryBuilder
                .withPageable(pageable)
                .build();

        // 검색 실행
        SearchHits<ProductSearchDocument> searchHits = elasticsearchOperations.search(query, ProductSearchDocument.class);

        // SearchHits를 Domain 객체 리스트로 변환
        List<Product> contents = searchHits.getSearchHits().stream()
                .map(hit -> ProductMapper.Get.toDomain(hit.getContent()))
                .collect(Collectors.toList());

        // SliceImpl을 통해 Slice 객체 생성 및 반환
        boolean hasNext = contents.size() >= pageable.getPageSize();

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    /**
     * 상품명으로 모든 상품을 검색(SQL)
     * @param productName
     * @return
     */
    @Override
    public List<Product> searchProductsBySql(String productName) {
        List<ProductPayload.Get> products = productJpaQuerySupport.searchProducts(productName);
        return products.stream().map(ProductPayload.Get::toDomain).collect(Collectors.toList());
    }

    /**
     * 상품명으로 모든 상품 검색(Elasticsearch)
     * @param productName
     * @return
     */
    @Override
    public List<Product> searchProductsByEs(String productName) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("productName")
                                .query(productName)
                        )
                ).build();

        // 검색 실행
        SearchHits<ProductSearchDocument> searchHits = elasticsearchOperations.search(query, ProductSearchDocument.class);

        // SearchHits를 Domain 객체 리스트로 변환
        List<Product> contents = searchHits.getSearchHits().stream()
                .map(hit -> ProductMapper.Get.toDomain(hit.getContent()))
                .collect(Collectors.toList());
        return contents;
    }

}
