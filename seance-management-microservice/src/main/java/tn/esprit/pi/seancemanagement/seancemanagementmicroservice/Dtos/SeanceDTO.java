package tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private Long numero;
    private Double note;
    private String sprintId;
    private List<String> critereIds;
    private Date date;
    private tn.esprit.pi.seancemanagement.seancemanagementmicroservice.Enum.TypeNote typeNote;
}
