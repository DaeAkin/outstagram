# UserAPI



## /user/email-validation

로그인 할 때 아이디를 입력하는데, 이 때 입력한 아이디가 있으면, 바로 비밀번호 입력으로 넘어가고, 없으면 
비밀번호 입력, 비밀번호 재입력, 두가지 창이 떠서 회원가입이 진행됨.

### Request 

```json
{
	"email":"donghyeon@gmail.com"
}
```



### Response 

```json
{
    "validationResult": true
}
```



### case 1. validationResult : true 

비밀번호 입력하는 창이 떠서 사용자가 비밀번호 입력하면 로그인 진행.

### case 2. validationResult : false

비밀번호 2번 입력창이 떠서 바로 회원가입 진행





## /user/join

사용자 회원가입

### Request 

```json
{
	"email":"donghyeon@gmail.com",
  "password" : "1234"
}
```



### Response 

```json
없음
```





## /whoami

로그인 사용자 정보

### Request 

#### header

```json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiIxIiwic2NvcGUiOlsibW9iaWxlY2xpZW50Il0sImFkZGl0aW9uYWxJbmZvIjoiaGloaWhpaGkiLCJleHAiOjE1NzgwODA3OTQsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJiYWJhYTZiYS1jNzMwLTQ2YzctODNkOS0yMzZhMTU2YTE1ODgiLCJjbGllbnRfaWQiOiJvdXRzdGFncmFtIn0.-ZK1gW9Jb6p96o8GAHLlObvtNkGbEyGaYvPoV0OjvTk
```

#### body 

```json
{
	"email":"donghyeon@gmail.com"
}
```



### Response 

```json
{
    "createdTime": null,
    "modifiedTime": null,
    "id": 1,
    "email": "donghyeon",
    "nickname": null,
    "enabled": true,
    "acceptOptionalBenefitAlerts": false,
    "fcmToken": null,
    "authorities": [
        {
            "authority": "ROLE_USER"
        }
    ],
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true,
    "username": "1"
}
```

예시라서 null값들이 많음.

