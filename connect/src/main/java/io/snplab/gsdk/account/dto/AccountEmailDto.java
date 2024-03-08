package io.snplab.gsdk.account.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class AccountEmailDto {
    private String email;
}
