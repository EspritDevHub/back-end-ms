package tn.esprit.pi.reclamationmanagement.reclamationmanagementmicroservice.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReclamationRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String description;

    private String image;
    private String status;

}
