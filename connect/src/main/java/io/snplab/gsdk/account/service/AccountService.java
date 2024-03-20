package io.snplab.gsdk.account.service;

import io.snplab.gsdk.account.dto.*;
import io.snplab.gsdk.common.domain.RestApiResponse;
import jdk.jfr.Description;
import org.json.JSONException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface AccountService {
	@Description("Sign up")
	RestApiResponse<Object> signUp(AccountCreateDto accountCreateDto);

	RestApiResponse<AccountCheckResponseDto> getEmail(String email);

	RestApiResponse<Object> sendEmail(HttpServletRequest httpServletRequest, String email) throws JSONException, MessagingException, IOException;

	RestApiResponse<Object> change(AccountChangeDto accountChangeDto) throws JSONException;

    RestApiResponse<AccountCheckTokenResponseDto> decryptToken(String token);

	RestApiResponse<AccountGetResponseDtoImpl> get(Long id);

	RestApiResponse<List<AccountGetResponseDtoImpl>> list();
}
