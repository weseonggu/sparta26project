package com.sparta26.baemin.dto.store;

import com.sparta26.baemin.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStoreDto {

    private String name;
    private String description;

    private String opening_time;
    private String closing_time;
    private String open_days;

    private String address;
    private String phone_number;
    private boolean is_active;

    public static ResponseStoreDto toDto(Store store) {
        return new ResponseStoreDto(store.getName(),
                store.getDescription(),
                store.getOpeningTime(),
                store.getClosingTime(),
                store.getOpenDays(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.isActive());
    }
}
