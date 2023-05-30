package meb.gov.tr.meb_accommidation.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import meb.gov.tr.meb_accommidation.entity.*;
import meb.gov.tr.meb_accommidation.repository.ReservationRepository;
import meb.gov.tr.meb_accommidation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    TeacherHomeService teacherHomeService;
    @Autowired
    RoomService roomService;
    @Autowired
    GuestService guestService;
    @Autowired
    ReservationRepository reservationRepository;

    private final PaymentService paymentService;
    public ReservationController(PaymentService paymentService) {
              this.paymentService = paymentService;
    }

    @GetMapping
    public String getReservationsByFilter(
            @RequestParam(value = "guestId", required = false) Integer guestId,
            @RequestParam(value = "guestName", required = false) String guestName,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        // İlgili filtrelerle veritabanından rezervasyonları al
        List<Reservation> reservations = reservationService.findReservationsByFilter(guestId, guestName, startDate, endDate);

        // Model'e rezervasyonları ekleyin ve görünümü döndürün
        model.addAttribute("reservations", reservations);
        return "reservations";
    }

    @GetMapping("/add")
    public String addReservation(Model model) {
        Reservation reservation = new Reservation();
        Room room = new Room();
        reservation.setRoom(room);
        model.addAttribute("reservation", reservation);
        model.addAttribute("room", room);
        List<Guest> guests = guestService.findAll();
        model.addAttribute("guests", guests);
        return "reservationAdd";
    }
    @PostMapping("/add")
    public String addReservation(@Valid @ModelAttribute("reservation") Reservation reservation, BindingResult bindingResult,
                                 @RequestParam("teacherHomeId") Integer teacherHomeId, @RequestParam("roomId") Integer roomId, Model model) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                System.out.println(error.toString());
            }
            return "reservationAdd";
        }

        Room room = roomService.findById(roomId).orElseThrow(() -> new NoSuchElementException("Room not found"));
        reservation.setRoom(room);

        if (!reservationService.isRoomAvailable(roomId, teacherHomeId, reservation.getStartDate(), reservation.getEndDate())) {
            bindingResult.rejectValue("startDate", "error.reservation", "A reservation already exists for this room and date range.");
            return "reservationAdd";
        }
        if (reservation.getStartDate().isEqual(reservation.getEndDate())) {
            bindingResult.rejectValue("endDate", "error.reservation", "End date must be different from the start date.");
            return "reservationAdd";
        }

        reservation.calculateDiscountedTotalCost(); // İndirimli toplam ücreti hesapla
        reservation.setReservationRefCode(reservation.generateRefCode());
        reservationService.save(reservation);
        reservationService.addReservation(reservation);
        model.addAttribute("reservation", reservation);
        roomService.updateRoomStatus(Integer.valueOf(reservation.getRoom().getId()), RoomStatus.RESERVED);
        return "reservationAdd";
    }

    @GetMapping("/edit/{id}")
    public String editReservation(@PathVariable("id") Long id, Model model) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

