package meb.gov.tr.meb_accommidation.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "payments")
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", referencedColumnName = "id")
    private Reservation reservation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_type_id")
    private PaymentType paymentType;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private String paymentReference;

    @ManyToOne
    @JoinColumn(name = "teacher_home_id")
    private TeacherHome teacherHome;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Column
    private boolean refunded;

    @OneToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;

    // getters and setters, constructors, and other methods
}