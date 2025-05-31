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
            // 1. Vérifier si le lien est accessible (on part du principe que le clone sera la vérification)
            File repoDir = Files.createTempDirectory("repo").toFile();
            Git.cloneRepository()
                    .setURI(lienGit)
                    .setDirectory(repoDir)
                    .call();
            note += 2; // Lien accessible
            commentaire.append("Lien accessible.\n");

            // 2. Vérifier si le dépôt n’est pas vide (présence de fichiers/dossiers)
            File[] fichiersRacine = repoDir.listFiles();
            if (fichiersRacine != null && fichiersRacine.length > 0) {
                note += 3;
                commentaire.append("Dépôt non vide.\n");
            } else {
                commentaire.append("Le dépôt est vide.\n");
                dto.setNote(note);
                dto.setCommentaire(commentaire.toString());
                return dto;
            }


            // 3. Vérifier les dossiers importants avec insensibilité à la casse
            boolean hasDtos = dossierExisteIgnoreCase(repoDir, "dto") || dossierExisteIgnoreCase(repoDir, "dto");
            boolean hasControllers = dossierExisteIgnoreCase(repoDir, "controller") || dossierExisteIgnoreCase(repoDir, "controller");
            boolean hasServices = dossierExisteIgnoreCase(repoDir, "service") || dossierExisteIgnoreCase(repoDir, "service");
            boolean hasRepositories = dossierExisteIgnoreCase(repoDir, "repositor") || dossierExisteIgnoreCase(repoDir, "repo");

            if (hasDtos) {
                commentaire.append("Dossier DTO(s) trouvé.\n");
                note += 4;
            } else {
                commentaire.append("Dossier DTO(s) manquant.\n");
                suggestion.append("Ajouter un dossier 'dtos' contenant les classes de transfert de données.\n");
            }
            if (hasControllers) {
                commentaire.append("Dossier Controller(s) trouvé.\n");
                note += 4;
            } else {
                commentaire.append("Dossier Controller(s) manquant.\n");
                suggestion.append("Ajouter un dossier 'controllers' avec les classes REST.\n");
            }
            if (hasServices) {
                commentaire.append("Dossier Service(s) trouvé.\n");
                note += 4;
            } else {
                commentaire.append("Dossier Service(s) manquant.\n");
                suggestion.append("Ajouter un dossier 'services' pour la logique métier.\n");
            }
            if (hasRepositories) {
                commentaire.append("Dossier Repository trouvé.\n");
                note += 4;
            } else {
                commentaire.append("Dossier Repository manquant.\n");
                suggestion.append("Ajouter un dossier 'repository' contenant les interfaces d'accès aux données.\n");
            }

            // 4. Vérifier présence de plusieurs fichiers pom.xml et *.properties pour détecter microservices
            int pomCount = 0;
            int propertiesCount = 0;
            List<File> allFiles = new ArrayList<>();
            // Récupérer tous les fichiers récursivement
            getAllFilesRecursively(repoDir, allFiles);

            for (File f : allFiles) {
                if (f.getName().equalsIgnoreCase("pom.xml")) pomCount++;
                if (f.getName().toLowerCase().endsWith(".properties")) propertiesCount++;
            }
            if (pomCount > 1 || propertiesCount > 1) {
                commentaire.append("Plusieurs fichiers pom.xml ou .properties détectés, suggérant une architecture microservices.\n");
                note += 3;
                suggestion.append("Travailler sur une architecture microservices bien organisée.\n");
            }

            // 5. Analyse des fichiers Java et méthodes (extrait de ton code)
            int totalFiles = 0, totalMethods = 0;
            for (String dossierNom : new String[]{"dto", "controller", "service", "repository"}) {
                File dossier = findDirectoryIgnoreCase(repoDir, dossierNom);
                if (dossier != null && dossier.isDirectory()) {
                    File[] files = dossier.listFiles((dir, name) -> name.endsWith(".java"));
                    if (files != null) {
                        totalFiles += files.length;
                        for (File file : files) {
                            List<String> lines = Files.readAllLines(file.toPath());
                            for (String line : lines) {
                                if (line.trim().startsWith("public") && line.contains("(")) totalMethods++;
                            }
                        }
                    }
                }
            }
            commentaire.append("Fichiers Java analysés : ").append(totalFiles).append("\n");
            commentaire.append("Méthodes publiques détectées : ").append(totalMethods).append("\n");

            if (totalFiles < 4) {
                suggestion.append("Ajouter plus de classes Java pour couvrir toutes les couches.\n");
                note -= 2;
            } else {
                note += 2;
            }
            if (totalMethods < 10) {
                suggestion.append("Ajouter plus de méthodes publiques, notamment CRUD et services.\n");
                note -= 2;
            } else {
                note += 2;
            }

            if (note < 0) note = 0;
            if (note > 20) note = 20;

            dto.setNote(note);
            dto.setCommentaire(commentaire.toString());
            dto.setSuggestion(suggestion.toString());
            dto.setDateEvaluation(new Date());

            // Nettoyage : supprimer le repo temporaire
            deleteFolderRecursively(repoDir);

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            dto.setNote(0.0);
            dto.setCommentaire("Erreur lors de l’analyse du dépôt Git : " + e.getMessage());
            dto.setSuggestion("Vérifier l'URL du dépôt et la connexion internet.");
            return dto;
        }
    }

    // Fonction utilitaire pour trouver un dossier en ignorant la casse
    boolean dossierExisteIgnoreCase(File parent, String nomDossier) {
        File[] files = parent.listFiles(File::isDirectory);
        if (files != null) {
            for (File f : files) {
                if (f.getName().equalsIgnoreCase(nomDossier)) return true;
            }
        }
        return false;
    }

    // Méthode pour récupérer tous les fichiers récursivement
    private void getAllFilesRecursively(File dir, List<File> fileList) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) getAllFilesRecursively(f, fileList);
                else fileList.add(f);
            }
        }
    }

    // Méthode pour chercher un dossier en ignorant la casse
    private File findDirectoryIgnoreCase(File parent, String name) {
        File[] files = parent.listFiles(File::isDirectory);
        if (files != null) {
            for (File f : files) {
                if (f.getName().equalsIgnoreCase(name)) return f;
            }
        }
        return null;
    }

    // Méthode pour supprimer un dossier récursivement (nettoyage)
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
