package io.snplab.gsdk.account.service;

import io.snplab.gsdk.common.RestApiResponse;
import io.snplab.gsdk.access.repository.Account;
import io.snplab.gsdk.access.repository.AccountRepository;
import io.snplab.gsdk.account.dto.AccountCreateDto;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static io.snplab.gsdk.common.ResponseManager.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountServiceImpl implements AccountService{
	private final AccountRepository accountRepository;

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
		accountRepository.save(account);
		return RestApiResponse.setResponse(CREATED.code, CREATED.message);
	}


}
