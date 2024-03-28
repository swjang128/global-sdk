package io.snplab.gsdk.keyword.service;

import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.keyword.dto.FileConfirmDto;
import io.snplab.gsdk.keyword.dto.KeywordGetResponseDto;

import java.io.IOException;
import java.time.LocalDateTime;

public interface KeywordService {
    RestApiResponse<Object> confirmFileUpload(FileConfirmDto fileConfirmDto) throws IOException;

    RestApiResponse<KeywordGetResponseDto> getKeyword(String serviceId, LocalDateTime updatedAt);
}
