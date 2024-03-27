package io.snplab.gsdk.keyword.controller;

import io.snplab.gsdk.account.util.LogIn;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.keyword.dto.FileConfirmDto;
import io.snplab.gsdk.keyword.dto.KeywordGetResponseDto;
import io.snplab.gsdk.keyword.service.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/keyword")
@Tag(name = "Global SDK 키워드 관리 API")
public class KeywordController {
    private final KeywordService keywordService;

    @Operation(summary = "File confirm API", description = "업로드 파일을 확정")
    @ApiResponse(responseCode = "200", description = "업로드한 파일에서 데이터 추출 후 DB에 저장 완료")
    @PostMapping("/confirm")
    @LogIn
    public RestApiResponse<Object> confirmFile(@RequestBody @Valid FileConfirmDto fileConfirmDto) throws IOException {

        return keywordService.confirmFileUpload(fileConfirmDto);
    }

    @Operation(summary = "Keyword 조회 API", description = "updatedAt 기준으로 서버에 활성화된 키워드, 비활성화된 키워드 조회")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/{serviceId}")
    public RestApiResponse<KeywordGetResponseDto> keyword(
            @Parameter(description = "서비스 uuid", example = "00711d8f-fbd5-4fa7-af3a-764fb783cd24") @PathVariable String serviceId,
            @Parameter(description = "마지막 업데이트한 일자 (해당값 없이 요청할 경우 해당 서비스의 활성화된 키워드 모두 가져온다.)", example = "2024-03-19T17:33:00") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from
    ) {
        return keywordService.getKeyword(serviceId, from);
    }
}
