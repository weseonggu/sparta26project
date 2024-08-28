package com.sparta26.baemin.product.service;

import com.sparta26.baemin.dto.member.ResponseMemberToProductDto;
import com.sparta26.baemin.dto.product.RequestProductDto;
import com.sparta26.baemin.dto.product.ResponseProductDto;
import com.sparta26.baemin.dto.store.ResponseStoreDto;
import com.sparta26.baemin.member.entity.Member;
import com.sparta26.baemin.member.entity.UserRole;
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
        ResponseMemberToProductDto findMember = productToMemberClient.findById(memberId);

        Member member = new Member(findMember.getMember_id(),findMember.getEmail(), findMember.getUsername(), findMember.getNickname(), UserRole.valueOf(findMember.getRole()));
        Store store = new Store(UUID.fromString(findStore.getId()),
                findStore.getName(),
                findStore.getDescription(),
                findStore.getAddress(),
                findStore.getPhone_number(),
                findStore.is_active(),
                member);

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
}
