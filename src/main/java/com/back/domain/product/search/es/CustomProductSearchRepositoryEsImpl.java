package com.back.domain.product.search.es;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

@RequiredArgsConstructor
public class CustomProductSearchRepositoryEsImpl implements CustomProductSearchRepositoryEs {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<ProductDocument> search(String keyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("name")
                                .query(keyword)
                        )
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
