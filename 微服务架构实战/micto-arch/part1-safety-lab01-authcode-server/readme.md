#获取token
- localhost:8080/oauth/authorize?client_id=clientapp&
  redirect_url=http://localhost:9001/callback&response_type=
  code&scope=read_userinfo
- localhost:8080/api/userinfo


####bug
- org.springframework.security.authentication.InsufficientAuthenticationException: User must be authenticated with Spring Security before authorization can be completed.