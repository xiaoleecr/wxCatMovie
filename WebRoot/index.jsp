<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'index.jsp' starting page</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
</head>

<body>
<table>
    <thead>
    <tr>
        <td>类别</td>
        <td>值</td>
    </tr>
    </thead>
    <tbody>
    <%
    String[] array = { "google","google","google","runoob","runoob","runoob","taobao"};
    request.setAttribute("array", array);
    %>
    <c:set var="count" value="1"></c:set>
    <c:set var="mergeIndex" value="0"></c:set>
    <c:forEach items="${array}" var="name" varStatus="i" >
        <c:set var="nextName" value="${array[i.count]}"></c:set>
        <c:choose>
            <c:when test="${ name != nextName}">
                <c:set var="flag" value="true"></c:set>
                <c:forEach begin="${mergeIndex}" end="${count-1}" items="${array}" var="n">
                    <c:choose>
                        <c:when test="${flag}">
                            <tr>
                                <td rowspan="${count-mergeIndex}">${mergeIndex}${n}${count}</td>
                                <td>${n}${count}</td>
                            </tr>
                            <c:set var="flag" value="false"></c:set>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td>${name}${count}</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:set var="mergeIndex" value="${count}"></c:set>
                <c:set var="count" value="${count+1}"></c:set>
            </c:when>
            <c:otherwise>
                <c:set var="count" value="${count+1}"></c:set>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
