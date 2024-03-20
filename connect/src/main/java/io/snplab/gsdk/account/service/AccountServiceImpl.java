package io.snplab.gsdk.account.service;

import com.sun.jdi.request.DuplicateRequestException;
import io.snplab.gsdk.account.dto.*;
import io.snplab.gsdk.account.repository.*;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.common.service.AES256;
import io.snplab.gsdk.common.service.MailService;
import io.snplab.gsdk.common.util.Constants;
import io.snplab.gsdk.common.util.MailUtil;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final ServiceInfoRepository serviceInfoRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final AccountRepository accountRepository;
    private final CompanyRepository companyRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final AES256 aes256;

    @Description("Sign up service")
    @Transactional
    @Override
    public RestApiResponse<Object> signUp(AccountCreateDto accountCreateDto) {
        if (getAccountByEncryptedEmail(accountCreateDto.getEmail()).isPresent()) {
            throw new DuplicateRequestException("이메일 중복.");
        }

        // set company
        Company company = companyRepository.findByName(accountCreateDto.getCompanyName())
                .orElseGet(() -> Company.of(accountCreateDto));
        companyRepository.save(company);

        // set account
        Account account = accountCreateDto.toAccountEntity(company.getId(),
                aes256.encrypt(accountCreateDto.getEmail()),
                passwordEncoder.encode(accountCreateDto.getPassword()),
                aes256.encrypt(accountCreateDto.getPhoneNumber()));
        accountRepository.save(account);

        // set service_info
        ServiceInfo serviceInfo = serviceInfoRepository.findByCompanyIdAndName(company.getId(), accountCreateDto.getServiceName())
                .orElseGet(() -> ServiceInfo.of(accountCreateDto, company.getId()));
        serviceInfoRepository.save(serviceInfo);

        // set account_role
        accountRoleRepository.save(AccountRole.of(account.getId(), serviceInfo.getId()));

        return RestApiResponse.success();
    }

    @Override
    public RestApiResponse<AccountCheckResponseDto> getEmail(String email) {
        return getAccountByEncryptedEmail(email)
                .map(account -> RestApiResponse.success(new AccountCheckResponseDto(aes256.decrypt(account.getEmail()))))
                .orElse(RestApiResponse.success(null));
    }

    @Override
    public RestApiResponse<Object> sendEmail(HttpServletRequest httpServletRequest, String email) throws JSONException, MessagingException, IOException {
        Optional<Account> optionalAccount = getAccountByEncryptedEmail(email);
        if (optionalAccount.isEmpty()) {
            throw new UsernameNotFoundException("등록되지않은 이메일.");
        }

        // 비밀번호 변경 링크 생성 (만료시간: 현재시간 +6시간, 암호화 방식: AES-256)
        String ciphertext = aes256.encrypt(MailUtil.getData(aes256.encrypt(email)).toString());

        String passwordChangeLink = MailUtil.getPasswordChangeLink(httpServletRequest, ciphertext);
        String content = MailUtil.generateHtmlContent(email, passwordChangeLink, Constants.PASSWORD_RESET_FILE);

        mailService.send(email, Constants.PASSWORD_RESET_EMAIL_TITLE, content);

        return RestApiResponse.success();
    }

    @Override
    @Transactional
    public RestApiResponse<Object> change(AccountChangeDto accountChangeDto) throws JSONException {
        String newPassword = accountChangeDto.getPassword();
        if (!newPassword.equals(accountChangeDto.getPasswordCheck())) {
            throw new RuntimeException("password 일치하지 않음.");
        }

        return accountRepository.findByEmail(getEmailFrom(accountChangeDto.getBase64EncryptedData()))
                .map(account -> {
                    account.setPassword(passwordEncoder.encode(newPassword));
                    return RestApiResponse.success();
                })
                .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 계정."));
    }

    @Override
    public RestApiResponse<AccountCheckTokenResponseDto> decryptToken(String token) {
        return accountRepository.findByEmail(getEmailFrom(token))
                .map(account -> RestApiResponse.success(new AccountCheckTokenResponseDto(aes256.decrypt(account.getEmail()), account.getFirstName(), account.getLastName())))
                .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 계정."));
    }

    @Override
    public RestApiResponse<AccountGetResponseDtoImpl> get(Long id) {
        return accountRepository.findAccountInfoById(id)
                .map(accountGetResponseDto -> RestApiResponse.success(generateAccountGetResponseDtoImpl(accountGetResponseDto)))
                .orElseThrow(() -> new UsernameNotFoundException("등록되지 않은 계정."));
    }

    @Override
    public RestApiResponse<List<AccountGetResponseDtoImpl>> list() {
        return RestApiResponse.success(accountRepository.findAccountInfoAll()
                .stream()
                .map(this::generateAccountGetResponseDtoImpl)
                .collect(Collectors.toList()));
    }

    private AccountGetResponseDtoImpl generateAccountGetResponseDtoImpl(AccountGetResponseDto accountGetResponseDto) {
        return AccountGetResponseDtoImpl.builder()
                .email(aes256.decrypt(accountGetResponseDto.getEmail()))
                .firstName(accountGetResponseDto.getFirstName())
                .lastName(accountGetResponseDto.getLastName())
                .phoneNumber(aes256.decrypt(accountGetResponseDto.getPhoneNumber()))
                .companyName(accountGetResponseDto.getCompanyName())
                .serviceName(accountGetResponseDto.getServiceName())
                .build();
    }

    private Optional<Account> getAccountByEncryptedEmail(String email) {
        return accountRepository.findByEmail(aes256.encrypt(email));
    }

    private String getEmailFrom(String base64EncryptedData) {
        String decrypted = aes256.decrypt(base64EncryptedData);
        JSONObject decryptedObject = new JSONObject(decrypted);

        String email = decryptedObject.getString("email");
        String expiration = decryptedObject.getString("expiration");

        OffsetDateTime expirationDateTime = OffsetDateTime.parse(expiration);
        OffsetDateTime now = OffsetDateTime.now().withNano(0);

        if (now.isAfter(expirationDateTime)) {
            throw new RuntimeException("토큰 시간 만료.");
        }

        return email;
    }

}
