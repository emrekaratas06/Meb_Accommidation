package meb.gov.tr.meb_accommidation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "beds")
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    @OneToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Enumerated(EnumType.STRING)
    private BedStatus status;    // Yatağın durumu ('EMPTY', 'OCCUPIED' gibi)
    public Bed(Room room, Guest guest, BedStatus status) {
        this.room = room;
        this.guest = guest;
        this.status = status;
    }
    // Getters and Setters
    // ...

}