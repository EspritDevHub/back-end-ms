package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.controllers;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.EvaluationDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.EmailService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/envoyer-email-evaluation")
public class EvaluationEmailController {

        private final EmailService emailService;

        @PostMapping("/envoyer-email-evaluation")
        public void envoyerEmail(@RequestBody Map<String, String> payload) throws MessagingException {
            String note = payload.get("note");
            String commentaire = payload.get("commentaire");
            String suggestion = payload.get("suggestion");

            String message = """
                <h3>Nouvelle évaluation Proposez :Veuillez Réctifier votre rendu avant la date limite</h3>
                <p><strong>Note :</strong> %s</p>
                <p><strong>Commentaire :</strong> %s</p>
                <p><strong>Suggestion :</strong> %s</p>
                """.formatted(note, commentaire, suggestion);

            emailService.envoyerEmailEvaluation("Évaluation Étudiant", message);
        }
    }


