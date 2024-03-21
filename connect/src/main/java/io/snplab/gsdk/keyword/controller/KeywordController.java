package io.snplab.gsdk.keyword.controller;

import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.keyword.dto.FileConfirmDto;
import io.snplab.gsdk.keyword.dto.KeywordGetRequestDto;
import io.snplab.gsdk.keyword.dto.KeywordGetResponseDto;
import io.snplab.gsdk.keyword.service.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/keyword")
@Tag(name = "Global SDK 키워드 관리 API")
public class KeywordController {
    private final KeywordService keywordService;

    @Operation(summary = "File confirm API", description = "업로드 파일을 확정")
    @ApiResponse(responseCode = "200", description = "업로드한 파일에서 데이터 추출 후 DB에 저장 완료")
    @PostMapping("/confirm")
    public RestApiResponse<Object> confirmFile(@RequestBody @Valid FileConfirmDto fileConfirmDto) throws IOException {

        return keywordService.confirmFileUpload(fileConfirmDto);
    }

    @Operation(summary = "Keyword 조회 API", description = "updatedAt 기준으로 서버에 활성화된 키워드, 비활성화된 키워드 조회")
    @ApiResponse(responseCode = "200", description = "Success")
    @PostMapping("/status")
    public RestApiResponse<KeywordGetResponseDto> status(@RequestBody @Valid KeywordGetRequestDto keywordUpdateRequestDto) {
        return keywordService.status(keywordUpdateRequestDto);
    }
}
