
function validatePoint(input) {
    let isDelete = false;

    document.getElementById("pointAdd").addEventListener("keydown", (e) => {
        if(e.key === "Delete" || e.key === "Backspace") isDelete = true;
    })

    if(isDelete) {
        isDelete = false;
        return;
    }

    if(isNaN(input.value)) {
        alert("Giá trị nhập vào phải là số");
        input.value = "";
    }
    else if(input.value > 10 || input.value < 0) {
        alert("Điểm phải nằm trong khoảng 0 - 10");
        input.value = "";
    }
}

const sendSortPoint = () => {
    let option = document.getElementById("sortPoint").value;
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


const clickPointInfor = id => {
    fetch("/get-point-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("studenNameInfor").innerText = data.student.studentName;
            document.getElementById("pointProcessInfor").innerText = data.pointProcess;
            document.getElementById("pointFinalInfor").innerText = data.pointFinal;
            document.getElementById("pointLetterInfor").innerText = data.pointLetter;
            document.getElementById("subjectInfor").innerText = data.subject.subjectName;
            document.getElementById("status").innerText = data.status;
            document.getElementById("semesterInfor").innerText = data.semester.semesterName;
            document.getElementById("classCodeInfor").innerText = data.classCode;
            document.getElementById("createAtInfor").innerText = data.createAt;
            document.getElementById("updateAtInfor").innerText = data.updateAt;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })

}

const clickPointInforUpdate = id => {
    fetch("/get-point-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("pointId").value = data.id;
            document.getElementById("studentupdate").value = data.studentId;
            document.getElementById("subjectUpdate").value = data.subjectId;
            document.getElementById("pointProcessUpdate").value = data.pointProcess;
            document.getElementById("pointFinalUpdate").value = data.pointFinal;
            document.getElementById("statusUpdate").value = data.status;
            document.getElementById("semesterUpdate").value = data.semesterId;
            document.getElementById("classCodeUpdate").value = data.classCode;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickModalDeletePoint = id => {
    fetch("/get-point-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("pointIdDelete").value = id;
            document.getElementById("nameSubject").innerText = data.subject.subjectName;
            document.getElementById("nameStudent").innerText = data.student.studentName;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickInforPointFromLecturer = id => {
    fetch("/get-point-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("pointId").value = data.id;
            document.getElementById("semester").innerText = data.semester.semesterName;
            document.getElementById("semesterId").value = data.semesterId;
            document.getElementById("classCode").innerText = data.classCode;
            document.getElementById("classCodeUpload").value = data.classCode;
            document.getElementById("studentName").innerText = data.student.studentName;
            document.getElementById("studentId").value = data.studentId;
            document.getElementById("subjectName").innerText = data.subject.subjectName;
            document.getElementById("subjectId").value = data.subjectId;
            document.getElementById("pointProcessUpdate").value = data.pointProcess;
            document.getElementById("pointFinalUpdate").value = data.pointFinal;
            document.getElementById("statusUpdate").value = data.status;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

