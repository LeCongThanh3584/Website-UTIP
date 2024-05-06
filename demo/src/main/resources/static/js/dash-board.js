document.addEventListener("DOMContentLoaded", function() {
    // Lấy tất cả các tuỳ chọn menu
    const menuItems = document.querySelectorAll(".list-group-item");

    // Lặp qua từng tuỳ chọn
    menuItems.forEach(function(item) {
        // Thêm sự kiện click cho mỗi tuỳ chọn
        item.addEventListener("click", function() {
            // Xóa lớp "active" từ tất cả các tuỳ chọn
            menuItems.forEach(function(menuItem) {
                menuItem.classList.remove("active");
            });

            // Thêm lớp "active" cho tuỳ chọn được chọn
            item.classList.add("active");
        });
    });
});

document.addEventListener("keyup", e => {
    if(e.key === "/") {
        e.preventDefault();
        document.getElementById("search").focus();
    }
})

var reader = new FileReader();
reader.onload = function(r_event) {
    document.getElementById('pre_img').setAttribute('src', r_event.target.result);
}

document.getElementsByName('imageUser')[0].addEventListener('change', function(event) {
    reader.readAsDataURL(this.files[0]);
});
