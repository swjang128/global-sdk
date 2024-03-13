package io.snplab.gsdk.account.controller;

import io.snplab.gsdk.account.dto.AccountChangeDto;
import io.snplab.gsdk.account.dto.AccountCheckTokenResponseDto;
import io.snplab.gsdk.account.dto.AccountCheckResponseDto;
import io.snplab.gsdk.account.dto.AccountCreateDto;
import io.snplab.gsdk.account.service.AccountService;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/account")
@Tag(name = "Global SDK 계정 관리 API", description = "Global SDK 계정 API")
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "회원가입 API", description = "요청 정보를 기반으로 회원가입 처리")
    @ApiResponse(responseCode = "200", description = "Success")
    @PostMapping("sign-up")
    public RestApiResponse<Object> signUp(@RequestBody @Valid AccountCreateDto accountCreateDto) {
        return accountService.signUp(accountCreateDto);
    }

    @Operation(summary = "계정 확인 API", description = "이메일로 계정 존재하는지 확인")
    @ApiResponse(responseCode = "200", description = "존재: 이메일값/존재x: null")
    @GetMapping("check")
    public RestApiResponse<AccountCheckResponseDto> check(@RequestParam String email) {
        return accountService.check(email);
    }

    @Operation(summary = "이메일 발송 API", description = "password 재설정 링크가 포함된 이메일 발송(6시간 유효 토큰)")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("email")
    public RestApiResponse<Object> email(HttpServletRequest httpServletRequest, @RequestParam String email) throws MessagingException, JSONException, IOException {
        return accountService.email(httpServletRequest, email);
    }

    @Operation(summary = "password 재설정 API", description = "토큰 복호화 로직 포함")
    @ApiResponse(responseCode = "200", description = "Success")
    @PostMapping("change")
    public RestApiResponse<Object> change(@RequestBody @Valid AccountChangeDto accountChangeDto) throws JSONException {
        return accountService.change(accountChangeDto);
    }

    @Operation(summary = "이메일 확인 API", description = "token 확인후 해당 이메일 관련 정보 반환")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("check/token")
    public RestApiResponse<AccountCheckTokenResponseDto> checkToken(@RequestParam String token) {
        return accountService.decryptToken(token);
    }
}
