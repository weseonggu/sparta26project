package com.sparta26.baemin.dto.address;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.sparta26.baemin.dto.common.AuditDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonFilter("AddressFilter")
public class ResponseAddressDto extends AuditDto implements Serializable {
    private String zipCode;

    private String roadAddress;

    private String roadAddressEnglish;
}
