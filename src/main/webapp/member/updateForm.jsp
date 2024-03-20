<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리 시스템 회원수정 페이지</title>
<link href="${pageContext.request.contextPath}/css/join.css" type="text/css" rel="stylesheet">
<style>
h3 {
	text-align: center; color: #1a92b9;
}
input[type=file] {
	display: none;
}
</style>
</head>
<body>
<jsp:include page="../board/header.jsp" />
<form name="joinform" action="updateProcess.net" method="post" enctype="multipart/form-data">
	<h3>회원 정보 수정</h3>
	<hr>
	<b>아이디</b>
	<input type="text" name="id" value="${memberinfo.id}" readonly>
	<b>비밀번호</b>
	<input type="password" name="pass" value="${memberinfo.password}" readonly>
	<b>이름</b>
	<input type="text" name="name" value="${memberinfo.name}" placeholder="Enter name" required>
	<b>나이</b>
	<input type="text" name="age" value="${memberinfo.age}" maxlength="2" 
			placeholder="Enter age" required>
	<b>성별</b>
		<div>
			<input type="radio" name="gender" value="남"><span>남자</span>
			<input type="radio" name="gender" value="여"><span>여자</span>
		</div>		
	<b>이메일 주소</b>
		<input type="text" name="email" value="${memberinfo.email}" maxlength="2" 
				placeholder="Enter email" required><span id="email_message"></span>
</form>
</body>
</html>