package com.ecommerce.adapter.out.persistence;

import com.ecommerce.adapter.out.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
}
