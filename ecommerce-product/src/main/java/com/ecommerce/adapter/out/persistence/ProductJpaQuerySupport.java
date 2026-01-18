package com.ecommerce.adapter.out.persistence;

import com.ecommerce.adapter.out.persistence.entity.ProductEntity;
import com.ecommerce.adapter.out.persistence.payload.ProductPayload;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ecommerce.adapter.out.persistence.entity.QProductEntity.productEntity;

@Repository
public class ProductJpaQuerySupport extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ProductJpaQuerySupport(JPAQueryFactory queryFactory) {
        super(ProductEntity.class);
        this.queryFactory = queryFactory;
    }

    public Slice<ProductPayload.Get> getProducts(Long categoryId, Pageable pageable) {
        List<ProductPayload.Get> products = queryFactory.select(
                        Projections.constructor(
                                ProductPayload.Get.class,
                                productEntity.id, productEntity.productName,
                                productEntity.description, productEntity.price,
                                productEntity.stock, productEntity.categoryId
                        )
                )
                .from(productEntity)
                .where(categoryEq(categoryId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(productEntity.id.desc())
                .fetch();

        boolean hasNext = products.size() > pageable.getPageSize();
        if (hasNext) products.remove(pageable.getPageSize());

        return new SliceImpl<>(products, pageable, hasNext);
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? productEntity.categoryId.eq(categoryId) : null;
    }

    public List<ProductPayload.Get> searchProducts(String productName) {
        return queryFactory.select(
                        Projections.constructor(
                                ProductPayload.Get.class,
                                productEntity.id, productEntity.productName,
                                productEntity.description, productEntity.price,
                                productEntity.stock, productEntity.categoryId
                        )
                )
                .from(productEntity)
                .where(productNameContains(productName))
                .orderBy(productEntity.id.desc())
                .fetch();
    }

    private BooleanExpression productNameContains(String productName) {
        if (productName == null || productName.isBlank()) {
            return null;
        }
        return productEntity.productName.containsIgnoreCase(productName);
    }
}
