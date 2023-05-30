package meb.gov.tr.meb_accommidation.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "Guest")
public class Guest {

    @Transient
    private UUID tempId = UUID.randomUUID();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 20)
    @NotNull(message = "required")
    @Column(nullable = false)
    private String firstName;

    @Size(min = 2, max = 20)
    @NotNull(message = "required")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "{notNull}")
    private String email;

    @NotNull(message = "{notNull}")
    private String phone;

    @Enumerated(EnumType.STRING)
    private GuestType guestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    private boolean child;

    @EqualsAndHashCode.Exclude//metodlarında ilişkili alanların dikkate alınmamasını sağlayabilir ve döngüsel çağrıları önleyebilirsiniz.
    @ManyToMany(mappedBy = "guest")
    @JsonBackReference
    private Set<Reservation> reservations = new HashSet<>();
}
