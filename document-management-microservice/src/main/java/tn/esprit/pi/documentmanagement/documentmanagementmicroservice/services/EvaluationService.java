package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;


import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.DocumentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.EvaluationDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Evaluation;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository.EvaluationRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.EvaluationService;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EvaluationService  {

    private final EvaluationRepository evaluationRepository;

    public EvaluationDto evaluerDocument(EvaluationDto dto) {
        Evaluation evaluation = Evaluation.builder()
                .documentId(dto.getDocumentId())
                .enseignantId(dto.getEnseignantId())
                .note(dto.getNote())
                .commentaire(dto.getCommentaire())
                .fichierEvaluationUrl(dto.getFichierEvaluationUrl())
                .dateEvaluation(new Date())
                .build();

        return toDto(evaluationRepository.save(evaluation));
    }

    public List<EvaluationDto> getEvaluationsByDocument(String documentId) {
        return evaluationRepository.findByDocumentId(documentId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<EvaluationDto> getEvaluationsByEnseignant(String enseignantId) {
        return evaluationRepository.findByEnseignantId(enseignantId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    private EvaluationDto toDto(Evaluation e) {
        EvaluationDto dto = new EvaluationDto();
        dto.setId(e.getId());
        dto.setDocumentId(e.getDocumentId());
        dto.setEnseignantId(e.getEnseignantId());
        dto.setNote(e.getNote());
        dto.setCommentaire(e.getCommentaire());
        dto.setFichierEvaluationUrl(e.getFichierEvaluationUrl());
        dto.setDateEvaluation(e.getDateEvaluation());
        return dto;
    }



    @Value("${openai.api.key}")
    private String openaiApiKey;

    public EvaluationDto analyserTexteAutomatiquement(String texte) {
        OpenAiService service = new OpenAiService(openaiApiKey);

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-4o-mini")
                .prompt("Lis et évalue ce texte d'étudiant :\n\n" + texte +
                        "\n\nDonne une note sur 20 et un commentaire pédagogique sous cette forme :\n" +
                        "Note: X/20\nCommentaire: ...")
                .temperature(0.7)
                .maxTokens(200)
                .build();

        CompletionResult result = service.createCompletion(request);
        String aiResponse = result.getChoices().get(0).getText().trim();

        // Parsing du résultat de l’IA
        String[] lignes = aiResponse.split("\n");
        double note = 0.0;
        String commentaire = "";

        for (String ligne : lignes) {
            if (ligne.toLowerCase().contains("note")) {
                try {
                    note = Double.parseDouble(ligne.replaceAll("[^0-9.]", ""));
                } catch (Exception ignored) {}
            } else if (ligne.toLowerCase().contains("commentaire")) {
                commentaire = ligne.replace("Commentaire:", "").trim();
            }
        }

        EvaluationDto dto = new EvaluationDto();
        dto.setNote(note);
        dto.setCommentaire(commentaire);
        dto.setDateEvaluation(new Date());
        return dto;
    }

    private static class AnalyseDetailleeResult {
        double note;
        String commentaire;
        String suggestion;

        public AnalyseDetailleeResult(double note, String commentaire, String suggestion) {
            this.note = note;
            this.commentaire = commentaire;
            this.suggestion = suggestion;
        }
    }

    private AnalyseDetailleeResult analyserTexteEtudiantDetaille(String texte) {
        if (texte == null || texte.isBlank()) {
            return new AnalyseDetailleeResult(0, "Le texte est vide.", "Merci de rédiger le texte avec les besoins attendus.");
        }

        String lowerText = texte.toLowerCase();

        StringBuilder commentaire = new StringBuilder();
        StringBuilder suggestion = new StringBuilder();

        double note = 0;

        if (!lowerText.contains("module")) {
            commentaire.append("Le texte ne mentionne pas le mot 'module'. ");
            suggestion.append("Merci d'indiquer un module avec son nom précis. ");
        } else {
            note += 2;
            commentaire.append("Le module est mentionné. ");
        }


        Pattern besoinPattern = Pattern.compile("besoin n°\\d+\\s*:\\s*(.+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = besoinPattern.matcher(texte);

        int besoinsFonctionnelsCount = 0;
        int besoinsNonFonctionnelsCount = 0;
        boolean justificationPresente = false;

        while (matcher.find()) {
            String besoin = matcher.group(1).toLowerCase();

            if (besoin.contains("fonctionnel")) {
                besoinsFonctionnelsCount++;
            } else if (besoin.contains("non fonctionnel") || besoin.contains("non-fonctionnel")) {
                besoinsNonFonctionnelsCount++;
            } else {
                if (besoin.contains("performance") || besoin.contains("sécurité") || besoin.contains("maintenabilité") || besoin.contains("robustesse")) {
                    besoinsNonFonctionnelsCount++;
                } else {
                    besoinsFonctionnelsCount++;
                }
            }

            if (besoin.matches(".*(justification|car|parce que|en effet|puisque).*")) {
                justificationPresente = true;
            }
        }

        if (besoinsFonctionnelsCount >= 5) {
            note += 8;
            commentaire.append("Au moins 5 besoins fonctionnels sont listés. ");
        } else {
            commentaire.append("Moins de 5 besoins fonctionnels listés. ");
            suggestion.append("Ajoutez au moins 5 besoins fonctionnels clairement numérotés. ");
        }

        if (besoinsNonFonctionnelsCount >= ) {
            note += 8;
            commentaire.append("Au moins 5 besoins non fonctionnels sont listés. ");
        } else {
            commentaire.append("Moins de 5 besoins non fonctionnels listés. ");
            suggestion.append("Ajoutez au moins 5 besoins non fonctionnels clairement numérotés. ");
        }

        // Bonus justification
        if (justificationPresente) {
            note += 2;
            commentaire.append("Des justifications sont présentes, ce qui améliore la qualité. ");
        } else {
            commentaire.append("Pas de justification détectée. Ajouter des justifications pour plus de clarté. ");
            suggestion.append("Pensez à justifier chaque besoin pour mieux expliquer son importance. ");
        }

        if (note > 20) note = 20;

        return new AnalyseDetailleeResult(note, commentaire.toString().trim(), suggestion.toString().trim());
    }

    public EvaluationDto evaluerDocumentText(EvaluationDto dto, DocumentDto documentDto) {
        String texte = documentDto.getContenu();

        AnalyseDetailleeResult result = analyserTexteEtudiantDetaille(texte);

        Evaluation evaluation = Evaluation.builder()
                .documentId(dto.getDocumentId())
                .enseignantId(dto.getEnseignantId())
                .note(result.note)
                .commentaire(result.commentaire + "\nSuggestion: " + result.suggestion)
                .fichierEvaluationUrl(dto.getFichierEvaluationUrl())
                .dateEvaluation(new Date())
                .build();

        return toDto(evaluationRepository.save(evaluation));
    }
}
