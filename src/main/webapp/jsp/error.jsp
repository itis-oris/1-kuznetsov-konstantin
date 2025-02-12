<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <jsp:include page="/assets/head.html" />
    <title>Ошибка ${statusCode}</title>
    <style>
        .error-container {
            border: 1px solid #dee2e6;
            border-radius: 0.5rem;
            padding: 2rem;
            background-color: #ffffff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .error-code {
            font-size: 5rem;
            font-weight: bold;
            color: #dc3545;
        }
    </style>
</head>
<body class="container d-flex justify-content-center align-items-center vh-100">
    <div class="text-center p-4 bg_dark rounded col-lg-6 col-md-8 col-12">
        <div class="error-code">${statusCode}</div>
        <h2>Что-то пошло не так</h2>
        <p>${errorMessage}</p>
        <a href="/" class="btn btn__old">Вернуться</a>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
