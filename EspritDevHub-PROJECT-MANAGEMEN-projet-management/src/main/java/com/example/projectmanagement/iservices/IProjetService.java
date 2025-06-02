package com.example.projectmanagement.iservices;

import com.example.projectmanagement.Dtos.ProjetDTO;
import com.example.projectmanagement.Entities.Sprint;

import java.util.List;

public interface IProjetService {

    // Créer un nouveau projet
    ProjetDTO createProjet(ProjetDTO projetDTO);

    // Mettre à jour un projet existant
    ProjetDTO updateProjet(String id, ProjetDTO projetDTO);

    // Récupérer un projet par son ID
    ProjetDTO getProjetById(String id);

    // Récupérer tous les projets
    List<ProjetDTO> getAllProjets();

    // Supprimer un projet par son ID
    void deleteProjet(String id);

    void getAvancementProjet();

    double calculerScoreRisqueRetard(String id);

    String interpreterScoreRisque(double score);
}
