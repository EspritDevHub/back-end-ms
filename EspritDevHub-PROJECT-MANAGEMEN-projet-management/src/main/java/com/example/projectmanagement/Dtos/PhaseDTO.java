package com.example.projectmanagement.Dtos;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhaseDTO {

    private String id;
    private String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private List<SprintDTO> sprints;

    public PhaseDTO (String id , String nom, LocalDate dateDebut, LocalDate dateFin ){
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.sprints = new ArrayList<>();
    }
}

