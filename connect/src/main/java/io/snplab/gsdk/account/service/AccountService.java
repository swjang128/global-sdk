package io.snplab.gsdk.account.service;

import io.snplab.gsdk.account.dto.AccountChangeDto;
import io.snplab.gsdk.account.dto.AccountCheckTokenResponseDto;
import io.snplab.gsdk.account.dto.AccountCheckResponseDto;
import io.snplab.gsdk.account.dto.AccountCreateDto;
import io.snplab.gsdk.common.domain.RestApiResponse;
import jdk.jfr.Description;
import org.json.JSONException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AccountService {
	@Description("Sign up")
	RestApiResponse<Object> signUp(AccountCreateDto accountCreateDto);

	RestApiResponse<AccountCheckResponseDto> check(String email);

	RestApiResponse<Object> email(HttpServletRequest httpServletRequest, String email) throws JSONException, MessagingException, IOException;

	RestApiResponse<Object> change(AccountChangeDto accountChangeDto) throws JSONException;

    RestApiResponse<AccountCheckTokenResponseDto> decryptToken(String token);
}
