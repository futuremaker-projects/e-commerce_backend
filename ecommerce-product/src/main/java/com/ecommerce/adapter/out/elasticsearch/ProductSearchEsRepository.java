package com.ecommerce.adapter.out.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchEsRepository extends ElasticsearchRepository<ProductSearchDocument, Long> {
}
