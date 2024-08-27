package com.sparta26.baemin.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;

/**
 * 회원 가입시 사용하는 DTO
 */
@Data
@NoArgsConstructor
public class RequestLogInDto {


    @NotNull(message = "이메일을 입력해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식으로 입력해 주세요")
    private String email;

    @NotNull(message = "비번을 입력해주세요. 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자")
    @NotBlank(message = "비번을 입력해주세요. 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 형식으로 만들어 주세요.")
    private String password;



    public RequestLogInDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
