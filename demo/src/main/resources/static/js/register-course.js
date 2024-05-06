const registerCourse = (button) => {
    const scheduleId = button.getAttribute("scheduleId");
    const subjectId = button.getAttribute("subjectId");
    const studentId = button.getAttribute("studentId");
    const semesterId = button.getAttribute("semesterId");
    const schoolDay = button.getAttribute("schoolDay");
    const timeLearn = button.getAttribute("timeLearn");

    const dataUpload = {
        studentId: studentId,
        subjectId: subjectId,
        semesterId: semesterId,
        schoolDay: schoolDay,
        timeLearn: timeLearn
    }

    fetch("/add-new-timetable?scheduleId=" + scheduleId, {  //POST ở đây là gửi cả @RequestParam và @RequestBody
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dataUpload)
    })
        .then(response => {
            if(response.ok) {
                button.style.display = "none";
                button.nextElementSibling.style.display = "inline-block";
            }
            return response.json();
        })
        .then(data => {
            alert(data.message);
            // window.location.reload();
        })
        .catch(error => {
            console.error("Đã xảy ra lỗi:", error);
            alert("Đăng ký học phần thất bại. Vui lòng thử lại sau.");
        });
}

const unRegisterCourse = button => {

}

