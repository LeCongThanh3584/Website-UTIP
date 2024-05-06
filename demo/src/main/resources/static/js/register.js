document.getElementById("submit-register").addEventListener("submit", function (e) {
    let email = document.getElementById("typeEmail").value;
    let password = document.getElementById("typePassword1").value;
    let reenter_password = document.getElementById("typePassword2").value;

    if(email === "" || password === "" || reenter_password === "") {
        alert("Các trường không được để trống");
        e.preventDefault()
    }else if(password !== reenter_password) {
        alert("Mật khẩu và xác nhận mật khẩu phải giống nhau");
        e.preventDefault()
    }
})