package tn.esprit.pi.usermanagement.suermanagementmicroservice.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendWelcomeEmail(String toEmail, String userName, String espritId, String password) {
        String subject = "🎉 Welcome to Esprit!";
        String htmlContent = generateHtmlContent(userName, espritId, password);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("jobjob.bs2@gmail.com");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("Email sent to: " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendForgotPasswordEmail(String toEmail, String userName, int twoFactorCode) {
        String subject = "🔐 Reset Your Password";
        String htmlContent = generateForgotPasswordContent(userName, twoFactorCode);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom("jobjob.bs2@gmail.com");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("2FA email sent to: " + toEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send forgot password email", e);
        }
    }

    private String generateHtmlContent(String userName, String espritId, String password) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("espritId", espritId);
        context.setVariable("password", password);

        return templateEngine.process("welcome-email", context);
    }

    private String generateForgotPasswordContent(String userName, int twoFactorCode) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("twoFactorCode", twoFactorCode);

        return templateEngine.process("forgot-password", context);
    }
}



