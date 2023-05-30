package meb.gov.tr.meb_accommidation.repository;

import meb.gov.tr.meb_accommidation.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentReference(String paymentReference);
    List<Payment> findByTeacherHomeIdOrderByPaymentDateAsc(Long teacherHomeId);



}

