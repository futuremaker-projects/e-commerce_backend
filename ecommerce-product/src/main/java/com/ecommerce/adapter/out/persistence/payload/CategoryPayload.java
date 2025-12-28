package com.ecommerce.adapter.out.persistence.payload;

public class CategoryPayload {

    public record Get(Long categoryId, String name, Long parentId) {

    }

}
