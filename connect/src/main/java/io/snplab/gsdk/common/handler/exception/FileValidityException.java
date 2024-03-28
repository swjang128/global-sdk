package io.snplab.gsdk.common.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FileValidityException extends RuntimeException {

    private final HttpStatus code;

    public FileValidityException() { code = HttpStatus.BAD_REQUEST; }

    public FileValidityException(String message) {
        super(message);
        code = HttpStatus.BAD_REQUEST;
    }
}
