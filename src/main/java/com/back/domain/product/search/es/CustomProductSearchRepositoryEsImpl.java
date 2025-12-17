package com.back.domain.product.search.es;

import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.response.CompletionSuggestion;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;

import java.util.Collections;
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

    private SearchHits<ProductDocument> suggestQuery(String prefix) {
        CompletionSuggester completionSuggester = new CompletionSuggester.Builder()
                .field("suggestion")
                .size(10)
                .skipDuplicates(true)
                .build();

        FieldSuggester fieldSuggester = new FieldSuggester.Builder()
                .prefix(prefix)
                .completion(completionSuggester)
                .build();

        Suggester suggester = new Suggester.Builder()
                .suggesters("prod-suggest", fieldSuggester)
                .build();

        NativeQuery nativeQuery = NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0) // hits는 필요 없고 suggest만
                .build();

        return elasticsearchOperations.search(nativeQuery, ProductDocument.class);
    }

    @Override
    public List<ProductDocument> autoComplete(String prefix) {

        SearchHits<ProductDocument> searchHits = suggestQuery(prefix);
        Suggest suggest = searchHits.getSuggest();

        if (suggest == null) {
            return Collections.emptyList();
        }

        return suggest.getSuggestions().stream()
                .filter(CompletionSuggestion.class::isInstance)
                .map(CompletionSuggestion.class::cast)
                .flatMap(completionSuggestion ->
                        completionSuggestion.getEntries().stream()
                                .flatMap(entry -> ((CompletionSuggestion.Entry<ProductDocument>) entry).getOptions().stream())
                                .map(obj -> {
                                    CompletionSuggestion.Entry.Option option = (CompletionSuggestion.Entry.Option) obj;

                                    return option.getSearchHit() == null ? null : option.getSearchHit().getContent();
                                })
                ).toList();

    }

    @Override
    public List<String> getSuggestions(String prefix) {

        SearchHits<ProductDocument> searchHits = suggestQuery(prefix);
        Suggest suggest = searchHits.getSuggest();

        if (suggest == null) {
            return Collections.emptyList();
        }

        return suggest.getSuggestions().stream()
                .filter(s -> s instanceof CompletionSuggestion)
                .map(s -> (CompletionSuggestion<?>) s)
                .flatMap(cs -> cs.getEntries().stream())
                .flatMap(e -> e.getOptions().stream())
                .map(option -> option.getText())
                .distinct()
                .toList();

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
