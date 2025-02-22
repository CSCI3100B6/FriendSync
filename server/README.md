# Backend of FriendSync

## Quick start

1. If `sh` is in environment
   Run `gradlew bootRun` to start the server
2. If there is no `sh` in environment
   You need to install `gradle` to run the server. Recommand version is `Gradle 8.12.1`.
   Then run `gradle bootRun` to start the server.

The server will run on the port `8088`. Please make sure the is no conflict on this port.

## Check database
You can check the database online [link](http://localhost:8088/h2).
The username is `admin` and the password is `B6`.
All these can be chaged by edit the file `src\main\resources\application.yml`