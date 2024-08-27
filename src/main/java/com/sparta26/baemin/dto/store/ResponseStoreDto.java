package com.sparta26.baemin.dto.store;

import com.sparta26.baemin.store.entity.Store;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseStoreDto {

    private String id;
    private String name;
    private String description;

    private String address;
    private String phone_number;
    private boolean is_active;

    public static ResponseStoreDto toDto(Store store) {
        return new ResponseStoreDto(store.getId().toString(),
                store.getName(),
                store.getDescription(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.isActive());
    }
}
