package doktoree.backend.mailTemplates;

import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.ReservationNotification;
import lombok.Data;

import java.io.Serializable;
import java.util.stream.Collectors;

@Data
public class ChangeClassroomsTemplate implements Serializable {

    private ReservationNotification reservationNotification;

    public ChangeClassroomsTemplate(ReservationNotification reservationNotification){

        this.reservationNotification = reservationNotification;

    }

    public String getTo(){

        return reservationNotification.getUser().getEmail();

    }

    public String getSubject(){

        return reservationNotification.getMessage();

    }

    public String formMailText(){

        Reservation reservation = reservationNotification.getReservaiton();

        String text = "Poštovani, <br><br>" +
                "Došlo je do promene rezervacije:<br>" +
                "<b>Datum:</b> " + reservation.getDate() + "<br>" +
                "<b>Vreme:</b> " + reservation.getStartTime() + " - " + reservation.getEndTime() + "<br>" +
                "<b>Odobrene sale:</b><br>" +
                "<ul>" +
                reservation.getClassrooms().stream()
                        .map(classroom -> "<li>" + classroom.getClassRoomNumber() + "</li>")
                        .collect(Collectors.joining()) +
                "</ul>";

        return text;

    }

}
