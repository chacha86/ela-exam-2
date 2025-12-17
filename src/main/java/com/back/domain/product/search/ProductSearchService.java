package com.back.domain.product.search;

import com.back.domain.product.search.es.ProductDocument;
import com.back.domain.product.search.es.ProductSearchRepositoryEs;
import com.back.domain.product.search.jpa.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ProductSearchRepositoryEs productSearchRepositoryEs;

    public void resetIndex() {
        productSearchRepositoryEs.deleteIndex();
        syncAll();
        System.out.println("인덱스 재생성 완료");
    }

    public void syncAll() {

        if(productSearchRepositoryEs.isExistIndex()) {
            System.out.println("인덱스가 이미 존재하여 스킵합니다.");
            return;
        }

        productSearchRepositoryEs.createIndex(); // DDL(인덱스 구조 생성)

        // 문서 저장 - DML
        // 문서 저장만 해도 인덱스는 만들어지지만 analyzer 설정 등이 반영되지 않음. 꼭 명시적 인덱스 생성하자
        List<ProductDocument> documents = productSearchRepository.findAll().stream()
                .map(ProductDocument::from)
                .toList();

        productSearchRepositoryEs.saveAll(documents);

        System.out.println("인덱싱 완료: " + documents.size() + "건");
    }

}
