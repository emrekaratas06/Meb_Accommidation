package meb.gov.tr.meb_accommidation.repository;

import meb.gov.tr.meb_accommidation.entity.Guest;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {
    //Bu sorgu, belirtilen Room nesnesi için belirtilen tarih aralığı boyunca Availability tablosundaki tüm kayıtların
    // available alanını false olarak ayarlar,böylece oda rezerve edilir.
    @Modifying
    @Query("UPDATE Availability a SET a.available = false WHERE a.room01 = :room AND a.startDate >= :startDate AND a.endDate <= :endDate")
    void reserveRoom(@Param("room") Integer room, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //Room nesnesi için belirtilen tarih aralığı boyunca Availability tablosundaki tüm kayıtların
    // available alanını true olarak ayarlar, böylece oda rezervasyonu iptal edilir.
    @Modifying
    @Query("UPDATE Availability a SET a.available = true WHERE a.room01 = :room AND a.startDate >= :startDate AND a.endDate <= :endDate")
    void cancelReservation(@Param("room") Room room, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //Verilen ogretmenEvi'ndeki odanın tüm rezervasyonlarını almak için

    List<Reservation> findByRoomIdAndTeacherHomeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer roomId, Integer teacherHomeId, LocalDate endDate, LocalDate startDate);

    // ReservationRepository
    List<Reservation> findByGuestsContaining(Guest guest);
    List<Reservation> findByGuestsIn(List<Guest> guests);

    List<Reservation> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(Date startDate,Date endDate);

    List<Reservation> findAllByRoomId(Integer roomId);

    // Rezervasyonları oda numarasına göre silen metot
    //bir odayı silmeden önce o odaya ait tüm rezervasyonları silen bir metot yazmanız gerekiyor
    @Transactional
    void deleteByRoom_RoomNumber(String roomNumber);

    Reservation findByReservationRefCode(String reservationRefCode);


    List<Reservation> findByRoom(Room room);

    List<Reservation> findByRoomIdAndDate(Long roomId, LocalDate date);

}
