document.addEventListener("DOMContentLoaded", function() {
    var toastEl = document.getElementById("errorToast");
    var toast = new bootstrap.Toast(toastEl, { delay: 5000 });
    toast.show();
});