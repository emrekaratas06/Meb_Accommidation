package meb.gov.tr.meb_accommidation.controller;

import meb.gov.tr.meb_accommidation.entity.Payment;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.ReservationStatus;
import meb.gov.tr.meb_accommidation.repository.PaymentRepository;
import meb.gov.tr.meb_accommidation.repository.ReservationRepository;
import meb.gov.tr.meb_accommidation.service.PaymentService;
import meb.gov.tr.meb_accommidation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PaymentController {
    private final PaymentService paymentService;

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;

    @Autowired
    public PaymentController(PaymentService paymentService, ReservationRepository reservationRepository, PaymentRepository paymentRepository, ReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.reservationService = reservationService;
    }


    @GetMapping("/payment/{reservationRefCode}")
    public String showPaymentForm(@PathVariable String reservationRefCode, Model model) {
        Reservation reservation = reservationRepository.findByReservationRefCode(reservationRefCode);
        if (reservation == null || reservation.getStatus() != ReservationStatus.PENDING) {
            return "redirect:/reservations";
        }
        model.addAttribute("reservationRefCode", reservation.getReservationRefCode());
        return "payment-form";
    }
    // Ödeme düzenleme sayfasına yönlendirme
    @GetMapping("/payments/edit/{id}")
    public String showPaymentEditForm(@PathVariable("id") Long id, Model model) {
        Payment payment = paymentService.getPaymentById(id);
        model.addAttribute("payment", payment);
        return "paymentEdit";
    }


    @PostMapping("/payment")
    public String processPayment(@RequestParam String reservationRefCode,
                                 @RequestParam Double amount,
                                 Model model) {
        try {
            Payment payment = paymentService.makePayment(reservationRefCode, amount);
            model.addAttribute("payment", payment);
            model.addAttribute("reservation", reservationRepository.findByReservationRefCode(reservationRefCode));
            return "payment-result";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "reservation-form";
        }
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(@RequestParam String reservationRefCode, @RequestParam String amount, Model model) throws Exception {
        if (reservationRefCode == null || reservationRefCode.isEmpty()) {
            throw new IllegalArgumentException("Reservation reference code is required.");
        }

        try {
            Payment payment = paymentService.makePayment(reservationRefCode,Double.parseDouble(amount));
            model.addAttribute("message", "Payment completed successfully for Reservation with refCode: " + reservationRefCode);
            model.addAttribute("reservation", reservationRepository.findByReservationRefCode(reservationRefCode));
            model.addAttribute("amount", amount);
            return "thanks";
        } catch (Exception e) {
            reservationService.cancelReservation(reservationRefCode);
            model.addAttribute("errorMessage", e.getMessage());
            return "payment-form";
        }
    }

    @GetMapping("/payments/{teacherHomeId}")
    public String getPayments(@PathVariable Long teacherHomeId, Model model) {
        List<Payment> payments = paymentRepository.findByTeacherHomeIdOrderByPaymentDateAsc(teacherHomeId);
        double totalAmount = payments.stream().mapToDouble(Payment::getAmount).sum();
        model.addAttribute("payments", payments);
        model.addAttribute("totalAmount", totalAmount);
        return "payments-list";
    }

    // Ödeme bilgilerini güncelleme
    @PostMapping("/payments/update")
    public String updatePayment(Payment payment) {
        paymentService.updatePayment(payment);
        return "redirect:/payments";
    }

    // Ödeme iadesi
    @PostMapping("/payments/refund")
    public String refundPayment(Long id) {
        paymentService.refundPayment(id);
        return "redirect:/payments";
    }

}