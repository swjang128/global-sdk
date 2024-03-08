package io.snplab.gsdk.account.service;

import io.snplab.gsdk.common.RestApiResponse;
import io.snplab.gsdk.access.repository.Account;
import io.snplab.gsdk.access.repository.AccountRepository;
import io.snplab.gsdk.account.dto.AccountChangeDto;
import io.snplab.gsdk.account.dto.AccountCheckDto;
import io.snplab.gsdk.account.dto.AccountCreateDto;
import io.snplab.gsdk.account.dto.AccountEmailDto;
import io.snplab.gsdk.common.service.MailService;
import io.snplab.gsdk.common.util.AES256;
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
import java.util.Optional;

import static io.snplab.gsdk.common.ResponseManager.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Description("Email duplicate check service")
    @Override
    public RestApiResponse emailDuplicateCheck(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            return RestApiResponse.setResponse(DUPLICATE_EMAIL.code, email);
        }
        return RestApiResponse.setResponse(NON_DUPLICATE_EMAIL.code, email);
    }

    @Description("Sign up service")
    @Transactional
    @Override
    public RestApiResponse signUp(AccountCreateDto accountCreateDto) {
        Account account = accountCreateDto.toEntity();
        account.setPassword(passwordEncoder.encode(accountCreateDto.getPassword()));
        accountRepository.save(account);
        return RestApiResponse.setResponse(CREATED.code, CREATED.message);
    }

    @Override
    public RestApiResponse check(AccountCheckDto accountCheckDto) {
        return accountRepository.findByEmail(accountCheckDto.getEmail().trim())
                .map(account -> {
                    String fullName = account.getFirstName() + " " + account.getLastName();
                    return fullName.equals(accountCheckDto.getName()) ? RestApiResponse.setResponse(SUCCESS) : null;
                })
                .orElseThrow(() -> new UsernameNotFoundException("not found account."));
    }

    @Override
    public RestApiResponse email(HttpServletRequest httpServletRequest, AccountEmailDto accountEmailDto) throws JSONException, MessagingException, IOException {
        String email = accountEmailDto.getEmail();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            throw new UsernameNotFoundException("not registered account.");
        }

        // 비밀번호 변경 링크 생성 (만료시간: 현재시간 +6시간, 암호화 방식: AES-256)
        String ciphertext = new AES256().encrypt(MailUtil.getData(email).toString());

        String passwordChangeLink = MailUtil.getPasswordChangeLink(httpServletRequest, ciphertext);
        String content = MailUtil.generateHtmlContent(email, passwordChangeLink, Constants.PASSWORD_RESET_FILE);

        mailService.send(email, Constants.PASSWORD_RESET_EMAIL_TITLE, content);

        return RestApiResponse.setResponse(SUCCESS);
    }

    @Override
    @Transactional
    public RestApiResponse change(AccountChangeDto accountChangeDto) throws JSONException {
        String newPassword = accountChangeDto.getPassword();
        if (!newPassword.equals(accountChangeDto.getPasswordCheck())) {
            throw new RuntimeException("password does not match.");
        }

        String decrypted = new AES256().decrypt(accountChangeDto.getBase64EncryptedData());
        JSONObject jsonObject = new JSONObject(decrypted);

        String email = jsonObject.getString("email");
        String expiration = jsonObject.getString("expiration");

        OffsetDateTime expirationDateTime = OffsetDateTime.parse(expiration);
        OffsetDateTime now = OffsetDateTime.now().withNano(0);

        if (now.isAfter(expirationDateTime)) {
            throw new RuntimeException("Time has expired.");
        }

        return accountRepository.findByEmail(email)
                .map(account -> {
                    account.setPassword(passwordEncoder.encode(newPassword));
                    return RestApiResponse.setResponse(CREATED);
                })
                .orElseThrow(() -> new UsernameNotFoundException("Account not found."));
    }
}
