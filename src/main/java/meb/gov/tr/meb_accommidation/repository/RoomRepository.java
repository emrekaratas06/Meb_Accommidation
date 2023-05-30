package meb.gov.tr.meb_accommidation.repository;

import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.Room;
import meb.gov.tr.meb_accommidation.entity.RoomType;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query(value = "SELECT inv.* FROM Room AS inv " +
            "WHERE inv.DT_AVAILABLE_FROM <= :dateFrom AND inv.DT_AVAILABLE_TO >= :dateTo " +
            "AND inv.ID NOT IN " +
            "(SELECT Room_ID FROM Reservation WHERE (DT_CHECK_IN > :dateFrom OR DT_CHECK_OUT < :dateTo))", nativeQuery = true)
    List<Room> findAllBetweenDates(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);

//Aşağıdaki metodlar metodlarıyla sırasıyla odaları numaralarına göre, müsaitlik durumlarına göre,
// numaralarına, öğretmen evlerine, yatak sayısına, oda tipine ve geceleme ücretine göre listeler.
    List<Room> findByTeacherHome(TeacherHome teacherHome);

    List<Room> findAllByOrderByRoomNumberAsc();

    //List<Room> findAllByStatusTrueOrderByRoomNumberAsc();

    List<Room> findByReservationSwitchTrueOrderByRoomNumberAsc();

    Optional<Room> findByRoomNumber(String roomNumber);

    List<Room> findByBeds(Integer beds);

    List<Room> findByRoomType(RoomType roomType);

    List<Room> findByCostPerNightLessThan(BigDecimal costPerNight);

 //updateStatusById ve updateRoom metodlarıyla da bir odanın müsaitlik durumunu ve tüm özelliklerini günceller.
    @Modifying
    @Query("update Room r set r.reservationSwitch = :reservationSwitch where r.id = :id")
    void updateStatusById(@Param("id") Integer id, @Param("reservationSwitch") boolean reservationSwitch);

    List<Room> findByTeacherHomeId(Integer teacherHomeId);

    List<Room> findByTeacherHomeIdAndReservationSwitchTrue(Long teacherHomeId);

    @Query("SELECT r FROM Room r WHERE r.teacherHome.id = :teacherHomeId AND r.reservationSwitch = true AND r.id NOT IN (SELECT re.room.id FROM Reservation re WHERE (re.startDate <= :endDate) AND (re.endDate >= :startDate))")
    List<Room> getAvailableRooms(@Param("teacherHomeId") Integer teacherHomeId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
