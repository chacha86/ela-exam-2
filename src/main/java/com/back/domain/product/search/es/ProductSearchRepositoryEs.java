package com.back.domain.product.search.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepositoryEs extends ElasticsearchRepository<ProductDocument, Long>, CustomProductSearchRepositoryEs {
}
