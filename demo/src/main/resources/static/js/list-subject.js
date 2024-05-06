
const sendSortSubjectRequest = () => {
    let option = document.getElementById("sortSubject").value;
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
const clickInforSubject = id => {
    fetch("/get-subject-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("subjectNameInfor").innerText = data.subjectName;
            document.getElementById("courseCreditInfor").innerText = data.courseCredits;
            document.getElementById("subjectCodeInfor").innerText = data.subjectCode;
            document.getElementById("departmentInfor").innerText = data.department.departmentName;
            document.getElementById("lecturerInfor").innerText = data.lecturer.lecturerName;
            document.getElementById("descriptionInfor").innerHTML = data.description;
            document.getElementById("timeCreateInfor").innerHTML = data.createAt;
            document.getElementById("timeUpdateInfor").innerHTML = data.updateAt;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickInforSubjectFromLecturer = id => {
    fetch("/get-subject-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("subjectNameInfor").innerText = data.subjectName;
            document.getElementById("courseCreditInfor").innerText = data.courseCredits;
            document.getElementById("subjectCodeInfor").innerText = data.subjectCode;
            document.getElementById("descriptionInfor").innerHTML = data.description;
            document.getElementById("departmentInfor").innerHTML = data.department.departmentName;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickInforUpdateSubject = id => {
    fetch("/get-subject-by-id/" + id)
        .then(respnse => respnse.json())
        .then(data => {
            document.getElementById("subjectIdUpdate").value = data.subjectId;
            document.getElementById("nameSubjectUpdate").value = data.subjectName;
            document.getElementById("subjectCodeUpdate").value = data.subjectCode;
            document.getElementById("courseCreditsUpdate").value = data.courseCredits;
            document.getElementById("lecturerUpdate").value = data.lecturerId;
            document.getElementById("departmentUpdate").value = data.departmentId;

            //Gán giá trị cho textarea băng summernote
            $("#descriptionUpdate").summernote('code', data.description);

        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })

}

const clickModalDeleteSubject = id => {
    document.getElementById("subjectIdDelete").value = id;
    document.getElementById("idSubject").innerText = id;
}