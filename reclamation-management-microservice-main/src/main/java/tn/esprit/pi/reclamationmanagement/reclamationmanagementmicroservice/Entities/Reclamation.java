package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Entities;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Enums.ReclamationStatus;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Enums.SentimentType;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reclamations")
public class Reclamation {
    @Id
    private String id;

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private ReclamationStatus status;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String image;
    private String jiraTicketId;
    private String email;

}
