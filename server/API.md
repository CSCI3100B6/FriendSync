# API details

## Register
Path: `/user/register`
Method: `POST`
Body: json format with three properties: `name`, `email` and `password`. Example:
``` json
{
    "name": "testing",
    "email": "123@gg.com",
    "password": "123"
}
```
Response:
- success
  Http code: OK(200)
  Message: seccess to register a new user
- used email
  Http code: BAD_REQUEST(400)
  Message: registered email
- something is empty
  Http code: BAD_REQUEST(400)
  Message: "email can not be empty" or "password can not be empty"

## Login
Path: `/user/login`
Method: `POST`
Body: json format with two properties: `email` and `password`. Example:
``` json
{
    "email": "123@gg.com",
    "password": "abc"
}
```
Response:
- success
  Http code: OK(200)
  Message: seccess to login
- unregistered email
  Http code: BAD_REQUEST(400)
  Message: unregistered email
- password is empty
  Http code: BAD_REQUEST(400)
  Message: empty password
- password is wrong
  Http code: UNAUTHORIZED(401)
  Message: wrong password