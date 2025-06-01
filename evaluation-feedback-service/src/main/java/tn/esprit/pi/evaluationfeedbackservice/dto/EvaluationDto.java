package tn.esprit.pi.evaluationfeedbackservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import tn.esprit.pi.evaluationfeedbackservice.entity.Critere;

@Data
@ToString
@Builder
public class EvaluationDto {

    private String id; // changed from Long to String for MongoDB

    private String description;
    private Integer note;
    private Critere critere;
    private Long projet;
    private Long user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Critere getCritere() {
        return critere;
    }

    public void setCritere(Critere critere) {
        this.critere = critere;
    }

    public Long getProjet() {
        return projet;
    }

    public void setProjet(Long projet) {
        this.projet = projet;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
