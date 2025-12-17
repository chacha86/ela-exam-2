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

    @Test
    @DisplayName("3. 동의어 - '핸드폰' 검색")
    void 동의어_테스트() {
        String keyword = "핸드폰";

        List<Product> mysqlResult = productSearchRepository.searchByKeyword(keyword);
        List<ProductDocument> esResult = productSearchRepositoryEs.search(keyword);

        printResult(keyword, mysqlResult, esResult);

        // MySQL: "핸드폰 거치대"만
        // ES: 동의어 설정 시 "휴대폰", "스마트폰" 포함 상품도 검색
    }

    @Test
    @DisplayName("4. 유사어 - '이어폰' 검색")
    void 유사어_테스트() {
        String keyword = "이어폰";

        List<Product> mysqlResult = productSearchRepository.searchByKeyword(keyword);
        List<ProductDocument> esResult = productSearchRepositoryEs.search(keyword);

        printResult(keyword, mysqlResult, esResult);

        // MySQL: 정확히 "이어폰" 포함된 것만
        // ES: 연관도 기반 정렬
    }

    @Test
    @DisplayName("5. 오타 교정 - '놑트북' 검색")
    void 오타교정_테스트() {
        String keyword = "놑트북";

        List<Product> mysqlResult = productSearchRepository.searchByKeyword(keyword);
        List<ProductDocument> esResult = productSearchRepositoryEs.search(keyword);

        printResult(keyword, mysqlResult, esResult);

        // MySQL: 0건
        // ES: Fuzzy 검색으로 "노트북" 관련 상품 찾음
    }

    @Test
    @DisplayName("6. 오타 교정 - '키볻드' 검색")
    void 오타교정_테스트2() {
        String keyword = "키볻드";

        List<Product> mysqlResult = productSearchRepository.searchByKeyword(keyword);
        List<ProductDocument> esResult = productSearchRepositoryEs.search(keyword);

        printResult(keyword, mysqlResult, esResult);

        // MySQL: 0건
        // ES: Fuzzy 검색으로 "키보드" 관련 상품 찾음
    }

    @Test
    @DisplayName("8. 제시어 목록 자동 완성 - '노트' 입력했을 때 노트 관련 제시어 목록 제공")
    void 제시어_목록_자동완성_테스트() {
        String keyword = "노트";
        List<String> esResult = productSearchRepositoryEs.getSuggestions(keyword);

        esResult.forEach(s -> System.out.println("  - " + s));
    }

    @Test
    @DisplayName("9. 문서 자동 완성 - '노트' 입력했을 때 노트 관련 키워드와 해당 문서 제공")
    void 문서_자동완성_테스트() {
        String keyword = "노트";
        List<ProductDocument> esResult = productSearchRepositoryEs.autoComplete(keyword);

        printResult(keyword, List.of(), esResult);
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
