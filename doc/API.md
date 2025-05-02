# server API document

The api root url is `localhost:8088/api`. It can be changed by the `src/main/resources/application.yml`.

---

## user API

path: `localhost:8088/api/user`

### Register

path: `localhost:8088/api/user/register`
method: POST
request body: 
``` java
public class UserRegisterRequest implements Serializable {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;  // I don't know what it means. Ask stevenPang
}
```
return body:
``` java
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data; // Generics increase reusability
    private String message;
    private String description;
}
```
  data:
  - -1 if failed
  - user id if success

### Login

path: `localhost:8088/api/user/login`
method: POST
request body: 
``` java
public class UserLoginRequest implements Serializable {
    private String userAccount;
    private String userPassword;
}
```
return body:
``` java
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data; // Generics increase reusability
    private String message;
    private String description;
}
```
  data:
  - null if failed
  - User object if success
``` java
public class User implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 
     */
    private String userPassword;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0 正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 用户角色 0 普通用户 1 管理员
     */
    private Integer userRole;

    /**
     * 用户编号
     */
    private String planetCode;
    /**
     * 标签列表 json
     */
    private String tags;
}
```
after login, session will have a new attribute: `"userLoginState": User object`

### Logout

path: `localhost:8088/api/user/register`
method: POST
request body: not needed
return body:
``` java
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data; // Generics increase reusability
    private String message;
    private String description;
}
```
  data:
  - 1
Will remove the `"userLoginState"` attribute of the session.

### Get current user

path: `localhost:8088/api/user/current`
method: GET
request body: not needed
return body:
``` java
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data; // Generics increase reusability
    private String message;
    private String description;
}
```
  data:
  - User object

### Search

path: `localhost:8088/api/user/search`
method: GET
request parameter: `username`
return body:
``` java
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data; // Generics increase reusability
    private String message;
    private String description;
}
```
  data:
  - `List<User>`

### Delete

path: `localhost:8088/api/user/current`
method: POST
request body: `Long id`
return body:
``` java
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data; // Generics increase reusability
    private String message;
    private String description;
}
```
  data:
  - true if success
  - false if failed
**The current user needs to be Admin**

---

## Conversation API

path: `localhost:8088/api/conversation`

### Search

path: `localhost:8088/api/conversation/search`
method: GET
request parameter: `String keyword`
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "need GET param: keyword"` if keyword is empty
HttpStatus.OK 200:
  - `List<Conversation>` if success
``` java
public class Conversation implements Serializable {
    public enum ConversationType { CHAT, TEAM, ROOM }
    private Long id;
    private ConversationType type;
    private Long ownerId;
    private String name;
    private String information;
    private String license;
}
```

### Create team or room

path: `localhost:8088/api/conversation/create-team-room`
method: POST
request body:
``` java
public class CreateTeamRoomRequest implements Serializable {
    private Long ownerId;
    private String name;
    private String information;
    private Conversation.ConversationType type;
}
```
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "wrong conversation type"` if keyword is empty
  - `String "failed"` if failed
HttpStatus.OK 200:
  - `Conversation` object if success
**License will be empty after create. If you want to join a team by license, need to generate one first.**

### Create Chat

path: `localhost:8088/api/conversation/create-chat`
method: POST
request body:
``` java
public class CreateChatRequest implements Serializable {
    private Long ownerId;
    private Long othersId;
}
```
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "cannot chat with self"` if `othersId` is same as `ownerId`
  - `String "failed"` if failed
HttpStatus.OK 200:
  - `Conversation` object if success
**OwnerId should be same as current user's id.**

### Generate License

path: `localhost:8088/api/conversation/generate`
method: GET
request parameters:
  - `id` the id of the team conversation
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "failed"` if failed
HttpStatus.OK 200:
  - `String` the license if success
**The current user should be the owner of the team or the Admin.**

### Get the conversations own

path: `localhost:8088/api/conversation/getown`
method: GET
request parameters: not needed
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
HttpStatus.OK 200:
  - `List<Conversation` the list of the conversation the current user own

### Delete conversation

path: `localhost:8088/api/conversation/delete`
method: GET
request parameters:
  - `id` the id of the conversation
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "failed"` if failed
HttpStatus.OK 200:
  - `String "success"` if success
**The current user should be the owner of the team or the Admin.**

### Get all conversations

path: `localhost:8088/api/conversation/all`
method: GET
request parameters: not needed
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "not admin"` if the user is not the admin
HttpStatus.OK 200:
  - `List<Conversation>` if success
**The current user should be the Admin.**

---

## User conversation API

path: `localhost:8088/api/user-conversation`


### Get joined conversation
path: `localhost:8088/api/user-conversation/joined`
method: GET
request parameters: not needed
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
HttpStatus.OK 200:
  - `List<Conversation>` if success

### Join room
path: `localhost:8088/api/user-conversation/join-room`
method: GET
request parameters: `id` the room id
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
HttpStatus.OK 200:
  - `Conversation` the room conversation object if success

### Join team
path: `localhost:8088/api/user-conversation/join-team`
method: GET
request parameters:
  - `id` the team id
  - `license` the license
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "wrong license"` if the license is wrong
HttpStatus.OK 200:
  - `Conversation` the room conversation object if success

### Leave
path: `localhost:8088/api/user-conversation/leave`
method: GET
request parameters:
  - `id` the conversation id
  - `new_owner` the new owner id (not required if the user is not the owner)
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
  - `String "user or new owner not in the conversation"` if not in the conversation
HttpStatus.OK 200:
  - `String "success"` the room conversation object if success

---

## Chat message API

path: `localhost:8088/api/message`

### Get message
path: `localhost:8088/api/message/get`
method: POST
request body:
``` java
public class GetMessageRequest implements Serializable {
    Long conversationId;
    Integer offset;
    Integer num;
}
```
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
HttpStatus.OK 200:
  - `List<ChatMessage>` if success
``` java
public class ChatMessage implements Serializable {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private Timestamp sendTime;
    private String msgContent;
}
```

### Send message
path: `localhost:8088/api/message/send`
method: POST
request body:
``` java
public class SendMessageRequest implements Serializable {
    Long conversationId;
    Long userId;
    String content;
}
```
return body:
HttpStatus.BAD_REQUEST 400:
  - `String "login first"` if not login
HttpStatus.OK 200:
  - `String "success"` if success