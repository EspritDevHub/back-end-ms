package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "noteGroupes")
public class NoteGroupe {

    @Id
    private String id;

    private String groupeId;  // L'ID du groupe
    private String seanceId;  // L'ID de la séance
    private String critereEvaluationId; // L'ID du critère d'évaluation
    private double note;  // La note du groupe pour le critère

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupeId() {
        return groupeId;
    }

    public void setGroupeId(String groupeId) {
        this.groupeId = groupeId;
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

    // Constructor
    public NoteGroupe(String groupeId, String seanceId, String critereEvaluationId, double note) {
        this.groupeId = groupeId;
        this.seanceId = seanceId;
        this.critereEvaluationId = critereEvaluationId;
        this.note = note;
    }

    // Default constructor
    public NoteGroupe() {}
}
