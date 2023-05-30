package meb.gov.tr.meb_accommidation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "teacher_home", schema = "PUBLIC")
public class TeacherHome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "{notNull}")
    private String province;

    @Column(nullable = false)
    @NotNull(message = "{notNull}")
    private String district;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "teacherHome",
            //cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "teacherHome")
    @JsonManagedReference
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST,
            org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<Employee> employeeList;

/*    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherHome")
    @JsonManagedReference
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE,
            org.hibernate.annotations.CascadeType.MERGE,
            org.hibernate.annotations.CascadeType.PERSIST,
            org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Room> roomList;*/

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teacherHome",fetch = FetchType.LAZY)
    @JsonManagedReference
    public List<Room> roomList;

/*
    @PostLoad
    public void initialize() {
        this.roomList = new ArrayList<>();
    }
*/

    @Value(value = "111111111")
    private String taxNumber;

    public TeacherHome() {
        this.roomList = new ArrayList<>();
    }


}
