package com.back.domain.product.search;

import com.back.domain.product.product.Product;
import com.back.domain.product.search.es.ProductDocument;
import com.back.domain.product.search.es.ProductSearchRepositoryEs;
import com.back.domain.product.search.jpa.ProductSearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;


@SpringBootTest
public class ProductSearchUseCaseTest {

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Autowired
    private ProductSearchRepositoryEs productSearchRepositoryEs;

    @Test
    @DisplayName("1. 복합어 분리 - '노트북' 검색")
    void 복합어_테스트() {
        String keyword = "삼성 노트북";

        List<Product> mysqlResult = productSearchRepository.searchByKeyword(keyword);
        List<ProductDocument> esResult = productSearchRepositoryEs.search(keyword);
        printResult(keyword, mysqlResult, esResult);

        // MySQL: "삼성 노트북"이 포함된 결과만 나옴.
        // ES: 형태소 분석으로 "삼성전자노트북", "LG그램노트북" 등도 모두 찾음
    }

    @Test
    @DisplayName("2. 한영 혼용 - '맥북' 검색")
    void 한영혼용_테스트() {
        String keyword = "맥북";

        List<Product> mysqlResult = productSearchRepository.searchByKeyword(keyword);
        List<ProductDocument> esResult = productSearchRepositoryEs.search(keyword);

        printResult(keyword, mysqlResult, esResult);

        // MySQL: "Apple MacBook Pro" 못 찾음
        // ES: 동의어 설정 시 찾을 수 있음
    }

    private void printResult(String keyword, List<Product> mysql, List<ProductDocument> es) {
        System.out.println("\n========================================");
        System.out.println("검색어: " + keyword);
        System.out.println("========================================");

        System.out.println("\n[MySQL 결과] " + mysql.size() + "건");
        mysql.forEach(p -> System.out.println("  - " + p.getName()));

        System.out.println("\n[Elasticsearch 결과] " + es.size() + "건");
        es.forEach(p -> System.out.println("  - " + p.getName()));

        System.out.println();
    }
}
