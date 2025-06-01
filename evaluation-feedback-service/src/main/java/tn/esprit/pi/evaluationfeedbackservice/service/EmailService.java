package tn.esprit.pi.evaluationfeedbackservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLowRatingAlertToAdmin(Long projectId, double avgRating/*, Long userId*/) {
        // get the project owner email here
        String to = "idriss.belhajhammeda@esprit.tn";
        String subject = "⚠️ Project Requires Review";
        String body = String.format("Project with ID %d has an average rating of %.2f.\nPlease consider reviewing it.", projectId, avgRating);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
