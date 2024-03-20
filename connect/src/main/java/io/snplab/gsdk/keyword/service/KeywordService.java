package io.snplab.gsdk.keyword.service;

import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.keyword.dto.FileConfirmDto;
import io.snplab.gsdk.keyword.dto.KeywordGetRequestDto;
import io.snplab.gsdk.keyword.dto.KeywordGetResponseDto;

import java.io.IOException;

public interface KeywordService {
    RestApiResponse<Object> confirmFileUpload(FileConfirmDto fileConfirmDto) throws IOException;
    RestApiResponse<KeywordGetResponseDto> status(KeywordGetRequestDto keywordUpdateRequestDto);
}
