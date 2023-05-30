package meb.gov.tr.meb_accommidation.repository;

import meb.gov.tr.meb_accommidation.entity.Availability;
import meb.gov.tr.meb_accommidation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
//Bu metot, belirli bir `Room` nesnesi için belirli bir tarih aralığı boyunca müsait olan oda sayısını verir.
// Eğer bu sayı sıfırdan farklı ise, oda müsaittir ve rezervasyon yapılabilir.
    @Query("SELECT COUNT(a) FROM Availability a WHERE a.room01.id = :roomId AND a.available = true AND (a.startDate BETWEEN :startDate AND :endDate) AND (a.endDate BETWEEN :startDate AND :endDate)")
    Long countAvailableRooms(@Param("roomId") Integer roomId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

//Bu, Room nesnesinin belirli bir tarih aralığı boyunca müsait olup olmadığını kontrol etmek için kullanılabilir.
// Bu, Availability tablosunda available alanının true olduğu kayıtları döndürür.
    List<Availability> findByRoom01AndStartDateGreaterThanEqualAndEndDateLessThanEqualAndAvailableIsTrue(Room room,
                                                                                                         Date startDate, Date endDate);
//Bu yöntem findAvailableRooms isimli bir sorgu metodu kullanır.
// Bu yöntem, başlangıç tarihi ve bitiş tarihi parametreleri alır ve bu tarihler arasında müsait olan Room nesnelerinin bir listesini döndürür.
    @Query("SELECT DISTINCT a.room01 FROM Availability a WHERE a.available = true AND a.room01 NOT IN "
            + "(SELECT DISTINCT a2.room01 FROM Availability a2 WHERE ((a2.startDate BETWEEN :startDate AND :endDate) "
            + "OR (a2.endDate BETWEEN :startDate AND :endDate)) AND a2.available = true)")
    List<Room> findAvailableRooms(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


  //  Bu sorgu, Availability tablosundaki verilere dayanarak, belirli bir tarih aralığı boyunca müsait olan SingleRoom tipi Room nesnelerini döndürür.
    @Query("SELECT DISTINCT a.room01 FROM Availability a WHERE a.available = true AND a.room01.roomType = 'SingleRoom' AND a.room01 NOT IN "
            + "(SELECT DISTINCT a2.room01 FROM Availability a2 WHERE ((a2.startDate BETWEEN :startDate AND :endDate) "
            + "OR (a2.endDate BETWEEN :startDate AND :endDate)) AND a2.available = true)")
    List<Room> findAvailableSingleRooms(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    //Bu yöntem, başlangıç tarihi ve bitiş tarihi parametreleri alır ve bu tarih aralığında endDate alanı bugün olan Room nesnelerinin bir listesini döndürür.
    @Query("SELECT DISTINCT a.room01 FROM Availability a WHERE a.endDate = :endDate AND a.startDate BETWEEN :startDate AND :endDate")
    List<Room> findRoomsByEndDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


   // Bu sorgu, veritabanında Availability tablosunda oda numarası, başlangıç tarihi ve bitiş tarihiyle eşleşen ve müsaitlik durumu true olan kayıtları sayacaktır.
   // Bu sayı 0'dan büyükse, oda müsait değildir ve false döndürülür. Aksi takdirde, oda müsaittir ve true döndürülür.
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Availability a " +
            "WHERE a.room01 = :room AND a.startDate <= :endDate AND a.endDate >= :startDate AND a.available = true")
    boolean checkAvailability(@Param("room") Room room, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    //Bu sorgu, verilen oda, başlangıç tarihi ve bitiş tarihi için Availability tablosunda bir kaydı güncelleyecektir.
    @Modifying
    @Query("UPDATE Availability a SET a.available = :availability WHERE a.room01 = :room AND a.startDate = :startDate AND a.endDate = :endDate")
    void updateAvailability(@Param("room") Room room, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("availability") Boolean availability);

}
