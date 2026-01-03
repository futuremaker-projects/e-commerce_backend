package com.ecommerce.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
public class ProductEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '상품명'")
    private String productName;

    @Column(columnDefinition = "TEXT COMMENT '상품 상세'")
    private String description;

    @Column(columnDefinition = "BIGINT UNSIGNED default 0 NOT NULL COMMENT '가격'")
    private Long price;

    @Column(columnDefinition = "INT UNSIGNED default 0 NOT NULL COMMENT '수량'")
    private Integer quantity;

    @Column(columnDefinition = "BIGINT UNSIGNED NOT NULL COMMENT '카테고리 ID'")
    private Long categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEntity productEntity)) return false;
        return id != null && id.equals(productEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
