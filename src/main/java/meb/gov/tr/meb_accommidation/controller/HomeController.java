package meb.gov.tr.meb_accommidation.controller;

import meb.gov.tr.meb_accommidation.entity.Room;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.exception.TeacherHomeNotFoundException;
import meb.gov.tr.meb_accommidation.service.RoomService;
import meb.gov.tr.meb_accommidation.service.TeacherHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private TeacherHomeService teacherHomeService;

    @GetMapping("/")
    public String homePage(Model model) {
        List<TeacherHome> teacherHomes = teacherHomeService.findAll();
        model.addAttribute("teacherHomes", teacherHomes);
        return "home";
    }

}
