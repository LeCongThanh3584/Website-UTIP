var reader_3 = new FileReader();
reader_3.onload = function(r_event) {
    document.getElementById('pre_imageAddnew').setAttribute('src', r_event.target.result);
}

document.getElementsByName('imageAddnew')[0].addEventListener('change', function(event) {
    reader_3.readAsDataURL(this.files[0]);
});

var reader_4 = new FileReader();
reader_4.onload = function(r_event) {
    document.getElementById('pre_imageUpdate').setAttribute('src', r_event.target.result);
}

document.getElementsByName('imageUpdate')[0].addEventListener('change', function(event) {
    reader_4.readAsDataURL(this.files[0]);
});

const clickInforLecturer = id => {
    fetch("/get-lecturer-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            document.getElementById("nameLecturerInfor").innerText = data.lecturerName;
            document.getElementById("emailLecturerInfor").innerText = data.email;
            document.getElementById("dobLecturerInfor").innerText = data.dateOfBirth;
            document.getElementById("genderLecturerInfor").innerText = data.gender;
            document.getElementById("addressLecturerInfor").innerText = data.address;
            document.getElementById("phomeNumberLecturerInfor").innerText = data.phoneNumber;
            document.getElementById("codeLecturerInfor").innerText = data.lecturerCode;
            document.getElementById("levelInfor").innerText = data.level;
            document.getElementById("depaertmentInfor").innerText = data.department.departmentName;
            document.getElementById("enableLecturer").innerText = data.enable ? "Đang hoạt động" : "Đã bị khoá";
            document.getElementById("imageLecturerInfor").src = "/images/lecturers/id-" + id + "/" + data.image;
            document.getElementById("lecturerCreateAt").innerText = data.createAt;
            document.getElementById("lecturerUpdateAt").innerText = data.updateAt;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickInforLecturerUpdate = id => {
    fetch("/get-lecturer-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("lecturerIdUpdate").value = data.lecturerId;
            document.getElementById("nameLecturerUpdate").value = data.lecturerName;
            document.getElementById("lecturerCodeUpdate").value = data.lecturerCode;
            document.getElementById("emailLecturerUpdate").value = data.email;
            document.getElementById("phomeNumberLecturerUpdate").value = data.phoneNumber;
            document.getElementById("addressLecturerUpdate").value = data.address;

            let partDate = data.dateOfBirth.split("-");
            document.getElementById("dobLecturerUpdate").value = partDate[2] + "-" + partDate[1] + "-" + partDate[0];
            document.getElementById("genderLecturerUpdate").value = data.gender;
            document.getElementById("levelUpdate").value = data.level;
            document.getElementById("departmentUpdate").value = data.departmentId;
            document.getElementById("enableLecturerUpdate").value = data.enable;
            document.getElementById("pre_imageUpdate").src = "/images/lecturers/id-" + id + "/" + data.image;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickModalDeleteLecturer = id => {
    document.getElementById("lecturerIdDelete").value = id;
    document.getElementById("idLecturer").innerText = id;
}

const sendSortLecturer = () => {
    let option = document.getElementById("sortLecturer").value;
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