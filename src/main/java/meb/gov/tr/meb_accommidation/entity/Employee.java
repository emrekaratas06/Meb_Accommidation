package meb.gov.tr.meb_accommidation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "{notNull}")
    @JsonProperty("id")
    private Integer employeeId;

    @Column(name="employeetc_no")
    @NotNull(message = "{notNull}")
    private String employeeTcNo;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    @JoinColumn(name="teacherHomeId", insertable=false, updatable=false)
    private TeacherHome teacherHome;

    private Integer teacherHomeId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    //@NotBlank(message = "{notBlank}")
    private EmployeeType employeeType;

    @Column
    private String email;

    public EmployeeType getEmployeeType() {
        return employeeType;
    }
}
