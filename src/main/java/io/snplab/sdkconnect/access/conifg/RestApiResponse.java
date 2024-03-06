package io.snplab.sdkconnect.access.conifg;

import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestApiResponse {
	private int code;
	private Object data;

	@Description("Setting up Rest API response")
	public static RestApiResponse setResponse(int code, Object data) {
		return new RestApiResponse(code, data);
	}
}
