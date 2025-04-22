package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class CritereEvaluation {
    @Id
    private String id;

    private String titre;          // Exemple: "Code qualité"
    private String description;    // Exemple: "Respect des bonnes pratiques de développement"
    private double score;          // Score attribué pour ce critère, en pourcentage (0-100)

    @ManyToOne
    private Sprint sprint;         // Chaque critère est associé à un sprint spécifique

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }
}
