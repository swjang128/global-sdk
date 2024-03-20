package io.snplab.gsdk.keyword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("File Detail Info DTO")
public class FileDetailInfoResponseDto {

    @Schema(description = "파일 이름", example = "fileName.xlsx")
    private String fileName;

    @Schema(description = "파일 Unique ID", example = "testFileUUID")
    private String fileUuid;

    @Schema(description = "파일 업로드 시간", example = "2024-03-18")
    private LocalDateTime lastUpdate;
}
