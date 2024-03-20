package io.snplab.gsdk.keyword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordGetResponseDto {
    @Schema(description = "활성화 시킬 키워드", example = "['word to be added']")
    private List<String> activeKeywords;
    @Schema(description = "비활성화 시킬 키워드", example = "['word to be deleted']")
    private List<String> inactiveKeywords;
}
