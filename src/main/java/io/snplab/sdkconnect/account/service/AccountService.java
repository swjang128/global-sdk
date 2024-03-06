package io.snplab.sdkconnect.account.service;

import io.snplab.sdkconnect.access.conifg.RestApiResponse;
import io.snplab.sdkconnect.account.dto.AccountCreateDto;
import jdk.jfr.Description;

public interface AccountService {
	@Description("Email duplicate check")
	public RestApiResponse emailDuplicateCheck(String email);

	@Description("Sign up")
	public RestApiResponse signUp(AccountCreateDto accountCreateDto);

}
