package tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos;


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
    private String titre;
    private String description;
    private Number Numero;
    private Number Note;
    private String sprintId;
    private List<String> critereIds;
    private Date date;
    private String typeNote;
}
