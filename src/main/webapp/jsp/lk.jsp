<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="/assets/head.html" />
	<title>Intercraft</title>
</head>
<body>

<jsp:include page="header.jsp" />

<c:set var="Label" value="Смена пароля"/>
<c:if test="${not empty success}">
    <c:set var="Label" value="${success}" />
</c:if>

<c:set var="oldPasswordLabel" value="Старый пароль" />
<c:if test="${not empty oldPasswordError}">
    <c:set var="oldPasswordLabel" value="${oldPasswordError}" />
</c:if>

<c:set var="passwordLabel" value="Новый пароль" />
<c:if test="${not empty passwordError}">
    <c:set var="passwordLabel" value="${passwordError}" />
</c:if>

<c:set var="password2Label" value="Повторите пароль" />
<c:if test="${not empty password2Error}">
    <c:set var="password2Label" value="${password2Error}" />
</c:if>

<c:set var="oldPasswordColor" value="normal" />
<c:if test="${not empty oldPasswordError}">
    <c:set var="oldPasswordColor" value="red" />
</c:if>

<c:set var="passwordColor" value="normal" />
<c:if test="${not empty passwordError}">
    <c:set var="passwordColor" value="red" />
</c:if>

<c:set var="password2Color" value="normal" />
<c:if test="${not empty password2Error}">
    <c:set var="password2Color" value="red" />
</c:if>

<div class="container">
    <div class="row justify-content-center">
        <ul class="nav nav-tabs col-12 justify-content-center" id="myTab" role="tablist">
            <li class="nav-item col-4 text-center" role="presentation">
                <a class="nav-link text-simple-white ${tabSkins} color1" id="tab1-tab" data-bs-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">Скины</a>
            </li>
            <li class="nav-item col-4 text-center" role="presentation">
                <a class="nav-link text-simple-white ${tabPayments} color2" id="tab2-tab" data-bs-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">Кошелёк</a>
            </li>
            <li class="nav-item col-4 text-center" role="presentation">
                <a class="nav-link text-simple-white ${tabSettings} color3" id="tab3-tab" data-bs-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">Настройки</a>
            </li>
        </ul>
    </div>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane fade ${tabSkins} text-center" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <h2 class="pt-4 pb-2">Текущий скин:</h2>
            <div class = "row align-items-center justify-content-center">
                <img src="skins/preview/${player.activeSkin.filePath}?t=" class="col-lg-6 col-12 mx-auto mb-4 py-4 bg-secondary rounded">
                <div class="mx-auto col mb-4">
                    <button class="btn__old btn__old-1 mx-auto" name="uploadbtn__old" id="uploadbtn__old">Выбрать файл</button>
                    <form class="mx-auto" action="/lk" method="post" enctype="multipart/form-data">
                        <input type="file" name="skin_file" id="skin_file" hidden>
                        <button class="btn__old btn__old-2 mx-auto" name="change_skin" id="change_skin" type="submit">Сохранить скин</button>
                    </form>
                </div>
            </div>
            <c:set var="allSkins" value="${player.allSkins}" />
            <h2 class="">История скинов:</h2>
            <div class="row g-3">
                <c:forEach var = 'skin' items="${allSkins}" >
                    <form class="col-md-6 col-xl-4 col-12" action="/lk" method="post">
                        <div class = "bg-secondary rounded my-2 py-2">
                            <input type="hidden" name="id" value="${skin.skinId}" />
                            <img src="skins/preview/${skin.filePath}?t=" class="col-12">
                            <div class="dropdown d-flex justify-content-center mt-2">
                                <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                    <strong> Действия </strong>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-dark text-small shadow">
                                    <li><button type="submit" name="action" value="activate" class="dropdown-item">Сделать активным</button></li>
                                    <li><a href="/skins/skin/${skin.filePath}" download="${skin.filePath}" class="dropdown-item">Скачать</a></li>
                                    <c:if test = "${fn:length(allSkins) > 1}">
                                        <li><button type="submit" name="action" value="delete" class="dropdown-item">Удалить</button></li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </form>
                </c:forEach>
            </div>
        </div>
        <div class="tab-pane fade ${tabPayments}" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
                    <div class="container mt-5">
                        <div class="row d-flex justify-content-center">
                            <div class="col-lg-6 mb-4 ">
                                <div class="bg_dark p-3 mb-3">
                                    <h4 class="text-center mb-4 mt-2">Информация о балансе</h4>
                                    <p class="mb-1">Баланс: <span style='color:#31F7B2' id="balance">${player.stringBalance}</span></p>
                                    <p>Количество оплаченнего времени: <span style='color:#31F7B2' id="paidDays">${player.expirationTime}</span></p>
                                </div>
                                <div class="bg_dark p-3 mb-3 text-center">
                                    <h4 class="mb-3">Пополнение баланса</h4>
                                    <form method="POST">
                                        <div class="mb-3">
                                            <input type="text" class="form__input" id="amount" name="amount" placeholder="Сумма" inputmode="numeric" pattern="[0-9]*" required>
                                        </div>
                                        <button type="submit" name="top_up_balance" class="btn btn__old mt-4">Пополнить баланс</button>
                                    </form>
                                </div>
                                <div class="bg_dark p-3 text-center">
                                    <form method="POST">
                                        <p class="mt-2 mb-0">Стоимость: <span style='color:#31F7B2'>${costOfPassage}р</span></p>
                                        <p class="m-0">Длительность: неделя</p>
                                        <button type="submit" name="buy_passage" class="btn btn__old mt-4">Купить проходку</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        <div class="tab-pane fade ${tabSettings}" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
            <div class="container__main row d-flex align-items-center justify-content-center" style="height:70vh">
            	<div class="col-md-6 col-xl-4 col-8 text-center bg_dark py-3">
            		<h2 class="">${Label}</h1>
            		<form action="" class="text-right" method="POST">
            			<div class="form">
            				<label class="placeholder-color-${oldPasswordColor}">${oldPasswordLabel}</label>
            				<input type="password" class="form__input" id="oldPassword-input" name="oldPassword" placeholder="" value="">
            				<a href="#" class="password-control" data-target="oldPassword"></a>

            			</div>
            			<div class="form">
            				<label class="placeholder-color-${passwordColor}">${passwordLabel}</label>
            				<input type="password" class="form__input" id="password-input" name="password" placeholder="" value="">
            				<a href="#" class="password-control" data-target="password"></a>
            			</div>
            			<div class="form">
                            <label class="placeholder-color-${password2Color}">${password2Label}</label>
                            <input type="password" class="form__input" id="password2-input" name="password2" placeholder="" value="">
                            <a href="#" class="password-control" data-target="password2"></a>
                        </div>
            			<button class="btn__old btn__old__auth" name="do_change_password" type="submit">Сменить</button>
            		</form>
            	</div>
            </div>
        </div>
    </div>


</div>
<script src="assets/js/skin_upload.js"></script>
<script src="assets/js/toggle_view.js"></script>
</body>
</html>