package com.example.projectmanagement.iservices;

import com.example.projectmanagement.Dtos.TacheDTO;
import com.example.projectmanagement.Entities.Tache;

import java.util.List;
import java.util.Map;

public interface ITacheService {

    TacheDTO createTache(TacheDTO tacheDTO);
    TacheDTO updateTache(String id, TacheDTO tacheDTO);
    TacheDTO getTacheById(String id);
    void verifierEtEnvoyerRappels();
    List<TacheDTO> getAllTaches();
    void deleteTache(String id);
    Map<String, Double> getTachesStats();
    List<TacheDTO> getTachesByProjet(String projetId);
    TacheDTO modifierTache(String id, TacheDTO dto);
    Tache ajouterTacheAuProjet(TacheDTO tacheDTO);

}
