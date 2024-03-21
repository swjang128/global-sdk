package io.snplab.gsdk.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountGetResponseDto {
    @Schema(description = "이메일", example = "johnconnor@example.com")
    private String email;
    @Schema(description = "이름", example = "John")
    private String firstName;
    @Schema(description = "성", example = "Connor")
    private String lastName;
    @Schema(description = "핸드폰번호", example = "01000009999")
    private String phoneNumber;
    @Schema(description = "회사이름", example = "비바리퍼블리카")
    private String companyName;
    @Schema(description = "담당 서비스 이름", example = "토스")
    private String serviceName;
}
