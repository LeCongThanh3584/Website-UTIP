package com.example.demo.Controllers;

import com.example.demo.Services.TimeTable.ITimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TimetableController {

    @Autowired
    private ITimetableService iTimetableService;

    @Autowired
    private StudentController studentController;

   @PostMapping("/add-new-timetable")
    public String addNewTimetable(@RequestParam("classCode") String classCode, @RequestParam("studentId") Integer studentId) {
       String response = iTimetableService.addNewTimeTable(classCode, studentId);
       studentController.setShowToast(true);
       studentController.setMessageResponse(response);
        return "redirect:/student/register-course";
   }

   @PostMapping("/delete-timetable")
    public String deleteTimeTable(@RequestParam("classCode") String classCode, @RequestParam("studentId") Integer studentId) {
       String response = iTimetableService.deleteTimetable(classCode,studentId);
       studentController.setMessageResponse(response);
       studentController.setShowToast(true);
       return "redirect:/student/register-course";
   }
}
