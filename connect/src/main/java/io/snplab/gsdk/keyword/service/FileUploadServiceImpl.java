package io.snplab.gsdk.keyword.service;

import io.snplab.gsdk.account.repository.Account;
import io.snplab.gsdk.account.repository.AccountRepository;
import io.snplab.gsdk.account.repository.AccountRole;
import io.snplab.gsdk.account.repository.AccountRoleRepository;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.common.handler.exception.FileOrDirectoryNotExistException;
import io.snplab.gsdk.common.handler.exception.FileSaveException;
import io.snplab.gsdk.common.handler.exception.FileValidityException;
import io.snplab.gsdk.common.util.Environment;
import io.snplab.gsdk.common.util.FileExtension;
import io.snplab.gsdk.common.util.IDTool;
import io.snplab.gsdk.common.util.RootPath;
import io.snplab.gsdk.keyword.dto.FileDetailInfoResponseDto;
import io.snplab.gsdk.keyword.dto.FileUploadResponseDto;
import io.snplab.gsdk.keyword.repository.KeywordHistory;
import io.snplab.gsdk.keyword.repository.KeywordHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static Pattern REGEX = Pattern.compile("(?i)\\bproduct[\\s_-]*name\\b");
    private static final long MAX_FILE_SIZE_BYTES = 20 * 1024 * 1024;

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final KeywordHistoryRepository keywordHistoryRepository;

    public RestApiResponse<FileDetailInfoResponseDto> getDetailInfo() {

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByEmail(user).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾지 못하였습니다."));

        AccountRole accountRole = accountRoleRepository.findByAccountId(account.getId()).orElseThrow(() -> new UsernameNotFoundException("사용자 권한을 찾지 못하였습니다."));
        String serviceId = accountRole.getServiceId();

        KeywordHistory keywordHistory = keywordHistoryRepository.findTopByServiceIdOrderByCreatedAtDesc(serviceId).orElseThrow(() -> new FileSaveException("파일 업로드 기록이 존재하지 않습니다."));
        FileDetailInfoResponseDto responseDto = FileDetailInfoResponseDto.builder()
                .fileName(keywordHistory.getFileName())
                .fileUuid(keywordHistory.getFileUniqueId())
                .lastUpdate(keywordHistory.getCreatedAt()).build();

        return RestApiResponse.success(responseDto);
    }

    @Transactional
    public RestApiResponse<Object> vaildateAndUploadExcel(MultipartHttpServletRequest request) throws IOException, InvalidFormatException {

        String GSDK = Paths.get(Environment.getRootPath(RootPath.WEB)).toAbsolutePath().toString() + File.separator + "gsdk";
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByEmail(user).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾지 못하였습니다."));

        AccountRole accountRole = accountRoleRepository.findByAccountId(account.getId()).orElseThrow(() -> new UsernameNotFoundException("사용자 권한을 찾지 못하였습니다."));

        List<FileUploadResponseDto> fileUploadList = new ArrayList<>();
        String filePath = GSDK + File.separator + accountRole.getServiceId();
        File dir = new File(filePath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                log.error("이미 생성된 디렉토리입니다. : {}", filePath);
            }
        }

        Iterator<String> itr = request.getFileNames();
        while (itr.hasNext()) {
            String fileUniqueId = IDTool.randomString(15);
            MultipartFile mpf = request.getFile(itr.next());
            String orginalFileName = mpf.getOriginalFilename();

            if (mpf.getSize() > MAX_FILE_SIZE_BYTES) {
                throw new FileValidityException("파일의 크기가 20MB를 초과합니다.");
            }

            String fileExtension = FilenameUtils.getExtension(orginalFileName).toLowerCase();
            if (!validateFileExtension(fileExtension)) {
                throw new FileValidityException("지원하지 않는 확장자입니다. 확장자 확인 부탁드립니다.");
            }

            if (!validateFile(mpf.getInputStream(), fileExtension, REGEX)) {
                throw new FileValidityException("파일 내부 규격이 유효하지 않습니다.");
            }
            String fileLocalPath = filePath + File.separator + fileUniqueId + "." + fileExtension;
            try {
                mpf.transferTo(new File(fileLocalPath));
                fileUploadList.add(new FileUploadResponseDto(orginalFileName, fileUniqueId));
            } catch (IOException e) {
                throw new FileSaveException("파일 업로드에 실패하였습니다.");
            }
        }
        return RestApiResponse.success(fileUploadList);
    }

    public ResponseEntity downloadFile() {

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByEmail(user).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾지 못하였습니다."));

        AccountRole accountRole = accountRoleRepository.findByAccountId(account.getId()).orElseThrow(() -> new UsernameNotFoundException("사용자 권한을 찾지 못하였습니다."));
        String serviceId = accountRole.getServiceId();

        KeywordHistory keywordHistory = keywordHistoryRepository.findTopByServiceIdOrderByCreatedAtDesc(serviceId).orElseThrow(() -> new FileSaveException("파일 업로드 기록이 존재하지 않습니다."));

        File directory = new File(keywordHistory.getFilePath());
        if (!directory.exists()) {
            throw new FileOrDirectoryNotExistException("폴더가 존재하지 않습니다.");
        }
        File file = Arrays.stream(directory.listFiles()).filter(f -> f.getName().equals(keywordHistory.getFileUniqueId() + "." + keywordHistory.getFileExtension())).findFirst().orElseThrow(() -> new FileSaveException("파일 존재하지 않습니다."));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", keywordHistory.getFileName());

        InputStreamResource inputStreamResource = null;
        try {
            inputStreamResource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new FileOrDirectoryNotExistException("파일이 존재하지 않습니다.");
        }
        return ResponseEntity.ok().headers(headers).body(inputStreamResource);
    }

    private boolean validateFileExtension(String fileExtension) {

        return FileExtension.isValidExcelExtension(fileExtension);
    }

    private boolean validateFile(InputStream file, String extension, Pattern pattern) throws IOException, InvalidFormatException {

        Matcher matcher = null;
        if ("csv".equals(extension)) {

            BufferedReader br = new BufferedReader(new InputStreamReader(file));
            String validateData = br.readLine();
            if (validateData != null) {
                matcher = pattern.matcher(validateData);
                return matcher.find();
            } else {
                return false;
            }
        } else {
            Workbook workbook = null;
            if ("xls".equals(extension)) {
                workbook = new HSSFWorkbook(file);
            } else if ("xlsx".equals(extension)) {
                workbook = new XSSFWorkbook(file);
            }

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            if (row != null) {
                Cell dataCell = row.getCell(0);

                if (dataCell != null && dataCell.getCellType() == CellType.STRING) {

                    String cellValue = dataCell.getStringCellValue();
                    matcher = pattern.matcher(cellValue);
                    return matcher.find();
                }
            }
            return false;
        }
    }
}
