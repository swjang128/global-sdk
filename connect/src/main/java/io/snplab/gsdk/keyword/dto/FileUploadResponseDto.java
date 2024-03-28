package io.snplab.gsdk.keyword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUploadResponseDto {

    @Schema(description = "파일 이름", example = "fileName.xlsx")
    private String fileName;

    @Schema(description = "파일 Unique ID", example = "testFileUUID")
    private String fileUuid;
}
