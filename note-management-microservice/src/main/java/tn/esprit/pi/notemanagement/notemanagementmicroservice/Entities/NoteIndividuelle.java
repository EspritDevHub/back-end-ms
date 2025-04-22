package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "note_individuelle")
public class NoteIndividuelle {

    @Id
    private String id; // Utilisation de String pour l'ObjectId de MongoDB

    private String userId; // Référence à l'ID de l'utilisateur
    private String seanceId; // Référence à l'ID de la séance
    private String critereEvaluationId; // Référence à l'ID du critère d'évaluation
    private double note; // La note attribuée

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSeanceId() {
        return seanceId;
    }

    public void setSeanceId(String seanceId) {
        this.seanceId = seanceId;
    }

    public String getCritereEvaluationId() {
        return critereEvaluationId;
    }

    public void setCritereEvaluationId(String critereEvaluationId) {
        this.critereEvaluationId = critereEvaluationId;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}
