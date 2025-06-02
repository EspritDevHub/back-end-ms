package com.example.projectmanagement.services;






import com.example.projectmanagement.Dtos.TacheDTO;

import com.example.projectmanagement.Entities.Enums.EtatTacheEnum;
import com.example.projectmanagement.Entities.Enums.TypeDureeEnum;
import com.example.projectmanagement.Entities.Projet;
import com.example.projectmanagement.Entities.Tache;
import com.example.projectmanagement.iservices.ITacheService;
import com.example.projectmanagement.repository.ProjetRepository;
import com.example.projectmanagement.repository.TacheRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class TacheService implements ITacheService {

    private final TacheRepository tacheRepository;
    private final ProjetRepository projetRepository;
    private final NotificationService notificationService;

    @Override
    public TacheDTO createTache(TacheDTO tacheDTO) {
        Tache tache = new Tache(tacheDTO);
        return convertToDTO(tacheRepository.save(tache));
    }

    @Override
    public TacheDTO updateTache(String id, TacheDTO tacheDTO) {
        Tache existing = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec ID : " + id));

        // Clone de la version avant modification (pour la comparaison)
        Tache ancienneTache = new Tache(existing); // Assure-toi d’avoir un constructeur de copie


        existing.setTitre(tacheDTO.getTitre());
        existing.setDescription(tacheDTO.getDescription());
        existing.setEtat(tacheDTO.getEtat());
        existing.setAssigneA(tacheDTO.getAssigneA());
        existing.setDateDebut(tacheDTO.getDateDebut());
        existing.setDateFin(tacheDTO.getDateFin());

        // Sauvegarde
        Tache saved = tacheRepository.save(existing);


        notificationService.notifierChangementTache(ancienneTache, saved);

        return convertToDTO(saved);
    }


    @Override
    public TacheDTO getTacheById(String id) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche non trouvée avec ID : " + id));
        return convertToDTO(tache);
    }

    @Override
    public List<TacheDTO> getAllTaches() {
        return tacheRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTache(String id) {
        tacheRepository.deleteById(id);
    }

    @Override
    public List<TacheDTO> getTachesByProjet(String projetId) {
        List<Tache> taches = tacheRepository.findByProjetId(projetId);

        // Retourner une liste vide si aucune tâche n'est trouvée
        return taches.isEmpty() ? List.of() : taches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    private TacheDTO convertToDTO(Tache tache) {
        // Conversion de l'entité Tache en DTO
        return new TacheDTO(tache);  // Utilisez le constructeur pour convertir
    }


    @Override
    public Map<String, Double> getTachesStats() {
        List<Tache> toutesLesTaches = this.tacheRepository.findAll();

        double totalEcart = 0;
        int countWithEstimations = 0;
        int accurateEstimations = 0;
        int surestimees = 0;
        int sousEstimees = 0;

        for (Tache t : toutesLesTaches) {
            if (t.getEtat() == EtatTacheEnum.TERMINEE &&
                    t.getDateDebut() != null &&
                    t.getDateFin() != null &&
                    t.getDuree() != null &&
                    t.getTypeDuree() != null) {

                long durationInMinutes = Duration.between(t.getDateDebut(), t.getDateFin()).toMinutes();
                double durationReelle;

                if (t.getTypeDuree() == TypeDureeEnum.HEURE) {
                    durationReelle = durationInMinutes / 60.0;
                } else {
                    durationReelle = durationInMinutes / (60.0 * 8); // Suppose 8h par jour
                }

                double estimation = t.getDuree();
                double ecart = durationReelle - estimation;
                totalEcart += Math.abs(ecart);
                countWithEstimations++;

                if (Math.abs(ecart) <= 0.25) {
                    accurateEstimations++;
                } else if (ecart > 0.25) {
                    sousEstimees++;
                } else {
                    surestimees++;
                }
            }
        }

        double moyenneEcart = countWithEstimations == 0 ? 0 : totalEcart / countWithEstimations;
        double tauxPrecision = countWithEstimations == 0 ? 0 : (accurateEstimations * 100.0 / countWithEstimations);
        double tauxSousEstimees = countWithEstimations == 0 ? 0 : (sousEstimees * 100.0 / countWithEstimations);
        double tauxSurestimees = countWithEstimations == 0 ? 0 : (surestimees * 100.0 / countWithEstimations);

        Map<String, Double> stats = new HashMap<>();
        stats.put("moyenneEcart", moyenneEcart);
        stats.put("tauxPrecisionEstimation", tauxPrecision);
        stats.put("tauxTachesSousEstimees", tauxSousEstimees);
        stats.put("tauxTachesSurestimees", tauxSurestimees);

        return stats;
    }
    @Override
    public TacheDTO modifierTache(String id, TacheDTO dto) {
        Tache existing = tacheRepository.findById(id).orElseThrow();
        boolean statutChange = !existing.getEtat().equals(dto.getEtat());
        boolean reassigned = !existing.getAssigneA().equals(dto.getAssigneA());

        Tache updated = new Tache(dto);
        updated.setId(id);
        Tache saved = tacheRepository.save(updated);

        if (statutChange) {
            notificationService.sendEmail(
                    saved.getAssigneA(),
                    "📌 Statut modifié",
                    "Le statut de la tâche \"" + saved.getTitre() + "\" est désormais : " + saved.getEtat(),false
            );
        }

        if (reassigned) {
            notificationService.sendEmail(
                    saved.getAssigneA(),
                    "👤 Nouvelle tâche assignée",
                    "Une tâche vous a été assignée : " + saved.getTitre(),false
            );
        }

        return new TacheDTO(saved);
    }
    @Scheduled(fixedRate = 3600000)
    public void verifierEtEnvoyerRappels() {
        LocalDateTime maintenant = LocalDateTime.now();
        LocalDateTime dans24h = maintenant.plusHours(24);

        List<Tache> prochesDeadline = tacheRepository.findByDateFinBetween(maintenant, dans24h);
        for (Tache tache : prochesDeadline) {
            notificationService.sendEmail(
                    tache.getAssigneA(),
                    "⏰ Rappel : tâche bientôt due",
                    "Votre tâche \"" + tache.getTitre() + "\" est prévue pour le " + tache.getDateFin(),false
            );
        }
    }
    @Override
    public Tache ajouterTacheAuProjet(TacheDTO dto) {
        Projet projet = projetRepository.findById(dto.getProjetId())
                .orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        Tache tache = new Tache(dto);
        tache.setProjetId(projet.getId());

        return tacheRepository.save(tache);
    }


}