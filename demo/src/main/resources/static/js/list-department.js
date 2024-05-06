document.getElementById("addNewDepartment").addEventListener("submit", e => {
    let departmentName = document.getElementById("nameDepart").value;
    let departmentCode = document.getElementById("codeDepart").value;

    if(departmentName === "") {
        alert("Tên khoa không được để trống");
        e.preventDefault();
    } else if(departmentCode === "") {
        alert("Mã khoa không được để trống");
        e.preventDefault();
    }
})

const getInforDepartmentById = id => {
    fetch("/get-department-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("departmentNameInfor").innerText = data.departmentName;
            document.getElementById("departmentCodeInfor").innerText = data.departmentCode;
            document.getElementById("departInforCreateAt").innerText = data.createAt;
            document.getElementById("departInforUpdateAt").innerText = data.updateAt;
        })
        .catch(err => {
            alert("Có lỗi xảy ra, vui lòng thử lại ", err);
        })
}

const clickInforDepartUpdate = id => {
    fetch("/get-department-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("departId").value = data.departmentId;
            document.getElementById("nameDepartUpdate").value = data.departmentName;
            document.getElementById("codeDepartUpdate").value = data.departmentCode;
        })
        .catch(err => alert("Có lỗi xảy ra, vui lòng thử lại ", err))
}

const clickGetModalDepartmentDelete = id => {
    document.getElementById("departIdDelete").value = id;
    document.getElementById("idDepartment").innerText = id;
}

const sendSortDepartmentRequest = () => {
    let option = document.getElementById("sortDepartment").value;
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