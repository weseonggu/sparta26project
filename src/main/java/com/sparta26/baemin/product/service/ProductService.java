package com.sparta26.baemin.product.service;

import com.sparta26.baemin.dto.product.RequestProductDto;
import com.sparta26.baemin.dto.product.RequestProductWithoutStockDto;
import com.sparta26.baemin.dto.product.ResponseProductDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.exception.exceptionsdefined.ProductNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.UuidFormatException;
import com.sparta26.baemin.product.clinet.ProductToMemberClient;
import com.sparta26.baemin.product.clinet.ProductToStoreClient;
import com.sparta26.baemin.product.entity.Product;
import com.sparta26.baemin.product.repository.ProductRepository;
import com.sparta26.baemin.store.entity.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductToStoreClient productToStoreClient;
    private final ProductToMemberClient productToMemberClient;

    /**
     * 상품 등록 가게 주인만 가능
     * @param request
     * @param storeId
     * @param memberId
     * @param email
     * @return
     */
    @Transactional
    public ResponseProductDto createProduct(RequestProductDto request, String storeId, Long memberId, String email) {
        // product 세팅을 위한 조회
        ResponseStoreDto findStore = productToStoreClient.findByIdAndMemberId(storeId, memberId);

        Store store = new Store(UUID.fromString(findStore.getId()));

        Product product = Product.createProduct(request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock_quantity(),
                request.getCategory(),
                request.getImageUrl(),
                email,
                store);

        Product savedProduct = productRepository.save(product);
        return ResponseProductDto.toDto(savedProduct);
    }


    @Transactional
    public ResponseProductDto updateProduct(RequestProductWithoutStockDto request, Long memberId, String email, String role, String productId) {

        if (!isValidUUID(productId)) {
            log.error("Invalid UUID = {}",productId);
            throw new UuidFormatException("Invalid UUID format");
        }

        Product findProduct = productRepository.findById(UUID.fromString(productId)).orElseThrow(() ->
                new ProductNotFoundException("not found product"));

        Product product = productRepository.findById(UUID.fromString(productId)).orElseThrow(() -> new ProductNotFoundException("not found product"));

        if (role.equals("ROLE_OWNER")) {
            // 가게 주인 본인이 수정시 본인 확인 메서드
            ResponseStoreDto owner = productToStoreClient.findByIdAndMemberId(findProduct.getStore().getId().toString(), memberId);
            product.update(request, email);
        }else {
            product.update(request, email);
        }
        return ResponseProductDto.toDto(product);
    }

    /**
     * UUID 형식 검증 메서드
     * @param storeId
     * @return
     */
    public boolean isValidUUID(String storeId) {
        try {
            UUID.fromString(storeId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
