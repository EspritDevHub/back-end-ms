package tn.esprit.pi.notemanagement.notemanagementmicroservice.services;

import org.springframework.stereotype.Service;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Entities.Sprint;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.repository.ISprintRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SprintService {

    private final ISprintRepository sprintRepository;

    public SprintService(ISprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    public Sprint createSprint(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    public Optional<Sprint> getSprintById(String id) {
        return sprintRepository.findById(id);
    }

    public Sprint updateSprint(String id, Sprint sprintDetails) {
        return sprintRepository.findById(id).map(sprint -> {
            sprint.setTitre(sprintDetails.getTitre());
            sprint.setDescription(sprintDetails.getDescription());
            sprint.setOrdre(sprintDetails.getOrdre());
            sprint.setType(sprintDetails.getType());
            sprint.setDateDebut(sprintDetails.getDateDebut());
            sprint.setDateFin(sprintDetails.getDateFin());
            return sprintRepository.save(sprint);
        }).orElse(null);
    }

    public void deleteSprint(String id) {
        sprintRepository.deleteById(id);
    }
}
