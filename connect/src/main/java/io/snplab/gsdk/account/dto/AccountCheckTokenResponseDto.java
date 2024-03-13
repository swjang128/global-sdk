package io.snplab.gsdk.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCheckTokenResponseDto {
    @Schema(description = "이메일", example = "eee@example.com")
    private String email;
    @Schema(description = "이름", example = "John")
    private String firstName;
    @Schema(description = "성", example = "Connor")
    private String lastName;
}
