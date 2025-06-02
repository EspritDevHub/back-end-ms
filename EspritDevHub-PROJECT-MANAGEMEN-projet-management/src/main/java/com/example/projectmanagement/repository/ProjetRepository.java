package com.example.projectmanagement.repository;

import com.example.projectmanagement.Entities.Enums.EtapeProjetEnum;
import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import com.example.projectmanagement.Entities.Projet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetRepository extends MongoRepository<Projet, String> {

    // Méthode pour récupérer un projet par son code
    Optional<Projet> findByCode(String code);

    // Méthode pour récupérer un projet par son titre
    Optional<Projet> findByTitre(String titre);

    // Méthode pour récupérer un projet par son statut
    List<Projet> findByEtat(EtatProjetEnum etat);

    // Méthode pour récupérer un projet par son étape
    List<Projet> findByEtapeProjet(EtapeProjetEnum etapeProjet);
    // Trouver les projets par état (utile pour afficher une colonne Kanban)
    List<Projet> findByEtatOrderByOrdreAsc(EtatProjetEnum etat);

    // Trouver tous les projets triés par état et ordre (utile pour tableau complet Kanban)
    List<Projet> findAllByOrderByEtatAscOrdreAsc();

    // Trouver les projets créés par un utilisateur spécifique (optionnel pour dashboard utilisateur)
    List<Projet> findByCreatedBy(Long createdBy);

    // Rechercher par titre contenant un mot-clé (recherche dans la barre de recherche frontend)
    List<Projet> findByTitreContainingIgnoreCase(String keyword);

}
