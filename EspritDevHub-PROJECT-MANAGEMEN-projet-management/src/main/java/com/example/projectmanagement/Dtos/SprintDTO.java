package com.example.projectmanagement.Dtos;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintDTO {

    private String id;
    private String nom;

    private List<TacheDTO> taches;
}

