package com.sparta26.baemin.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 가입시 사용하는 DTO
 */
@Data
@NoArgsConstructor
public class RequestSignUpDto {

    @NotNull(message = "사용자 이름을 입력해주세요")
    @NotBlank(message = "사용자 이름을 입력해주세요")
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 만들어 주세요")
    private String username;

    @NotNull(message = "닉네일을 입력해주세요")
    @NotBlank(message = "닉네일을 입력해주세요")
    private String nickname;

    @NotNull(message = "이메일을 입력해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식으로 입력해 주세요")
    private String email;

    @NotNull(message = "비번을 입력해주세요. 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자")
    @NotBlank(message = "비번을 입력해주세요. 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,15}$",
            message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 형식으로 만들어 주세요.")
    private String password;

    @NotBlank(message = "일반 사용자는 작성하면 안됩니다.")
    private String roleCode;

    public RequestSignUpDto(String username, String nickname, String email, String password, String roleCode) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
