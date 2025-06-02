package com.example.projectmanagement.services;

import com.example.projectmanagement.Entities.Tache;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String to, String subject, String body, boolean isHtml) {
        try {
            if (to == null || to.isEmpty()) {
                System.err.println("❌ L'adresse e-mail du destinataire est vide ou nulle.");
                return;
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml); // ✅ Enable HTML content if needed

            mailSender.send(message);
            System.out.println("✅ Email envoyé à : " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de l'envoi du mail : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void notifierChangementTache(Tache ancienne, Tache nouvelle) {
        String nouvelEmail = nouvelle.getAssigneA();
        String ancienEmail = ancienne.getAssigneA();

        if (nouvelEmail == null || nouvelEmail.isEmpty()) {
            System.err.println("❌ Aucun email pour l'utilisateur assigné à la tâche.");
            return;
        }

        // Vérifie changement de statut
        if (!ancienne.getEtat().equals(nouvelle.getEtat())) {
            sendEmail(nouvelEmail,
                    "Changement de statut de votre tâche",
                    "Bonjour,\n\nLe statut de la tâche '" + nouvelle.getTitre() +
                            "' a changé de " + ancienne.getEtat() + " à " + nouvelle.getEtat() + ".\n\nCordialement.",false);
        }

        // Vérifie changement d'assignation
        if (ancienEmail != null && !ancienEmail.equals(nouvelEmail)) {
            String sujet = "Nouvelle tâche assignée";
            String contenuHtml = """
                    <html>
                      <body style="font-family: Arial, sans-serif; color: #333;">
                        <h2 style="color: #2E86C1;">Nouvelle tâche assignée</h2>
                        <p>Bonjour,</p>
                        <p>Une nouvelle tâche vous a été assignée :</p>
                        <blockquote style="background-color: #f0f0f0; border-left: 5px solid #2E86C1; margin: 10px 0; padding: 10px;">
                          <strong>%s</strong>
                        </blockquote>
                        <p>Merci de bien vouloir prendre connaissance de cette tâche et la traiter dans les délais.</p>
                        <br/>
                        <p>Cordialement,</p>
                        <p><em>Votre équipe Projet Management</em></p>
                      </body>
                    </html>
                    """.formatted(nouvelle.getTitre());
            sendEmail(nouvelEmail, sujet, contenuHtml, true);
        }
    }
}
