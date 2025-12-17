package com.back.domain.product.search.es;

import com.back.domain.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.suggest.Completion;

@Document(indexName = "products")
@Setting(
        settingPath = "elasticsearch/products-settings.json"
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDocument {

    @Id
    private Long id;

    @Field(type= FieldType.Text, analyzer = "korean_index_analyzer", searchAnalyzer = "korean_search_analyzer")
    private String name;

    @CompletionField
    private Completion suggestion;

    public static ProductDocument from(Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .suggestion(new Completion(product.getSuggestions()))
                .build();
    }
}
