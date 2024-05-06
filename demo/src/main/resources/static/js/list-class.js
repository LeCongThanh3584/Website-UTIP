document.getElementById("addNewClass").addEventListener("submit", function (e) {
    let className = document.getElementById("classname");
    let location = document.getElementById("location");

    if(className.value === "") {
        alert("Tên lớp không được để trống");
        e.preventDefault();
    }
    else if(location.value === "") {
        alert("Tên phòng học không được để trống")
        e.preventDefault();
    }
})

function sendSortRequest() {
    let option = document.getElementById("sortClass").value;
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

const clickInforClass = id => {
   fetch("/get-class-by-id/" + id)
       .then(response => response.json())
       .then(data => {
           console.log(data);
           document.getElementById("nameclassinfor").innerText = data.className;
           document.getElementById("locationinfor").innerText = data.location;
           document.getElementById("sizeinfor").innerHTML = data.size;
           document.getElementById("classInforCreateAt").innerText = data.createAt;
           document.getElementById("classInforUpdateAt").innerText = data.updateAt;

           const listStudent = document.getElementById("listStudent");
           listStudent.innerHTML= "";
           if(data.studentList.length > 0) {
               data.studentList.forEach(item => {
                   const studentItem = document.createElement("li");
                   studentItem.innerText = `${item.studentName} - ${item.studentCode}`;
                   listStudent.appendChild(studentItem);
               });
           }
           else {
               const message = document.createElement("p");
               message.style.color = "red";
               message.innerText = "Không có sinh viên trong lớp này";
               listStudent.appendChild(message);
           }

       })
       .catch(function (err) {
           alert("Có lỗi xảy ra, vui lòng thử lại", err);
       });
}

const clickInforUpdateClass = id => {
   fetch("/get-class-by-id/" + id)
       .then(response => response.json())
       .then(data => {
           document.getElementById("classId").value = data.classId;
           document.getElementById("nameclassupdate").value = data.className;
           document.getElementById("locationupdate").value = data.location;
           document.getElementById("size").value = data.size;
       })
       .catch(err => alert("Có lỗi xảy ra, vui lòng thử lại", err))
}

const clickGetModalClassDelete = id => {
    document.getElementById("idClass").innerText = id;
    document.getElementById("classIdDelete").value = id;
}

