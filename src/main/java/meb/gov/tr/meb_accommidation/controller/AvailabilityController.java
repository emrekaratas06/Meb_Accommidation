package meb.gov.tr.meb_accommidation.controller;

import meb.gov.tr.meb_accommidation.entity.Availability;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.Room;
import meb.gov.tr.meb_accommidation.repository.AvailabilityRepository;
import meb.gov.tr.meb_accommidation.repository.RoomRepository;
import meb.gov.tr.meb_accommidation.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/availability")
public class AvailabilityController {
    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/countAvailableRooms")
    public Long countAvailableRooms(@RequestParam("roomId") Integer roomId,
                                    @RequestParam("startDate") Date startDate,
                                    @RequestParam("endDate") Date endDate) {
        Room room = new Room();
        room.setId(roomId);
        return availabilityService.countAvailableRooms(room, startDate, endDate);
    }

    @GetMapping("/isRoomAvailable")
    public boolean isRoomAvailable(@RequestParam("roomId") Integer roomId,
                                   @RequestParam("startDate") Date startDate,
                                   @RequestParam("endDate") Date endDate) {
        Room room = new Room();
        room.setId(roomId);
        return availabilityService.isRoomAvailable(room, startDate, endDate);
    }

    @GetMapping("/findAvailableRooms")
    public List<Room> findAvailableRooms(@RequestParam("startDate") Date startDate,
                                         @RequestParam("endDate") Date endDate) {
        return availabilityService.findAvailableRooms(startDate, endDate);
    }

    @GetMapping("/findAvailableSingleRooms")
    public List<Room> findAvailableSingleRooms(@RequestParam("startDate") Date startDate,
                                               @RequestParam("endDate") Date endDate) {
        return availabilityService.findAvailableSingleRooms(startDate, endDate);
    }

    @GetMapping("/findRoomsByEndDate")
    public List<Room> findRoomsByEndDate(@RequestParam("startDate") Date startDate,
                                         @RequestParam("endDate") Date endDate) {
        return availabilityService.findRoomsByEndDate(startDate, endDate);
    }

    @PutMapping("/updateAvailability")
    public void updateAvailability(@RequestParam("roomId") Integer roomId,
                                   @RequestParam("startDate") Date startDate,
                                   @RequestParam("endDate") Date endDate,
                                   @RequestParam("availability") boolean availability) {
        Room room = new Room();
        room.setId(roomId);
        availabilityService.updateAvailability(room, startDate, endDate, availability);
    }

    @GetMapping("/checkAvailability")
    public boolean checkAvailability(@RequestParam("roomId") Integer roomId,
                                     @RequestParam("startDate") Date startDate,
                                     @RequestParam("endDate") Date endDate) {
        Room room = new Room();
        room.setId(roomId);
        return availabilityService.checkAvailability(room, startDate, endDate);
    }

}

