package com.sparta26.baemin.dto.store;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta26.baemin.dto.category.ResponseCategoryWithOutStoreIdDto;
import com.sparta26.baemin.dto.deliveryzone.ResponseDeliveryWithOutStoreIdDto;
import com.sparta26.baemin.dto.operatinghours.ResponseOperatingDto;
import com.sparta26.baemin.dto.product.ResponseProductWithOutStoreIdDto;
import com.sparta26.baemin.store.entity.Store;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
public class ResponseSearchStoreDto {

    private UUID store_id;
    private String name;
    private String description;

    private String address;
    private String phone_number;
    private boolean is_active;
    private Long member_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private List<ResponseProductWithOutStoreIdDto> product = new ArrayList<>();
    private List<ResponseOperatingDto> operatingHours = new ArrayList<>();
    private List<ResponseDeliveryWithOutStoreIdDto> delivery = new ArrayList<>();
    private List<ResponseCategoryWithOutStoreIdDto> category = new ArrayList<>();


    @QueryProjection
    public ResponseSearchStoreDto(UUID store_id, String name, String description, String address, String phone_number, boolean is_active, Long member_id, LocalDateTime created_at, LocalDateTime updated_at,
                                  List<ResponseProductWithOutStoreIdDto> products,
                                  List<ResponseOperatingDto> operating,
                                  List<ResponseDeliveryWithOutStoreIdDto> delivery,
                                  List<ResponseCategoryWithOutStoreIdDto> category) {
        this.store_id = store_id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone_number = phone_number;
        this.is_active = is_active;
        this.member_id = member_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.product = products;
        this.operatingHours = operating;
        this.delivery = delivery;
        this.category = category;
    }


    public static List<ResponseSearchStoreDto> toDtoList(List<Store> stores) {
        List<ResponseSearchStoreDto> storeDtos = new ArrayList<>();
        for (Store store : stores) {
            ResponseSearchStoreDto storeDto = new ResponseSearchStoreDto();
            storeDto.setStore_id(store.getId());
            storeDto.setName(store.getName());
            storeDto.setDescription(store.getDescription());
            storeDto.setAddress(store.getAddress());
            storeDto.setPhone_number(store.getPhoneNumber());
            storeDto.set_active(store.isActive());
            storeDto.setMember_id(store.getMember().getId());
            storeDto.setCreated_at(store.getCreatedAt());
            storeDto.setUpdated_at(store.getUpdatedAt());

            // 관련 리스트 매핑 (예시 - 각 리스트를 DTO 리스트로 변환하는 메소드 호출)
            storeDto.setProduct(ResponseProductWithOutStoreIdDto.toDtoList(store.getProducts()));
            storeDto.setOperatingHours(ResponseOperatingDto.toDtoList(store.getOperatingHours()));
            storeDto.setDelivery(ResponseDeliveryWithOutStoreIdDto.toDtoList(store.getDeliveryZones()));
            storeDto.setCategory(ResponseCategoryWithOutStoreIdDto.toDtoList(store.getCategories()));
            storeDtos.add(storeDto);
        }
        return storeDtos;
    }


}

