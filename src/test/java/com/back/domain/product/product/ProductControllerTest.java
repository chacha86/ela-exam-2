package com.back.domain.product.product;

import com.back.domain.product.product.dto.CreateProductRequest;
import com.back.domain.product.product.dto.UpdateProductRequest;
import com.back.domain.product.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    private static final String BASE_URL = "/api/v1/products";

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /api/v1/products")
    class CreateProduct {

        @Test
        @DisplayName("상품 생성 성공")
        void createProduct_Success() throws Exception {
            // given
            CreateProductRequest request = new CreateProductRequest(
                    "김치찌개",
                    "food",
                    "맛있는 김치찌개",
                    12000,
                    List.of()
            );

            // when
            ResultActions result = mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("201-1"))
                    .andExpect(jsonPath("$.msg").value("상품 등록 성공"))
                    .andExpect(jsonPath("$.data.id").exists())
                    .andExpect(jsonPath("$.data.name").value("김치찌개"))
                    .andExpect(jsonPath("$.data.category").value("food"))
                    .andExpect(jsonPath("$.data.price").value(12000))
                    .andExpect(jsonPath("$.data.createdAt").exists())
                    .andExpect(jsonPath("$.data.updatedAt").exists());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/products/{id}")
    class GetProduct {

        @Test
        @DisplayName("상품 단건 조회 성공")
        void getProduct_Success() throws Exception {
            // given
            Product product = createAndSaveProduct("열무김치", "food", 8000);

            // when
            ResultActions result = mockMvc.perform(get(BASE_URL + "/{id}", product.getId()));

            // then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("200-1"))
                    .andExpect(jsonPath("$.data.id").value(product.getId()))
                    .andExpect(jsonPath("$.data.name").value("열무김치"))
                    .andExpect(jsonPath("$.data.category").value("food"))
                    .andExpect(jsonPath("$.data.price").value(8000));
        }

        @Test
        @DisplayName("존재하지 않는 상품 조회 시 실패")
        void getProduct_NotFound() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get(BASE_URL + "/{id}", 999L));

            // then
            result.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.resultCode").value("404-1"));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/products/{id}")
    class UpdateProduct {

        @Test
        @DisplayName("상품 수정 성공")
        void updateProduct_Success() throws Exception {
            // given
            Product product = createAndSaveProduct("김치전", "food", 10000);

            UpdateProductRequest request = new UpdateProductRequest(
                    "김치전(수정)",
                    "food",
                    "수정된 김치전",
                    15000
            );

            // when
            ResultActions result = mockMvc.perform(put(BASE_URL + "/{id}", product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("200-1"))
                    .andExpect(jsonPath("$.msg").value("상품 수정 성공"))
                    .andExpect(jsonPath("$.data.id").value(product.getId()))
                    .andExpect(jsonPath("$.data.name").value("김치전(수정)"))
                    .andExpect(jsonPath("$.data.price").value(15000));
        }

        @Test
        @DisplayName("존재하지 않는 상품 수정 시 실패")
        void updateProduct_NotFound() throws Exception {
            // given
            UpdateProductRequest request = new UpdateProductRequest(
                    "없는상품",
                    "food",
                    "없는 상품 설명",
                    10000
            );

            // when
            ResultActions result = mockMvc.perform(put(BASE_URL + "/{id}", 999L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.resultCode").value("404-1"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/products/{id}")
    class DeleteProduct {

        @Test
        @DisplayName("상품 삭제 성공")
        void deleteProduct_Success() throws Exception {
            // given
            Product product = createAndSaveProduct("삭제할상품", "food", 5000);

            // when
            ResultActions result = mockMvc.perform(delete(BASE_URL + "/{id}", product.getId()));

            // then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("200-1"))
                    .andExpect(jsonPath("$.msg").value("상품 삭제 성공"));

            // verify deletion
            mockMvc.perform(get(BASE_URL + "/{id}", product.getId()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("존재하지 않는 상품 삭제 시 실패")
        void deleteProduct_NotFound() throws Exception {
            // when
            ResultActions result = mockMvc.perform(delete(BASE_URL + "/{id}", 999L));

            // then
            result.andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.resultCode").value("404-1"));
        }
    }

    private Product createAndSaveProduct(String name, String category, int price) {
        Product product = Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .build();
        return productRepository.save(product);
    }
}

