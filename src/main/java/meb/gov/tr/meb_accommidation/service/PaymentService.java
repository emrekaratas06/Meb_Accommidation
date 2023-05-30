package meb.gov.tr.meb_accommidation.service;

import meb.gov.tr.meb_accommidation.entity.Payment;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.ReservationStatus;
import meb.gov.tr.meb_accommidation.repository.PaymentRepository;
import meb.gov.tr.meb_accommidation.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Payment makePayment(String reservationRefCode, double amount) throws Exception {
        Reservation reservation = reservationRepository.findByReservationRefCode(reservationRefCode);

        if (reservation == null) {
            throw new Exception("Reservation not found with refCode: " + reservationRefCode);
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new Exception("Payment is already made or reservation is cancelled for reservation with refCode: " + reservationRefCode);
        }

        // Payment işlemleri burada gerçekleştirilir.
        // Eğer ödeme başarılı olursa, aşağıdaki kod bloğu çalışacak.
        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(amount);
        paymentRepository.save(payment);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        // Eğer ödeme başarısız olursa, aşağıdaki kod bloğu çalışacak.
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        return payment;
    }

    public void save(Payment payment) {
        paymentRepository.save(payment);
    }

    public void updatePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public void refundPayment(Long id) {
        Payment payment = getPaymentById(id);
        payment.setRefunded(true);
        updatePayment(payment);
    }
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid payment ID: " + id));
    }

}
