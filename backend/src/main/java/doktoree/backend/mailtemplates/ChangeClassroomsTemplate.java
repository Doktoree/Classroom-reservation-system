package doktoree.backend.mailtemplates;

import doktoree.backend.domain.Reservation;
import doktoree.backend.domain.ReservationNotification;
import lombok.Data;

import java.util.stream.Collectors;

@Data
public class ChangeClassroomsTemplate {

    private ReservationNotification reservationNotification;

    public ChangeClassroomsTemplate(ReservationNotification reservationNotification) {

        this.reservationNotification = reservationNotification;

    }

    public String getTo() {

        return reservationNotification.getUser().getEmail();

    }

    public String getSubject() {

        return reservationNotification.getMessage();

    }

    public String formMailText() {

        Reservation reservation = reservationNotification.getReservation();

        String text = "Dear Sir/Madam, <br><br>"
            + "There has been a change in the reservation:<br>"
            + "<b>Date:</b> " + reservation.getDate() + "<br>"
            + "<b>Time:</b> " + reservation.getStartTime()
            + " - "
            + reservation.getEndTime() + "<br>"
            + "<b>Classrooms:</b><br>"
            + "<ul>"
            + reservation.getClassrooms().stream()
            .map(classroom -> "<li>" + classroom.getClassRoomNumber() + "</li>")
            .collect(Collectors.joining())
            + "</ul>";

        return text;

    }

}
