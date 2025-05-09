# FriendSync Test Document

 This comprehensive testing document outlines the procedures and objectives for testing the FriendSync product, a software application based on a Client-Server architecture. The testing process is divided into two main sections: front-end testing and back-end testing. Both sections aim to ensure the system's robustness, performance, and functionality.

## Test Scope and Objectives

### Backend

The back-end testing focuses on validating the services' correct operation within the Spring Boot Framework. The test codes are designed to ascertain that each service performs as expected.

### Frontend

The front-end testing employs Lighthouse to evaluate the website's performance, including metrics such as load times, interactivity, and accessibility.

 

## Test Cases

### Back-end Unit Test

All the test cases are written by java code, including 

#### User Service Test

Test the user service, which is for the user register, login and so on.

#### Add / Update User

Steps: Input all the information of the user

Excepted result: new data is added into the database.

Pass/Fail criteria: the boolean value returned from the function.

#### Register user

Steps: Input all the information of the user

Excepted result: new user is registered and new data is added into the database if the user name and password are valid. Otherwise reject to register.

Pass/Fail criteria: the int value returned from the function.

#### Search users by tags

Steps: Input the tags of the users

Excepted result: return the user list

Pass/Fail criteria: the list content is expected.

```java
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("test");
        user.setUserAccount("123");
        user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("dogYupi");
        user.setUserAccount("123");
        user.setAvatarUrl("https://636f-codenav-8grj8px727565176-1256524210.tcb.qcloud.la/img/logo.png");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.updateById(user);
        Assertions.assertTrue(result);
    }

    @Test
    public void testDeleteUser() {
        boolean result = userService.removeById(1L);
        Assertions.assertTrue(result);
    }

    @Test
    public void testGetUser() {
        User user = userService.getById(1L);
        Assertions.assertNotNull(user);
    }

    @Test
    void userRegister() {
        String userAccount = "yupi";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yu";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yu pi";
        userPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "dogYupi";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
        userAccount = "yupi";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void testSearchUsersByTags() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        Assert.assertNotNull(userList);
    }
}
```

#### Conversation Service Test

Test the conversion service, which is for the conversion creation, deletion and management.

#### Search conversation

Steps: Input the search key words

Excepted result: return the conversation list

Pass/Fail criteria: the list content is expected.

#### Add conversation

Steps: Input the conversation name, information and type.

Excepted result: new data is added to the database.

Pass/Fail criteria: the data base content is expected.

#### Generate license

Steps: input the owner id and conversation id

Excepted result: new license

Pass/Fail criteria: the license is new and consistent with the license in the database.

#### Delete conversation

Steps: input the owner id and conversation id

Excepted result: the conversation data is removed from the data table

Pass/Fail criteria: the boolean return from the function is true.

#### Get own conversation

Steps: call the service function

Excepted result: the list of the all conversation

Pass/Fail criteria: the size of the return list is as same as the database

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConversationServiceTest {
    @Resource
    private ConversationService conversationService;

    @Test
    public void testSearch() {
        List<Conversation> l = conversationService.searchConversations("est");
        System.out.println(l.size());
        for (Conversation conversation : l) {
            System.out.println(conversation.getInformation());
        }
    }

    @Test
    public void addConversation() {
        User owner = new User();
        owner.setId(1L);
        conversationService.createConversation(owner, "test 0", "info abc", Conversation.ConversationType.CHAT);
        conversationService.createConversation(owner, "test 1", "info ccb", Conversation.ConversationType.TEAM);
        owner.setId(2L);
        conversationService.createConversation(owner, "test 2", "info bin", Conversation.ConversationType.ROOM);
    }

    @Test
    public void testGenerateLicense() {
        User owner = new User();
        owner.setId(1L);
        String lic = conversationService.generateLicense(5L, owner);
        System.out.println(lic);
    }

    @Test
    public void testGetOwn() {
        User owner = new User();
        owner.setId(1L);
        System.out.println(conversationService.getOwnConversations(owner).size());
    }

    @Test
    public void testDelete() {
        User owner = new User();
        owner.setId(1L);
        assert(conversationService.deleteConversation(4L, owner));
    }

    @Test
    public void testGetAll() {
        System.out.println(conversationService.getAllConversation().size());
    }
}
```

#### User Conversation Test

Test the user conversion service, which is for the user-conversation relation management.

#### Get joined conversation

Steps: input the user id

Excepted result: the conversations that the user have joined

Pass/Fail criteria: the list content is expected.

#### Join conversation

Steps: input the user id and conversation id.

Excepted result: new data is added to the database table

Pass/Fail criteria: database content is expected.

#### Join with license

Steps: input the user id, conversation id and license.

Excepted result: new data is added to the database table

Pass/Fail criteria: database content is expected.

#### Leave conversation

Steps: input the user id, conversation id and new owner id.

Excepted result: new data is added to the database table, the conversation owner is updated if the leaving user is the original owner.

Pass/Fail criteria: database content is expected.

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserConversationTest {
    @Resource
    UserConversationService service;
    @Resource
    ConversationService conversationService;

    @Test
    public void testGetJoined() {
        User user = new User();
        user.setId(0L);
        List<Conversation> list = service.getJoinedConversation(0L);
        for (Conversation c : list)
            System.out.println(c.getName());
    }

    @Test
    public void testJoin() {
        User user = new User();
        user.setId(0L);
        Conversation c;
        c = conversationService.createConversation(user, "test1", "", ConversationType.CHAT);
        service.join(user.getId(), c.getId());
        c = conversationService.createConversation(user, "test2", "", ConversationType.ROOM);
        service.join(user.getId(), c.getId());
        c = conversationService.createConversation(user, "test3", "", ConversationType.TEAM);
        service.join(user.getId(), c.getId());
    }

    @Test
    public void testLicense() {
        User user = new User();
        user.setId(0L);
        Conversation c;
        c = conversationService.createConversation(user, "test for license", "", ConversationType.TEAM);
        String license = conversationService.generateLicense(c.getId(), user);
        System.out.println(license);
        User user2 = new User();
        user2.setId(2L);
        service.joinWithLicense(user2.getId(), c.getId(), license);
        List<Conversation> list = service.getJoinedConversation(user2.getId());
        for (Conversation cc : list)
            System.out.println(cc.getName());
    }

    @Test
    public void leave() {
        User user = new User();
        user.setId(0L);
        Conversation c;
        c = conversationService.createConversation(user, "test1", "", ConversationType.CHAT);
        service.join(user.getId(), c.getId());
        user.setId(1L);
        service.join(user.getId(), c.getId());
        System.out.println(service.leave(0L, c.getId(), 1L));
        System.out.println(service.leave(1L, c.getId(), null));
    }
}
```



