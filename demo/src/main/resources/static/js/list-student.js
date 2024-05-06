var reader_1 = new FileReader();
reader_1.onload = function(r_event) {
    document.getElementById('pre_image').setAttribute('src', r_event.target.result);
}

document.getElementsByName('imageStudent')[0].addEventListener('change', function(event) {
    reader_1.readAsDataURL(this.files[0]);
});

var reader_2 = new FileReader();

reader_2.onload = function(r_event) {
    document.getElementById('imageStudentUpdate').setAttribute('src', r_event.target.result);
}

document.getElementsByName('imageStudentUpdate')[0].addEventListener('change', function(event) {
    reader_2.readAsDataURL(this.files[0]);
});

//Các đoạn code trên có thể giống nhau, nhưng không dùng chung được, bởi vì làm như vậy sẽ bị ghi đè code, khiến code chạy k đúng

function validatePhoneNumber(input) {
    // Loại bỏ các ký tự không phải số từ giá trị của input
    input.value = input.value.replace(/[^0-9]/g, '');

    if(input.value.length > 10) {
        alert("Số điện thoại không hợp lệ")
    }
}

const clickInfoStudent = id => {
    fetch("/get-student-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("namestudentinfor").innerText = data.studentName;
            document.getElementById("addressinfor").innerText = data.address;
            document.getElementById("studentCodeInfor").innerText = data.studentCode;
            document.getElementById("phonenumberinfor").innerText = data.phoneNumber;
            document.getElementById("genderinfor").innerText = data.gender;
            document.getElementById("studentClassInfor").innerText = data.studentClass.className;
            document.getElementById("departStudentInfor").innerText = data.department.departmentName;
            document.getElementById("studentDOBInfor").innerText = data.dateOfBirth;
            document.getElementById("emailStudentInfor").innerText = data.email;
            document.getElementById("creditsAccumulate").innerText = data.creditsAccumulate;
            document.getElementById("creditsOwe").innerText = data.creditsOwe;
            document.getElementById("CPA").innerText = data.cumulativeGPA;
            document.getElementById("statusLearnStudentInfor").innerText = data.statusLearn;
            document.getElementById("enable").innerText = data.enable ? "Đang hoạt động" : "Đã bị khoá";
            document.getElementById("studentImageInfor").src = "/images/student/id-" + data.studentId + "/" + data.image;
            document.getElementById("studentInforCreateAt").innerText = data.createAt;
            document.getElementById("studentInforUpdateAt").innerText = data.updateAt;

        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại: ", err);
        })
}

const clickInfoUpdateStudent = id => {
    fetch("/get-student-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            document.getElementById("studentId").value = data.studentId
            document.getElementById("nameStudentUpdate").value = data.studentName;
            document.getElementById("addressStudentUpdate").value = data.address;
            document.getElementById("studentCodeUpdate").value = data.studentCode;
            document.getElementById("phonenumberStudentUpdate").value = data.phoneNumber;
            document.getElementById("genderStudentUpdate").value = data.gender;
            document.getElementById("classStudentUpdate").value = data.classId;
            document.getElementById("depaertmentStudentUpdate").value = data.departmentId;
            document.getElementById("emailStudentUpdate").value = data.email;
            document.getElementById("statusLearnUpdate").value = data.statusLearn;
            document.getElementById("enableStudentUpdate").value = data.enable;

            let partDate = data.dateOfBirth.split("-");  //Format date từ dd-MM-yyyy sang yyyy-MM-dd
            let formatDate = partDate[2] + "-" + partDate[1] + "-" + partDate[0];
            document.getElementById("studentDOBUpdate").value = formatDate;

            document.getElementById("imageStudentUpdate").src = "/images/student/id-" + data.studentId + "/" + data.image;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại ", err);
        })
}

const clickModalDeleteStudent = id => {
    document.getElementById("studentIdDelete").value = id;
    document.getElementById("idStudent").innerText = id;
}

const sendSortStudent = () => {
    let option = document.getElementById("sortStudent").value;
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
