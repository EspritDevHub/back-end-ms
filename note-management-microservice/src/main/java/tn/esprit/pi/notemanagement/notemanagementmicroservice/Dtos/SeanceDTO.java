package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Number Numero;
    private Number Note;
    private String sprintId;
    private List<String> critereIds;
    private Date date;
    private String typeNote;
}
