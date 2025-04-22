package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;

import org.springframework.data.annotation.Id;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enums.DocumentStatus;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Enums.DocumentType;

import java.util.Date;

public class Document {

    @Id
    private String id;
    private String title;            // Titre du document
    private DocumentType type;       // Type du document (Git, Fichier, Screenshot, Q&A)
    private String filePath;         // Chemin du fichier (si applicable)
    private String gitLink;          // Lien Git (si applicable)
    private Date submissionDate;     // Date de soumission
    private Date deadline;           // Date limite de soumission
    private DocumentStatus status;   // Statut du document (In Progress, In Evaluation, Approved, Rejected)

    private String seanceId;         // ID de la séance associée (référence par ID)
    private String userId;           // Identifiant de l'étudiant (utilisateur)

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getGitLink() {
        return gitLink;
    }

    public void setGitLink(String gitLink) {
        this.gitLink = gitLink;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(String seanceId) {
        this.seanceId = seanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
