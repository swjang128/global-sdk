package io.snplab.gsdk.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCheckResponseDto {
    @Schema(description = "이메일", example = "eee@example.com")
    private String email;
}
