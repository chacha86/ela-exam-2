package com.back.domain.product.search.es;

import com.back.domain.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {

    @Id
    private Long id;

    @Field(type= FieldType.Text)
    private String name;

    public static ProductDocument from(Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .build();
    }
}
