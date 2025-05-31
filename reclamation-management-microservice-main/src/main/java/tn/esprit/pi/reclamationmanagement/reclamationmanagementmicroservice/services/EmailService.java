package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendReclamationResolvedEmail(String to, String reclamationTitle, String jiraId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Your Reclamation has been Resolved");
            helper.setFrom("sebteouii@gmail.com");

            String htmlBody = buildHtmlBody(reclamationTitle, jiraId);
            helper.setText(htmlBody, true);

            // Load logo image from resources/static
            ClassPathResource logo = new ClassPathResource("static/logo.png");

            if (logo.exists()) {
                helper.addInline("companyLogo", logo);
            } else {
                System.err.println("⚠️ Logo image not found at static/logo.png");
            }

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildHtmlBody(String reclamationTitle, String jiraId) {
        String jiraLine = (jiraId != null && !jiraId.isEmpty())
                ? """
                  <p style="font-size: 16px;">
                    <strong>Jira Ticket:</strong> 
                    <a href="https://jira.yourcompany.com/browse/%s" target="_blank" style="color:#2E86C1;">%s</a>
                  </p>
                  """.formatted(jiraId, jiraId)
                : "";

        return """
            <html>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; margin:0; padding:0; background-color:#f4f6f8;">
              <table align="center" cellpadding="0" cellspacing="0" width="600" style="background-color:#fff; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                <tr>
                  <td align="center" style="padding: 30px 0 20px 0;">
                    <img src="cid:companyLogo" alt="Company Logo" width="80" style="display:block;" />
                  </td>
                </tr>
                <tr>
                  <td style="padding: 0 40px 30px 40px;">
                    <h2 style="color: #2E86C1; margin-bottom: 10px;">🎉 Reclamation Resolved</h2>
                    <p style="font-size: 16px;">Hello,</p>
                    <p style="font-size: 16px; line-height: 1.5;">
                      We're happy to inform you that your reclamation titled <strong>%s</strong> has been successfully marked as 
                      <span style="color: green; font-weight: bold;">RESOLVED ✅</span>.
                    </p>
                    %s
                    <p style="font-size: 16px; line-height: 1.5;">
                      Thank you for your patience and trust in our service. If you have any further questions or need assistance, feel free to contact our support team.
                    </p>
                    <p style="font-size: 16px;">Best regards,<br/>The Reclamation Management Team</p>
                  </td>
                </tr>
                <tr>
                  <td style="background-color:#2E86C1; color:#fff; text-align:center; padding:15px; font-size:14px; border-radius: 0 0 8px 8px;">
                    This is an automated notification from Reclamation Management System.<br/>
                    &copy; 2025 Your Company. All rights reserved.
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(reclamationTitle, jiraLine);
    }
}
