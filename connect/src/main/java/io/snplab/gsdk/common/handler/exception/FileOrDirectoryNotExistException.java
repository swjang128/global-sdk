package io.snplab.gsdk.common.handler.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FileOrDirectoryNotExistException extends RuntimeException {

    private final HttpStatus code;

    public FileOrDirectoryNotExistException() { code = HttpStatus.BAD_REQUEST; }

    public FileOrDirectoryNotExistException(String message) {
        super(message);
        code = HttpStatus.BAD_REQUEST;
    }
}
