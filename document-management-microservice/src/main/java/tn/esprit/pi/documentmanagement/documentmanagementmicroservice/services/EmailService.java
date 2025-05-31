package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.EvaluationDto;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    public void envoyerEmailEvaluation(String sujet, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo("sana.benhammouda@esprit.tn");
        helper.setSubject(sujet);
        helper.setText(message, true); // `true` pour HTML

        mailSender.send(mimeMessage);
    }
}
