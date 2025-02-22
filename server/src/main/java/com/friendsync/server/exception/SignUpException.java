package com.friendsync.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "bad request")
public class SignUpException extends RuntimeException {
    public SignUpException(String msg) {
        super(msg);
    }
}
