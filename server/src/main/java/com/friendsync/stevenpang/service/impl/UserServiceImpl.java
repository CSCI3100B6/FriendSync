package com.friendsync.stevenpang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.friendsync.stevenpang.common.ErrorCode;
import com.friendsync.stevenpang.exception.BusinessException;
import com.friendsync.stevenpang.mapper.UserMapper;
import com.friendsync.stevenpang.model.User;
import com.friendsync.stevenpang.model.request.UserDTO;
import com.friendsync.stevenpang.model.request.UserRequest;
import com.friendsync.stevenpang.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.friendsync.stevenpang.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户实现类
 * @author lenovo
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-02-18 10:35:17
 */

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "Steven";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "账户或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";

        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户包含特殊字符");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不同");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户已经存在");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUsername(userAccount);
        user.setPhone(userAccount);
        user.setIsDelete(0);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            //  todo 修改为自定义异常
            throw new BusinessException(ErrorCode.PARAMS_NULL_ERROR, "账户或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";

        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账户包含特殊字符");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //  用户不存在
        if (user == null) {
            log.info("User login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.USER_NOT_EXIST, "用户不存在");
        }
        //  3. 用户脱敏
        User safetyUser = getSafetyUser(user);
        //  4. 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;

    }

    /**
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setTags(originUser.getTags());
        return safetyUser;
    }

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 根据标签搜索用户
     * 两种查询方式：
     *  1. 数据库查询
     *  2. 内存查询
     * @param tagNameList
     * @return
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 1. 查询所有用户
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        // 2. 在内存中判断是否包含要求的标签
        // 并发流：改stream为parallelStream；缺点：需要线程池
        return userList.stream()
                .filter(user -> {
                    String tagStr = user.getTags();
                    // 所有数据库取出来的值都需要进行校验
                    if (StringUtils.isEmpty(tagStr)) {
                        return false;
                    }

                    try {
                        Set<String> userTags = gson.fromJson(tagStr, new TypeToken<Set<String>>() {}.getType());
                        return userTags != null && userTags.stream().anyMatch(tagNameList::contains);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(this::getSafetyUser)
                .collect(Collectors.toList());

    }

    /**
     * 随机获取5条数据
     * @return
     */
    @Override
    public List<User> getRandomRecords() {
        return userMapper.getRandomRecords();
    }


    @Override
    public List<User> searchUsers(UserRequest userRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String username = userRequest.getUsername();
        if (username != null && !username.isBlank()){
            queryWrapper.like("username", username);
        }
        List<String> tagNameList = userRequest.getTagNameList();
        if (!CollectionUtils.isEmpty(tagNameList)){
            for (String tagName : tagNameList) {
                queryWrapper = queryWrapper.like("tags", tagName);
            }
        }
        // 查询用户数据
        List<User> userList = userMapper.selectList(queryWrapper);
        // 用户数据脱敏
        userList = userList.stream().map(this::getSafetyUser).collect(Collectors.toList());

        return userList;
    }
}




