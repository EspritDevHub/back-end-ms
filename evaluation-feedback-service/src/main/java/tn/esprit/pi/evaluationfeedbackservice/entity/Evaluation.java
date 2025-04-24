package tn.esprit.pi.evaluationfeedbackservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "evaluation")
public class Evaluation {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id; // changed from Long to String for MongoDB

    private String description;
    private Integer note;
    private Critere critere;
    private Long projet;
    private Long user;

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
