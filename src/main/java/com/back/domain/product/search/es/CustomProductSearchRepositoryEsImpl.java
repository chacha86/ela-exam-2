package com.back.domain.product.search.es;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomProductSearchRepositoryEsImpl implements CustomProductSearchRepositoryEs {

    private final ElasticsearchOperations elasticsearchOperations;

    Map<String, List<String>> sholudMap = Map.of(
            "이어폰", List.of("헤드폰", "헤드셋")
    );

    @Override
    public List<ProductDocument> search(String keyword) {

        List<String> synonyms = sholudMap.getOrDefault(keyword, List.of());

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            b.should(s -> s
                                    .match(m -> m
                                            .field("name")
                                            .query(keyword)
                                            .boost(2.0f)
                                            .fuzziness("AUTO")
                                    )
                            );

                            for (String synonym : synonyms) {
                                b.should(s -> s
                                        .match(m -> m
                                                .field("name")
                                                .query(synonym)
                                                .boost(1.0f)
                                                .fuzziness("AUTO")
                                        )
                                );
                            }

                            b.minimumShouldMatch("1");

                            return b;
                        })
                )
                .build();

        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        return searchHits.map(hit -> hit.getContent()).toList();
    }

    @Override
    public boolean isExistIndex() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(ProductDocument.class);

        // 인덱스가 이미 존재하고 데이터가 있으면 스킵
        if (indexOps.exists()) {
            return true;
        }

        return false;
    }

    @Override
    public void deleteIndex() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(ProductDocument.class);

        if (indexOps.exists()) {
            indexOps.delete();
        }
    }

    @Override
    public void createIndex() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(ProductDocument.class);

        if (!indexOps.exists()) {
            indexOps.create(); // 명시적으로 인덱스 생성해야 @Setting이 적용됨
            indexOps.putMapping(indexOps.createMapping());
        }
    }
}
