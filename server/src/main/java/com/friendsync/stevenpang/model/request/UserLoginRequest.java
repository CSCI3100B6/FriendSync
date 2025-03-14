package com.friendsync.stevenpang.model.request;

import lombok.Data;

import java.io.Serializable;


/**
 * 用户登录请求体
 *
 * @author Steven
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 123456L;
    private String userAccount;
    private String userPassword;
}
