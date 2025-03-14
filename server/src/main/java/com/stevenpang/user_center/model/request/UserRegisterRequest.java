package com.stevenpang.user_center.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author Steven
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 114514L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
