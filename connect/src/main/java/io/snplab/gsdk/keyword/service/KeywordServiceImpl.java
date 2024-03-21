package io.snplab.gsdk.keyword.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.opencsv.CSVReader;
import io.snplab.gsdk.account.repository.Account;
import io.snplab.gsdk.account.repository.AccountRepository;
import io.snplab.gsdk.account.repository.AccountRole;
import io.snplab.gsdk.account.repository.AccountRoleRepository;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.common.handler.exception.FileOrDirectoryNotExistException;
import io.snplab.gsdk.common.util.Environment;
import io.snplab.gsdk.common.util.RootPath;
import io.snplab.gsdk.keyword.dto.FileConfirmDto;
import io.snplab.gsdk.keyword.dto.KeywordGetRequestDto;
import io.snplab.gsdk.keyword.dto.KeywordGetResponseDto;
import io.snplab.gsdk.keyword.repository.Keyword;
import io.snplab.gsdk.keyword.repository.KeywordHistory;
import io.snplab.gsdk.keyword.repository.KeywordHistoryRepository;
import io.snplab.gsdk.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {
    private static String GSDK = Paths.get(Environment.getRootPath(RootPath.WEB)).toAbsolutePath().toString() + File.separator + "gsdk";

    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final KeywordRepository keywordRepository;
    private final KeywordHistoryRepository keywordHistoryRepository;


    public RestApiResponse<Object> confirmFileUpload(FileConfirmDto fileConfirmDto) throws IOException {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountRepository.findByEmail(user).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾지 못하였습니다."));
        AccountRole accountRole = accountRoleRepository.findByAccountId(account.getId()).orElseThrow(() -> new UsernameNotFoundException("사용자 권한을 찾지 못하였습니다."));

        // history 작성
        KeywordHistory keywordHistory = KeywordHistory.builder()
                .accountId(account.getId())
                .serviceId(accountRole.getServiceId())
                .fileName(fileConfirmDto.getFileName())
                .filePath(GSDK + File.separator + accountRole.getServiceId())
                .fileExtension(FilenameUtils.getExtension(fileConfirmDto.getFileName()).toLowerCase())
                .fileUniqueId(fileConfirmDto.getFileUuid()).build();

        KeywordHistory saveEntity = keywordHistoryRepository.save(keywordHistory);

        // find file
        File directory = new File(saveEntity.getFilePath());
        if (!directory.exists()) {
            throw new FileOrDirectoryNotExistException("폴더가 존재하지 않습니다.");
        }
        File file = Arrays.stream(directory.listFiles()).filter(f -> f.getName().equals(saveEntity.getFileUniqueId() + "." + saveEntity.getFileExtension())).findFirst().orElse(null);

        if (file != null && file.isFile()) {
            // 업로드한 키워드 파일에서 키워드들을 가져와 리스트에 담기
            TreeSet<String> keywordSet = findKeyword(file, saveEntity.getFileExtension());
            // 리스트에 담겨있는 키워드들을 keyword 테이블에 Insert
            saveKeyword(keywordSet, saveEntity.getId(), saveEntity.getServiceId());
        } else {
            throw new FileOrDirectoryNotExistException("파일이 존재하지 않습니다.");
        }
        return RestApiResponse.success();
    }

    private TreeSet<String> findKeyword(File file, String fileExtension) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        DataFormatter dataFormatter = new DataFormatter();
        TreeSet<String> keywordSet = new TreeSet<>();
        if ("csv".equals(fileExtension)) {
            CSVReader csvReader = new CSVReader(new FileReader(file));
            csvReader.readNext();
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                keywordSet.add(line[0]);
            }
        } else {
            Workbook workbook = null;
            if ("xls".equals(fileExtension)) {
                workbook = new HSSFWorkbook(fis);
            } else if ("xlsx".equals(fileExtension)) {
                workbook = new XSSFWorkbook(fis);
            }
            Sheet sheet = workbook.getSheetAt(0);
            int startRow = 1;
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(0);
                    if (cell != null && cell.getCellType() != CellType.BLANK) {
                        // 숫자로만 된 셀인 경우 문자열로 변환해서 가져오기
                        keywordSet.add(dataFormatter.formatCellValue(cell));
                    }
                }
            }
        }
        return keywordSet;
    }

    @Transactional
    private void saveKeyword(TreeSet<String> keywordSet, Long historyId, Long serviceId) {
        // 기존 해당 서비스 ID로 등록했던 키워드 조회
        Map<String, Keyword> existingKeywords = keywordRepository.findByIsActivatedAndServiceId(true, serviceId)
                .stream()
                .collect(Collectors.toMap(Keyword::getKeyword, keyword -> keyword));
        // 존재하지 않았던 키워드 및 기존에 존재했던 키워드들을 save (키워드 히스토리 관리를 위해 미사용했던 키워드들이 다시 업로드 된 경우 새 row로 save)
        List<Keyword> keywordsToSave = keywordSet.stream()
                .filter(keyword -> !existingKeywords.containsKey(keyword))
                .map(keyword -> new Keyword(keyword, historyId, serviceId, true))
                .collect(Collectors.toList());
        keywordRepository.saveAll(keywordsToSave);
        // 기존에 있었는데, 이번에는 업로드한 파일에 없는 키워드인 경우 해당 row를 미사용으로 변경
        existingKeywords.values().stream()
                .filter(keyword -> !keywordSet.contains(keyword.getKeyword()))
                .forEach(keyword -> keyword.setActivated(false));
        keywordRepository.saveAll(existingKeywords.values());
    }

    @Override
    public RestApiResponse<KeywordGetResponseDto> status(KeywordGetRequestDto keywordGetRequestDto) {
        KeywordGetResponseDto.KeywordGetResponseDtoBuilder keywordGetResponseDtoBuilder = null;

        switch (keywordGetRequestDto.getStatus()) {
            //  updatedAt 이후의 업데이트 상태 조회후 active, inactive 분류
            case ALL -> {
                List<Keyword> updatedKeywords = keywordRepository.findByServiceIdAndUpdatedAtGreaterThan(keywordGetRequestDto.getServiceId(), keywordGetRequestDto.getUpdatedAt());
                keywordGetResponseDtoBuilder = KeywordGetResponseDto.builder()
                        .activeKeywords(updatedKeywords.stream().filter(Keyword::isActivated).map(Keyword::getKeyword).collect(Collectors.toList()))
                        .inactiveKeywords(updatedKeywords.stream().filter(keyword -> !keyword.isActivated()).map(Keyword::getKeyword).collect(Collectors.toList()));
            }
            //  해당 서비스의 activated 가 true인 keyword 전부 조회
            case ACTIVATED -> keywordGetResponseDtoBuilder = KeywordGetResponseDto.builder()
                    .activeKeywords(keywordRepository.findByServiceIdAndIsActivated(keywordGetRequestDto.getServiceId(), true).stream()
                            .map(Keyword::getKeyword)
                            .collect(Collectors.toList()));
            default -> throw new NotFoundException("올바르지않은 상태값.");
        }

        return RestApiResponse.success(keywordGetResponseDtoBuilder.build());
    }
}
