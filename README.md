# security
spring boot + spring security

#Oauth2.0 Authorization Server
Authorization Server：授权服务器，能够成功验证资源拥有者和获取授权，并在此之后分发令牌的服务器；
Resource Server：资源服务器，存储用户的数据资源，能够接受和响应受保护资源请求的服务器；
Client：客户端，获取授权和发送受保护资源请求的第三方应用；
Resource Owner：资源拥有者，能够对受保护资源进行访问许可控制的实体；
Protected Resource：受保护资源，能够使用OAuth请求获取的访问限制性资源；
Authorization Code：授权码；
Refresh Token：刷新令牌；
Access Token：访问令牌。

#OAuth2.0 的工作流程简述如下：
(1) 客户端从资源拥有者（最终用户）那里请求授权。授权请求能够直接发送给资源拥有者，或者间接的通过授权服务器发送请求；
(2) 资源拥有者为客户端授权，给客户端发送一个访问许可(Authorization Code)；
(3) 客户端出示自己的私有证书(client_id 和 client_secret)和上一步拿到的访问许可，来向授权服务器请求一个访问令牌；
(4) 授权服务器验证客户端的私有证书和访问许可的有效性，如果验证有效，则向客户端发送一个访问令牌，访问令牌包括许可的作用域、有效时间和一些其他属性信息；
(5) 客户端出示访问令牌向资源服务器请求受保护资源；
(6) 资源服务器对访问令牌做出响应

cas-client-core利用HashMapBackedSessionMappingStorage实现了ST和session的内存存储

#cas logout

在CAS的单点注销下，原理是CAS在注销模块中，通过使用各个业务系统认证时所提供的URL做为路径逐个再去反向访问各个业务系统，发送的是POST请求，那么各个业务系统的CAS-CLIENT接受到该请求后，从请求内容中分解出票据，然后依据票据找到之前已经保存好的session，最后执行session.invalidate方法使其失效。原理很简单，在一般业务系统下也很正常，但如果业务系统使用了spring-security，则会出现问题，原因是spring-security中有一个默认配置是防止会话伪造的功能为开启，开启此功能后，用户的当前会话会被撤销从而产生一个新的会话，这时CAS-CLIENT在做撤销处理时就无法得到票据所对象的session对象了。
解决方法：
Spring Security默认就会启用session-fixation-protection，这会在登录时销毁用户的当前session，然后为用户创建一个新session，并将原有session中的所有属性都复制到新session中。
如果希望禁用session-fixation-protection，可以在http中将session-fixation-protection设置为none。 

session-fixation-protection的值共有三个可供选择，none，migrateSession和newSession。默认使用的是migrationSession，如同我们上面所讲的，它会将原有session中的属性都复制到新session中。上面我们也见到了使用none来禁用session-fixation功能的场景，最后剩下的newSession会在用户登录时生成新session，但不会复制任何原有属性。