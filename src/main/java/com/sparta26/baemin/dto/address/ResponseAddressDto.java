package com.sparta26.baemin.dto.address;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.sparta26.baemin.address.entity.Address;
import com.sparta26.baemin.dto.common.AuditDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonFilter("AddressFilter")
public class ResponseAddressDto extends AuditDto implements Serializable {
    private UUID id;

    private String zipCode;

    private String roadAddress;

    private String roadAddressEnglish;

    public ResponseAddressDto(Address address) {
        this.id = address.getId();
        this.zipCode = address.getZipCode();
        this.roadAddress = address.getRoadAddress();
        this.roadAddressEnglish = address.getRoadAddressEnglish();
    }

}
