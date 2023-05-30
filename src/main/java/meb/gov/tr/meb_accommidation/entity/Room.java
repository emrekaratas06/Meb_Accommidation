package meb.gov.tr.meb_accommidation.entity;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import meb.gov.tr.meb_accommidation.repository.RoomRepository;
import org.hibernate.annotations.Cascade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull(message = "{notNull}")
    private String roomNumber;

    @Column(name = "dt_available_from")
    private String availableFrom;

    @Column(name = "dt_available_to")
    private String availableTo;

    @Column(name = "reservation_switch")
    private boolean reservationSwitch;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Bed> beds;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "teacherHomeId", referencedColumnName = "id", insertable=false, updatable=false)
    //@JsonBackReference // Bu anotasyon JSON döngüsel referans sorununu çözmek için
    @JsonIgnore
    private TeacherHome teacherHome;

    private Integer teacherHomeId;

    @Column(nullable = false)
    private BigDecimal costPerNight;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Reservation> reservation;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    //@NotBlank(message = "{notBlank}")
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatuses=RoomStatus.EMPTY;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL)
    private Payment payment;

    @Column(name = "status_dates")
    private String statusDates;

    @PostLoad
    public void fillStatusDates() {
        List<Reservation> reservations = getReservation();
        StringBuilder builder = new StringBuilder();
        for (Reservation reservation : reservations) {
            builder.append(reservation.getDate()).append("=").append(reservation.getStatus()).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1); // Son virgülü kaldır
        }
        setStatusDates(builder.toString());
    }
    public Room(String roomNumber, RoomType roomType, Date availableFrom, Date availableTo, boolean reservationSwitch, Set<Bed> beds, BigDecimal costPerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.availableFrom = String.valueOf(availableFrom);
        this.availableTo = String.valueOf(availableTo);
        this.reservationSwitch = reservationSwitch;
        this.beds = beds;
        this.costPerNight = costPerNight;
    }

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    public void shareBeds() {
        // Yeni odaları oluştur
        Room roomA = new Room(roomNumber + "A", roomType, convertToDate(availableFrom), convertToDate(availableTo), reservationSwitch, beds, costPerNight);
        Room roomB = new Room(roomNumber + "B", roomType, convertToDate(availableFrom), convertToDate(availableTo), reservationSwitch, beds, costPerNight);

        // Mevcut odadaki yatakları paylaşan yeni odaları ayarla
        Set<Bed> originalBeds = getBeds();
        Set<Bed> bedsA = new HashSet<>();
        Set<Bed> bedsB = new HashSet<>();

        for (Bed bed : originalBeds) {
            Bed bedA = new Bed(bed.getRoom(), bed.getGuest(), BedStatus.EMPTY);
            Bed bedB = new Bed(roomB, null, BedStatus.EMPTY);
            bedsA.add(bedA);
            bedsB.add(bedB);

            bed.setRoom(roomA);
        }

        roomA.setBeds(bedsA);
        roomB.setBeds(bedsB);

        roomA.setBeds(bedsA);
        roomB.setBeds(bedsB);

        // Yeni odaları veritabanına kaydet
        roomRepository.save(roomA);
        roomRepository.save(roomB);

        // Orijinal odanın yataklarını güncelle
        originalBeds.clear();
        originalBeds.addAll(bedsA);

        // Orijinal odanın güncellenmesini sağla
        roomRepository.save(this);
    }

    private Date convertToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            // Handle the exception or throw it further
            e.printStackTrace();
            return null; // Or return a default value
        }
    }
}
