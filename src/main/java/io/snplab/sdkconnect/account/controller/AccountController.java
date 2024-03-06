package io.snplab.sdkconnect.account.controller;

import io.snplab.sdkconnect.access.conifg.RestApiResponse;
import io.snplab.sdkconnect.account.dto.AccountCreateDto;
import io.snplab.sdkconnect.account.service.AccountService;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("account")
public class AccountController {
	private final AccountService accountService;

	@Description("Email duplicate check API controller")
	@GetMapping("duplicate-check")
	public RestApiResponse duplicateCheck(@Param("email") String email) {
		return accountService.emailDuplicateCheck(email);
	}

	@Description("Sign up API controller")
	@PostMapping("sign-up")
	public RestApiResponse signUp(@RequestBody @Valid AccountCreateDto accountCreateDto) {
		return accountService.signUp(accountCreateDto);
	}
}
