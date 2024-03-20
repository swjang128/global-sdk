package io.snplab.gsdk.keyword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Description("File confirm DTO")
public class FileConfirmDto {

    @NotBlank(message = "파일 Unique ID를 입력해주세요.")
    @Size(min = 1, max = 15, message = "File UUID 입력값 15자 미만.")
    @Schema(description = "File UUID", example = "VESdsecvSDFEfSF", required = true)
    private String fileUuid;

    @NotBlank(message = "파일 이름 입력해주세요.")
    @Size(min = 1, max = 50, message = "File name 입력값 50자 미만.")
    @Schema(description = "File Name", example = "test.csv", required = true)
    private String fileName;
}
