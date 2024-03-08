package io.snplab.gsdk.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import jdk.jfr.Description;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Description("Exception Handlers to customize message and result")
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Description("Bad request exception abort")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error("handle MethodArgumentNotValid: {}", e.getMessage());
        RestApiResponse restApiResponse = RestApiResponse.setResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(restApiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Description("Http message not readable exception abort")
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error("handle HttpMessageNotReadable: {}", e.getMessage());
        RestApiResponse restApiResponse = RestApiResponse.setResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(restApiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Description("Method not allowed exception abort")
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        logger.error("handle HttpRequestMethodNotSupported: {}", e.getMessage());
        RestApiResponse restApiResponse = RestApiResponse.setResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
        return new ResponseEntity<>(restApiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Description("Exception abort")
    @ExceptionHandler({RuntimeException.class, InterruptedException.class, InternalError.class, JsonProcessingException.class, JSONException.class, IOException.class})
    public RestApiResponse handleException(Exception e) {
        logger.error("handle Exception: {}", e.getMessage());
        return RestApiResponse.setResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @Description("Request bad request exception abort")
    @ExceptionHandler({HttpClientErrorException.BadRequest.class})
    public RestApiResponse handleBadRequestException(Exception e) {
        logger.error("handle BadRequestException: {}", e.getMessage());
        return RestApiResponse.setResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @Description("Request unauthorized exception abort")
    @ExceptionHandler({HttpClientErrorException.Unauthorized.class})
    public RestApiResponse handleUnauthorizedException(Exception e) {
        logger.error("handle UnauthorizedException: {}", e.getMessage());
        return RestApiResponse.setResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @Description("Request timeout exception abort")
    @ExceptionHandler({TimeoutException.class})
    public RestApiResponse handleTimeoutException(Exception e) {
        logger.error("handle TimeoutException: {}", e.getMessage());
        return RestApiResponse.setResponse(HttpStatus.REQUEST_TIMEOUT.value(), e.getMessage());
    }

    @Description("Request forbidden exception abort")
    @ExceptionHandler({HttpClientErrorException.Forbidden.class})
    public RestApiResponse handleForbiddenException(Exception e) {
        logger.error("handle ForbiddenException: {}", e.getMessage());
        return RestApiResponse.setResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    @Description("Request not found exception abort")
    @ExceptionHandler({HttpClientErrorException.NotFound.class, UsernameNotFoundException.class})
    public RestApiResponse handleNotFoundException(Exception e) {
        logger.error("handle NotFoundException: {}", e.getMessage());
        return RestApiResponse.setResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @Description("Request not acceptable exception abort")
    @ExceptionHandler({HttpClientErrorException.NotAcceptable.class})
    public RestApiResponse handleNotAcceptableException(Exception e) {
        logger.error("handle NotAcceptableException: {}", e.getMessage());
        return RestApiResponse.setResponse(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
    }

    // Please define various exception situations below...

}
