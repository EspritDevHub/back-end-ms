package com.example.projectmanagement.Projection;

import com.example.projectmanagement.Entities.Enums.EtatTacheEnum;
import com.example.projectmanagement.Entities.Enums.TypeDureeEnum;

import java.time.LocalDateTime;

public interface TacheProjection {
    String getId();
    String getTitre();
    String getDescription();
    String getAssigneA();

    LocalDateTime getDateDebut();
    LocalDateTime getDateFin();

    EtatTacheEnum getEtat();
    Integer getAvancement();

    Float getDuree();
    TypeDureeEnum getTypeDuree();
}
