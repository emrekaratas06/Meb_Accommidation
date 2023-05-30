package meb.gov.tr.meb_accommidation.service;

import meb.gov.tr.meb_accommidation.entity.Employee;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);//silme işleminde karşılaştığım hata sebebiyle. H2 u gözlemek için

    @Autowired
    private EntityManager entityManager; //flush() için
    @Autowired
    private EmployeeRepository employeeRepository;

    // Verilen öğretmenevine ait tüm çalışanları döndürür
    public List<Employee> getAllEmployeesByTeacherHome(TeacherHome teacherHome) {
        return employeeRepository.findByTeacherHome(teacherHome);
    }

    // TC kimlik numarasına göre çalışanı bulur
    public Employee getEmployeeByTcNo(String tcNo) {
        return employeeRepository.findByEmployeeTcNo(tcNo);
    }

    // Yeni bir çalışan kaydı oluşturur
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Var olan bir çalışan kaydını günceller
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Var olan bir çalışan kaydını siler
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Integer id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            entityManager.flush();
            logger.info("Employee with ID {} has been deleted successfully", id);
        } else {
            logger.warn("Employee with ID {} does not exist", id);
        }
    }
    public Employee findById(Integer id) {
        return employeeRepository.findById(id).orElse(null);
    }
}

