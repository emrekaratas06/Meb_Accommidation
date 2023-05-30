package meb.gov.tr.meb_accommidation.service;

import meb.gov.tr.meb_accommidation.entity.*;
import meb.gov.tr.meb_accommidation.repository.GuestRepository;
import meb.gov.tr.meb_accommidation.repository.PaymentRepository;
import meb.gov.tr.meb_accommidation.repository.ReservationRepository;
import meb.gov.tr.meb_accommidation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ReservationService {
    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    PaymentRepository paymentRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private final GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

   public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
       this.guestRepository = guestRepository;
   }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation reservation) {
       return reservationRepository.save(reservation);
    }

/*
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
*/

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
    public boolean isRoomAvailable(Integer roomId, Integer teacherHomeId, LocalDate startDate, LocalDate endDate) {
        List<Reservation> reservations = reservationRepository.findByRoomIdAndTeacherHomeIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(roomId, teacherHomeId, endDate, startDate);
        return reservations.isEmpty();
    }

    public Reservation updateReservation(Reservation updatedReservation) {
        Reservation existingReservation = reservationRepository.findById(updatedReservation.getId()).orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // Diğer özelliklerin güncellenmesi
        existingReservation.setTeacherHomeId(updatedReservation.getTeacherHomeId());
        existingReservation.setRoom(updatedReservation.getRoom());
        existingReservation.setStartDate(updatedReservation.getStartDate());
        existingReservation.setEndDate(updatedReservation.getEndDate());
        existingReservation.setGuests(updatedReservation.getGuests());
        existingReservation.setCheckIn(updatedReservation.getCheckIn());
        existingReservation.setCheckOut(updatedReservation.getCheckOut());
        // Diğer özelliklerin güncellenmesi için buraya ekleyin...
        existingReservation.setStatus(ReservationStatus.PENDING);
        return reservationRepository.save(existingReservation);
    }

    public List<Reservation> findReservationsByGuestId(Integer guestId) {
        Guest guest = guestRepository.findById(guestId).orElse(null);
        if (guest != null) {
            return reservationRepository.findByGuestsContaining(guest);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Reservation> findReservationsByGuestName(String guestName) {
        List<Guest> guests = guestRepository.findByFirstNameContainingOrLastNameContaining(guestName, guestName);
        if (!guests.isEmpty()) {
            return reservationRepository.findByGuestsIn(guests);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Reservation> findReservationsByDateRange(Date startDate, Date endDate) {
        return reservationRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate);
    }

    public List<Reservation> findReservationsByFilter(Integer guestId, String guestName, LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> reservation = cq.from(Reservation.class);
        List<Predicate> predicates = new ArrayList<>();

        if (guestId != null) {
            Join<Reservation, Guest> guest = reservation.join("guests"); // Burada ilişkilendirmeyi açıkça belirtiyoruz.
            predicates.add(cb.equal(guest.get("id"), guestId));
        }

        if (guestName != null && !guestName.trim().isEmpty()) {
            Join<Reservation, Guest> guest = reservation.join("guests"); // Burada ilişkilendirmeyi açıkça belirtiyoruz.
            predicates.add(cb.like(cb.lower(guest.get("firstName")), "%" + guestName.toLowerCase() + "%"));
        }

        if (startDate != null && endDate != null) {
            predicates.add(cb.between(reservation.get("startDate"), startDate, endDate));
            predicates.add(cb.between(reservation.get("endDate"), startDate, endDate));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    public Reservation findByReservationRefCode(String reservationRefCode) {
        return reservationRepository.findByReservationRefCode(reservationRefCode);
    }
    @Transactional
    public void cancelReservation(String reservationRefCode) throws Exception {
        Reservation reservation = reservationRepository.findByReservationRefCode(reservationRefCode);

        if (reservation == null) {
            throw new Exception("Reservation not found with refCode: " + reservationRefCode);
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new Exception("Reservation is already cancelled for refCode: " + reservationRefCode);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    public Reservation confirmReservation(String reservationRefCode, String paymentRefCode) throws Exception {
        Reservation reservation = reservationRepository.findByReservationRefCode(reservationRefCode);

        if (reservation == null) {
            throw new Exception("Reservation not found with refCode: " + reservationRefCode);
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new Exception("Reservation is not in PENDING status for reservation with refCode: " + reservationRefCode);
        }

        Payment payment = paymentRepository.findByPaymentReference(paymentRefCode);

        if (payment == null) {
            throw new Exception("Payment not found with refCode: " + paymentRefCode);
        }

        if (payment.getReservation().getId() != reservation.getId()) {
            throw new Exception("Payment is not associated with the given reservation with refCode: " + reservationRefCode);
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
        return reservation;
    }

    public Reservation addReservation(Reservation reservation) {
        String reservationRefCode = UUID.randomUUID().toString();
        reservation.setReservationRefCode(reservationRefCode);
        reservation.setStatus(ReservationStatus.PENDING);
        return reservationRepository.save(reservation);
    }
    public void updateRoomStatusDates(Room room, String statusDates) {
        room.setStatusDates(statusDates);
        roomRepository.save(room);
    }
    public List<Reservation> getReservationsByRoom(Room room) {
        LocalDate now = LocalDate.now();
        List<Reservation> reservations = reservationRepository.findByRoom(room);
        List<Reservation> futureReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            LocalDate checkInDate = LocalDate.parse(reservation.getCheckIn());

            LocalDate checkOutDate = LocalDate.parse(reservation.getCheckOut());
            if (checkOutDate.isAfter(now)) {
                futureReservations.add(reservation);
            }
        }

        return futureReservations;
    }
    public List<String> getReservedDatesByRoomId(Integer roomId) {
        List<String> reservedDates = new ArrayList<>();

        // roomId parametresine göre rezervasyonları veritabanından alın
        List<Reservation> reservations = reservationRepository.findAllByRoomId(roomId);

        // Her rezervasyon için rezerve edilen tarihleri alın ve reservedDates listesine ekleyin
        for (Reservation reservation : reservations) {
            LocalDate startDate = reservation.getStartDate();
            LocalDate endDate = reservation.getEndDate();

            // startDate ve endDate arasındaki tüm tarihleri alarak reservedDates listesine ekleyin
            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
                reservedDates.add(date.toString());
                date = date.plusDays(1);
            }
        }

        return reservedDates;
    }

    public List<Reservation> getReservationsByRoomId(Integer roomId) {
        // roomId parametresine göre ilgili odanın rezervasyonlarını veritabanından alın ve döndürün
        // Örneğin, Reservation tablosundaki roomId alanını kullanarak rezervasyonları filtreleyebilirsiniz

        // Örnek olarak JPA kullanarak veritabanı sorgusu yapabilirsiniz
        String query = "SELECT r FROM Reservation r WHERE r.room.id = :roomId";
        TypedQuery<Reservation> typedQuery = entityManager.createQuery(query, Reservation.class);
        typedQuery.setParameter("roomId", roomId);
        List<Reservation> reservations = typedQuery.getResultList();

        return reservations;
    }

}