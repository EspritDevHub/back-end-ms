package com.example.projectmanagement.services;



import com.example.projectmanagement.Dtos.*;
import com.example.projectmanagement.Entities.*;
import com.example.projectmanagement.Entities.Enums.EtapeProjetEnum;
import com.example.projectmanagement.Entities.Enums.EtatPhaseEnum;
import com.example.projectmanagement.Entities.Enums.EtatProjetEnum;
import com.example.projectmanagement.repository.*;
import com.example.projectmanagement.iservices.IProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


import com.example.projectmanagement.Dtos.ProjetDTO;
import com.example.projectmanagement.Entities.Projet;
import com.example.projectmanagement.Entities.Enums.EtatTacheEnum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service


public class ProjetServiceImpl implements IProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TacheRepository tacheRepository;
    public ProjetServiceImpl(ProjetRepository projetRepository, TacheRepository tacheRepository) {
        this.projetRepository = projetRepository;
        this.tacheRepository = tacheRepository;
    }
    public double calculerScoreRisqueRetard(String projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Projet introuvable"));

        List<Tache> taches = tacheRepository.findByProjetId(projetId);
        if (taches.isEmpty()) return 0.0;

        long totalTaches = taches.size();
        long tachesTerminees = taches.stream()
                .filter(t -> t.getEtat() == EtatTacheEnum.TERMINEE)
                .count();

        long tachesEnRetard = taches.stream()
                .filter(t -> t.getDateFin() != null &&
                        t.getDateFin().toLocalDate().isBefore(LocalDate.now()) &&
                        t.getEtat() != EtatTacheEnum.TERMINEE)
                .count();

        double proportionTerminees = (double) tachesTerminees / totalTaches;
        double proportionEnRetard = (double) tachesEnRetard / totalTaches;

        long joursRestants = ChronoUnit.DAYS.between(LocalDate.now(), projet.getDateFinPrevu());
        if (joursRestants < 0) joursRestants = 0;

        double poidsAvancement = 0.5;
        double poidsRetard = 0.3;
        double poidsTempsRestant = 0.2;

        double score = (1 - proportionTerminees) * poidsAvancement
                + proportionEnRetard * poidsRetard
                + (joursRestants == 0 ? 1 : 1.0 / joursRestants) * poidsTempsRestant;

        if (projet.getEtat() == EtatProjetEnum.TERMINE) {
            score = 0.0;
        } else if (projet.getEtapeProjet() == EtapeProjetEnum.ETUDE) {
            score *= 0.7;
        }

        return Math.min(1.0, score);
    }

    public String interpreterScoreRisque(double score) {
        if (score < 0.3) return "Faible risque de retard";
        if (score < 0.7) return "Risque modéré de retard";
        return "Risque élevé de retard";
    }


    @Override
    public ProjetDTO createProjet(ProjetDTO projetDTO) {
        Projet projet = convertToEntity(projetDTO);

        projet.setCreationDate(LocalDate.now());
        projet.setEtapeProjet(EtapeProjetEnum.ETUDE);
        projet.setEtat(EtatProjetEnum.NON_COMMENCE);

        // ✅ Safely fetch and assign the Groupe entity by ID
        if (projetDTO.getGroupe() != null && projetDTO.getGroupe().getId() != null) {
            Optional<Groupe> optionalGroupe = groupeRepository.findById(projetDTO.getGroupe().getId());
            optionalGroupe.ifPresent(projet::setGroupe);
        }

        Projet saved = projetRepository.save(projet);
        return convertToDTO(saved);
    }
