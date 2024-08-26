package com.sparta26.baemin.dto.store;

import com.sparta26.baemin.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestStoreDto {

    @NotBlank(message = "가게 이름은 필수 입니다.")
    private String name;
    
    private String description;
    private String opening_time;
    private String closing_time;
    private String open_days;
    
    @NotBlank(message = "가게 주소는 필수 입니다.")
    private String address;
    @NotBlank(message = "가게 번호는 필수 입니다.")
    private String phone_number;


}
