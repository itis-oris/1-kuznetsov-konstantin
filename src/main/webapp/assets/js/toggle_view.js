const oldPasswordControl = document.querySelector('.password-control[data-target="oldPassword"]');
const passwordControl = document.querySelector('.password-control[data-target="password"]');
const password2Control = document.querySelector('.password-control[data-target="password2"]');

if (oldPasswordControl) {
    oldPasswordControl.addEventListener("click", function() {
        togglePasswordVisibility('oldPassword-input', this);
    });
}

if (passwordControl) {
    passwordControl.addEventListener("click", function() {
        togglePasswordVisibility('password-input', this);
    });
}

if (password2Control) {
    password2Control.addEventListener("click", function() {
        togglePasswordVisibility('password2-input', this);
    });
}

function togglePasswordVisibility(inputId, target) {
    var input = document.getElementById(inputId);
    if (input.getAttribute('type') === 'password') {
        target.classList.add('view');
        input.setAttribute('type', 'text');
    } else {
        target.classList.remove('view');
        input.setAttribute('type', 'password');
    }
    return false;
}