// .get() kullanarak Reservation nesnesini elde edin (Eğer değer yoksa NoSuchElementException fırlatır)
        Reservation reservation = optionalReservation.get();
        List<Guest> guests = guestService.findAll();
        model.addAttribute("guests", guests);
        model.addAttribute("reservation", reservation);
        return "reservationEdit";
    }
    @PostMapping("/update")
    public String updateReservation(@Valid @ModelAttribute Reservation reservation, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "reservationEdit";
        }
        if (reservation.getStartDate().isEqual(reservation.getEndDate())) {
            bindingResult.rejectValue("endDate", "error.reservation", "End date must be different from the start date.");
            return "reservation/edit/{id}";
        }

        reservationService.save(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteById(id);
        return "redirect:/reservations";
    }

    @ModelAttribute("teacherHomes")
    public List<TeacherHome> getAllTeacherHomes() {
        return teacherHomeService.findAll();
    }

    @GetMapping("/rooms-by-teacherhome/{teacherHomeId}")
    public String getRoomsByTeacherHome(@PathVariable("teacherHomeId") Integer teacherHomeId, Model model) {
        List<Room> rooms = roomService.findByTeacherHomeId(teacherHomeId);
        model.addAttribute("rooms", rooms);
        return "room-list";
    }
    @GetMapping("/rooms-by-teacherhome01/{teacherHomeId}")
    @ResponseBody
    public List<Room> getRoomsByTeacherHome01(@PathVariable("teacherHomeId") Integer teacherHomeId) {
        return roomService.findByTeacherHomeId(teacherHomeId);
    }
    @Transactional
    @RequestMapping(value="/reservation/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public String delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return "redirect:/room/rooms";
    }

    @PostMapping("/reservation/edit/{id}")
    public String updateReservation(@PathVariable Long id, @ModelAttribute("reservation") Reservation reservation, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("reservation", reservation);
            return "reservation/edit/{id}";
        }

        Optional<Reservation> existingReservation = reservationService.findById(id);
        if (existingReservation.isPresent()) {
            Reservation updatedReservation= existingReservation.get();
            updatedReservation.setTeacherHomeId(reservation.getTeacherHomeId());
            updatedReservation.setRoomId(reservation.getRoomId());
            updatedReservation.setStartDate(reservation.getStartDate());
            updatedReservation.setEndDate(reservation.getEndDate());
            updatedReservation.setGuests(reservation.getGuests());
            updatedReservation.setCheckIn(reservation.getCheckIn());
            updatedReservation.setCheckOut(reservation.getCheckOut());
            // Diğer özelliklerin güncellenmesi

            updatedReservation.calculateDiscountedTotalCost(); // İndirimli toplam ücreti hesapla
            reservationService.save(updatedReservation);
            return "redirect:/reservations";
        } else {
            throw new NoSuchElementException("Reservation not found");
        }
    }

    @GetMapping("/find")
    public String findReservations(@RequestParam(value = "guestId", required = false) Integer guestId,
                                   @RequestParam(value = "guestName", required = false) String guestName,
                                   @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                   @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                   Model model) {
        List<Reservation> reservations = new ArrayList<>();
        if (startDate != null && endDate != null) {
            // Tarih aralığına göre rezervasyonları bulun
            reservations = reservationService.findReservationsByDateRange(startDate, endDate);
        }else if (guestId != null) {
            // Guest ID'ye göre rezervasyonları bulun
            reservations = reservationService.findReservationsByGuestId(guestId);
        } else if (guestName != null && !guestName.isEmpty()) {
            // Guest adına göre rezervasyonları bulun
            reservations = reservationService.findReservationsByGuestName(guestName);
        }

        model.addAttribute("reservations", reservations);
        return "reservation/reservationFind";
    }

    @PostMapping("/pay")
    public String payForReservation(@RequestParam("refCode") String refCode,
                                    @RequestParam("amount") double amount,
                                    Model model) {
        try {
            paymentService.makePayment(refCode, amount);
            model.addAttribute("message", "Payment is successful");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "payment-result";
    }
   //Rezervasyon iptali için
    @PostMapping("/cancel-reservation")
    public String cancelReservation(@RequestParam("refCode") String refCode, Model model) {
        try {
            Reservation reservation = reservationService.findByReservationRefCode(refCode);
            if (reservation == null) {
                throw new Exception("Reservation not found with refCode: " + refCode);
            }
            if (reservation.getStatus() != ReservationStatus.PENDING) {
                throw new Exception("Reservation is already cancelled or payment is made for reservation with refCode: " + refCode);
            }
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationService.save(reservation);
            model.addAttribute("message", "Reservation is successfully cancelled.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "reservation-cancel-result";
    }
    //Bu metodu ReservationController sınıfında,(reservationAdd) rezervasyon oluşturma işlemi yapıldıktan sonra çağırabiliriz
    @PostMapping("/reserve-room")
    public String reserveRoom(@ModelAttribute("reservation") Reservation reservation, Model model) {

        try {

            Optional<Room> optionalRoom = roomService.findById(reservation.getRoomId());
            if (optionalRoom.isPresent()) {
                Room room = optionalRoom.get();
                reservation.setRoom(room);
            } else {
                throw new NoSuchElementException("Room not found");
            }
            reservation.setTotalCost(reservation.calculateDiscountedTotalCost());
            /*reservation.setDiscountRate(reservation.calculateDiscountPercentage());*/
            String refCode = reservation.generateRefCode();
            reservation.setReservationRefCode(refCode);

            reservation.setStartDate(reservation.getStartDate());
            reservation.setEndDate(reservation.getEndDate());

            reservationService.save(reservation);
            model.addAttribute("message", "Reservation is successfully made with RefCode: " + reservation.getReservationRefCode());
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "reservation-result";
    }

    @PostMapping("/generate-reference-code/{reservationId}")
    public String generateReferenceCode(@PathVariable Long reservationId, Model model) {
        try {
            Reservation reservation = reservationService.findById(reservationId).orElseThrow(() -> new NoSuchElementException("Reservation not found"));
            if (reservation.getStatus() != ReservationStatus.PENDING) {
                throw new Exception("Reservation is not in PENDING status");
            }
            String refCode = reservation.generateRefCode();
            reservation.setReservationRefCode(refCode);
            reservationService.save(reservation);
            model.addAttribute("message", "Reference code generated successfully for Reservation with ID: " + reservationId);
            return "reservation-result";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation-form";
        }
    }
    @PostMapping("/process-payment")
    public String processReservationPayment(@RequestParam String reservationRefCode, @RequestParam double amount, Model model) throws Exception {
        try {
            Payment payment = paymentService.makePayment(reservationRefCode, amount);
            model.addAttribute("message", "Payment completed successfully for Reservation with refCode: " + reservationRefCode);
            return "thank-you";
        } catch (Exception e) {
            reservationService.cancelReservation(reservationRefCode);
            model.addAttribute("errorMessage", e.getMessage());
            return "payment-form";
        }
    }
    @GetMapping("/reserved-dates/{roomId}")
    @ResponseBody
    public List<String> getReservedDates(@PathVariable Integer roomId) {
        List<String> reservedDates = reservationService.getReservedDatesByRoomId(roomId);
        return reservedDates;
    }
    @GetMapping("/room/{roomId}/statusDates")
    @ResponseBody
    public List<String> getReservedDatesByRoomId(@PathVariable("roomId") Integer roomId) {
        // roomId parametresine göre rezervasyon yapılan tarihleri veritabanından alın ve döndürün
        List<String> reservedDates = reservationService.getReservedDatesByRoomId(roomId);
        return reservedDates;
    }
    @GetMapping("/rooms-by-teacherhome/{teacherHomeId}/add-reservation")
    public String addReservationForm(@PathVariable("teacherHomeId") Long teacherHomeId,
                                     @RequestParam("roomId") Integer roomId,
                                     Model model) {
        // Gerekli işlemleri gerçekleştirin
        Optional<TeacherHome> teacherHomeOptional = Optional.ofNullable(teacherHomeService.findById(teacherHomeId));
        Optional<Room> roomOptional = roomService.findById(roomId);

        if (teacherHomeOptional.isPresent() && roomOptional.isPresent()) {
            TeacherHome teacherHome = teacherHomeOptional.get();
            Room room = roomOptional.get();

            Reservation reservation = new Reservation();
            reservation.setTeacherHomeId(Math.toIntExact(teacherHomeId));
            reservation.setRoom(room);
            // Diğer gerekli ayarlamaları yapın ve model'e ekleme yapın
            model.addAttribute("reservation", reservation);

            return "reservation-form";
        } else {
            throw new NoSuchElementException("Teacher Home or Room not found");
        }
    }

    @GetMapping("/rooms/{roomId}/reservations")
    public ResponseEntity<List<Reservation>> getRoomReservations(@PathVariable("roomId") Integer roomId) {
        // İlgili roomId'ye sahip oda rezervasyonlarını veritabanından veya başka bir kaynaktan alın
        List<Reservation> reservations = reservationService.getReservationsByRoomId(roomId);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }



}
