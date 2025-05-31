package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services;


import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.stereotype.Service;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.DocumentDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos.EvaluationDto;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Entities.Evaluation;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.repository.EvaluationRepository;
import tn.esprit.pi.documentmanagement.documentmanagementmicroservice.services.EvaluationService;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
        dto.setSuggestion(e.getSuggestion());
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
            commentaire.append("Le texte ne mentionne pas le mot 'module'. "+ "\n ");
            suggestion.append("Merci d'indiquer un module avec son nom précis. "+ "\n ");
        } else {
            note += 2;
            commentaire.append("Le module est mentionné. "+ "\n ");
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
            commentaire.append("Au moins 5 besoins fonctionnels sont listés. "+ "\n ");
        } else {
            commentaire.append("Moins de 5 besoins fonctionnels listés. ");
            suggestion.append("Ajoutez au moins 5 besoins fonctionnels clairement numérotés. "+ "\n ");
        }

        if (besoinsNonFonctionnelsCount >=5 ) {
            note += 8;
            commentaire.append("Au moins 5 besoins non fonctionnels sont listés. "+ "\n ");
        } else {
            commentaire.append("Moins de 5 besoins non fonctionnels listés. "+ "\n ");
            suggestion.append("Ajoutez au moins 5 besoins non fonctionnels clairement numérotés. "+ "\n ");
        }

        if (justificationPresente) {
            note += 2;
            commentaire.append("Des justifications sont présentes, ce qui améliore la qualité.+ \"\\n \" ");
        } else {
            commentaire.append("Pas de justification détectée. Ajouter des justifications pour plus de clarté. "+ "\n ");
            suggestion.append("Pensez à justifier chaque besoin pour mieux expliquer son importance. "+ "\n ");
        }

        if (note > 20) note = 20;
        return new AnalyseDetailleeResult(note, commentaire.toString().trim(), suggestion.toString());
    }

    public EvaluationDto evaluerDocumentText(EvaluationDto dto, DocumentDto documentDto) {
        String texte = documentDto.getContenu();

        AnalyseDetailleeResult result = analyserTexteEtudiantDetaille(texte);

        Evaluation evaluation = Evaluation.builder()
                .documentId(dto.getDocumentId())
                .enseignantId(dto.getEnseignantId())
                .note(result.note)
                .commentaire(result.commentaire + "\n ")
                .suggestion(result.suggestion)
                .dateEvaluation(new Date())
                .build();

        return toDto(evaluationRepository.save(evaluation));
    }

    public EvaluationDto analyserDepotGit(String lienGit) {
        EvaluationDto dto = new EvaluationDto();
        double note = 0;
        StringBuilder commentaire = new StringBuilder();
        StringBuilder suggestion = new StringBuilder();

        try {
            File repoDir = Files.createTempDirectory("repo").toFile();
            Git.cloneRepository()
                    .setURI(lienGit)
                    .setDirectory(repoDir)
                    .call();

            note += 2;
            commentaire.append("✔️ Lien Git accessible.\n");

            File[] fichiersRacine = repoDir.listFiles();
            if (fichiersRacine == null || fichiersRacine.length == 0) {
                commentaire.append("❌ Le dépôt est vide.\n");
                dto.setNote(note);
                dto.setCommentaire(commentaire.toString());
                suggestion.append("🔧 Ajouter du contenu au dépôt.\n");
                dto.setSuggestion(suggestion.toString());
                return dto;
            } else {
                note += 3;
                commentaire.append("✔️ Dépôt non vide.\n");
            }

            List<File> allFiles = new ArrayList<>();
            getAllFilesRecursively(repoDir, allFiles);

            int dtoCount = 0, serviceCount = 0, repoCount = 0, ctrlCount = 0;
            int javaFilesCount = 0, methodCount = 0, testCount = 0;
            boolean hasSecurity = false, hasConfig = false;
            HashSet<String> modules = new HashSet<>();

            for (File file : allFiles) {
                String nameLower = file.getName().toLowerCase();
                String pathLower = file.getAbsolutePath().toLowerCase();

                if (nameLower.equals("pom.xml")) {
                    modules.add(file.getParentFile().getAbsolutePath());
                }

                if (nameLower.equals("application.properties") || nameLower.equals("application.yml")) {
                    hasConfig = true;
                }

                if (file.getName().endsWith(".java")) {
                    javaFilesCount++;

                    if (pathLower.contains("dto")) dtoCount++;
                    if (pathLower.contains("service")) serviceCount++;
                    if (pathLower.contains("repo")) repoCount++;
                    if (pathLower.contains("controller")) ctrlCount++;

                    List<String> lines = Files.readAllLines(file.toPath());
                    for (String line : lines) {
                        String trimmed = line.trim().toLowerCase();
                        if (trimmed.startsWith("public") && line.contains("(")) {
                            methodCount++;
                        }
                        if (trimmed.contains("@test")) {
                            testCount++;
                        }
                        if (trimmed.contains("websecurityconfigureradapter") || trimmed.contains("springsecurity")) {
                            hasSecurity = true;
                        }
                    }
                }
            }

            commentaire.append("📁 Modules détectés : ").append(modules.size()).append("\n");
            commentaire.append("📄 Fichiers Java : ").append(javaFilesCount).append("\n");
            commentaire.append("📌 DTOs : ").append(dtoCount)
                    .append(", Services : ").append(serviceCount)
                    .append(", Repositories : ").append(repoCount)
                    .append(", Controllers : ").append(ctrlCount).append("\n");
            commentaire.append("🔧 Méthodes publiques détectées : ").append(methodCount).append("\n");
            commentaire.append("🧪 Tests unitaires : ").append(testCount).append("\n");

            if (hasConfig) {
                commentaire.append("⚙️ Fichier de configuration trouvé.\n");
                note += 1;
            } else {
                suggestion.append("🔧 Ajouter un fichier de configuration `application.properties` ou `application.yml`.\n");
            }

            if (hasSecurity) {
                commentaire.append("🔒 Sécurité détectée dans le projet.\n");
                note += 2;
                suggestion.append("✅ Bon point : le projet intègre un module de sécurité.\n");
            } else {
                suggestion.append("🔐 Ajouter un module de sécurité avec Spring Security.\n");
            }

            if (testCount > 0) {
                note += 2;
                suggestion.append("✅ Tests unitaires présents. Vous pouvez compléter par des tests d’intégration.\n");
            } else {
                suggestion.append("🧪 Ajouter des tests unitaires avec JUnit.\n");
            }

            // Points par composant
            if (dtoCount > 0) note += 2;
            else suggestion.append("📦 Ajouter des classes DTO pour structurer les données.\n");

            if (serviceCount > 0) note += 2;
            else suggestion.append("🧠 Ajouter des services pour la logique métier.\n");

            if (repoCount > 0) note += 2;
            else suggestion.append("💾 Ajouter des repositories pour l'accès aux données.\n");

            if (ctrlCount > 0) note += 2;
            else suggestion.append("🌐 Ajouter des contrôleurs REST pour exposer les APIs.\n");

            if (javaFilesCount < 4) {
                note -= 1;
                suggestion.append("📄 Ajouter plus de classes Java pour enrichir le projet.\n");
            } else {
                note += 1;
                suggestion.append("👍 Bonne base de code. Pensez à modulariser si le projet grandit.\n");
            }

            if (methodCount < 10) {
                note -= 1;
                suggestion.append("🔧 Ajouter plus de méthodes publiques pour couvrir les fonctionnalités attendues.\n");
            } else {
                note += 1;
            }

            if (modules.size() > 1) {
                commentaire.append("🏗️ Architecture multi-modules détectée.\n");
                note += 2;
                suggestion.append("✅ Bien joué pour l’approche modulaire. Veiller à bien découpler les responsabilités.\n");
            } else {
                suggestion.append("🧱 Envisager une structure multi-modules si le projet devient complexe.\n");
            }

            // Note finale
            note = Math.max(0, Math.min(20, note));

            dto.setNote(note);
            dto.setCommentaire(commentaire.toString());
            dto.setSuggestion(suggestion.toString());
            dto.setDateEvaluation(new Date());

            deleteFolderRecursively(repoDir);
            return dto;

        } catch (Exception e) {
            dto.setNote(0.0);
            dto.setCommentaire("❌ Erreur lors de l’analyse du dépôt Git : " + e.getMessage());
            dto.setSuggestion("Vérifier l'URL du dépôt et la connexion internet.");
            return dto;
        }
    }

    private void getAllFilesRecursively(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) getAllFilesRecursively(f, fileList);
                else fileList.add(f);
            }
        }
    }

    private void deleteFolderRecursively(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) deleteFolderRecursively(f);
                else f.delete();
            }
        }
        folder.delete();
    }






}
