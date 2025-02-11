<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="ru">
<head>
	<jsp:include page="/assets/head.html" />
	<title>Intercraft</title>
</head>
<body>

<jsp:include page="header.jsp" />

<c:set var="nicknameLabel" value="Введите никнейм" />
<c:if test="${not empty nicknameError}">
    <c:set var="nicknameLabel" value="${nicknameError}" />
</c:if>

<c:set var="passwordLabel" value="Введите пароль" />
<c:if test="${not empty passwordError}">
    <c:set var="passwordLabel" value="${passwordError}" />
</c:if>

<c:set var="nicknameColor" value="normal" />
<c:if test="${not empty nicknameError}">
    <c:set var="nicknameColor" value="red" />
</c:if>

<c:set var="passwordColor" value="normal" />
<c:if test="${not empty passwordError}">
    <c:set var="passwordColor" value="red" />
</c:if>

<div class="container__auth">
	<div class="main__auth">
		<h2 class="text_logo">Вход</h1>
		<form action="/auth" method="POST">
			<div class="form">
				<label class="placeholder-color-${nicknameColor}">${nicknameLabel}</label>
				<input type="login-email" class="form__input" id="nickname-input" name="nickname" placeholder="" value="${param.nickname}">

			</div>

			<div class="form">
				<label class="placeholder-color-${passwordColor}">${passwordLabel}</label>
				<input type="password" class="form__input" id="password-input" name="password" placeholder="" value="">
				<a href="#" class="password-control" data-target="password"></a>
			</div>
			<a href="restore.php" class="restore__password">Забыли пароль?</a>

			<button class="btn__old btn__old__auth" name="do_login" type="submit">Войти</button>
		</form>
	</div>
</div>

</body>
<script src="assets/js/toggle_view.js"></script>
</html>