package com.friendsync.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "login bad request")
public class LoginException extends RuntimeException {
    public LoginException(String msg) {
        super(msg);
    }
}
