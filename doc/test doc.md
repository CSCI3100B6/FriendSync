# FriendSync Test Document

 This comprehensive testing document outlines the procedures and objectives for testing the FriendSync product, a software application based on a Client-Server architecture. The testing process is divided into two main sections: front-end testing and back-end testing. Both sections aim to ensure the system's robustness, performance, and functionality.

## Test Scope and Objectives

### Backend

The back-end testing focuses on validating the services' correct operation within the Spring Boot Framework. The test codes are designed to ascertain that each service performs as expected.

#### Scope:

1. User Service
2. Conversation Service
3. User Conversation Service
4. Chat Message Service

### Frontend

Vue unit test tool is used to check the front-end logic. The front-end testing also employs Lighthouse to evaluate the website's performance, including metrics such as load times, interactivity, and accessibility.

#### Scope

##### Authentication

- Login
- Registration
- Logout functionality

##### User Management

- User Profile
- User Edit Page

##### Navigation

- Basic Layout
- Tab Bar Navigation

##### Search Functionality

- Tag-based search
- User recommendations

##### Team s Room Management

- Team listing
- Team creation
- Team joining/leaving

##### Conversation Features

- Chat interface
- Message sending/receiving

 

## Test Cases

### Back-end Unit Test

All the test cases are written by java code, including 

#### User Service Test

Test the user service, which is for the user register, login and so on.

##### Add / Update User

Steps: Input all the information of the user

Excepted result: new data is added into the database.

Pass/Fail criteria: the boolean value returned from the function.

##### Register user

Steps: Input all the information of the user

Excepted result: new user is registered and new data is added into the database if the user name and password are valid. Otherwise reject to register.

Pass/Fail criteria: the int value returned from the function.

##### Search users by tags

Steps: Input the tags of the users

Excepted result: return the user list

Pass/Fail criteria: the list content is expected.

#### Conversation Service Test

Test the conversion service, which is for the conversion creation, deletion and management.

##### Search conversation

Steps: Input the search key words

Excepted result: return the conversation list

Pass/Fail criteria: the list content is expected.

##### Add conversation

Steps: Input the conversation name, information and type.

Excepted result: new data is added to the database.

Pass/Fail criteria: the data base content is expected.

##### Generate license

Steps: input the owner id and conversation id

Excepted result: new license

Pass/Fail criteria: the license is new and consistent with the license in the database.

##### Delete conversation

Steps: input the owner id and conversation id

Excepted result: the conversation data is removed from the data table

Pass/Fail criteria: the boolean return from the function is true.

##### Get own conversation

Steps: call the service function

Excepted result: the list of the all conversation

Pass/Fail criteria: the size of the return list is as same as the database

#### User Conversation Test

Test the user conversion service, which is for the user-conversation relation management.

##### Get joined conversation

Steps: input the user id

Excepted result: the conversations that the user have joined

Pass/Fail criteria: the list content is expected.

##### Join conversation

Steps: input the user id and conversation id.

Excepted result: new data is added to the database table

Pass/Fail criteria: database content is expected.

##### Join with license

Steps: input the user id, conversation id and license.

Excepted result: new data is added to the database table

Pass/Fail criteria: database content is expected.

##### Leave conversation

Steps: input the user id, conversation id and new owner id.

Excepted result: new data is added to the database table, the conversation owner is updated if the leaving user is the original owner.

Pass/Fail criteria: database content is expected.


#### Chat Message Service Test

Test the chat message service, which is for the message sending logics.

##### Add Message

Steps: input the conversation id, sender id, send time, message content.

Excepted result: new data is added to the database table

Pass/Fail criteria: the function return int one.

##### Get Message

Steps: input the conversation id, offset and numbers.

Excepted result: the list of the message.

Pass/Fail criteria: the list is expected.

##### Delete Message

Steps: input the conversation id

Excepted result: the message data is deleted from the database

Pass/Fail criteria: the function returns int one.

### Front-end

#### Authentication Tests

##### TC-AU-001: User Registration

###### Test Steps:

Navigate to registration page

Enter valid user information:

User account (minimum 4 characters)

Password (minimum 8 characters)

Confirm password

Planet code (maximum 5 characters)

Click "Register" button

###### Expected Result:

User account created successfully

User redirected to login page

Appropriate success message displayed

##### TC-AU-002: User Registration - Invalid Data

###### Test Steps:

Navigate to registration page

Test with invalid inputs:

User account less than 4 characters

Password less than 8 characters

Non-matching password confirmation

Special characters in user account

Click "Register" button

###### Expected Result:

Registration fails

Appropriate validation error messages displayed

User remains on the registration page

##### TC-AU-003: User Login

###### Test Steps:

Navigate to login page

Enter valid credentials

Click "Login" button

###### Expected Result:

User successfully logs in

User redirected to home page

User session created

##### TC-AU-004: User Logout

###### Test Steps:

Log in as a valid user

Click on profile icon

Select "Logout" option

###### Expected Result:

User logged out successfully

User redirected to login page

User session terminated

#### User Profile Tests

##### TC-UP-001: View User Profile

###### Test Steps:

Login with valid credentials

Navigate to the user profile page

###### Expected Result:

User information displayed correctly:

Username

UserID

Email

#### TC-UP-002: Edit User Profile

##### Test Steps:

Login with valid credentials

Navigate to the user profile page

Click "Edit" button

Modify user information

Click "Save Changes"

##### Expected Result:

Changes saved successfully

Updated profile information displayed

Success message shown to user

#### Search Functionality Tests

##### TC-SE-001: Search Users by Tags

###### Test Steps:

Navigate to search page

Enter tag name in search field

Click "Search" button

###### Expected Result:

Users with matching tags displayed in results

Appropriate message if no matches found

#### Team Management Tests

##### TC-TM-001: Create Team

###### Test Steps:

Navigate to team creation page

Enter team details:

Name (maximum 20 characters)

Description (maximum 512 characters)

Maximum number of members (1-20)

Click "Create" button

###### Expected Result:

Team created successfully

User redirected to team page

Success message displayed

##### TC-TM-002: Join Team

###### Test Steps:

Navigate to team list page

Select a team to join

For public teams: Click "Join Team"

For secret teams: Enter password and click "Join Team"

###### Expected Result:

User successfully joins the team

Team appears in user's joined teams list

Success message displayed

##### TC-TM-003: Leave Team

###### Test Steps:

Navigate to user's joined teams

Select a team

Click "Leave Team" button

###### Expected Result:

User successfully leaves the team

Team removed from user's joined teams list

Success message displayed

#### Responsive Design Tests

##### TC-RD-001: Mobile Responsiveness

###### Test Steps:

Open application on mobile devices or using device emulation

Check all pages for proper rendering and functionality

Test navigation, forms, and interactive elements

###### Expected Result:

All pages are rendered correctly on mobile devices

Navigation works properly




## Test Platform

### Development Environment

We use the IDEA and VSCode to develop the back-end and front-end.

### Testing Environment

#### Back-end

System: Windows

Java version: 17

#### Front-end

- Node.js
- npm
- Supported browsers:
	- Chrome (latest 2 versions)
	- Firefox (latest 2 versions)
	- Edge (latest 2 versions)



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

