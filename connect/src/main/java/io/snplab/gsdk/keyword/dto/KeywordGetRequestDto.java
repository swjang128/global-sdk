package io.snplab.gsdk.keyword.dto;

import io.snplab.gsdk.keyword.domain.KeywordRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KeywordGetRequestDto {
    @Schema(description = "서비스 id", example = "12", required = true)
    private Long serviceId;
    @Schema(description = "마지막 업데이트한 일자", example = "2024-03-19T17:33:00")
    private LocalDateTime updatedAt;
    @Schema(description = "조회 상태값", example = "ACTIVATED", required = true)
    private KeywordRequestStatus status;
}
