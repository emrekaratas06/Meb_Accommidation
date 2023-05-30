package meb.gov.tr.meb_accommidation.repository;

import meb.gov.tr.meb_accommidation.entity.Guest;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {
    // E-posta adresine göre bir misafiri bulur
    Guest findByEmail(String email);

    // Telefon numarasına göre bir misafiri bulur
    Guest findByPhone(String phone);

    // Çocuk statüsüne göre misafirleri bulur
    List<Guest> findByChild(boolean child);

    // Belirli bir rezervasyona ait misafirleri bulur
    List<Guest> findByReservationsContaining(Reservation reservation);

    List<Guest> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);

}
