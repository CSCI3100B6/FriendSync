package com.friendsync.stevenpang.controller;

import com.friendsync.stevenpang.common.BaseResponse;
import com.friendsync.stevenpang.common.ResultUtils;
import com.friendsync.stevenpang.model.Tag;
import com.friendsync.stevenpang.service.TagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController {
    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public BaseResponse<List<Tag>> listTag(){
        return ResultUtils.success(tagService.list());
    }
}
