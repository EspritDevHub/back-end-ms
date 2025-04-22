package tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "sprints")
public class Sprint {
    @Id
    private String id;
    private String titre;          // Ex: "Sprint 0", "Sprint 1"
    private String description;
    private int ordre;             // Ex: 0, 1, 2, 3
    private String type;           // "individuelle", "groupe", "mixte"
    private Date dateDebut;        // Date de début du sprint
    private Date dateFin;          // Date de fin du sprint

    // Constructeurs
    public Sprint() {}

    public Sprint(String titre, String description, int ordre, String type, Date dateDebut, Date dateFin) {
        this.titre = titre;
        this.description = description;
        this.ordre = ordre;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getOrdre() { return ordre; }
    public void setOrdre(int ordre) { this.ordre = ordre; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
}
