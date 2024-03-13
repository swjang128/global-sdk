package io.snplab.gsdk.account.dto;

import io.snplab.gsdk.account.domain.AccountStatus;
import io.snplab.gsdk.account.repository.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("회원가입 DTO")
public class AccountCreateDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(min = 1, max = 50, message = "이메일 입력값 50자 미만.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @Schema(description = "이메일", example = "example@snplab.io", required = true)
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}",
            message = "숫자, 특수문자, 소문자, 대문자가 각각 하나이상들어간 8자리이상 조합")
    @Schema(description = "비밀번호", example = "examplePassword1!", required = true)
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=1, max=20, message="이름 입력값 20자 미만.")
    @Schema(description = "사용자 이름", example = "Barret", required = true)
    private String firstName;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=1, max=20, message="이름 입력값 20자 미만.")
    @Schema(description = "사용자 이름", example = "Wallace", required = true)
    private String lastName;

    @Size(min = 1, max = 30, message = "회사 입력값 30자 미만.")
    @Schema(description = "사용자 회사명", example = "snplab")
    private String companyName;

    @Size(min = 1, max = 30, message = "서비스 입력값 30자 미만.")
    @Schema(description = "서비스 이름", example = "myD")
    private String serviceName;

    @Size(min = 1, max = 30, message = "국가 입력값 30자 미만.")
    @Schema(description = "사용자 국가", example = "Republic of Korea")
    private String country;

    @Size(min = 1, max = 20, message = "핸드폰번호 입력값 20자 미만.")
    @Schema(description = "핸드폰번호", example = "01000009999")
    private String phoneNumber;

    public Account toAccountEntity(long companyId) {
        return Account.builder()
                .firstName(this.firstName.strip())
                .lastName(this.lastName.strip())
                .companyId(companyId)
                .status(AccountStatus.ACTIVATED)
                .build();
    }
}
