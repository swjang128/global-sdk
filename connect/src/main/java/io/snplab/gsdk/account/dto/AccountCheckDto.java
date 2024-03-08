package io.snplab.gsdk.account.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountCheckDto {
    @NotBlank(message = "Input required.")
    private String name;

    @NotBlank(message = "Input required.")
    @Email(message = "Please enter correct email format.")
    private String email;
}
