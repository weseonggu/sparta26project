package com.sparta26.baemin.dto.store;

import com.sparta26.baemin.dto.operatinghours.ResponseOperatingDto;
import com.sparta26.baemin.operatinghours.entity.OperatingHours;
import com.sparta26.baemin.store.entity.Store;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class ResponseFindStoreDto {

    private String id;
    private String name;
    private String description;

    private String address;
    private String phone_number;
    private boolean is_active;
    private List<ResponseOperatingDto> operating = new ArrayList<>();

    public ResponseFindStoreDto(String id, String name, String description, String address, String phone_number, boolean is_active, List<ResponseOperatingDto> operating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone_number = phone_number;
        this.is_active = is_active;
        this.operating = operating;
    }

    public ResponseFindStoreDto(Store store) {
        this.id = store.getId().toString();
        this.name = store.getName();
        this.description = store.getDescription();
        this.address = store.getAddress();
        this.phone_number = store.getPhoneNumber();
        this.is_active = store.isActive();
        List<OperatingHours> operatingHours = store.getOperatingHours();
        for (OperatingHours op : operatingHours) {
            operating.add(new ResponseOperatingDto(op.getId().toString(),
                    op.getOpeningTime(),
                    op.getClosingTime(),
                    op.getOpenDays(),
                    op.getLastOrder()));
        }
    }


}
