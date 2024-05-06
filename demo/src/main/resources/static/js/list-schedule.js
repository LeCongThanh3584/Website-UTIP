const clickInForSchedule = id => {
    fetch("/get-schedule-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("subjectNameInfor").innerText = data.subject.subjectName;
            document.getElementById("timeLearnInfor").innerText = data.timeLearn;
            document.getElementById("schoolDayInfor").innerText = data.schoolDay;
            document.getElementById("semesterInfor").innerText = data.semester.semesterName;
            document.getElementById("createAtInfor").innerText = data.createAt;
            document.getElementById("maxRegisterInfor").innerText = data.maxRegister;
            document.getElementById("registeredInfor").innerText = data.registered;
            document.getElementById("classCodeInfor").innerText = data.classCode;
            document.getElementById("locationInfor").innerText = data.location;
            document.getElementById("regisStartTimeInfor").innerText = data.regisStartTime;
            document.getElementById("regisEndTimeInfor").innerText = data.regisEndTime;
            document.getElementById("updateAtInfor").innerText = data.updateAt;
        })
        .catch((err) => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickInforUpdateSchedule = id => {
    fetch("/get-schedule-by-id/" + id)
        .then(response => response.json())
        .then(data => {
            document.getElementById("scheduleId").value = data.scheduleId;
            document.getElementById("studentUpdate").value = data.subjectId;
            document.getElementById("timeLearnUpdate").value = data.timeLearn;
            document.getElementById("schoolDayUpdate").value = data.schoolDay;
            document.getElementById("semesterUpdate").value = data.semesterId;
            document.getElementById("maxRegisterUpdate").value = data.maxRegister;
            document.getElementById("classCodeUpdate").value = data.classCode;
            document.getElementById("locationUpdate").value = data.location;

            let YMD = convertDMYToYMD(data.regisStartTime);
            document.getElementById("regisStartTimeUpdate").value = YMD;

            YMD = convertDMYToYMD(data.regisEndTime);
            document.getElementById("regisEndTimeUpdate").value = YMD;
        })
        .catch((err) => {
            alert("Có lỗi xảy ra, vui lòng thử lại", err);
        })
}

const clickModalDeleteSchedule = id => {
    document.getElementById("scheduleDeleteId").value = id;
    document.getElementById("idSchedule").innerText = id;
}

const convertDMYToYMD = DMY => {
    const arr = DMY.split("-");
    return arr[2] + "-" + arr[1] + "-" + arr[0];
}