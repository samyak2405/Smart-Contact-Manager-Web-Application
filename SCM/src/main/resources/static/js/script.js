let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", function () {
    changeTheme();
})
console.log(currentTheme);

function changeTheme() {
    console.log(currentTheme);
    document.querySelector('html').classList.add(currentTheme);
    const changeThemeButton = document.querySelector('#theme_change_button');
    changeThemeButton.addEventListener('click', () => {
        console.log("Change theme button clicked");
        document.querySelector('html').classList.remove(currentTheme);
        if (currentTheme === "dark") {
            currentTheme = "light";
        }else{
            currentTheme = "dark";
        }
        setTheme(currentTheme);
        document.querySelector('html').classList.add(currentTheme);
        // changeThemeButton.querySelector('span').textContent = currentTheme == "dark"?"Dark":"Light";
    })
}

function setTheme(theme){
    localStorage.setItem("theme", theme);
}

function getTheme(){
    let theme = localStorage.getItem("theme");
    if (theme == null) {
        return theme;
    }else
        return theme? theme:"light";
}