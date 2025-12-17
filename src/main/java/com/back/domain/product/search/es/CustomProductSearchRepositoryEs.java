package com.back.domain.product.search.es;

import java.util.List;

public interface CustomProductSearchRepositoryEs {
    List<ProductDocument> search(String keyword);
    List<ProductDocument> autoComplete(String prefix);
    List<String> getSuggestions(String prefix);
    boolean isExistIndex();
    void deleteIndex();
    void createIndex();
}
