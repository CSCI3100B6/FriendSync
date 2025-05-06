package com.friendsync.stevenpang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.friendsync.stevenpang.model.domain.UserTeam;
import com.friendsync.stevenpang.service.UserTeamService;
import com.friendsync.stevenpang.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2025-05-01 19:22:14
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




