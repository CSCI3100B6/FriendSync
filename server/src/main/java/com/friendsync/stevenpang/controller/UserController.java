package com.friendsync.stevenpang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendsync.stevenpang.common.BaseResponse;
import com.friendsync.stevenpang.common.ErrorCode;
import com.friendsync.stevenpang.common.ResultUtils;
import com.friendsync.stevenpang.constant.UserConstant;
import com.friendsync.stevenpang.exception.BusinessException;
import com.friendsync.stevenpang.model.User;
import com.friendsync.stevenpang.model.request.*;
import com.friendsync.stevenpang.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.friendsync.stevenpang.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 *
 * @author Steven
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        int logout = userService.userLogout(request);
        return ResultUtils.success(logout);
    }


    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }



    @PostMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestBody UserRequest userRequest) {
        if (userRequest == null){
            return ResultUtils.success(userService.getRandomRecords());
        }

        return ResultUtils.success(userService.searchUsers(userRequest));
    }

    @PostMapping("/updateTags")
    public BaseResponse<Boolean> updateTags(@RequestBody UserTagsRequest userTagsRequest) {
        if (userTagsRequest == null){
            return ResultUtils.success(false);
        }
        Integer id = userTagsRequest.getId();
        User user = userService.getById(id);

        List<String> tags = userTagsRequest.getTags();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String tagsJson = objectMapper.writeValueAsString(tags);
            user.setTags(tagsJson);

            return ResultUtils.success(userService.updateById(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResultUtils.success(false);
        }
    }

}
