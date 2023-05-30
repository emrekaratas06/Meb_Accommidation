package meb.gov.tr.meb_accommidation.controller;

import javassist.NotFoundException;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.Room;
import meb.gov.tr.meb_accommidation.entity.RoomType;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.service.ReservationService;
import meb.gov.tr.meb_accommidation.service.RoomService;
import meb.gov.tr.meb_accommidation.service.TeacherHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private TeacherHomeService teacherHomeService;
    @Autowired
    private ReservationService reservationService;
    public Model addModelAttributes(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("teacherHomes" , teacherHomeService.findAll());
        return model;
    }

    @GetMapping("/availability")
    public List<Room> findAllBetweenDates(@RequestParam("from") String dateFrom, @RequestParam("to") String dateTo) {
        return roomService.findAllBetweenDates(dateFrom, dateTo);
    }

    @GetMapping("/teacherHomes/{teacherHomeId}/rooms")
    public List<Room> getRoomsByTeacherHomeId(@PathVariable("teacherHomeId") Integer teacherHomeId) {
        return roomService.findByTeacherHomeId(teacherHomeId);
    }

    @GetMapping
    public List<Room> findAllByOrderByRoomNumberAsc() {
        return roomService.findAllByOrderByRoomNumberAsc();
    }

    @GetMapping("/available")
    public List<Room> findAllByStatusTrueOrderByRoomNumberAsc() {
        return roomService.findAllByStatusTrueOrderByRoomNumberAsc();
    }

/*    @GetMapping("/rooms/{id}")
    public String roomsOfTeacherHome(@PathVariable("id") long id, Model model) {
        Optional<TeacherHome> teacherHome = Optional.ofNullable(teacherHomeService.findById(id));
        if (teacherHome.isPresent()) {
            model.addAttribute("teacherHome", teacherHome.get());
            return "room-list-modal :: roomListModalContent";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Home not found");
        }
    }*/


    @GetMapping("/beds/{beds}")
    public List<Room> findByBeds(@PathVariable("beds") Integer beds) {
        return roomService.findByBeds(beds);
    }

    @GetMapping("/room-types/{roomType}")
    public List<Room> findByRoomType(@PathVariable("roomType") RoomType roomType) {
        return roomService.findByRoomType(roomType);
    }

    @GetMapping("/cost-per-night/{costPerNight}")
    public List<Room> findByCostPerNightLessThan(@PathVariable("costPerNight") BigDecimal costPerNight) {
        return roomService.findByCostPerNightLessThan(costPerNight);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatusById(@PathVariable("id") Integer id, @RequestParam("status") boolean status) {
        roomService.updateStatusById(id, status);
        return ResponseEntity.noContent().build();
    }


    //Get All States
    @GetMapping("/rooms")
    public String findAll(Model model){
        addModelAttributes(model);
        return "/room/rooms";
    }

    @GetMapping("/room/roomAdd")
    public String addRoom(Model model){
        addModelAttributes(model);
        return "room/roomAdd";
    }

    @GetMapping("/room/room/{op}/{id}")
    public String editRoom(@PathVariable Integer id, @PathVariable String op, Model model) {
        addModelAttributes(model);
        model.addAttribute("room", roomService.findById(id).orElse(null));
        return "/room/room" + op;
    }


    //Add State
    @PostMapping(value="/room/rooms")
    public String addNew(@ModelAttribute Room room) {
        roomService.save(room);
        return "redirect:/rooms";
    }


  @Transactional
  @RequestMapping(value="/room/room/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
  public String delete(@PathVariable Integer id) throws NotFoundException {
      Room room = roomService.getRoomById(id);
      if (room != null) {
          roomService.delete(id);
      } else {
          throw new NotFoundException("Room not found with id: " + id);
      }
      return "redirect:/rooms";
  }

    @PostMapping("/room/rooms/{id}")
    public String updateRoom(@PathVariable("id") int id, @ModelAttribute Room room, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("room", room);
            return "/room/roomEdit";
        }
        Room updatedRoom = roomService.getRoomById(id);
        updatedRoom.setRoomNumber(room.getRoomNumber());
        updatedRoom.setAvailableFrom(room.getAvailableFrom());
        updatedRoom.setAvailableTo(room.getAvailableTo());
        updatedRoom.setReservationSwitch(room.isReservationSwitch());
        updatedRoom.setBeds(room.getBeds());
        updatedRoom.setCostPerNight(room.getCostPerNight());
        updatedRoom.setTeacherHome(room.getTeacherHome());
        updatedRoom.setRoomType(room.getRoomType());
        // Diğer özelliklerin güncellenmesi
        roomService.save(updatedRoom);
        return "redirect:/rooms";
    }
    @GetMapping("/rooms/teacher-home/{teacherHomeId}")
    public String showRooms(Model model, @PathVariable(value = "teacherHomeId") long teacherHomeId) {
        TeacherHome teacherHome = teacherHomeService.getTeacherHomeById(teacherHomeId);
        List<Room> rooms = roomService.findByTeacherHome(teacherHome);

        for (Room room : rooms) {
            List<Reservation> reservations = reservationService.getReservationsByRoom(room);
            String statusDates = "";
            for (Reservation reservation : reservations) {
                String reservationStatus = reservation.getStatus().toString();
                LocalDate startDate = reservation.getStartDate();
                LocalDate endDate = reservation.getEndDate();
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    if (!statusDates.isEmpty()) {
                        statusDates += ",";
                    }
                    statusDates += date.toString() + ":" + reservationStatus;
                }
            }
            room.setStatusDates(statusDates);
            roomService.updateRoom(room);
        }

        model.addAttribute("rooms", rooms);
        model.addAttribute("teacherHome", teacherHome);
        return "roomsEmployee";
    }

}
