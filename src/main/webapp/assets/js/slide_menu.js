const menuBtn = document.getElementById("open-menu");
const menuBtn2 = document.getElementById("close-menu");
const menu = document.getElementById("menu");

const showSideMenu = () => {
    menu.classList.toggle("show");
};

menuBtn.addEventListener("click", showSideMenu);
menuBtn2.addEventListener("click", showSideMenu);