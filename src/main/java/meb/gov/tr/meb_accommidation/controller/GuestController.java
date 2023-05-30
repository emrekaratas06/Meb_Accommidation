package meb.gov.tr.meb_accommidation.controller;

import meb.gov.tr.meb_accommidation.entity.Guest;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class GuestController {
    @Autowired
    private GuestService guestService;

    public Model addModelAttributes(Model model) {
        model.addAttribute("guests", guestService.getAllGuests());
        return model;
    }

    // e-posta adresi verilen konuğu getirir
    @GetMapping("/email/{email}")
    public ResponseEntity<Guest> getGuestByEmail(@PathVariable String email) {
        Guest guest = guestService.getGuestByEmail(email);
        if (guest != null) {
            return ResponseEntity.ok(guest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // telefonu verilen konuğu getirir
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Guest> getGuestByPhone(@PathVariable String phone) {
        Guest guest = guestService.getGuestByPhone(phone);
        if (guest != null) {
            return ResponseEntity.ok(guest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Misafirleri çocuk durumuna göre getirmek için
    @GetMapping("/child/{child}")
    public ResponseEntity<List<Guest>> getGuestsByChildStatus(@PathVariable boolean child) {
        List<Guest> guests = guestService.getGuestsByChildStatus(child);
        if (!guests.isEmpty()) {
            return ResponseEntity.ok(guests);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Misafirleri rezervasyon id sine göre getirmek için
    @GetMapping("/reservation/{id}")
    public ResponseEntity<List<Guest>> getGuestsByReservation(@PathVariable Long id) {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        List<Guest> guests = guestService.getGuestsByReservation(reservation);
        if (!guests.isEmpty()) {
            return ResponseEntity.ok(guests);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Yeni bir misafir oluşturur
    @PostMapping("/")
    public ResponseEntity<Guest> createGuest(@Valid @RequestBody Guest guest) {
        Guest createdGuest = guestService.createGuest(guest);
        return ResponseEntity.ok(createdGuest);
    }

    // Mevcut bir konuk kaydını güncellemek için
    @PutMapping("/")
    public ResponseEntity<Guest> updateGuest(@Valid @RequestBody Guest guest) {
        Guest updatedGuest = guestService.updateGuest(guest);
        if (updatedGuest != null) {
            return ResponseEntity.ok(updatedGuest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Mevcut bir konuk kaydını silmek için
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteGuest(@RequestBody Guest guest) {
        guestService.deleteGuest(guest);
        return ResponseEntity.noContent().build();
    }

    //Get All Guests
    @GetMapping("/guests")
    public String findAll(Model model) {
        addModelAttributes(model);
        return "/guest/guests";
    }

    @GetMapping("guest/guestAdd")
    public String addGuest(Model model) {
        Guest guest = new Guest(); // boş bir nesne oluşturuldu
        model.addAttribute("guest", guest); // view'a aktarıldı
        return "/guest/guestAdd";
    }


    //The op parameter is either Edit or Details
    @GetMapping("/guest/{op}/{id}")
    public String editGuest(@PathVariable Integer id, @PathVariable String op, Model model) {
        Guest guest = guestService.findById(id);
        model.addAttribute("guest", guest);
        addModelAttributes(model);
        return "/guest/guest" + op; //returns guestEdit or guestDetails
    }


    //Add TeacherHome
    @PostMapping("/guest/guests")
    public String addNew(@ModelAttribute Guest guest) {
        guestService.save(guest);
        return "redirect:/guests";
    }

    @Transactional //**Eğer guset'i silersek, o misafire ait rezarvasyonu da silecek şekilde ayarladım.Dikkat!
    @RequestMapping(value = "/guest/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String delete(@PathVariable Integer id) {
        guestService.delete(id);
        return "redirect:/guests";
    }

    @PostMapping("/guest/update/{id}")
    public String updateGuest(@PathVariable("id") Integer id, @ModelAttribute("guest") Guest guest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("guest", guest);
            return "guestEdit";
        }

        Guest existingGuest = guestService.findById(id);
        if (existingGuest != null) {
            existingGuest.setFirstName(guest.getFirstName());
            existingGuest.setLastName(guest.getLastName());
            existingGuest.setEmail(guest.getEmail());
            existingGuest.setPhone(guest.getPhone());
            existingGuest.setChild(guest.isChild());
            existingGuest.setGender(guest.getGender());
            // Diğer özelliklerin güncellenmesi

            guestService.updateGuest(existingGuest);
            model.addAttribute("guests", guestService.getAllGuests());
            return "redirect:/guests";
        } else {
            model.addAttribute("errorMessage", "Guest not found with ID: " + id);
            return "guestEdit";
        }
    }


}
