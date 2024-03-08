package io.snplab.gsdk.account.controller;

import io.snplab.gsdk.account.dto.AccountChangeDto;
import io.snplab.gsdk.account.dto.AccountCheckDto;
import io.snplab.gsdk.account.dto.AccountCreateDto;
import io.snplab.gsdk.account.dto.AccountEmailDto;
import io.snplab.gsdk.account.service.AccountService;
import io.snplab.gsdk.common.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/account")
@Tag(name = "Global SDK Account API", description = "Global SDK Account API Specification")
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "Email duplicate check",
			description = "Check duplicate email(account).",
			responses = {
				@ApiResponse(responseCode = "1000", description = "Duplicate email", content = @Content(examples = @ExampleObject(value = ""))),
				@ApiResponse(responseCode = "1004", description = "Non duplicate email", content = @Content(examples = @ExampleObject(value = "")))
			},
			security = @SecurityRequirement(name = "X-Auth-Token"))

	@GetMapping("duplicate-check")
	public RestApiResponse duplicateCheck(@Parameter(name = "email", description = "email from parameter", example = "test@snplab.io")
										@Param("email") String email) {
		return accountService.emailDuplicateCheck(email);
	}

	@Operation(summary = "Sign up account",
			description = "Sign up account",
			responses = {
				@ApiResponse(responseCode = "201", description = "Created", content = @Content(examples = @ExampleObject(value = "")))
			},
			security = @SecurityRequirement(name = "X-Auth-Token"))
	@Description("Sign up API controller")
	@PostMapping("sign-up")
	public RestApiResponse signUp(@RequestBody @Valid AccountCreateDto accountCreateDto) {
		return accountService.signUp(accountCreateDto);
	}

    @GetMapping("check")
    public RestApiResponse check(@RequestBody @Valid AccountCheckDto accountCheckDto) {
        return accountService.check(accountCheckDto);
    }

    @GetMapping("email")
    public RestApiResponse email(HttpServletRequest httpServletRequest, @RequestBody @Valid AccountEmailDto accountEmailDto) throws MessagingException, JSONException, IOException {
        return accountService.email(httpServletRequest, accountEmailDto);
    }

    @PostMapping("change")
    public RestApiResponse change(@RequestBody @Valid AccountChangeDto accountChangeDto) throws JSONException {
        return accountService.change(accountChangeDto);
    }
}
