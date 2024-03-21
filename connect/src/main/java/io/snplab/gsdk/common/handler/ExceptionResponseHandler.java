package io.snplab.gsdk.common.handler;

import com.amazonaws.services.kms.model.NotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jdi.request.DuplicateRequestException;
import io.snplab.gsdk.common.domain.RestApiResponse;
import io.snplab.gsdk.common.handler.exception.FileOrDirectoryNotExistException;
import io.snplab.gsdk.common.handler.exception.FileSaveException;
import io.snplab.gsdk.common.handler.exception.FileValidityException;
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
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
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
        RestApiResponse<Object> restApiResponse = RestApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(restApiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Description("Http message not readable exception abort")
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        logger.error("handle HttpMessageNotReadable: {}", e.getMessage());
        RestApiResponse<Object> restApiResponse = RestApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(restApiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Description("Method not allowed exception abort")
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        logger.error("handle HttpRequestMethodNotSupported: {}", e.getMessage());
        RestApiResponse<Object> restApiResponse = RestApiResponse.error(HttpStatus.FORBIDDEN, e.getMessage());
        return new ResponseEntity<>(restApiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Description("Exception abort")
    @ExceptionHandler({RuntimeException.class, InterruptedException.class, InternalError.class, JsonProcessingException.class, JSONException.class, IOException.class, TimeoutException.class, DuplicateRequestException.class})
    public RestApiResponse<Object> handleException(Exception e) {
        logger.error("handle Exception: {}", e.getMessage());
        return RestApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @Description("Request bad request exception abort")
    @ExceptionHandler({HttpClientErrorException.BadRequest.class, HttpClientErrorException.NotFound.class, NotFoundException.class, IllegalArgumentException.class})
    public RestApiResponse<Object> handleBadRequestException(Exception e) {
        logger.error("handle BadRequestException: {}", e.getMessage());
        return RestApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @Description("Request unauthorized exception abort")
    @ExceptionHandler({HttpClientErrorException.Unauthorized.class, UsernameNotFoundException.class})
    public RestApiResponse<Object> handleUnauthorizedException(Exception e) {
        logger.error("handle UnauthorizedException: {}", e.getMessage());
        return RestApiResponse.error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @Description("Request forbidden exception abort")
    @ExceptionHandler({HttpClientErrorException.Forbidden.class, HttpClientErrorException.NotAcceptable.class})
    public RestApiResponse<Object> handleForbiddenException(Exception e) {
        logger.error("handle ForbiddenException: {}", e.getMessage());
        return RestApiResponse.error(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @Description("File validation exception abort")
    @ExceptionHandler(FileValidityException.class)
    public RestApiResponse<Object> handleFileValidateException(FileValidityException e) {
        logger.error("handle FileValidationException: {}", e.getMessage());
        return RestApiResponse.error(e.getCode(), e.getMessage());
    }

    @Description("File upload exception abort")
    @ExceptionHandler(FileSaveException.class)
    public RestApiResponse<Object> handleFileSaveException(FileSaveException e) {
        logger.error("handle FileSaveException: {}", e.getMessage());
        return RestApiResponse.error(e.getCode(), e.getMessage());
    }

    @Description("File or Directory not found exception abort")
    @ExceptionHandler(FileOrDirectoryNotExistException.class)
    public RestApiResponse<Object> handleFileOrDirectoryNotExistException(FileOrDirectoryNotExistException e) {
        logger.error("handle File or Directory NotExistException: {}", e.getMessage());
        return RestApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class})
    public void processAuthenticationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