#### Chat Message Service Test

Test the chat message service, which is for the message sending logics.

#### Add Message

Steps: input the conversation id, sender id, send time, message content.

Excepted result: new data is added to the database table

Pass/Fail criteria: the function return int one.

#### Get Message

Steps: input the conversation id, offset and numbers.

Excepted result: the list of the message.

Pass/Fail criteria: the list is expected.

#### Delete Message

Steps: input the conversation id

Excepted result: the message data is deleted from the database

Pass/Fail criteria: the function returns int one.

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatMessageServiceTest {

    @Resource
    private ChatMessageService chatMessageService;
    
    @Test
    public void testAddMsg() {
        assertNotNull(chatMessageService);
        
        for (int i = 0; i < 10; i++)
        {
            ChatMessage msg = new ChatMessage();
            msg.setConversationId(0L);
            msg.setSenderId((long)i * 10L);
            msg.setSendTime(new Timestamp(System.currentTimeMillis() + i * 1000L));
            msg.setMsgContent("test msg " + i);

            int rtn = chatMessageService.addMessage(msg);
            assertEquals(1, rtn);
        }
    }

    @Test
    public void testGetMsg() {
        List<ChatMessage> msgList =
            chatMessageService.getRecentChatMessagesByConversationID(
                0L, 0, 100);
        System.out.println(msgList.size());
        for (ChatMessage chatMessage : msgList) {
            System.out.println(chatMessage.getSendTime());
        }
    }

    @Test
    public void testDeleteMsg() {
        List<ChatMessage> msgList =
            chatMessageService.getRecentChatMessagesByConversationID(
                0L, 0, 5);
        for (ChatMessage chatMessage : msgList) {
            assertEquals(1, chatMessageService.deleteMessage(chatMessage));
            System.out.println("delete msg: " + chatMessage.toString());
        }
    }
}
```



## Test Platform

### Development Environment

We use the IDEA and VSCode to develop the back-end and front-end.

### Testing Environment

System: Windows

Java version: 17

Web browser: Chrome



## Team Roles and Responsibilities

WU Xuanwei and PANG Jia is for the back-end testing and debugging.

WONG Cheuk Lam and ZHU Kevin is for the front-end testing and debugging.

 

## Test Approach

Back-end testing utilizes unit testing to isolate and test each service independently.

Front-end testing employs integrated testing to measure overall performance and user experience.

 

## Timeline and Schedule

| Time | Schedule |
|------|-----|
| Day 1   | Sprint planning, finalize acceptance  criteria, write initial test cases. |
| Day 2   | Begin exploratory testing, prepare test  environment, start test case execution. |
| Day 3-4 | Execute test cases for completed user  stories, log bugs, perform regression testing. |
| Day 5   | Final acceptance testing, system testing,  smoke testing for sprint review. |
| Day 6   | Sprint review/demo, retrospective,  prepare test summary report. |

