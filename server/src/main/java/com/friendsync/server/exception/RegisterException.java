package com.friendsync.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "register bad request")
public class RegisterException extends RuntimeException {
    public RegisterException(String msg) {
        super(msg);
    }
}
