package com.friendsync.muush.repo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@TableName(value = "conversation")
@Data
public class Conversation implements Serializable {

    public enum ConversationType { CHAT, TEAM, ROOM }

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("type")
    private ConversationType type;

    @TableField("owner_id")
    private Long ownerId;

    @TableField("name")
    private String name;

    @TableField("information")
    private String information;

    @TableField("license")
    private String license;
}
