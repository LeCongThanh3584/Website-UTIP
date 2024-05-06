
const chartLecturer = document.getElementById('chartLecturer');

const chartStudent = document.getElementById("chartStudent");

document.addEventListener("DOMContentLoaded",  () => {
    if (window.location.pathname === "/dash-board/home") {

        Promise.all([
            fetch('/get-quantity-lecturer-chart'),
            fetch('/get-quantity-student-chart'),
        ])
            .then(function(responses) {
                return Promise.all(responses.map(function(response) {
                    return response.json();
                }));
            })
            .then(function(data) {
                let lecturerChart = data[0];
                let studentChart = data[1];

                new Chart(chartLecturer, {
                    type: 'bar',
                    data: {
                        labels: ["Thạc sĩ", "Tiến sĩ", "Giáo sư", "Phó giáo sư"],
                        datasets: [{
                            label: 'Số lượng giảng viên',
                            data: lecturerChart,
                            borderWidth: 1
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });

                new Chart(chartStudent, {
                    type: 'bar',
                    data: {
                        labels: ["Nam", "Nữ", "Học", "Bảo lưu", "Buộc thôi học"],
                        datasets: [{
                            label: 'Số lượng sinh viên',
                            data: studentChart,
                            borderWidth: 1
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });

            })
            .catch(err => alert("Có lỗi xảy ra ở API lấy dữ liệu biểu đồ, vui lòng thử lại", err));
    }
});



