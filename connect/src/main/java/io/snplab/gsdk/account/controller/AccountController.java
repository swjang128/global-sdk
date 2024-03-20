package io.snplab.gsdk.account.controller;

import io.snplab.gsdk.account.dto.*;
import io.snplab.gsdk.account.service.AccountService;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
        return accountService.signUp(accountCreateDto.strip(accountCreateDto));
    }

    @Operation(summary = "계정 확인 API", description = "이메일로 계정 존재하는지 확인")
    @ApiResponse(responseCode = "200", description = "존재: 이메일값/존재x: null")
    @GetMapping("email")
    public RestApiResponse<AccountCheckResponseDto> email(@RequestParam String email) {
        return accountService.getEmail(email.strip());
    }

    @Operation(summary = "이메일 발송 API", description = "password 재설정 링크가 포함된 이메일 발송(6시간 유효 토큰)")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("sendEmail")
    public RestApiResponse<Object> sendEmail(HttpServletRequest httpServletRequest, @RequestParam String email) throws MessagingException, JSONException, IOException {
        return accountService.sendEmail(httpServletRequest, email.strip());
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

    @Operation(summary = "단일 계정 정보 조회 API", description = "id로 계정 정보 확인")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("{id}")
    public RestApiResponse<AccountGetResponseDtoImpl> get(@PathVariable Long id) {
        return accountService.get(id);
    }
}
