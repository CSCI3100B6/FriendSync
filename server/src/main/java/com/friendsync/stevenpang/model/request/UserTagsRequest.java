package com.friendsync.stevenpang.model.request;

import lombok.Data;

import java.util.List;

@Data
public class UserTagsRequest {
    private List<String> tags;
    private Integer id;
}
