package com.back.global.baseInitData;

import com.back.domain.product.product.ProductService;
import com.back.domain.product.search.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;
    private final ProductService productService;
    private final ProductSearchService productSearchService;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {
            self.work1();
            self.work2();
        };
    }

    @Transactional
    public void work1() {
        if (productService.count() > 0) return;

        // 1. 띄어쓰기/복합어 테스트
        productService.create("삼성전자노트북", "전자기기", "삼성에서 만든 고성능 노트북입니다", 1500000,
                List.of("삼성 노트북", "삼성전자 노트북", "삼성 랩탑"));
        productService.create("삼성 노트북 펜", "전자기기", "펜 기능이 있는 노트북", 1800000,
                List.of("삼성 펜 노트북", "노트북 펜", "삼성 터치 노트북"));
        productService.create("LG그램노트북", "전자기기", "LG전자의 초경량 노트북", 1600000,
                List.of("LG 그램", "엘지 노트북", "그램 노트북", "LG 경량 노트북"));
        productService.create("노트북파우치", "액세서리", "노트북 보관용 파우치", 35000,
                List.of("노트북 가방", "노트북 케이스", "랩탑 파우치"));
        productService.create("노트 북커버", "문구", "다이어리 노트 커버", 15000,
                List.of("다이어리 커버", "노트 케이스", "수첩 커버"));

        // 2. 영문/한글 혼용 테스트
        productService.create("Apple MacBook Pro", "전자기기", "애플 맥북 프로 16인치", 3500000,
                List.of("맥북 프로", "애플 맥북", "macbook pro", "맥북프로"));
        productService.create("맥북프로 케이스", "액세서리", "MacBook 전용 케이스", 45000,
                List.of("맥북 케이스", "macbook case", "애플 노트북 케이스"));
        productService.create("에어팟 프로", "전자기기", "AirPods Pro 2세대", 359000,
                List.of("airpods pro", "애플 이어폰", "에어팟프로", "애플 에어팟"));
        productService.create("AirPods Max", "전자기기", "애플 에어팟 맥스 헤드폰", 769000,
                List.of("에어팟 맥스", "애플 헤드폰", "airpods max"));
        productService.create("아이폰15 프로", "전자기기", "iPhone 15 Pro 256GB", 1550000,
                List.of("iphone 15 pro", "애플 아이폰", "아이폰 프로", "아이폰15프로"));

        // 3. 유사어/동의어 테스트
        productService.create("무선 이어폰", "전자기기", "블루투스 이어폰 최신형", 89000,
                List.of("블루투스 이어폰", "무선 이어버드", "와이어리스 이어폰"));
        productService.create("블루투스 헤드셋", "전자기기", "무선 헤드폰 게이밍용", 120000,
                List.of("무선 헤드셋", "게이밍 헤드셋", "블루투스 헤드폰"));
        productService.create("유선 이어폰", "전자기기", "고음질 유선 이어폰", 45000,
                List.of("유선 이어버드", "줄 이어폰", "유선 헤드폰"));
        productService.create("게이밍 헤드폰", "전자기기", "게임용 무선 헤드셋", 150000,
                List.of("게임용 헤드폰", "게이밍 헤드셋", "게임 헤드폰"));
        productService.create("휴대폰 케이스", "액세서리", "스마트폰 보호 케이스", 25000,
                List.of("핸드폰 케이스", "스마트폰 케이스", "폰케이스"));
        productService.create("핸드폰 거치대", "액세서리", "휴대폰 차량용 거치대", 18000,
                List.of("휴대폰 거치대", "스마트폰 거치대", "차량용 거치대"));
        productService.create("스마트폰 충전기", "액세서리", "고속 휴대폰 충전기", 22000,
                List.of("핸드폰 충전기", "휴대폰 충전기", "고속 충전기"));

        // 4. 오타 테스트용 단어
        productService.create("키보드 기계식", "전자기기", "청축 기계식 키보드", 89000,
                List.of("기계식 키보드", "청축 키보드", "게이밍 키보드"));
        productService.create("무선 키보드", "전자기기", "블루투스 키보드 슬림형", 55000,
                List.of("블루투스 키보드", "와이어리스 키보드", "무선 키보드"));
        productService.create("모니터 27인치", "전자기기", "삼성 27인치 모니터", 350000,
                List.of("27인치 모니터", "삼성 모니터", "컴퓨터 모니터"));
        productService.create("모니터 암", "액세서리", "모니터 거치대 암", 45000,
                List.of("모니터 거치대", "모니터 스탠드", "모니터암"));
        productService.create("마우스 무선", "전자기기", "로지텍 무선 마우스", 35000,
                List.of("무선 마우스", "블루투스 마우스", "로지텍 마우스"));
        productService.create("마우스 패드", "액세서리", "게이밍 마우스패드 대형", 25000,
                List.of("마우스패드", "게이밍 패드", "장패드"));

        // 5. 부분 일치 테스트
        productService.create("운동화 나이키", "신발", "나이키 에어맥스 운동화", 159000,
                List.of("나이키 운동화", "에어맥스", "나이키 신발"));
        productService.create("나이키 반팔티", "의류", "Nike 드라이핏 반팔", 45000,
                List.of("나이키 티셔츠", "나이키 반팔", "nike 반팔"));
        productService.create("아디다스 운동화", "신발", "아디다스 울트라부스트", 189000,
                List.of("아디다스 신발", "울트라부스트", "adidas 운동화"));
        productService.create("런닝화 아식스", "신발", "아식스 젤카야노 런닝화", 169000,
                List.of("아식스 런닝화", "젤카야노", "아식스 운동화"));

        // 6. 설명에만 키워드 있는 경우
        productService.create("여름 반바지", "의류", "시원한 면 소재로 제작된 캐주얼 반바지", 35000,
                List.of("반바지", "여름 바지", "캐주얼 반바지"));
        productService.create("린넨 셔츠", "의류", "여름에 입기 좋은 시원한 린넨 소재", 55000,
                List.of("린넨 옷", "여름 셔츠", "시원한 셔츠"));
        productService.create("가디건", "의류", "봄가을에 입기 좋은 얇은 니트", 45000,
                List.of("니트 가디건", "봄 가디건", "얇은 가디건"));
        productService.create("패딩 점퍼", "의류", "겨울철 따뜻한 오리털 패딩", 250000,
                List.of("겨울 패딩", "오리털 패딩", "패딩 자켓"));

        // 7. 특수 케이스
        productService.create("C타입 충전기", "액세서리", "USB-C 고속충전기", 15000,
                List.of("usb-c 충전기", "타입c 충전기", "C타입 케이블"));
        productService.create("USB 허브", "액세서리", "USB 3.0 4포트 허브", 25000,
                List.of("usb 멀티포트", "usb 확장", "usb 분배기"));
        productService.create("HDMI 케이블", "액세서리", "4K HDMI 2.1 케이블", 18000,
                List.of("hdmi 선", "hdmi 연결선", "4k 케이블"));
        productService.create("65W 충전어댑터", "액세서리", "PD 65W 고속 충전기", 35000,
                List.of("pd 충전기", "65w 충전기", "고속 어댑터"));

        System.out.println("테스트 데이터 저장 완료");
    }

    public void work2() {
        productSearchService.resetIndex();
    }
}
