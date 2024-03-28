package io.snplab.gsdk.common.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FileSaveException extends RuntimeException {

    private final HttpStatus code;

    public FileSaveException() { code = HttpStatus.INTERNAL_SERVER_ERROR; }

    public FileSaveException(String message) {
        super(message);
        code = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
