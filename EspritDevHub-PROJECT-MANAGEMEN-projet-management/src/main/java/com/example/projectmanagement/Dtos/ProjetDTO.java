package com.example.projectmanagement.Dtos;

import com.example.projectmanagement.Dtos.JalonDTO;
import com.example.projectmanagement.Dtos.PhaseDTO;
import com.example.projectmanagement.Entities.Enums.EtapeProjetEnum;
import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import com.example.projectmanagement.Entities.Groupe;
import com.example.projectmanagement.Entities.Phase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetDTO {

    private String id;
    private String code;
    private String titre;
    private String description;

    private EtatProjetEnum etat;
    private EtapeProjetEnum etapeProjet;
    private Integer ordre;

    private LocalDate dateDebut;
    private LocalDate dateFinPrevu;

    private Groupe groupe; // Ou nomGroupe si vous préférez exposer le nom

    private List<PhaseDTO> jalons;

    private Long createdBy;
    private LocalDate creationDate;


}