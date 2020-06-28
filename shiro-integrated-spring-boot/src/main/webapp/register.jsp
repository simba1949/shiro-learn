<%--
  Created by IntelliJ IDEA.
  User: Theodore
  Date: 2020/6/27
  Time: 20:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册</title>
</head>
<body>
    <h1>用户注册</h1>
    <form action="${pageContext.request.contextPath}/user/register" method="post">
        账户：<input type="text" name="username"><br>
        密码：<input type="password" name="password"><br>
        <input type="submit" value="注册">
    </form>
</body>
</html>
