package com.ecommerce.domain;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {

    private Long categoryId;
    private String name;

    private Long parentId;

    public static Category of(Long id, String name, Long parentId) {
        return Category.builder()
                .categoryId(id)
                .name(name)
                .parentId(parentId)
                .build();
    }

}
