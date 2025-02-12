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

<c:set var="password2Label" value="Повторите пароль" />
<c:if test="${not empty password2Error}">
    <c:set var="password2Label" value="${password2Error}" />
</c:if>

<c:set var="nicknameColor" value="normal" />
<c:if test="${not empty nicknameError}">
    <c:set var="nicknameColor" value="red" />
</c:if>

<c:set var="passwordColor" value="normal" />
<c:if test="${not empty passwordError}">
    <c:set var="passwordColor" value="red" />
</c:if>

<c:set var="password2Color" value="normal" />
<c:if test="${not empty password2Error}">
    <c:set var="password2Color" value="red" />
</c:if>

<div class="container__reg">
	<div class="main__reg">
		<h2 class="text_logo">Регистрация</h1>
		<form action="/register" method="POST">
			<div class="form">
				<label class="placeholder-color-${nicknameColor}">${nicknameLabel}</label>
				<input type="login-email" class="form__input" id="nickname-input" name="nickname" placeholder="" value="${param.nickname}">

			</div>

			<div class="form">
				<label class="placeholder-color-${passwordColor}">${passwordLabel}</label>
				<input type="password" class="form__input" id="password-input" name="password" placeholder="" value="${param.password}">
				<a href="#" class="password-control" data-target="password"></a>
			</div>

			<div class="form">
				<label class="placeholder-color-${password2Color}">${password2Label}</label>
				<input type="password" class="form__input" id="password2-input" name="password2" placeholder="" value="">
				<a href="#" class="password-control" data-target="password2"></a>
			</div>
			<button class="btn__old" name="do_signup" type="submit">Регистрация</button>
			<!-- <label class="info"><?php echo $info; ?></label> -->
		</form>
	</div>
</div>

</body>
<script src="assets/js/toggle_view.js"></script>
</html>