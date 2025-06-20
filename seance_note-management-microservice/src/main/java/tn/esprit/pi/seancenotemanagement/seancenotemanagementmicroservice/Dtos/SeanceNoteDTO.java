package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Enum.TypeNote;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeanceNoteDTO {
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
    private TypeNote typeNote;
}
