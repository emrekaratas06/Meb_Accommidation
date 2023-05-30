package meb.gov.tr.meb_accommidation.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ReservationDAO {
    @Autowired
    private EntityManager entityManager;

    public List<LocalDate> getReservedDates() {
        String query = "SELECT DISTINCT r.startDate, r.endDate FROM Reservation r";
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(query, Object[].class);
        List<Object[]> results = typedQuery.getResultList();

        List<LocalDate> reservedDates = new ArrayList<>();
        for (Object[] result : results) {
            LocalDate startDate = (LocalDate) result[0];
            LocalDate endDate = (LocalDate) result[1];
            reservedDates.addAll(getDatesInRange(startDate, endDate));
        }

        return reservedDates;
    }

    private List<LocalDate> getDatesInRange(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dates;
    }
}
