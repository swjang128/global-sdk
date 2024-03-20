package io.snplab.gsdk.upload.controller;

import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.upload.dto.FileConfirmDto;
import io.snplab.gsdk.upload.dto.FileDetailInfoResponseDto;
import io.snplab.gsdk.upload.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/file")
@Tag(name = "Global SDK Upload 관리 API", description = "Global SDK Upload API")
public class UploadController {

    private final FileUploadService fileUploadService;

    @Operation(summary = "File Detail API", description = "Keyword 상세 정보")
    @ApiResponse(responseCode = "200", description = "Keyword 업로드 정보 및 파일 정보 전달")
    @PostMapping("detail")
    public RestApiResponse<FileDetailInfoResponseDto> detailFile(){

        return fileUploadService.getDetailInfo();
    }


    @Operation(summary = "File Upload API", description = "파일의 유효성 검사 및 저장")
    @ApiResponse(responseCode = "200", description = "파일 유효성 통과 및 업로드 성공")
    @PostMapping("upload")
    public RestApiResponse<Object> uploadFile(MultipartHttpServletRequest request) throws IOException, InvalidFormatException {

        return fileUploadService.vaildateAndUploadExcel(request);
    }

    @Operation(summary = "File Download API", description = "파일 다운로드")
    @ApiResponse(responseCode = "200", description = "파일 다운로드 성공")
    @PostMapping("download")
    public ResponseEntity downloadFile() {

        return fileUploadService.downloadFile();
    }

    @Operation(summary = "File confirm API", description = "업로드 파일을 확정")
    @ApiResponse(responseCode = "200", description = "업로드한 파일에서 데이터 추출 후 DB에 저장 완료")
    @PostMapping("confirm")
    public RestApiResponse<Object> confirmFile(@RequestBody @Valid FileConfirmDto fileConfirmDto) throws IOException {

        return fileUploadService.confirmFileUpload(fileConfirmDto);
    }
}
