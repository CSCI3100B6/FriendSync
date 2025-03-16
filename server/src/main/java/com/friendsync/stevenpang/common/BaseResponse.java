package com.friendsync.stevenpang.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 * @author Stevenpang
 */
// lombok mark to generate Data method
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;// Generics increase reusability

    private String message;;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String description) {
        this(code, data, "", description);
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());

    }

}
