package io.snplab.gsdk.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class AccountChangeDto {
    @NotBlank(message = "입력값 필요")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}",
            message = "숫자, 특수문자, 소문자, 대문자가 각각 하나이상들어간 8자리이상 조합")
    private String password;

    @NotBlank(message = "입력값 필요.")
    private String passwordCheck;

    @NotBlank(message = "입력값 필요.")
    @Schema(description = "password 변경을 위한 토큰")
    private String base64EncryptedData;
}
