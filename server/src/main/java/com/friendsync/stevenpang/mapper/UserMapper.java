package com.friendsync.stevenpang.mapper;

import com.friendsync.stevenpang.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 21639
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2025-05-07 21:33:56
* @Entity com.friendsync.stevenpang.model.User
*/
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM your_table ORDER BY RAND() LIMIT 5")
    List<User> getRandomRecords();
}




