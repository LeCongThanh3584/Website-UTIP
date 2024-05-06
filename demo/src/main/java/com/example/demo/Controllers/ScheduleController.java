package com.example.demo.Controllers;

import com.example.demo.DTOs.ScheduleDTO;
import com.example.demo.DTOs.SemesterDTO;
import com.example.demo.DTOs.SubjectDTO;
import com.example.demo.Entities.Schedule;
import com.example.demo.Mapper.ScheduleMapper;
import com.example.demo.Mapper.SemesterMapper;
import com.example.demo.Mapper.SubjectMapper;
import com.example.demo.Services.Schedule.IScheduleService;
import com.example.demo.Services.Semester.ISemesterService;
import com.example.demo.Services.User.IUserService;
import com.example.demo.Services.subject.ISubjectService;
import org.aspectj.weaver.ast.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ScheduleController {

    private String messageResponse;

    private boolean showToast = false;
    private final IScheduleService iScheduleService;

    private final IUserService iUserService;

    private final ISubjectService iSubjectService;

    private final ISemesterService iSemesterService;

    @Autowired
    public ScheduleController(IScheduleService iScheduleService,
                              IUserService iUserService,
                              ISubjectService iSubjectService,
                              ISemesterService iSemesterService) {
        this.iScheduleService = iScheduleService;
        this.iUserService = iUserService;
        this.iSubjectService = iSubjectService;
        this.iSemesterService = iSemesterService;
    }

    @GetMapping("/dash-board/list-schedule")
    public String getListSchedulePage(Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {

        Page<Schedule> schedulePage = iScheduleService.getListScheduleAndPagination(page);

        List<ScheduleDTO> scheduleDTOList = schedulePage.stream().map(
                item -> ScheduleMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SubjectDTO> subjectDTOList = iSubjectService.getAllSubject().stream().map(
                item -> SubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SemesterDTO> semesterDTOList = iSemesterService.getAllSemester().stream().map(
                item -> SemesterMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        if(scheduleDTOList.isEmpty()) model.addAttribute("noResultFindAll", true);
        else model.addAttribute("noResultFindAll", false);

        model.addAttribute("listSchedules", scheduleDTOList);
        model.addAttribute("listSubjects", subjectDTOList);
        model.addAttribute("listSemesters", semesterDTOList);
        model.addAttribute("totalPage", schedulePage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("messageResponse",messageResponse);
        model.addAttribute("showToast", showToast);
        model.addAttribute("active", 8);
        model.addAttribute("pageName", "/admin/list-schedule");
        showToast = false;
        return "/admin/dash-board";
    }

    @GetMapping("/dash-board/list-schedule/search")
    public String getListScheduleSearch(Model model,
                                        @RequestParam(required = false) String keyword,
                                        @RequestParam(required = false, defaultValue = "0") Integer page
    ) {

        Page<Schedule> schedulePage = iScheduleService.searchSchedule(keyword, page);

        List<ScheduleDTO> scheduleDTOList = schedulePage.stream().map(
                item -> ScheduleMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SubjectDTO> subjectDTOList = iSubjectService.getAllSubject().stream().map(
                item -> SubjectMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        List<SemesterDTO> semesterDTOList = iSemesterService.getAllSemester().stream().map(
                item -> SemesterMapper.mapFromEntityToDTO(item)
        ).collect(Collectors.toList());

        Authentication authen = SecurityContextHolder.getContext().getAuthentication();
        if(authen.isAuthenticated()) model.addAttribute("user", iUserService.getUserByEmail(authen.getName()).get());
        else model.addAttribute("user",null);

        if(scheduleDTOList.isEmpty()) model.addAttribute("noResultSearch", true);
        else model.addAttribute("noResultSearch", false);

        model.addAttribute("active", 8);
        model.addAttribute("totalPage", schedulePage.getTotalPages());
        model.addAttribute("pageNumber", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listSemesters", semesterDTOList);
        model.addAttribute("listSchedules", scheduleDTOList);
        model.addAttribute("listSubjects", subjectDTOList);
        model.addAttribute("pageName", "/admin/list-schedule");

        return "/admin/dash-board";
    }

    @GetMapping("/get-schedule-by-id/{id}")
    public ResponseEntity<?> getScheduleById(@PathVariable Integer id) {
        try {
            Schedule scheduleReturn = iScheduleService.getScheduleById(id);
            ScheduleDTO scheduleDTOReturn = ScheduleMapper.mapFromEntityToDTO(scheduleReturn);
            return new ResponseEntity<>(scheduleDTOReturn, HttpStatus.OK);
        }catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-new-schedule")
    public String addNewSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
        Schedule scheduleInput = ScheduleMapper.mapFromDTOToEntity(scheduleDTO);
        messageResponse = iScheduleService.addNewSchedule(scheduleInput);
        showToast = true;
        return "redirect:/dash-board/list-schedule";
    }

    @PostMapping("/update-schedule")
    public String updateSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
        Schedule scheduleInput = ScheduleMapper.mapFromDTOToEntity(scheduleDTO);
        messageResponse = iScheduleService.updateSchedule(scheduleInput);
        showToast = true;
        return "redirect:/dash-board/list-schedule";
    }

    @PostMapping("/delete-schedule")
    public String deleteSchedule(@RequestParam("id") Integer id) {
        messageResponse = iScheduleService.deleteSchedule(id);
        showToast = true;
        return "redirect:/dash-board/list-schedule";
    }
}
