package com.sparta26.baemin.dto.store;

import com.sparta26.baemin.dto.operatinghours.ResponseOperatingDto;
import com.sparta26.baemin.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<ResponseOperatingDto> operatingHours = new ArrayList<>();


    public static ResponseStoreDto toDto(Store store) {
        List<ResponseOperatingDto> operatingHours = store.getOperatingHours().stream()
                .map(ResponseOperatingDto::toDto)
                .collect(Collectors.toList());

        return  new ResponseStoreDto(store.getId().toString(),
                store.getName(),
                store.getDescription(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.isActive(),
                operatingHours);
    }
}
