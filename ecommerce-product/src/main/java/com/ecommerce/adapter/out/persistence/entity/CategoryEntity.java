package com.ecommerce.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(columnDefinition = "VARCHAR(100) NOT NULL COMMENT '카테고리명'")
    private String name;

    @Column(columnDefinition = "BIGINT UNSIGNED COMMENT '상위 부모 id'")
    private Long parentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntity categoryEntity)) return false;
        return id != null && id.equals(categoryEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
