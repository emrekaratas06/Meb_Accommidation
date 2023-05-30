package meb.gov.tr.meb_accommidation.service;
import meb.gov.tr.meb_accommidation.entity.*;
import meb.gov.tr.meb_accommidation.repository.ReservationRepository;
import meb.gov.tr.meb_accommidation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationService reservationService;
    public Room getRoomById(Integer id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            return room.get();
        } else {
            throw new EntityNotFoundException("Room with id " + id + " not found.");
        }
    }
    public List<Room> findAllBetweenDates(String dateFrom, String dateTo) {
        return roomRepository.findAllBetweenDates(dateFrom, dateTo);
    }

    public List<Room> findByTeacherHome(TeacherHome teacherHome) {
        return roomRepository.findByTeacherHome(teacherHome);
    }
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public List<Room> findAllByOrderByRoomNumberAsc() {
        return roomRepository.findAllByOrderByRoomNumberAsc();
    }

    public List<Room> findAllByStatusTrueOrderByRoomNumberAsc() {
        return roomRepository.findByReservationSwitchTrueOrderByRoomNumberAsc();
    }

    public Optional<Room> findByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    public List<Room> findByBeds(Integer beds) {
        return roomRepository.findByBeds(beds);
    }

    public List<Room> findByRoomType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    public List<Room> findByCostPerNightLessThan(BigDecimal costPerNight) {
        return roomRepository.findByCostPerNightLessThan(costPerNight);
    }

    public void updateStatusById(Integer id, boolean reservationSwitch) {
        roomRepository.updateStatusById(id, reservationSwitch);
    }


    public Room save(Room room) {
        return roomRepository.save(room);
    }

   /* public void delete(int id) {
        roomRepository.deleteById(id);
    }*/
   public void delete(Integer id) {
       roomRepository.deleteById(id);
   }
    public Room updateRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> findByTeacherHomeId(Integer teacherHomeId) {
        return roomRepository.findByTeacherHomeId(teacherHomeId);
    }
    public Optional<Room> findById(Integer id) {
        return roomRepository.findById(id);
    }

    @Transactional
    public void toggleReservationSwitch(Integer roomId, boolean isChecked) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            // Oda durumunu değiştirin
            room.setReservationSwitch(isChecked);
            roomRepository.save(room);

            if (!isChecked) {
                // Rezervasyonları silin
                List<Reservation> reservations = reservationRepository.findAllByRoomId(Integer.valueOf(room.getId()));
                for (Reservation reservation : reservations) {
                    reservationRepository.delete(reservation);
                }
            }
        }
    }
    public List<Room> getRoomsByTeacherHomeId(Long teacherHomeId) {
        return roomRepository.findByTeacherHomeIdAndReservationSwitchTrue(teacherHomeId);
    }
    public boolean isRoomAvailableForDateRange(Integer roomId, LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = reservationRepository.findAllByRoomId(roomId);

        for (Reservation reservation : reservations) {
            LocalDate reservationStartDate = reservation.getStartDate();
            LocalDate reservationEndDate = reservation.getEndDate();

            if (startDate.isBefore(reservationEndDate) && endDate.isAfter(reservationStartDate)) {
                return false;
            }
        }
        return true;
    }

    public Reservation saveReservation(Reservation reservation) {
        Room room = roomRepository.findById(Integer.valueOf(reservation.getRoom().getId())).orElse(null);
        if (room == null || !room.isReservationSwitch()) {
            throw new IllegalArgumentException("Rezervasyon yapılamaz, oda rezervasyona kapalı.");
        }

        return reservationRepository.save(reservation);
    }
    public List<Room> getAvailableRooms(Integer teacherHomeId, Date startDate, Date endDate) {
        return roomRepository.getAvailableRooms(teacherHomeId, startDate, endDate);
    }
    //Odayı silmeden önce  o odaya ait rezervasyonların olup olmadığını kontrol etmeniz gerekir
    public void deleteRoomAndReservations(Integer roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            // Bu odanın rezervasyonları var mı diye kontrol et
            if (!room.getReservation().isEmpty()) {
                reservationRepository.deleteByRoom_RoomNumber(room.getRoomNumber());
            }
            // Odayı sil
            roomRepository.delete(room);
        }
    }

    @Transactional
    public void updateRoomStatus(Integer roomId, RoomStatus newStatus) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setRoomStatuses(newStatus);
            roomRepository.save(room);
        } else {
            throw new EntityNotFoundException("Room with id " + roomId + " not found.");
        }
    }
    public boolean isRoomAvailable(Long roomId, LocalDate date) {
        // oda ve belirtilen tarih için rezervasyonları getir
        List<Reservation> reservations = reservationRepository.findByRoomIdAndDate(roomId, date);
        // Eğer herhangi bir rezervasyon varsa oda müsait değil, yoksa müsait
        return reservations.isEmpty();
    }

}