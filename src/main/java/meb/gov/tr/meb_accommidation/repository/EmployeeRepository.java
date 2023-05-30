package meb.gov.tr.meb_accommidation.repository;

import meb.gov.tr.meb_accommidation.entity.Employee;
import meb.gov.tr.meb_accommidation.entity.Guest;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    //verilen öğretmenevinin çalışanlarını listeler
    List<Employee> findByTeacherHome(TeacherHome teacherHome);
    //tcNo su verilen çalışanı bulur
    Employee findByEmployeeTcNo(String tcNo);
}
