package tn.esprit.pi.notemanagement.notemanagementmicroservice.Feign;

import org.springframework.stereotype.Component;
import tn.esprit.pi.notemanagement.notemanagementmicroservice.Dtos.SprintDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class SprintClientFallback implements SprintClient {

    @Override
    public List<SprintDTO> getSprints() {
        List<SprintDTO> fallbackSprints = new ArrayList<>();

        // Example of fallback SprintDTO data
        fallbackSprints.add(new SprintDTO(1L, "0", "Sprint Initiation du projet", "2024-12-01", "2025-12-15"));
        fallbackSprints.add(new SprintDTO(2L, "1", "Premier sprint du projet", "2025-01-01", "2025-01-15"));
        fallbackSprints.add(new SprintDTO(3L, "2", "Deuxième sprint du projet", "2025-02-01", "2025-02-15"));
        fallbackSprints.add(new SprintDTO(4L, "3", "Troisième sprint du projet", "2025-03-01", "2025-03-15"));
        fallbackSprints.add(new SprintDTO(5L, "4", "Quatrième sprint du projet", "2025-04-01", "2025-04-15"));

        return fallbackSprints;
    }

    @Override
    public SprintDTO getSprintById(Long id) {
       return new SprintDTO(id, "Sprint 1", "Premier sprint du projet", "2025-01-01", "2025-01-15");
    }

}

