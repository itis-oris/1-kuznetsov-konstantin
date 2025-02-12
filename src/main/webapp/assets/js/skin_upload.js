const skinFile = document.getElementById("skin_file");
const changeSkin = document.getElementById("change_skin");
changeSkin.disabled = true;
const uploadBtn = document.getElementById("uploadbtn__old");

uploadBtn.addEventListener("click", function() {
    skinFile.click();
});

skinFile.addEventListener("change", function() {
    if (skinFile.value) {
        changeSkin.disabled = false;
    } else {
        changeSkin.disabled = true;
    }
});