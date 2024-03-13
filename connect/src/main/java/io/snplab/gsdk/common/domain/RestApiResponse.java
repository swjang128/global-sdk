package io.snplab.gsdk.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jdk.jfr.Description;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestApiResponse<T> {
	@Schema(example = "200")
	private int code;
	@Schema(example = "Success")
	private String message;
	private T data;

	@Description("Setting up Rest API response")
	public static <T> RestApiResponse<T> success() {
		RestApiResponse<T> response = new RestApiResponse<>();
		response.setCode(HttpStatus.OK.value());
		response.setMessage(HttpStatus.OK.getReasonPhrase());
		return response;
	}

	public static <T> RestApiResponse<T> success(T data) {
		RestApiResponse<T> response = new RestApiResponse<>();
		response.setCode(HttpStatus.OK.value());
		response.setMessage(HttpStatus.OK.getReasonPhrase());
		response.setData(data);
		return response;
	}

	public static <T> RestApiResponse<T> error(HttpStatus status, String message) {
		RestApiResponse<T> response = new RestApiResponse<>();
		response.setCode(status.value());
		response.setMessage(message);
		return response;
	}
}
