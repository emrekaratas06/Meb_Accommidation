package meb.gov.tr.meb_accommidation.service;

import meb.gov.tr.meb_accommidation.entity.Guest;
import meb.gov.tr.meb_accommidation.entity.Reservation;
import meb.gov.tr.meb_accommidation.entity.Room;
import meb.gov.tr.meb_accommidation.entity.TeacherHome;
import meb.gov.tr.meb_accommidation.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    @Autowired
    private GuestRepository guestRepository;

    // E-posta adresine göre bir misafir bulur
    public Guest getGuestByEmail(String email) {
        return guestRepository.findByEmail(email);
    }

    // Telefon numarasına göre bir misafir bulur
    public Guest getGuestByPhone(String phone) {
        return guestRepository.findByPhone(phone);
    }

    // Çocuk statüsüne göre misafirleri bulur
    public List<Guest> getGuestsByChildStatus(boolean child) {
        return guestRepository.findByChild(child);
    }

    // Belirli bir rezervasyona ait misafirleri bulur
    public List<Guest> getGuestsByReservation(Reservation reservation) {
        return guestRepository.findByReservationsContaining(reservation);
    }

    // Yeni bir misafir kaydı oluşturur
    public Guest createGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    // Var olan bir misafir kaydını günceller
    public Guest updateGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    // Var olan bir misafir kaydını siler
    public void deleteGuest(Guest guest) {
        guestRepository.delete(guest);
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    //Get All TeacherHomes
    public List<Guest> findAll(){
        return guestRepository.findAll();
    }

    //Get TeacherHome By Id
    public Guest findById(Integer id) {
        return guestRepository.findById(id).orElse(null);
    }

    //Delete TeacherHome
    public void delete(int id) {
        guestRepository.deleteById(id);
    }

    //Update Employee
    public void save( Guest guest) {
        guestRepository.save(guest);
    }

    public Guest updateTeacherHome(Guest updatedGuest) {
        Guest existingguest = guestRepository.findById(updatedGuest.getId()).orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        // Diğer özelliklerin güncellenmesi
        existingguest.setFirstName(updatedGuest.getFirstName());
        existingguest.setLastName(updatedGuest.getLastName());
        existingguest.setEmail(updatedGuest.getEmail());
        existingguest.setPhone(updatedGuest.getPhone());
        existingguest.setChild(updatedGuest.isChild());
        // Diğer özelliklerin güncellenmesi için buraya ekleyin...

        // roomList özelliğinin güncellenmesi
        existingguest.getReservations().clear();
        existingguest.getReservations().addAll(updatedGuest.getReservations());

        return guestRepository.save(existingguest);
    }

}
