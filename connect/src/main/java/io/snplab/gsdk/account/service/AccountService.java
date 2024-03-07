package io.snplab.gsdk.account.service;

import io.snplab.gsdk.account.dto.AccountCreateDto;
import io.snplab.gsdk.common.RestApiResponse;
import jdk.jfr.Description;

public interface AccountService {
	@Description("Email duplicate check")
	public RestApiResponse emailDuplicateCheck(String email);

	@Description("Sign up")
	public RestApiResponse signUp(AccountCreateDto accountCreateDto);

}
