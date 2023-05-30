package meb.gov.tr.meb_accommidation.controller;
import meb.gov.tr.meb_accommidation.entity.Room;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.service.RoomService;
import meb.gov.tr.meb_accommidation.service.TeacherHomeService;
import org.springframework.beans.PropertyEditorRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Controller
public class TeacherHomeController {

    private TeacherHomeService teacherHomeService;

    @Autowired
    public TeacherHomeController(TeacherHomeService teacherHomeService) {
        this.teacherHomeService = teacherHomeService;
    }
    @Autowired
    private RoomService roomService;


    public Model addModelAttributes(Model model) {
        model.addAttribute("teacherHomes", teacherHomeService.getAllTeacherHomes());
        return model;
    }


    //Get All TeacherHomes
    @GetMapping("/teacherhomes")
    public String findAll(Model model) {
        addModelAttributes(model);
        return "/teacherHome/teacherHomes";
    }

    @GetMapping("teacherHome/teacherHomeAdd")
    public String addTeacherHome(Model model) {
        TeacherHome teacherHome = new TeacherHome(); // boş bir nesne oluşturuldu
        model.addAttribute("teacherHome", teacherHome); // view'a aktarıldı
        return "/teacherHome/teacherHomeAdd";
    }


    //The op parameter is either Edit or Details
    @GetMapping("/teacherHome/teacherHome/{op}/{id}")
    public String editTeacherHome(@PathVariable Long id, @PathVariable String op, Model model) {
        TeacherHome teacherHome = teacherHomeService.findById(id);
        model.addAttribute("teacherHome", teacherHome);
        addModelAttributes(model);
        return "/teacherHome/teacherHome" + op; //returns teacherHomeEdit or teacherHomeDetails
    }

    //Add TeacherHome
    @PostMapping("/teacherHome/teacherHomes")
    public String addNew(@ModelAttribute TeacherHome teacherHome) {
        teacherHomeService.save(teacherHome);
        return "redirect:/teacherhomes";
    }

    @Transactional
    @RequestMapping(value = "/teacherHome/teacherHome/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String delete(@PathVariable Integer id) {
        teacherHomeService.delete(id);
        return "redirect:/teacherhomes";
    }

    @PostMapping("/teacherHome/update/{id}")
    public String updateTeacherHome(@PathVariable Long id, @ModelAttribute("teacherHome") TeacherHome teacherHome, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("teacherHome", teacherHome);
            return "teacherHomeEdit";
        }

        Optional<TeacherHome> existingTeacherHome = Optional.ofNullable(teacherHomeService.findById(id));
        if (existingTeacherHome.isPresent()) {
            TeacherHome updatedTeacherHome = existingTeacherHome.get();
            updatedTeacherHome.setName(teacherHome.getName());
            updatedTeacherHome.setAddress(teacherHome.getAddress());
            updatedTeacherHome.setProvince(teacherHome.getProvince());
            updatedTeacherHome.setDistrict(teacherHome.getDistrict());
            updatedTeacherHome.setPhoneNumber(teacherHome.getPhoneNumber());
            updatedTeacherHome.setEmail(teacherHome.getEmail());
            // Diğer özelliklerin güncellenmesi

            teacherHomeService.updateTeacherHome(updatedTeacherHome);
            model.addAttribute("teacherHomes", teacherHomeService.getAllTeacherHomes());
            return "redirect:/teacherhomes";
        } else {
            model.addAttribute("errorMessage", "Teacher Home not found with ID: " + id);
            return "teacherHomeEdit";
        }
    }
    @GetMapping("/teacherHome/rooms/{teacherHomeId}/{startDate}/{endDate}")
    public String getAvailableRoomsByTeacherHomeIdAndDateRange(Model model, @PathVariable("teacherHomeId") Long teacherHomeId, @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Room> rooms = roomService.getRoomsByTeacherHomeId(teacherHomeId);
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isReservationSwitch() && roomService.isRoomAvailableForDateRange(Integer.valueOf(room.getId()), startDate, endDate)) {
                availableRooms.add(room);
            }
        }

        model.addAttribute("rooms", availableRooms);
        return "teacherHome/rooms";
    }

    @GetMapping("/teacherHomes/roomsStatus")
    public String getRoomsStatus(Model model) {
        List<TeacherHome> teacherHomes = teacherHomeService.getAllTeacherHomes();
        model.addAttribute("teacherHomes", teacherHomes);
        return "room/roomsEmployee";
    }

    @GetMapping("/room/roomsEmployee")
    public String roomsEmployee(@RequestParam("teacherHomeId") Long teacherHomeId, Model model) {
        List<TeacherHome> teacherHomes = teacherHomeService.getAllTeacherHomes();
        model.addAttribute("teacherHomes", teacherHomes);

        if (teacherHomeId != null) {
            TeacherHome selectedTeacherHome = teacherHomeService.getTeacherHomeById(teacherHomeId);
            model.addAttribute("selectedTeacherHome", selectedTeacherHome);
        }

        return "room/roomsEmployee";
    }

}