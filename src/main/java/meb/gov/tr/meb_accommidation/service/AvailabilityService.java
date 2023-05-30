package meb.gov.tr.meb_accommidation.service;

import meb.gov.tr.meb_accommidation.entity.Availability;
import meb.gov.tr.meb_accommidation.entity.Room;
import meb.gov.tr.meb_accommidation.repository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    // Belirli bir Room nesnesi için belirli bir tarih aralığı boyunca müsait olan oda sayısını döndürür.
    public Long countAvailableRooms(Room room, Date startDate, Date endDate) {
        return availabilityRepository.countAvailableRooms(Integer.valueOf(room.getId()), startDate, endDate);
    }

    // Room nesnesinin belirli bir tarih aralığı boyunca müsait olup olmadığını kontrol etmek için kullanılabilir.
    public boolean isRoomAvailable(Room room, Date startDate, Date endDate) {
        List<Availability> availabilities = availabilityRepository.findByRoom01AndStartDateGreaterThanEqualAndEndDateLessThanEqualAndAvailableIsTrue(room, startDate, endDate);
        return !availabilities.isEmpty();
    }

    // Başlangıç tarihi ve bitiş tarihi parametreleri alır ve bu tarihler arasında müsait olan Room nesnelerinin bir listesini döndürür.
    public List<Room> findAvailableRooms(Date startDate, Date endDate) {
        return availabilityRepository.findAvailableRooms(startDate, endDate);
    }

    // Bu sorgu, Availability tablosundaki verilere dayanarak, belirli bir tarih aralığı boyunca müsait olan SingleRoom tipi Room nesnelerini döndürür.
    public List<Room> findAvailableSingleRooms(Date startDate, Date endDate) {
        return availabilityRepository.findAvailableSingleRooms(startDate, endDate);
    }

    // Bu yöntem, başlangıç tarihi ve bitiş tarihi parametreleri alır ve bu tarih aralığında endDate alanı bugün olan Room nesnelerinin bir listesini döndürür.
    public List<Room> findRoomsByEndDate(Date startDate, Date endDate) {
        return availabilityRepository.findRoomsByEndDate(startDate, endDate);
    }

    // Verilen oda, başlangıç tarihi ve bitiş tarihi için Availability tablosunda bir kaydı günceller.
    public void updateAvailability(Room room, Date startDate, Date endDate, boolean availability) {
        availabilityRepository.updateAvailability(room, startDate, endDate, availability);
    }

    // Verilen oda, başlangıç tarihi ve bitiş tarihi için müsaitlik durumunu kontrol eder.
    public boolean checkAvailability(Room room, Date startDate, Date endDate) {
        return availabilityRepository.checkAvailability(room, startDate, endDate);
    }
}
