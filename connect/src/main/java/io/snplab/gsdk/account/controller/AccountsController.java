package io.snplab.gsdk.account.controller;

import io.snplab.gsdk.account.dto.AccountGetResponseDtoImpl;
import io.snplab.gsdk.account.service.AccountService;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/accounts")
@Tag(name = "Global SDK 모든 계정 API")
public class AccountsController {
    private final AccountService accountService;

    @Operation(summary = "모든 계정 정보 조회 API", description = "전계정 정보 조회")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public RestApiResponse<List<AccountGetResponseDtoImpl>> list() {
        return accountService.list();
    }
}
