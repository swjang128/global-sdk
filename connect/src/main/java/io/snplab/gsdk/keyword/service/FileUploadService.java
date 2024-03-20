package io.snplab.gsdk.keyword.service;

import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.keyword.dto.FileDetailInfoResponseDto;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

public interface FileUploadService {

    RestApiResponse<FileDetailInfoResponseDto> getDetailInfo();

    RestApiResponse<Object> vaildateAndUploadExcel(MultipartHttpServletRequest request) throws IOException, InvalidFormatException;

    ResponseEntity downloadFile();
}
