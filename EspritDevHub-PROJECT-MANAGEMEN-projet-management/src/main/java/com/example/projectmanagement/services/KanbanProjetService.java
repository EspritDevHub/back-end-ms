package com.example.projectmanagement.services;

import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import com.example.projectmanagement.Entities.Projet;
import com.example.projectmanagement.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class KanbanProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    public Map<EtatProjetEnum, List<Projet>> getProjetsParEtatKanban() {
        List<Projet> projets = projetRepository.findAll();
        return projets.stream()
                .collect(Collectors.groupingBy(Projet::getEtat));
    }

    public Projet updateEtatEtOrdreProjet(String id, EtatProjetEnum etat, Integer ordre) {
        Optional<Projet> projetOpt = projetRepository.findById(id);
        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            projet.setEtat(etat);
            projet.setOrdre(ordre);
            return projetRepository.save(projet);
        } else {
            throw new RuntimeException("Projet non trouvé avec l'ID : " + id);
        }
    }
}
