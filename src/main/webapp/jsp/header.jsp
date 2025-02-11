<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>

<header class="p-3 header__big">
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
            <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                <img class="bi me-2" width="32" height="32" role="img" aria-label="Bootstrap" src="assets/img/favicon.ico">
            </a>

            <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                <li><a href="/" class="nav-link px-2 nav__link">Главная</a></li>
                <li><a href="download" class="nav-link px-2 nav__link">Скачивание</a></li>
                <li><a href="contacts" class="nav-link px-2 nav__link">Ссылки</a></li>
            </ul>

            <div class="text-end">
                <c:if test="${empty player}">
                    <div class="text-end">
                        <a href="register" class="btn btn-outline-light me-2">Регистрация</a>
                        <a href="auth" class="btn btn-outline-light">Вход</a>
                    </div>
                </c:if>
                <c:if test="${not empty player}">
                    <!--<a href="lk" class="btn btn-outline-light me-2">${player.nickname}</a>
                    <a href="logout" class="btn btn-outline-light nav__out">Выйти</a>-->

                    <div class="dropdown">
                        <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                            <img src="skins/head/${player.activeSkin.filePath}" alt="" width="32" height="32" class="rounded-circle me-2">
                            <strong> ${player.nickname} </strong>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-dark text-small shadow">
                            <li><a class="dropdown-item" href="lk">Личный кабинет</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="logout">Выйти</a></li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</header>

<c:if test="${not empty error}">
    <script src="assets/js/error_vanish.js"></script>
    <div class="container align-items-center d-flex justify-content-center">
        <div id="errorToast" class="toast  mt-3 text-bg-danger border-0 px-4" role="alert" aria-live="assertive" aria-atomic="true" style="">
            <div class="d-flex justify-content-center">
                <div class="toast-body fs-4">
                    ${error}
                </div>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </div>
</c:if>

<div class="menu d-flex flex-column flex-shrink-0 p-3" id="menu" style="width: 280px; height:100%; background-color:#343a4d">
    <div class="d-flex align-items-center mb-0 mb-md-0 me-md-auto text-white text-decoration-none">
        <button name="close-menu" id="close-menu" type="submit" class="open-menu">
            <img class="bi pe-none me-2" width="40" height="40" src="assets/img/left-arrow.png">
        </button>
        <span class="fs-4" >Навигация</span>
    </div>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <a href="/" class="nav-link text-white d-flex align-items-center" aria-current="page">
                <img class="bi pe-none me-2" width="16" height="16" src="assets/img/home.png">
                Главная
            </a>
        </li>
        <li>
            <a href="download" class="nav-link text-white d-flex align-items-center">
                <img class="bi pe-none me-2" width="16" height="16" src="assets/img/downloading.png">
                Скачивание
            </a>
        </li>
        <li>
            <a href="contacts" class="nav-link text-white d-flex align-items-center">
                <img class="bi pe-none me-2" width="16" height="16" src="assets/img/external-link.png">
                Ссылки
            </a>
        </li>
    </ul>
    <hr>
    <c:if test="${empty player}">
        <div class="text-end">
            <a href="register" class="btn btn-outline-light me-2">Регистрация</a>
            <a href="auth" class="btn btn-outline-light">Вход</a>
        </div>
    </c:if>
    <c:if test="${not empty player}">
        <div class="dropdown">
            <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                <img src="skins/head/${player.activeSkin.filePath}" alt="" width="32" height="32" class="rounded-circle me-2">
                <strong> ${player.nickname} </strong>
            </a>
            <ul class="dropdown-menu dropdown-menu-dark text-small shadow">
                <li><a class="dropdown-item" href="lk">Личный кабинет</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="logout">Выйти</a></li>
            </ul>
        </div>
    </c:if>
</div>

<header class="p-3 header__small">
    <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-lg-start justify-content-between">

            <noscript><form method="POST" action="" class=""></noscript>
                <button name="open-menu" id="open-menu" type="submit" class="open-menu">
                    <img class="bi me-2" width="32" height="32" role="img" aria-label="Bootstrap" src="assets/img/menu.png">
                </button>
            <noscript></form></noscript>

            <div class="text-end">
            </div>
        </div>
    </div>
</header>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>
<script src="assets/js/slide_menu.js"></script>