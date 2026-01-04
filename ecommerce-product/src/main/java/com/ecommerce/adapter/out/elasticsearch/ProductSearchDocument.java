package com.ecommerce.adapter.out.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "search")
public class ProductSearchDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    // product 정보
    @Field(type = FieldType.Text)
    private String productName;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Long)
    private Long price;
    @Field(type = FieldType.Integer)
    private Integer stock;

    // category 정보
    @Field(type = FieldType.Keyword)
    private Long categoryId;
    @Field(type = FieldType.Text)
    private String categoryName;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime indexedAt;

}
