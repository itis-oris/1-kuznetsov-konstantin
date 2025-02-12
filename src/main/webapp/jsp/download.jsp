<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
	<jsp:include page="/assets/head.html" />
	<title>Intercraft</title>

</head>
<body>

<jsp:include page="header.jsp" />

<div class="d-flex flex-column justify-content-between" style="height:80vh">
	<div class="container text-center my-auto">
		<h2 class="mb-2">Выберите вашу платформу для скачивания:</h2>
		<div class="row justify-content-center">
			<div class="col-lg-4 col-md-6 col-12">
				<img src="assets/img/windows.png" class="download__logo my-2">
				<a class="text-simple-white btn btn__old-1 my-2" href="/launcher/IntercraftLauncher-setup.exe"><span class="h4">Windows</span></a>
			</div>
			<div class="col-lg-4 col-md-6 col-12">
				<img src="assets/img/linuxwhite.png" class="download__logo my-2">
				<a class="text-simple-white btn btn__old-2 my-2" href="download?file=linux"><span class="h4">Linux</span></a>
			</div>
			<div class="col-lg-4 col-md-6 col-12">
				<img src="assets/img/apple.png" class="download__logo my-2">
				<a class="text-simple-white btn btn__old-3 my-2" href="download?file=macos"><span class="h4">Mac OS</span></a>
			</div>
		</div>
	</div>
</div>

</body>
</html>