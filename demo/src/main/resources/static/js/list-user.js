var reader1 = new FileReader();
reader1.onload = function(r_event) {
    document.getElementById('pre_image').setAttribute('src', r_event.target.result);
}

document.getElementsByName('imageAddNew')[0].addEventListener('change', function(event) {
    reader1.readAsDataURL(this.files[0]);
});

var reader2 = new FileReader();
reader2.onload = function(r_event) {
    document.getElementById('pre_imageUpdate').setAttribute('src', r_event.target.result);
}

document.getElementsByName('imageUpdate')[0].addEventListener('change', function(event) {
    reader2.readAsDataURL(this.files[0]);
});

const sendSortUserRequest = () => {
    let option = document.getElementById("sortUser").value;
    let currentUrl = window.location.href;

    // Kiểm tra xem tham số 'sort' đã tồn tại trong URL hay chưa
    if (currentUrl.includes("&sort=")) {
        // Thay đổi giá trị của 'sort' trong URL
        let newUrl = currentUrl.replace(/&sort=[^&]*/, "&sort=" + option);
        window.location.href = newUrl;
    } else if(currentUrl.includes("?sort=")) {
        let newUrl = currentUrl.replace(/(\?sort=)[^&]*/, "$1" + option);
        window.location.href = newUrl;
    }
    else {
        // Thêm tham số 'sort' vào URL
        let separator = currentUrl.includes("?") ? "&" : "?";
        let newUrl = currentUrl + separator + "sort=" + option;
        window.location.href = newUrl;
    }
}

const clickGetInforUser = id => {
    fetch("/get-user-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("emailInfor").innerText = data.email;
            document.getElementById("userNameInfor").innerText = data.fullName;
            document.getElementById("roleInfor").innerText = data.role.roleName;
            document.getElementById("enableInfor").innerText = data.enable ? "Đang hoạt động" : "Đã bị khoá";
            document.getElementById("imageInfor").src = "/images/user/id-" + data.userId + "/" + data.image;
        })
        .catch(err => {
            alert("Có lỗi xảy ra,vui lòng thử lại");
        })
}

const clickInforUserUpdate = id => {
    fetch("/get-user-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("userIdUpdate").value = data.userId;
            document.getElementById("emailUpdate").value = data.email;
            document.getElementById("nameAccountUpdate").value = data.fullName;
            document.getElementById("roleUpdate").value = data.roleId;
            document.getElementById("pre_imageUpdate").src = "/images/user/id-" + data.userId + "/" + data.image;
            document.getElementById("enableUpdate").value = data.enable;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })

}

const clickGetModalDeleteUser = id => {
    document.getElementById("idUser").innerText = id;
    document.getElementById("userIdDelete").value = id;
}