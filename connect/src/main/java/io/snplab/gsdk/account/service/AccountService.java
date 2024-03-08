package io.snplab.gsdk.account.service;

import io.snplab.gsdk.account.dto.AccountChangeDto;
import io.snplab.gsdk.account.dto.AccountCreateDto;
import io.snplab.gsdk.account.dto.AccountCheckDto;
import io.snplab.gsdk.account.dto.AccountEmailDto;
import io.snplab.gsdk.common.RestApiResponse;
import jdk.jfr.Description;
import org.json.JSONException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AccountService {
	@Description("Email duplicate check")
	public RestApiResponse emailDuplicateCheck(String email);

	@Description("Sign up")
	public RestApiResponse signUp(AccountCreateDto accountCreateDto);

	RestApiResponse check(AccountCheckDto accountCheckDto);

	RestApiResponse email(HttpServletRequest httpServletRequest, AccountEmailDto accountEmailDto) throws JSONException, MessagingException, IOException;

	RestApiResponse change(AccountChangeDto accountChangeDto) throws JSONException;
}
