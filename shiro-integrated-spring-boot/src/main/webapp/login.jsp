<%--
  Created by IntelliJ IDEA.
  User: Theodore
  Date: 2020/6/27
  Time: 19:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录界面</title>
</head>
<body>
    <h1>用户登录</h1>
    <form action="${pageContext.request.contextPath}/user/login" method="post">
        账户：<input type="text" name="username"><br>
        密码：<input type="password" name="password"><br>
        <input type="submit" value="登录">
    </form>
</body>
</html>