/*

   public ProjetDTO createProjet(ProjetDTO projetDTO) {
        Projet projet = convertToEntity(projetDTO);
        projet.setCreationDate(LocalDate.now());
        projet.setEtapeProjet(EtapeProjetEnum.ETUDE);
        projet.setEtat(EtatProjetEnum.NON_COMMENCE);

        List<Phase> savedPhases = new ArrayList<>();
        System.out.println(projet);
        if (projet.getJalons() != null) {
            for (Phase phase : projet.getJalons()) {
                phase.setProjetId(projet.getId());

                List<Sprint> savedSprints = new ArrayList<>();
                if (phase.getSprints() != null) {
                    for (Sprint sprint : phase.getSprints()) {
                        sprint.setPhaseId(phase.getId());

                        List<Tache> savedTaches = new ArrayList<>();
                        if (sprint.getTaches() != null) {
                            for (Tache tache : sprint.getTaches()) {
                                tache.setProjetId(projet.getId());
                                tache.setSprintId(sprint.getId());
                                Tache savedTache = tacheRepository.save(tache); // ✅ Save Tache
                                savedTaches.add(savedTache);
                            }
                        }

                        sprint.setTaches(savedTaches);
                        Sprint savedSprint = sprintRepository.save(sprint); // ✅ Save Sprint
                        savedSprints.add(savedSprint);
                    }
                }

                phase.setSprints(savedSprints);
                Phase savedPhase = phaseRepository.save(phase); // ✅ Save Phase
                savedPhases.add(savedPhase);
            }
        }

        projet.setJalons(savedPhases);
        Projet saved = projetRepository.save(projet); // ✅ Save Projet referencing all @DBRef

        return convertToDTO(saved);
    }

*/

    @Override
    public ProjetDTO updateProjet(String id, ProjetDTO projetDTO) {
        Optional<Projet> existing = projetRepository.findById(id);
        if (existing.isPresent()) {
            Projet projet = convertToEntity(projetDTO);
            projet.setId(id); // Ensure we're updating the correct entity
            Projet updated = projetRepository.save(projet);
            return convertToDTO(updated);
        }
        throw new RuntimeException("Projet not found with id: " + id);
    }



    @Override
    public ProjetDTO getProjetById(String id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet not found with id: " + id));
        return convertToDTO(projet);
    }

    @Override
    public List<ProjetDTO> getAllProjets() {
        return projetRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProjet(String id) {
        projetRepository.deleteById(id);
    }

    // === Conversion methods ===

    private Projet convertToEntity(ProjetDTO dto) {
        Projet projet = new Projet();

        projet.setId(dto.getId());
        projet.setCode(dto.getCode());
        projet.setTitre(dto.getTitre());
        projet.setDescription(dto.getDescription());
        projet.setEtat(dto.getEtat());
        projet.setEtapeProjet(dto.getEtapeProjet());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFinPrevu(dto.getDateFinPrevu());
        projet.setCreatedBy(dto.getCreatedBy());
        projet.setCreationDate(dto.getCreationDate());

        // Convert phases
        if (dto.getJalons() != null) {
            projet.setJalons(dto.getJalons().stream().map(this::convertToEntity).collect(Collectors.toList()));
        }

        // If you want to associate a group manually, uncomment and manage it with a GroupId in the DTO
        // For example, if you add: private String groupeId; in ProjetDTO
        // Optional<Groupe> groupe = groupeRepository.findById(dto.getGroupeId());
        // groupe.ifPresent(projet::setGroupe);

        return projet;
    }

    private ProjetDTO convertToDTO(Projet projet) {
        ProjetDTO dto = new ProjetDTO();

        dto.setId(projet.getId());
        dto.setCode(projet.getCode());
        dto.setTitre(projet.getTitre());
        dto.setDescription(projet.getDescription());
        dto.setEtat(projet.getEtat());
        dto.setEtapeProjet(projet.getEtapeProjet());
        dto.setDateDebut(projet.getDateDebut());
        dto.setDateFinPrevu(projet.getDateFinPrevu());
        dto.setCreatedBy(projet.getCreatedBy());
        dto.setCreationDate(projet.getCreationDate());

        if (projet.getJalons() != null) {
            dto.setJalons(projet.getJalons().stream().map(this::convertToDTO).collect(Collectors.toList()));
        }

        return dto;
    }

    private Phase convertToEntity(PhaseDTO dto) {
        Phase phase = new Phase();
        phase.setId(dto.getId());
        phase.setNom(dto.getNom());
        phase.setDateDebut(dto.getDateDebut());
        phase.setDateFin(dto.getDateFin());
        return phase;
    }

    private PhaseDTO convertToDTO(Phase entity) {
        return new PhaseDTO(
                entity.getId(),
                entity.getNom(),
                entity.getDateDebut(),
                entity.getDateFin()
        );
    }
   // @Scheduled(cron = "0 0 8 * * *")
   @Scheduled(cron = "*/10 * * * * *") // Exécution toutes les 10 secondes
   public void getAvancementProjet() {
       List<Projet> projetList = projetRepository.findAll();

       for (Projet projet : projetList) {
           LocalDate dateDebut = projet.getDateDebut();
           LocalDate dateFinPrevu = projet.getDateFinPrevu();

           if (dateDebut == null || dateFinPrevu == null || !dateFinPrevu.isAfter(dateDebut)) continue;

           long joursÉcoulés = ChronoUnit.DAYS.between(dateDebut, LocalDate.now());
           long totalJours = ChronoUnit.DAYS.between(dateDebut, dateFinPrevu);
           if (totalJours == 0) continue;

           double avancementAttendu = ((double) joursÉcoulés / totalJours) * 100;

           List<Phase> phases = projet.getJalons();
           if (phases == null || phases.isEmpty()) continue;

           double totalPondere = 0;
           double sommePoids = 0;

           for (Phase phase : phases) {
               double poidsPhase = calculerPoidsPhase(phase);

               double avancementPhase = 0;

               if (phase.getEtat() == EtatPhaseEnum.TERMINEE) {
                   avancementPhase = 100;
               } else if (phase.getEtat() == EtatPhaseEnum.EN_COURS) {
                   avancementPhase = calculerAvancementPhasePonderee(phase);
               } else {
                   // phase non commencée → avancement = 0
                   avancementPhase = 0;
               }

               totalPondere += avancementPhase * poidsPhase;
               sommePoids += poidsPhase;
           }

           if (sommePoids == 0) continue;

           double avancementReel = totalPondere / sommePoids;

           boolean enRetard = avancementReel < avancementAttendu;

           projet.setAvancement(avancementReel);
           projet.setRetard(enRetard);
           projetRepository.save(projet);

           System.out.printf("Projet: %s | Attendu: %.2f%% | Réel: %.2f%% | Retard: %s%n",
                   projet.getTitre(), avancementAttendu, avancementReel, enRetard);
       }
   }
    private double calculerPoidsPhase(Phase phase) {
        if (phase.getSprints() == null) return 0;

        return phase.getSprints().stream()
                .flatMap(sprint -> sprint.getTaches() != null ? sprint.getTaches().stream() : Stream.empty())
                .filter(t -> t.getDuree() != null)
                .mapToDouble(Tache::getDuree)
                .sum();
    }

    private double calculerAvancementPhasePonderee(Phase phase) {
        if (phase.getSprints() == null) return 0;

        double sommePonderee = 0;
        double totalPoids = 0;

        for (Sprint sprint : phase.getSprints()) {
            if (sprint.getTaches() == null) continue;

            for (Tache tache : sprint.getTaches()) {
                Float duree = tache.getDuree();
                Integer avancement = tache.getAvancement();

                if (duree == null || avancement == null) continue;

                sommePonderee += duree * avancement;
                totalPoids += duree;
            }
        }

        return totalPoids == 0 ? 0 : (sommePonderee / totalPoids); // moyenne pondérée sur 100
    }

    }



