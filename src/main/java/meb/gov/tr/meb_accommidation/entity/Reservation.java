package meb.gov.tr.meb_accommidation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Reservation")
public class Reservation {

    public static final double TAX_AMOUNT = 0.10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "info_price")
    private boolean infoPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "roomId",insertable=false, updatable=false)
    private Room room;
    private Integer roomId;

    @Column(name = "dt_check_in")
    private String checkIn;

    @Column(name = "dt_check_out")
    private String checkOut;
    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "guest_reservation",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    private Set<Guest> guest = new HashSet<>();

    @Column(name = "teacher_home_id", nullable = false)
    private Integer teacherHomeId;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "{futOrPres}")
    private LocalDate startDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "{futOrPres}")
    private LocalDate endDate;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
            name = "guest_reservation",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    @JsonManagedReference
    private Set<Guest> guests = new HashSet<>();


    private ReservationStatus status;

    private BigDecimal totalCost;

    @Column(name = "discount_rate")
    private BigDecimal discountRate;

    @Column(name = "reservation_ref_code")
    private String reservationRefCode;

    public String getDate() {
        return this.startDate + "-" + this.endDate;
    }
// Reservation.java

    public BigDecimal calculateDiscountedTotalCost() {
        if (this.room == null || this.guests == null || this.guests.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal costPerNight = this.room.getCostPerNight();
        long totalNights = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal discountRate = BigDecimal.ZERO;

        for (Guest guest : this.guests) {
            if (GuestType.MebPeople.equals(guest.getGuestType())) {
                discountRate = new BigDecimal("0.5");
            } else if (GuestType.StatePeople.equals(guest.getGuestType())) {
                discountRate = new BigDecimal("0.25");
            }
        }

        for (Guest guest : this.guests) {
            BigDecimal guestCost = costPerNight
                    .multiply(BigDecimal.ONE.subtract(discountRate))
                    .multiply(BigDecimal.valueOf(totalNights));
            totalCost = totalCost.add(guestCost);
        }

        setTotalCost(totalCost);
        setDiscountRate(discountRate);
        return totalCost;
    }

    public int calculateDiscountRate() {
        int discountRate = 0;

        if (this.guests != null && !this.guests.isEmpty()) {
            Guest guest = this.guests.iterator().next();

            if (GuestType.MebPeople.equals(guest.getGuestType())) {
                discountRate = 50;
            } else if (GuestType.StatePeople.equals(guest.getGuestType())) {
                discountRate = 25;
            }
        }

        return discountRate;
    }


    //Bu metot rastgele bir referans kodu oluşturacak ve kodu reservationRefCode değişkenine atayacaktır.
    public String generateRefCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();

        while (sb.length() < 8) {
            int index = (int) (rnd.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }

        String refCode = sb.toString();
        return refCode;
    }

    private LocalDate date;
}
