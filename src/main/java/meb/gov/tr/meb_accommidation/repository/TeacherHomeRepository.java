package meb.gov.tr.meb_accommidation.repository;

import meb.gov.tr.meb_accommidation.entity.RoomType;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TeacherHomeRepository extends JpaRepository<TeacherHome, Long> {


    // İl ve ilçeye göre öğretmen evlerini getirir
    List<TeacherHome> findByProvinceAndDistrict(String province, String district);

    // E-posta adresine göre bir öğretmen evi getirir
    TeacherHome findByEmail(String email);


}