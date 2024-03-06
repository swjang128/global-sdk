package io.snplab.sdkconnect.account.dto;

import io.snplab.sdkconnect.access.repository.Account;
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
@Description("Sign up DTO")
public class AccountCreateDto {
    @NotBlank(message = "Please enter email.")
    @Size(min=1, max=50, message="Please enter your email address with 50 characters or less.")
    @Email(message = "Please enter correct email format.")
    private String email;

    @NotBlank(message="Please enter password.")
    @Pattern(regexp="(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{12,}",
            message="The password must be 12 characters long and contain at least one uppercase and lowercase English letter, number, and special symbol.")
    private String password;

    @NotBlank(message = "Please enter firstName.")
    @Size(min=1, max=20, message="Please enter your firstName with 20 characters or less.")
    private String firstName;

    @NotBlank(message = "Please enter lastName.")
    @Size(min=1, max=20, message="Please enter your lastName with 20 characters or less.")
    private String lastName;

    @Size(min=1, max=30, message="Please enter your companyName with 30 characters or less.")
    private String companyName;

    @Size(min=1, max=30, message="Please enter your serviceName with 30 characters or less.")
    private String serviceName;

    @Size(min=1, max=30, message="Please enter your country with 30 characters or less.")
    private String country;

    public Account toEntity() {
        return Account.builder()
                .email(this.email)
                .password(this.password)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .companyName(this.companyName)
                .serviceName(this.serviceName)
                .country(this.country)
                .build();
    }
}
