package doktoree.backend.services;

import doktoree.backend.domain.ReservationNotification;
import doktoree.backend.mailtemplates.ChangeClassroomsTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {

        this.mailSender = mailSender;

    }


    @Override
    public void sendEmailChangeClassrooms(ReservationNotification reservationNotification)
        throws MessagingException {

        ChangeClassroomsTemplate template = new ChangeClassroomsTemplate(reservationNotification);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(template.getTo());
        helper.setText(template.formMailText(), true);
        helper.setSubject(template.getSubject());

        mailSender.send(message);


    }
}
