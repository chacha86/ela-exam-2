package com.back.domain.product.product;

import com.back.domain.product.product.dto.CreateProductRequest;
import com.back.domain.product.product.dto.ProductResponse;
import com.back.domain.product.product.dto.UpdateProductRequest;
import com.back.standard.dto.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public RsData<ProductResponse> create(@RequestBody CreateProductRequest request) {
        ProductResponse response = productService.create(
                request.name(),
                request.category(),
                request.description(),
                request.price(),
                request.suggestions()
        );
        return RsData.success("201-1", "상품 등록 성공", response);
    }

    @GetMapping("/{id}")
    public RsData<ProductResponse> getById(@PathVariable Long id) {
        ProductResponse response = productService.getById(id);
        return RsData.success(response);
    }

    @PutMapping("/{id}")
    public RsData<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request
    ) {
        ProductResponse response = productService.update(id,
                request.name(),
                request.category(),
                request.description(),
                request.price()
        );
        return RsData.success("상품 수정 성공", response);
    }

    @DeleteMapping("/{id}")
    public RsData<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return RsData.success("200-1", "상품 삭제 성공", null);
    }
}
