package meb.gov.tr.meb_accommidation.entity;
import lombok.*;

import javax.persistence.Table;
import java.util.Date;


@Getter
@Setter
@Table(name = "ReservationRequest")
public class ReservationRequest {
    private Date startDate;
    private Date endDate;
}
