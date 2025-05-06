package tn.esprit.pi.documentmanagement.documentmanagementmicroservice.Dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeanceDTO {
    private String id;
    @NotBlank
    private String titre;
    @Size(max = 500)
    private String description;

}
