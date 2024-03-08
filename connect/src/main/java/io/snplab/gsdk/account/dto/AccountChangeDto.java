package io.snplab.gsdk.account.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class AccountChangeDto {
    @NotBlank(message = "Input required.")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{12,}",
            message = "The password must be 8 characters long and contain at least one uppercase and lowercase English letter, number, and special symbol.")
    private String password;
    @NotBlank(message = "Input required.")
    private String passwordCheck;
    @NotBlank(message = "Input required.")
    private String base64EncryptedData;
}
