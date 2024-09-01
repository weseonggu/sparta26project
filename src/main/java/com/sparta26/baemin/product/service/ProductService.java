package com.sparta26.baemin.product.service;

import com.sparta26.baemin.dto.product.RequestProductDto;
import com.sparta26.baemin.dto.product.RequestProductWithoutStockDto;
import com.sparta26.baemin.dto.product.RequestSearchProductDto;
import com.sparta26.baemin.dto.product.ResponseProductDto;
import com.sparta26.baemin.exception.exceptionsdefined.ProductNotFoundException;
import com.sparta26.baemin.exception.exceptionsdefined.StoreNotFoundException;
import com.sparta26.baemin.product.entity.Product;
import com.sparta26.baemin.product.repository.ProductRepository;
import com.sparta26.baemin.store.entity.Store;
import com.sparta26.baemin.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    /**
     * 상품 등록 가게 주인만 가능
     * @param request
     * @param storeId
     * @param memberId
     * @param email
     * @return
     */
    @Transactional
    public ResponseProductDto createProduct(RequestProductDto request, UUID storeId, Long memberId, String email) {


        Store findStore = storeRepository.findByIdAndMemberId(storeId, memberId).orElseThrow(() ->
                new ProductNotFoundException(String.format("Store with id %s not found", storeId)));

        Optional<Product> findProduct = productRepository.findByStoreIdAndName(storeId, request.getName());
        if (findProduct.isPresent()) {
            throw new IllegalArgumentException("중복된 상품명이 존재합니다.");
        }

        Store store = new Store(findStore.getId());

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

    /**
     * 개별 상품 수정 | 가게 주인, 관리자
     * @param request
     * @param memberId
     * @param email
     * @param role
     * @param productId
     * @return
     */
    @Transactional
    public ResponseProductDto updateProduct(RequestProductWithoutStockDto request, Long memberId, String email, String role, UUID productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("not found product"));

        List<Product> productList = productRepository.findDuplicatedByStoreIdAndName(product.getStore().getId(), product.getName());
        for (Product product1 : productList) {
            if (product1.getName().equals(request.getName())) {
                log.error("중복된 상품명이 이미 존재합니다. name = " + request.getName());
                throw new IllegalArgumentException(request.getName()+" 상품명이 이미 존재합니다.");
            }
        }

        if (role.equals("ROLE_OWNER")) {
            // 가게 주인 본인이 수정시 본인 확인 메서드
            storeRepository.findByIdAndMemberId(product.getStore().getId(), memberId).orElseThrow(() ->
                    new StoreNotFoundException("본인 가게의 상품만 수정이 가능합니다."));

            product.update(request);
        }else {
            product.update(request);
        }
        return ResponseProductDto.toDto(product);
    }

    /**
     * 개별 상품 조회
     * @param productId
     * @return
     */
    public ResponseProductDto findOneProduct(UUID productId) {

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("not found product"));

        return ResponseProductDto.toDto(product);
    }

    /**
     * 전체 상품 조회
     * @param condition
     * @param pageable
     * @return
     */
    public Page<ResponseProductDto> findAllProducts(RequestSearchProductDto condition, Pageable pageable) {

        return productRepository.findAllProduct(pageable, condition);
    }

    /**
     * 개별 상품 삭제 | 가게 주인, 관리자
     * @param productId
     * @param email
     * @param role
     */
    @Transactional
    public void deleteProduct(UUID productId, String email, String role) {

        if (role.equals("ROLE_OWNER")) {
            Product product = productRepository.findByIdAndCreatedBy(productId, email).orElseThrow(() ->
                    new ProductNotFoundException("본인 가게이거나 관리자만 삭제가 허용됩니다. or 등록된 상품이 없습니다."));

            product.delete(email);
        }else {
            Product product = productRepository.findById(productId).orElseThrow(() ->
                    new ProductNotFoundException("not found product"));

            product.delete(email);
        }
    }

    /**
     * 상품 활성화 | 가게 주인
     * @param productId
     * @return
     */
    @Transactional
    public String availableProduct(UUID productId, String email) {
        String answer = "";
        Product product = productRepository.findByIdAndCreatedBy(productId, email).orElseThrow(() ->
                new ProductNotFoundException("본인의 가게 상품만 접근 가능하거나 등록된 상품이 없습니다."));

        if (product.isAvailable()) {
            answer = "현재 상품은 활성화 상태입니다.";
        } else {
            product.changeIsAvailable(true);
            answer = "상품 활성화 완료";
        }
        return answer;
    }

    /**
     * 상품 비활성화 | 가게 주인
     * @param productId
     * @return
     */
    @Transactional
    public String unavailableProduct(UUID productId, String email) {
        String answer = "";
        Product product = productRepository.findByIdAndCreatedBy(productId, email).orElseThrow(() ->
                new ProductNotFoundException("not found product"));

        if (product.isAvailable()) {
            product.changeIsAvailable(false);
            answer = "상품 비활성화 완료";
        } else {
            answer = "현재 상품은 비활성화 상태입니다.";
        }
        return answer;

    }

    /**
     * 상품 수량 증가 | 가게 주인
     * @param stock
     * @param productId
     * @param email
     * @return
     */
    @Transactional
    public String addStockQuantity(Integer stock, UUID productId, String email) {
        Product product = productRepository.findByIdAndCreatedBy(productId, email).orElseThrow(() ->
                new ProductNotFoundException("본인의 가게 상품만 접근 가능하거나 등록된 상품이 없습니다."));

        product.addStock(stock);
        return "상품 수량 증가 완료";
    }
}
