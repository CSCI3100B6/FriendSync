package com.friendsync.stevenpang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.friendsync.stevenpang.model.Tag;
import com.friendsync.stevenpang.service.TagService;
import com.friendsync.stevenpang.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author 21639
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2025-05-08 10:59:32
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




