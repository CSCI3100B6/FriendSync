# FriendSync Test Document

 

This is the test document of the product FriendSync. This is a Client-Server architecture software. We divide the testing into two parts, front-end testing and back-end testing.

## Test Scope and Objectives

### Backend

​    For the back-end testing, we use the testing method integrated in the Spring Boot Framework. The test code is mainly focused on the service running correctly.

### Frontend

​    For the front-end testing, we use the lighthouse to measure the performance of the front-end website.

 

## Test Cases

### Front-end

#### User Service Test

Test the user service, which is for the user register, login and so on.

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



 

## Team Roles and Responsibilities

XXX is for the back-end testing and debugging.

XXX is for the front-end testing and debugging.

 

## Test Approach

The back-end is using the unit testing. We test different services individually.

The front-end is using the integrated testing. We measure the front-end overall performance.

 

## Timeline and Schedule

| Time | Schedule |
|------|-----|
| Day 1   | Sprint planning, finalize acceptance  criteria, write initial test cases. |
| Day 2   | Begin exploratory testing, prepare test  environment, start test case execution. |
| Day 3-4 | Execute test cases for completed user  stories, log bugs, perform regression testing. |
| Day 5   | Final acceptance testing, system testing,  smoke testing for sprint review. |
| Day 6   | Sprint review/demo, retrospective,  prepare test summary report. |

