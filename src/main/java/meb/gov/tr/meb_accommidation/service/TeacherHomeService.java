package meb.gov.tr.meb_accommidation.service;

import meb.gov.tr.meb_accommidation.entity.Employee;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.repository.TeacherHomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TeacherHomeService {

    @Autowired
    private TeacherHomeRepository teacherHomeRepository;

    public TeacherHome getTeacherHomeById(Long id) {
        Optional<TeacherHome> teacherHome = teacherHomeRepository.findById(id);
        if (teacherHome.isPresent()) {
            return teacherHome.get();
        } else {
            throw new EntityNotFoundException("TeacherHome with id " + id + " not found.");
        }
    }

    public List<TeacherHome> getAllTeacherHomes() {
        return teacherHomeRepository.findAll();
    }
    //verilen il ve ilçeye göre öğretmen evlerini döndürür
    public List<TeacherHome> getTeacherHomesByProvinceAndDistrict(String province, String district) {
        return teacherHomeRepository.findByProvinceAndDistrict(province, district);
    }

    public TeacherHome createTeacherHome(TeacherHome teacherHome) {
        return teacherHomeRepository.save(teacherHome);
    }

    public void deleteTeacherHome(Long id) {
        Optional<TeacherHome> teacherHome = teacherHomeRepository.findById(id);
        if (teacherHome.isPresent()) {
            teacherHomeRepository.delete(teacherHome.get());
        } else {
            throw new EntityNotFoundException("TeacherHome with id " + id + " not found.");
        }
    }

    //Get All TeacherHomes
    public List<TeacherHome> findAll(){
        return teacherHomeRepository.findAll();
    }

    //Get TeacherHome By Id
    public TeacherHome findById(Long id) {
        return teacherHomeRepository.findById(id).orElse(null);
    }

    //Delete TeacherHome
    public void delete(long id) {
        teacherHomeRepository.deleteById(id);
    }

    //Update Employee
    public void save( TeacherHome teacherHome) {
        teacherHomeRepository.save(teacherHome);
    }

    public TeacherHome updateTeacherHome(TeacherHome updatedTeacherHome) {
        TeacherHome existingTeacherHome = teacherHomeRepository.findById(updatedTeacherHome.getId()).orElseThrow(() -> new ResourceNotFoundException("TeacherHome not found"));

        // Diğer özelliklerin güncellenmesi
        existingTeacherHome.setName(updatedTeacherHome.getName());
        existingTeacherHome.setAddress(updatedTeacherHome.getAddress());
        existingTeacherHome.setPhoneNumber(updatedTeacherHome.getPhoneNumber());
        existingTeacherHome.setEmail(updatedTeacherHome.getEmail());
        existingTeacherHome.setProvince(updatedTeacherHome.getProvince());
        existingTeacherHome.setDistrict(updatedTeacherHome.getDistrict());
        // Diğer özelliklerin güncellenmesi için buraya ekleyin...

        // roomList özelliğinin güncellenmesi
        existingTeacherHome.getRoomList().clear();
        existingTeacherHome.getRoomList().addAll(updatedTeacherHome.getRoomList());

        //employeeList özelliğinin Güncellenmesi
        existingTeacherHome.getEmployeeList().clear();
        existingTeacherHome.getEmployeeList().addAll(updatedTeacherHome.getEmployeeList());
        return teacherHomeRepository.save(existingTeacherHome);
    }
}
