package doktoree.backend.services;


import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.mailTemplates.ChangeClassroomsTemplate;
import jakarta.mail.MessagingException;

public interface MailService {

    public void sendEmailChangeClassrooms(ReservationNotification reservationNotification) throws MessagingException;

}
