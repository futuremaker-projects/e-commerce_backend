package com.ecommerce.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 202
    CONTENT_NOT_FOUND(HttpStatus.ACCEPTED, "CONTENT_NOT_FOUND"),
    LIMIT_EXCEEDED(HttpStatus.ACCEPTED, "LIMIT_EXCEEDED"),
    UNABLE_TO_RESERVE(HttpStatus.ACCEPTED, "UNABLE_TO_RESERVE"),
    DISTRIBUTED_LOCK_NOT_AVAILABLE(HttpStatus.ACCEPTED,  "DISTRIBUTED_LOCK_NOT_AVAILABLE"),
    INVALID_INPUT(HttpStatus.ACCEPTED, "NO_VALUE"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

}
