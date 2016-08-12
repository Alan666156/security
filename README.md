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