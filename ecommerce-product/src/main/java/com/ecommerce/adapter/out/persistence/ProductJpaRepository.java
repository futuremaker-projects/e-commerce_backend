package com.ecommerce.adapter.out.persistence;

import com.ecommerce.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
